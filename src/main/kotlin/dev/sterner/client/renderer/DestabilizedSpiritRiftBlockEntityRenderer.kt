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


class DestabilizedSpiritRiftBlockEntityRenderer(ctx: BlockEntityRendererProvider.Context) :
    BlockEntityRenderer<DestabilizedSpiritRiftBlockEntity> {

    val frameCount = 32
    val frameHeight = 64
    val texture = VoidBound.id("textures/misc/destabilized_spirit_rift.png")

    val RIFT_TYPE: RenderType = LodestoneRenderTypeRegistry.TRANSPARENT_TEXTURE.apply(
        RenderTypeToken.createCachedToken(
            texture
        )
    )


    override fun render(
        blockEntity: DestabilizedSpiritRiftBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val renderType = VoidBoundRenderTypes.GRAVITY_VORTEX.apply(TOKEN)

        // Create the builder and bind the shader
        var builder = VFXBuilders.createWorld()
            .setRenderType(LodestoneRenderTypeRegistry.applyUniformChanges(
                renderType
            ) { s: ShaderInstance ->
                s.safeGetUniform("RingCount").set(20f)
                s.safeGetUniform("RingSpeed").set(30f)
                s.safeGetUniform("CycleDuration").set(1f)
                s.safeGetUniform("TunnelElongation").set(0.25f)
            })

        //val builder = VFXBuilders.createWorld().setRenderType(VoidBoundRenderTypes.GRAVITY_VORTEX.applyAndCache(Tokens.NOISY))
        poseStack.pushPose()
        poseStack.translate(0.5, 0.5, 0.5)
        val cam = Minecraft.getInstance().gameRenderer.mainCamera
        poseStack.mulPose(cam.rotation())
        poseStack.mulPose(Axis.XP.rotationDegrees(180f))

        builder.renderQuad(poseStack, 0.5f)

        poseStack.popPose()
    }
}