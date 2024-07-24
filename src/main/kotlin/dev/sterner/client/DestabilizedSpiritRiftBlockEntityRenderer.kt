package dev.sterner.client

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.sammy.malum.MalumMod.malumPath
import dev.sterner.VoidBound
import dev.sterner.blockentity.DestabilizedSpiritRiftBlockEntity
import dev.sterner.registry.VoidBoundParticleTypeRegistry
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import team.lodestar.lodestone.handlers.RenderHandler
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry
import team.lodestar.lodestone.systems.easing.Easing
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder
import team.lodestar.lodestone.systems.particle.data.GenericParticleData
import team.lodestar.lodestone.systems.rendering.VFXBuilders
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken


class DestabilizedSpiritRiftBlockEntityRenderer(ctx: BlockEntityRendererProvider.Context) : BlockEntityRenderer<DestabilizedSpiritRiftBlockEntity> {

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

        // Calculate the current frame based on the game time
        val gameTime = blockEntity.level!!.gameTime + partialTick
        val frameTime = 1 // Number of ticks per frame
        val currentFrame = ((gameTime / frameTime) % frameCount).toInt()

        // Calculate the texture offset
        val vMin = (currentFrame * frameHeight).toFloat() / 2048.0f
        val vMax = ((currentFrame + 1) * frameHeight).toFloat() / 2048.0f

        // Render the quad with the correct texture coordinates
        //val bufferBuilder = buffer.getBuffer(RenderType.entityTranslucent(texture))

        poseStack.pushPose()
        poseStack.translate(0.5f,0.5f,0.5f)



        /*
        VFXBuilders.createWorld()
            .setFormat(DefaultVertexFormat.PARTICLE)
            .setRenderType(RIFT_TYPE)
            .setUV(0.0f, vMin, 1f, vMax)
            .setColorRaw(1f, 1f, 1f)
            .setAlpha(0.5f)
            .setLight(LightTexture.FULL_BRIGHT)
            .renderQuad(poseStack, 1f)


         */
        poseStack.popPose()


/*
        poseStack.pushPose()
        // Example: rendering a quad with the animated texture
        poseStack.translate(0.5f,0.5f,0.5f)
        bufferBuilder
            .vertex(poseStack.last().pose(), -0.5f, 0.5f, 0.0f)
            .color(255, 255, 255, 255)
            .uv(0.0f, vMin)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(LightTexture.FULL_BRIGHT)
            .normal(1.0f, 0.0f, 0.0f)
            .endVertex()
        bufferBuilder
            .vertex(poseStack.last().pose(), 0.5f, 0.5f, 0.0f)
            .color(255, 255, 255, 255)
            .uv(1.0f, vMin)
            .overlayCoords(packedOverlay)
            .uv2(LightTexture.FULL_BRIGHT)
            .normal(1.0f, 0.0f, 0.0f)
            .endVertex()
        bufferBuilder
            .vertex(poseStack.last().pose(), 0.5f, -0.5f, 0.0f)
            .color(255, 255, 255, 255)
            .uv(1.0f, vMax)
            .overlayCoords(packedOverlay)
            .uv2(LightTexture.FULL_BRIGHT)
            .normal(1.0f, 0.0f, 0.0f)
            .endVertex()
        bufferBuilder
            .vertex(poseStack.last().pose(), -0.5f, -0.5f, 0.0f)
            .color(255, 255, 255, 255)
            .uv(0.0f, vMax)
            .overlayCoords(packedOverlay)
            .uv2(LightTexture.FULL_BRIGHT)
            .normal(1.0f, 0.0f, 0.0f)
            .endVertex()
        poseStack.popPose()


 */

    }
}