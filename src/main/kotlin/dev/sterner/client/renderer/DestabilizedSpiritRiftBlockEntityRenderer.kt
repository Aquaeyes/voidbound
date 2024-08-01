package dev.sterner.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import com.sammy.malum.client.RenderUtils
import dev.sterner.VoidBound
import dev.sterner.client.Tokens
import dev.sterner.client.renderer.SpiritBinderBlockEntityRenderer.Companion.TOKEN
import dev.sterner.common.blockentity.DestabilizedSpiritRiftBlockEntity
import dev.sterner.registry.VoidBoundRenderTypes
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry
import team.lodestar.lodestone.systems.rendering.VFXBuilders
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken
import java.awt.Color


class DestabilizedSpiritRiftBlockEntityRenderer(ctx: BlockEntityRendererProvider.Context) :
    BlockEntityRenderer<DestabilizedSpiritRiftBlockEntity> {

    override fun render(
        blockEntity: DestabilizedSpiritRiftBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val rt = LodestoneRenderTypeRegistry.ADDITIVE_TEXTURE.apply(TOKEN)
        val renderType = VoidBoundRenderTypes.GRAVITY_VORTEX.apply(TOKEN)
        val copy = LodestoneRenderTypeRegistry.copy(renderType)
        val copy2 = LodestoneRenderTypeRegistry.copy(renderType)


        var builder = VFXBuilders.createWorld()
            .setRenderType(LodestoneRenderTypeRegistry.applyUniformChanges(
                renderType
            ) { s: ShaderInstance ->
                s.safeGetUniform("RingCount").set(10f)
                s.safeGetUniform("RingSpeed").set(30f)
                s.safeGetUniform("CycleDuration").set(1f)
                s.safeGetUniform("TunnelElongation").set(0.25f)
                s.safeGetUniform("RotationSpeed").set(0f)
            })

        poseStack.pushPose()
        poseStack.translate(0.5, 0.5, 0.5)
        val cam = Minecraft.getInstance().gameRenderer.mainCamera
        poseStack.mulPose(cam.rotation())
        poseStack.mulPose(Axis.XP.rotationDegrees(180f))
        var sawBuilder = VFXBuilders.createWorld().setRenderType(rt).setColor(Color(1f, 1f, 1f)).setAlpha(0.55f)
        poseStack.translate(0f,0f,0.05f)
        sawBuilder.renderQuad(poseStack, 0.05f)
        sawBuilder = VFXBuilders.createWorld().setRenderType(rt).setColor(Color(1f, 1f, 1f)).setAlpha(0.45f)
        sawBuilder.renderQuad(poseStack, 0.1f)
        sawBuilder = VFXBuilders.createWorld().setRenderType(rt).setColor(Color(1f, 1f, 1f)).setAlpha(0.35f)
        sawBuilder.renderQuad(poseStack, 0.15f)
        poseStack.translate(0f,0f,-0.05f)

        builder.renderQuad(poseStack, 0.5f)

        builder = VFXBuilders.createWorld()
            .setRenderType(LodestoneRenderTypeRegistry.applyUniformChanges(
                copy
            ) { s: ShaderInstance ->
                s.safeGetUniform("RotationSpeed").set(1000f)
            })

        builder.renderQuad(poseStack, 0.4f)

        builder = VFXBuilders.createWorld()
            .setRenderType(LodestoneRenderTypeRegistry.applyUniformChanges(
                copy2
            ) { s: ShaderInstance ->
                s.safeGetUniform("RotationSpeed").set(2000f)
            })

        builder.renderQuad(poseStack, 0.3f)



        poseStack.popPose()
    }
}