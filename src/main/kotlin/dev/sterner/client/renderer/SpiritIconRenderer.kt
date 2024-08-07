package dev.sterner.client.renderer

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.sterner.VoidBound
import dev.sterner.api.VoidBoundApi
import dev.sterner.api.util.VoidBoundRenderUtils
import dev.sterner.api.util.VoidBoundUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import org.joml.Quaternionf
import org.lwjgl.opengl.GL11
import kotlin.math.sqrt


object SpiritIconRenderer {

    @JvmStatic
    fun render(entity: Entity, poseStack: PoseStack, buffers: MultiBufferSource, camera: Quaternionf) {
        val minecraft = Minecraft.getInstance()

        if (minecraft.player == null) {
            return
        }

        if (entity !is LivingEntity) {
            return
        }

        if (!VoidBoundApi.hasGoggles()) {
            return
        }

        val squareDistance: Double = minecraft.player!!.distanceToSqr(entity)
        val maxDistance = 12.0
        if (squareDistance > maxDistance * maxDistance) {
            return
        }

        val startFade: Double = ((1.0 - (25.0 / 100.0)) * maxDistance)
        val currentAlpha = Mth.clamp(1.0 - ((sqrt(squareDistance) - startFade) / (maxDistance - startFade)), 0.0, 0.85).toFloat()

        val entityHeight = entity.nameTagOffsetY - 0.2f

        poseStack.pushPose()
        poseStack.translate(0.0, entityHeight.toDouble(), 0.0)
        poseStack.mulPose(camera)
        poseStack.scale(0.025f, -0.025f, 0.025f)

        val depthTestEnabled = GL11.glIsEnabled(GL11.GL_DEPTH_TEST)
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.enableDepthTest()

        val spiritDataOptional = VoidBoundUtils.getSpiritData(entity)
        if (spiritDataOptional.isPresent) {
            val o = spiritDataOptional.get().size
            poseStack.translate(0f - o * 6, 0f, 0f)
            for ((index, spirit) in spiritDataOptional.get().withIndex()) {
                val id = spirit.type.identifier
                poseStack.translate(10f, 0f, index * 0.01f)
                VoidBoundRenderUtils.renderWobblyWorldIcon(
                    VoidBound.id("textures/spirit/$id.png"),
                    poseStack,
                    currentAlpha
                )
            }
        }

        if (depthTestEnabled) {
            RenderSystem.enableDepthTest()
        } else {
            RenderSystem.disableDepthTest()
        }

        poseStack.popPose()
    }
}