package dev.sterner.client

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexFormat
import dev.sterner.VoidBound
import dev.sterner.blockentity.SpiritBinderBlockEntity
import dev.sterner.registry.VoidBoundRenderTypes
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.resources.ResourceLocation
import team.lodestar.lodestone.helpers.RenderHelper
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder
import team.lodestar.lodestone.systems.rendering.VFXBuilders
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken


class SpiritBinderBlockEntityRenderer(ctx: BlockEntityRendererProvider.Context) :
    BlockEntityRenderer<SpiritBinderBlockEntity> {

    override fun render(
        blockEntity: SpiritBinderBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        poseStack.pushPose()
        poseStack.translate(0.5, 1.5, 0.5)
        VFXBuilders.createWorld()
            .setRenderType(SPHERE_RENDER_TYPE)
            .setAlpha(1.0f)
            .setLight(RenderHelper.FULL_BRIGHT)
            .renderSphere(
                poseStack,
                0.5f,
                20,
                20
            )
        poseStack.popPose()
    }

    companion object {
        var TOKEN = RenderTypeToken.createCachedToken(
            ResourceLocation(VoidBound.modid, "textures/aura.png")
        )

        val SPHERE_RENDER_TYPE: RenderType = VoidBoundRenderTypes.TRANSPARENT_GLOW_TEXTURE.applyWithModifier(TOKEN)
        { b: LodestoneCompositeStateBuilder ->
            b.replaceVertexFormat(
                VertexFormat.Mode.TRIANGLES
            )
        }
    }
}