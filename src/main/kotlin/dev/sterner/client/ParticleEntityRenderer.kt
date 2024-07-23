package dev.sterner.client

import com.mojang.blaze3d.vertex.PoseStack
import com.sammy.malum.client.RenderUtils
import com.sammy.malum.client.SpiritBasedWorldVFXBuilder
import com.sammy.malum.client.renderer.entity.FloatingItemEntityRenderer
import com.sammy.malum.core.systems.spirit.MalumSpiritType
import com.sammy.malum.registry.client.MalumRenderTypeTokens
import dev.sterner.VoidBound
import dev.sterner.entity.ParticleEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry
import java.awt.Color

class ParticleEntityRenderer(context: EntityRendererProvider.Context) : EntityRenderer<ParticleEntity>(context) {

    override fun render(
        entity: ParticleEntity,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int
    ) {

        val spiritType: MalumSpiritType? = entity.spiritType
        if (spiritType != null) {
            val trailBuilder =
                SpiritBasedWorldVFXBuilder.create(spiritType).setRenderType(LodestoneRenderTypeRegistry.ADDITIVE_TEXTURE_TRIANGLE.applyAndCache(MalumRenderTypeTokens.CONCENTRATED_TRAIL))
            RenderUtils.renderEntityTrail(
                poseStack,
                trailBuilder,
                entity.trailPointBuilder,
                entity,
                spiritType.primaryColor,
                spiritType.secondaryColor,
                1.0f,
                partialTick
            )
        }

        FloatingItemEntityRenderer.renderSpiritGlimmer(poseStack, Color(200,200,255,200), Color(100,100,255,200), partialTick)
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight)
    }

    override fun getTextureLocation(entity: ParticleEntity): ResourceLocation {
        return ResourceLocation(VoidBound.modid, "particles/$entity")
    }
}