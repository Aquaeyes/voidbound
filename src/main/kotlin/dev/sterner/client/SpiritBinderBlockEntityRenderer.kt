package dev.sterner.client

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexFormat
import com.sammy.malum.client.SpiritBasedWorldVFXBuilder
import com.sammy.malum.client.renderer.entity.FloatingItemEntityRenderer
import dev.sterner.VoidBound
import dev.sterner.blockentity.SpiritBinderBlockEntity
import dev.sterner.registry.VoidBoundRenderTypes
import dev.sterner.registry.VoidBoundShaders
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.resources.ResourceLocation
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder
import team.lodestar.lodestone.systems.rendering.VFXBuilders
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken
import java.awt.Color


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

        val alpha = 0.4f

        val renderType = VoidBoundRenderTypes.TRANSPARENT_GLOW_TEXTURE.applyWithModifierAndCache(
            TOKEN
        ) { b: LodestoneCompositeStateBuilder ->
            b.replaceVertexFormat(
                VertexFormat.Mode.TRIANGLES
            )
        }

        val builder = VFXBuilders.createWorld()
            .setRenderType(LodestoneRenderTypeRegistry.applyUniformChanges(
                renderType
            ) { s: ShaderInstance ->
                s.safeGetUniform("Alpha").set(alpha)
            })

        builder.renderSphere(poseStack,
            0.5f,
            20,
            20)


        FloatingItemEntityRenderer.renderSpiritGlimmer(poseStack, Color(255,255,255,255), Color(100,100,255,255), partialTick)
        poseStack.popPose()
    }

    companion object {
        var TOKEN = RenderTypeToken.createCachedToken(
            ResourceLocation(VoidBound.modid, "textures/aura.png")
        )


    }
}