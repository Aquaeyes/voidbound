package dev.sterner.common.item.tool

import com.sammy.malum.registry.common.SpiritTypeRegistry
import com.sammy.malum.visual_effects.SpiritLightSpecs
import dev.sterner.api.util.VoidBoundBlockUtils
import dev.sterner.api.util.VoidBoundPosUtils
import dev.sterner.networking.AxeOfTheStreamParticleEffect
import dev.sterner.networking.BubbleParticlePacket
import dev.sterner.registry.VoidBoundPacketRegistry
import io.github.fabricators_of_create.porting_lib.event.common.BlockEvents
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import team.lodestar.lodestone.helpers.DataHelper
import team.lodestar.lodestone.helpers.RandomHelper
import team.lodestar.lodestone.systems.item.tools.magic.MagicAxeItem
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder
import team.lodestar.lodestone.systems.particle.data.GenericParticleData
import kotlin.math.cos

class AxeOfTheStreamItem(material: Tier?, damage: Float, speed: Float, magicDamage: Float, properties: Properties?) :
    MagicAxeItem(
        material, damage, speed,
        magicDamage,
        properties
    ) {

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
                    var d6: Double = e.x - livingEntity.x
                    var d8: Double = e.y - livingEntity.y + (livingEntity.bbHeight / 2.0f)
                    var d10: Double = e.z - livingEntity.z
                    val d11 =
                        Mth.sqrt(d6.toFloat() * d6.toFloat() + d8.toFloat() * d8.toFloat() + d10.toFloat() * d10.toFloat())
                    d6 /= d11
                    d8 /= d11
                    d10 /= d11
                    val d13 = 0.3

                    val newMotionX = (e.deltaMovement.x - d6 * d13).coerceIn(-0.25, 0.25)
                    val newMotionY = (e.deltaMovement.y - (d8 * d13 - 0.1)).coerceIn(-0.25, 0.25)
                    val newMotionZ = (e.deltaMovement.z - d10 * d13).coerceIn(-0.25, 0.25)

                    val motion = Vec3(newMotionX, newMotionY, newMotionZ)
                    e.deltaMovement = motion

                    if (!level.isClientSide) {
                        for (player in PlayerLookup.tracking(e)) {
                            for (i in 0 .. 5) {
                                val pos = Vector3f(
                                    e.x.toFloat() + (level.random.nextFloat() - level.random.nextFloat()) * 0.2f,
                                    e.y.toFloat() + e.bbHeight + (level.random.nextFloat() - level.random.nextFloat()) * 0.2f,
                                    e.z.toFloat() + (level.random.nextFloat() - level.random.nextFloat()) * 0.2f
                                )
                                VoidBoundPacketRegistry.VOID_BOUND_CHANNEL.sendToClient(BubbleParticlePacket(
                                    Vector3f(
                                        motion.x.toFloat(), motion.y.toFloat(), motion.z.toFloat()
                                    ), pos), player)
                            }
                        }
                    }
                }
            }
        }

        super.onUseTick(level, livingEntity, stack, remainingUseDuration)
    }


    companion object {
        fun breakBlock(breakEvent: BlockEvents.BreakEvent?) {
            val player = breakEvent?.player
            val level = player?.level()
            val pos = breakEvent!!.pos
            if (level != null && player.mainHandItem.item is AxeOfTheStreamItem) {
                val block = level.getBlockState(pos)
                if (!player.isShiftKeyDown && block.`is`(BlockTags.LOGS)) {

                    println("Break")
                    if (level is ServerLevel) {
                        println("Server")
                        for (playerPart in PlayerLookup.tracking(level, pos)) {
                            VoidBoundPacketRegistry.VOID_BOUND_CHANNEL.sendToClient(AxeOfTheStreamParticleEffect(pos), playerPart)
                        }
                    } else {
                        println("Client")
                        val state = level.getBlockState(pos)
                        val coordPos = VoidBoundPosUtils.getFaceCoords(level, state, pos,  player.direction.opposite)
                        val lightSpecs: ParticleEffectSpawner = SpiritLightSpecs.spiritLightSpecs(level, coordPos, SpiritTypeRegistry.AQUEOUS_SPIRIT)
                        lightSpecs.builder.multiplyLifetime(1.5f)
                        lightSpecs.bloomBuilder.multiplyLifetime(1.5f)
                        lightSpecs.spawnParticles()
                        lightSpecs.spawnParticles()
                    }
                    VoidBoundBlockUtils.breakFurthestBlock(level, pos, block, player)
                    breakEvent.isCanceled = true
                }
            }
        }
    }
}