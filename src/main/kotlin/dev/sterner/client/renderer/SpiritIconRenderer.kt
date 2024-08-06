package dev.sterner.client.renderer

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.sterner.VoidBound
import dev.sterner.api.VoidBoundApi
import dev.sterner.api.util.VoidBoundRenderUtils
import dev.sterner.common.blockentity.SpiritBinderBlockEntity.Companion.getSpiritData
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import org.joml.Quaternionf
import org.lwjgl.opengl.GL11


object SpiritIconRenderer {

    @JvmStatic
    fun render(entity: Entity, poseStack: PoseStack, buffers: MultiBufferSource, camera: Quaternionf) {
        val minecraft = Minecraft.getInstance()

        if (entity !is LivingEntity) {
            return
        }

        if (!VoidBoundApi.hasGoggles()) {
            //return
        }

        val entityHeight = entity.getBbHeight() + 0.3f

        poseStack.pushPose()
        poseStack.translate(0.0, entityHeight.toDouble(), 0.0)
        poseStack.mulPose(camera)
        poseStack.mulPose(Axis.YP.rotationDegrees(180f))
        poseStack.scale(0.025f, -0.025f, 0.025f)

        var depthTestEnabled = GL11.glIsEnabled(GL11.GL_DEPTH_TEST)
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.enableDepthTest()

        val spiritDataOptional = getSpiritData(entity)
        if (spiritDataOptional.isPresent) {
            for ((index, spirit) in spiritDataOptional.get().withIndex()) {
                val id = spirit.type.identifier
                poseStack.translate(index * 8f, 0f, index * 0.01f)
                VoidBoundRenderUtils.renderMarker(VoidBound.id("textures/spirit/$id.png"), poseStack, -8, -18, 1f)
            }
        }

        if (depthTestEnabled) {
            RenderSystem.enableDepthTest();
        } else {
            RenderSystem.disableDepthTest();
        }
            poseStack.popPose()
    }
}