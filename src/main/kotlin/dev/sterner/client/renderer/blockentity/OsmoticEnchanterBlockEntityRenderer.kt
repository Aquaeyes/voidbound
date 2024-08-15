package dev.sterner.client.renderer.blockentity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import com.sammy.malum.common.block.storage.pedestal.ItemPedestalBlockEntity
import dev.sterner.common.blockentity.OsmoticEnchanterBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntityInventory
import kotlin.math.sin

class OsmoticEnchanterBlockEntityRenderer(ctx: BlockEntityRendererProvider.Context) :
    BlockEntityRenderer<OsmoticEnchanterBlockEntity> {

    val PEDESTAL_ITEM_OFFSET: Vec3 = Vec3(0.5, 0.85, 0.5)

     private fun getItemOffset(level: Level, partialTicks: Float): Vec3 {
         val gameTime: Float = level.gameTime + partialTicks

         return PEDESTAL_ITEM_OFFSET.add(0.0, (sin(((gameTime % 360) / 20f).toDouble()).toFloat() * 0.05f).toDouble(), 0.0)
    }
    override fun render(
        blockEntity: OsmoticEnchanterBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {

        val inventory: LodestoneBlockEntityInventory = blockEntity.inventory
        val itemRenderer = Minecraft.getInstance().itemRenderer
        val level = blockEntity.level

        if (!inventory.isEmpty && level != null) {
            val stack = inventory.getStackInSlot(0)
            poseStack.pushPose()
            val itemOffset: Vec3 = getItemOffset(blockEntity.level!!, partialTick)
            poseStack.translate(itemOffset.x(), itemOffset.y(), itemOffset.z())
            poseStack.mulPose(Axis.YP.rotationDegrees(((level.gameTime % 360) + partialTick) * 1))
            poseStack.scale(0.6f, 0.6f, 0.6f)
            itemRenderer.renderStatic(
                stack,
                ItemDisplayContext.FIXED,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                buffer,
                level,
                0
            )
            poseStack.popPose()
        }
    }
}