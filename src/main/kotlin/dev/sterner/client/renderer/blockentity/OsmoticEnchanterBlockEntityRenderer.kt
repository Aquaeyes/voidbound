package dev.sterner.client.renderer.blockentity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.sterner.api.ClientTickHandler.total
import dev.sterner.client.model.CrimsonBookModel
import dev.sterner.client.renderer.CrimsonRitesRenderer.texture
import dev.sterner.common.blockentity.OsmoticEnchanterBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.resources.model.Material
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntityInventory
import kotlin.math.sin

class OsmoticEnchanterBlockEntityRenderer(ctx: BlockEntityRendererProvider.Context) :
    BlockEntityRenderer<OsmoticEnchanterBlockEntity> {

    private val bookModel = CrimsonBookModel(ctx.bakeLayer(CrimsonBookModel.LAYER_LOCATION))

    private val itemOffset: Vec3 = Vec3(0.5, 0.85, 0.5)

    private fun getItemOffset(level: Level, partialTicks: Float): Vec3 {
        val gameTime: Float = level.gameTime + partialTicks

        return itemOffset.add(
            0.0,
            (sin(((gameTime % 360) / 20f).toDouble()).toFloat() * 0.05f).toDouble(),
            0.0
        )
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

        if (!inventory.isEmpty && level != null && false) {
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

        renderBook(blockEntity, partialTick, poseStack, buffer, packedLight, packedOverlay)
    }

    private fun renderBook(
        blockEntity: OsmoticEnchanterBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        buffers: MultiBufferSource?,
        packedLight: Int,
        packedOverlay: Int
    ) {
        poseStack.pushPose()

        poseStack.translate(0.5f, 0.05f, 0.5f)
        val f = blockEntity.time.toFloat() + partialTick
        poseStack.translate(0.0f, 0.1f + Mth.sin(f * 0.1f) * 0.01f, 0.0f)
        var g = blockEntity.rot - blockEntity.oRot

        while (g >= Math.PI.toFloat()) {
            g -= (Math.PI * 2).toFloat()
        }

        while (g < -Math.PI.toFloat()) {
            g += (Math.PI * 2).toFloat()
        }

        val h = blockEntity.oRot + g * partialTick
        poseStack.mulPose(Axis.YP.rotation((Math.PI.toFloat() / 2) - h))
        poseStack.mulPose(Axis.XP.rotationDegrees(-20.0f))
        poseStack.translate(0.0,-0.8, 0.4)
        val i = Mth.lerp(partialTick, blockEntity.oFlip, blockEntity.flip)
        val leftPageAngle = Mth.frac(i + 0.25f) * 1.6f - 0.3f
        val rightPageAngle = Mth.frac(i + 0.75f) * 1.6f - 0.3f
        val l = Mth.lerp(partialTick, blockEntity.oOpen, blockEntity.open)

        this.bookModel.setupAnim(total(), Mth.clamp(leftPageAngle, 0.0f, 1.0f), Mth.clamp(rightPageAngle, 0.0f, 1.0f), l, false)

        //this.bookModel.setupAnim(f, Mth.clamp(j, 0.0f, 1.0f), Mth.clamp(k, 0.0f, 1.0f), l)
        val mat: Material = texture
        val buffer = mat.buffer(buffers, RenderType::entitySolid)
        this.bookModel.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f)
        poseStack.popPose()
    }
}