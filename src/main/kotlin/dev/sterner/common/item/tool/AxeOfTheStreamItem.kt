package dev.sterner.common.item.tool

import dev.sterner.api.util.VoidBoundBlockUtils
import dev.sterner.networking.AxeOfTheStreamParticlePacket
import dev.sterner.networking.BubbleParticlePacket
import dev.sterner.registry.VoidBoundItemRegistry
import dev.sterner.registry.VoidBoundPacketRegistry
import io.github.fabricators_of_create.porting_lib.event.common.BlockEvents
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import team.lodestar.lodestone.systems.item.tools.magic.MagicAxeItem
import java.awt.Color


open class AxeOfTheStreamItem(
    material: Tier?,
    damage: Float,
    speed: Float,
    magicDamage: Float,
    properties: Properties?
) :
    MagicAxeItem(
        material, damage, speed,
        magicDamage,
        properties
    ), UpgradableTool {


    override fun getDestroySpeed(stack: ItemStack, state: BlockState): Float {
        return super.getDestroySpeed(stack, state) + getExtraMiningSpeed(stack)
    }

    override fun getUseDuration(stack: ItemStack): Int {
        return 72000
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        player.startUsingItem(usedHand)
        return super.use(level, player, usedHand)
    }

    override fun onUseTick(level: Level, livingEntity: LivingEntity, stack: ItemStack, remainingUseDuration: Int) {
        val stuff = level.getEntitiesOfClass(ItemEntity::class.java, livingEntity.boundingBox.inflate(10.0))

        if (stuff != null && stuff.isNotEmpty()) {
            val iterator = stuff.iterator()
            while (iterator.hasNext()) {
                val e: ItemEntity = iterator.next() as ItemEntity
                if (!e.isRemoved) {
                    val direction = Vec3(
                        livingEntity.x - e.x,
                        (livingEntity.y + livingEntity.bbHeight / 2.0) - (e.y + e.bbHeight / 2.0),
                        livingEntity.z - e.z
                    ).normalize()

                    val speedFactor = 0.045

                    val newMotion = direction.scale(speedFactor)

                    val clampedMotion = Vec3(
                        newMotion.x.coerceIn(-0.25, 0.25),
                        newMotion.y.coerceIn(-0.25, 0.25),
                        newMotion.z.coerceIn(-0.25, 0.25)
                    )

                    e.deltaMovement = e.deltaMovement.add(clampedMotion)

                    if (!level.isClientSide) {
                        for (player in PlayerLookup.tracking(e)) {
                            for (i in 0..5) {
                                val pos = Vector3f(
                                    e.x.toFloat() + (level.random.nextFloat() - level.random.nextFloat()) * 0.2f,
                                    e.y.toFloat() + e.bbHeight + (level.random.nextFloat() - level.random.nextFloat()) * 0.2f,
                                    e.z.toFloat() + (level.random.nextFloat() - level.random.nextFloat()) * 0.2f
                                )
                                VoidBoundPacketRegistry.VOID_BOUND_CHANNEL.sendToClient(
                                    BubbleParticlePacket(
                                        Vector3f(
                                            clampedMotion.x.toFloat(),
                                            clampedMotion.y.toFloat(),
                                            clampedMotion.z.toFloat()
                                        ), pos
                                    ), player
                                )
                            }
                        }
                    }
                }
            }
        }

        super.onUseTick(level, livingEntity, stack, remainingUseDuration)
    }

    override fun appendHoverText(
        stack: ItemStack,
        level: Level?,
        tooltipComponents: MutableList<Component>,
        isAdvanced: TooltipFlag
    ) {
        val tool = stack.item as UpgradableTool
        if (tool.getNetherited(stack)) {
            tooltipComponents.add(
                Component.translatable("Netherited").withStyle(ChatFormatting.ITALIC).withStyle(
                    Style.EMPTY.withColor(Color(90, 65, 0).rgb)
                )
            )
        }
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced)
    }

    companion object {

        /**
         * Cancels the event and perform the specific axe effect for axe of the stream as wel as ichorium axe.
         * Axe of the Stream breaks the furthest block on a tree
         * Ichorium Axe breaks the whole tree
         */
        fun breakBlock(breakEvent: BlockEvents.BreakEvent?) {
            val player = breakEvent?.player
            val level = player?.level()
            val pos = breakEvent!!.pos

            if (level != null) {
                val block = level.getBlockState(pos)
                if (!player.isShiftKeyDown && block.`is`(BlockTags.LOGS)) {
                    if (player.mainHandItem.`is`(VoidBoundItemRegistry.ICHORIUM_AXE.get())) {
                        val logCount = VoidBoundBlockUtils.getLogCount(level, pos)
                        val logsToBreak: List<BlockPos> = VoidBoundBlockUtils.getLogsToBreak(
                            level,
                            pos,
                            mutableListOf(),
                            logCount,
                            level.getBlockState(pos).block
                        )
                        for (logPos in logsToBreak) {

                            val logState = level.getBlockState(logPos)
                            val be = level.getBlockEntity(logPos)

                            Block.dropResources(logState, level, logPos, be)
                            level.setBlock(logPos, Blocks.AIR.defaultBlockState(), 3)
                        }
                        breakEvent.isCanceled = true
                    }

                    if (player.mainHandItem.`is`(VoidBoundItemRegistry.AXE_OF_THE_STREAM.get())) {

                        if (level is ServerLevel) {
                            for (playerPart in PlayerLookup.tracking(level, pos)) {
                                VoidBoundPacketRegistry.VOID_BOUND_CHANNEL.sendToClient(
                                    AxeOfTheStreamParticlePacket(
                                        level.getBlockState(
                                            pos
                                        ), pos
                                    ), playerPart
                                )
                            }
                        }

                        VoidBoundBlockUtils.breakFurthestBlock(level, pos, block, player)
                        breakEvent.isCanceled = true
                    }
                }
            }
        }
    }
}