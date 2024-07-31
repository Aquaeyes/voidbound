package dev.sterner.common.components

import com.mojang.blaze3d.vertex.PoseStack
import com.sammy.malum.MalumMod
import com.sammy.malum.client.RenderUtils
import com.sammy.malum.client.renderer.block.MoteOfManaRenderer
import com.sammy.malum.registry.common.item.ItemRegistry
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent
import dev.sterner.VoidBound
import dev.sterner.common.entity.SoulSteelGolemEntity
import dev.sterner.registry.VoidBoundComponentRegistry
import dev.sterner.registry.VoidBoundEntityTypeRegistry
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.toasts.Toast
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.Vec3
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry
import team.lodestar.lodestone.systems.rendering.VFXBuilders
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken
import java.awt.Color

class VoidBoundPlayerComponent(private val player: Player) : AutoSyncedComponent, CommonTickingComponent {

    private var highlightBlockList = mutableMapOf<BlockPos, Int>()

    fun addBlock(pos: BlockPos) {
        highlightBlockList[pos] = 20
        VoidBoundComponentRegistry.VOID_BOUND_PLAYER_COMPONENT.sync(player)
    }

    override fun tick() {
        val holdingTuningFork = player.getItemInHand(InteractionHand.MAIN_HAND).`is`(ItemRegistry.TUNING_FORK.get()) ||
                player.getItemInHand(InteractionHand.OFF_HAND).`is`(ItemRegistry.TUNING_FORK.get())

        val iterator = highlightBlockList.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (holdingTuningFork) {
                // Reset the counter to 40 if holding the tuning fork
                highlightBlockList[entry.key] = 20
            } else {
                val newTickCount = entry.value - 1
                if (newTickCount <= 0) {
                    // Remove the block from the list after 40 ticks
                    iterator.remove()
                    VoidBoundComponentRegistry.VOID_BOUND_PLAYER_COMPONENT.sync(player)
                } else {
                    // Update the tick count
                    highlightBlockList[entry.key] = newTickCount
                }
            }
        }
    }

    override fun readFromNbt(tag: CompoundTag) {
        highlightBlockList.clear()
        val list = tag.getList("HighlightBlocks", 10) // 10 is the ID for CompoundTag
        for (i in 0 until list.size) {
            val blockTag = list.getCompound(i)
            val pos = BlockPos(blockTag.getInt("x"), blockTag.getInt("y"), blockTag.getInt("z"))
            val ticks = blockTag.getInt("ticks")
            highlightBlockList[pos] = ticks
        }
    }

    override fun writeToNbt(tag: CompoundTag) {
        val list = ListTag()
        highlightBlockList.forEach { (pos, ticks) ->
            val blockTag = CompoundTag()
            blockTag.putInt("x", pos.x)
            blockTag.putInt("y", pos.y)
            blockTag.putInt("z", pos.z)
            blockTag.putInt("ticks", ticks)
            list.add(blockTag)
        }
        tag.put("HighlightBlocks", list)
    }

    companion object {

        val WARD_BORDER: RenderTypeToken =
            RenderTypeToken.createToken(VoidBound.id("textures/block/runeborder.png"))

        fun renderCubeAtPos(ctx: WorldRenderContext) {
            val camera = ctx.camera()
            val poseStack = ctx.matrixStack()
            val localPlayer = Minecraft.getInstance().player
            if (localPlayer != null) {
                val playerComponent = VoidBoundComponentRegistry.VOID_BOUND_PLAYER_COMPONENT.get(localPlayer)
                for (entry in playerComponent.highlightBlockList) {
                    renderCubeAtPos(camera, poseStack, entry.key, WARD_BORDER, entry.value)
                }
            }
        }

        private fun renderCubeAtPos(camera: Camera, poseStack: PoseStack, blockPos: BlockPos, renderTypeToken: RenderTypeToken, ticksRemaining: Int) {
            val targetPosition = Vec3(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble())
            val transformedPosition: Vec3 = targetPosition.subtract(camera.position)


            poseStack.pushPose()

            poseStack.translate(transformedPosition.x, transformedPosition.y, transformedPosition.z)

            val totalTicks = 20
            val alpha = 0.5f * (ticksRemaining / totalTicks.toFloat())

            val builder = VFXBuilders.createWorld().setRenderType(LodestoneRenderTypeRegistry.ADDITIVE_TEXTURE.applyAndCache(renderTypeToken))
            val cubeVertexData = RenderUtils.makeCubePositions(1f)

            RenderUtils.drawCube(
                poseStack,
                builder.setColor(Color(255, 200, 150), alpha),
                1.08f,
                cubeVertexData
            )


            poseStack.popPose()
        }

        fun useBlock(player: Player, level: Level?, interactionHand: InteractionHand, blockHitResult: BlockHitResult): InteractionResult {
            val stack = player.getItemInHand(interactionHand)
            if (stack.`is`(ItemRegistry.TUNING_FORK.get()) && stack.hasTag() && stack.tag!!.contains("GolemId")) {
                VoidBoundComponentRegistry.VOID_BOUND_PLAYER_COMPONENT.get(player).addBlock(blockHitResult.blockPos)
                val golem = level?.getEntity(stack.tag!!.getInt("GolemId"))
                if (golem is SoulSteelGolemEntity) {
                    golem.handleTuningFork(player, blockHitResult.blockPos)
                }
                stack.tag!!.remove("GolemId")
                return InteractionResult.SUCCESS
            }
            return InteractionResult.PASS
        }

        fun useEntity(player: Player, level: Level?, interactionHand: InteractionHand, entity: Entity?, entityHitResult: EntityHitResult?): InteractionResult {
            if (player.getItemInHand(interactionHand).`is`(ItemRegistry.TUNING_FORK.get())) {
                if (entityHitResult != null && entityHitResult.entity.type == VoidBoundEntityTypeRegistry.SOUL_STEEL_GOLEM_ENTITY.get()) {
                    val nbt = player.getItemInHand(interactionHand).orCreateTag
                    nbt.putInt("GolemId",entityHitResult.entity.id)
                    player.getItemInHand(interactionHand).tag = nbt
                    return InteractionResult.SUCCESS
                }
            }
            return InteractionResult.PASS
        }
    }
}