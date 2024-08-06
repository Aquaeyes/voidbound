package dev.sterner.client.renderer

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

object SpiritIconRenderer {

    @JvmStatic
    fun render(entity: Entity, poseStack: PoseStack, buffers: MultiBufferSource, camera: Quaternionf) {
        val minecraft = Minecraft.getInstance()

        if (entity !is LivingEntity) {
            return
        }

        if (!VoidBoundApi.hasGoggles()) {
            return
        }
        val living = entity
        poseStack.pushPose()
        poseStack.translate(0.0, living.bbHeight + 0.6, 0.0)
        poseStack.pushPose()
        val yRotation = Quaternionf().rotateY(camera.y) // Rotate only around the y-axis
        poseStack.mulPose(Axis.YP.rotation(camera.y * 2))

        poseStack.scale(-0.025f, -0.025f, 0.025f)

        //poseStack.translate(0.0, living.bbHeight * 100.0, 0.0)
        var iconOffset = 0f

        val spiritDataOptional = getSpiritData(entity)
        if (spiritDataOptional.isPresent) {
            for ((index, spirit) in spiritDataOptional.get().withIndex()) {
                val id = spirit.type.identifier
                poseStack.pushPose()
                renderIcon(poseStack, id, iconOffset)
                poseStack.popPose()
                iconOffset += 9F
            }
        }
        poseStack.popPose()
        poseStack.popPose()
    }

    fun renderIcon(poseStack: PoseStack, id: String, iconOffset: Float) {

        val dx: Float = (8 - iconOffset)
        poseStack.translate(dx, 0f, 0f)
        VoidBoundRenderUtils.drawRawIcon(poseStack, VoidBound.id("textures/spirit/$id.png"))

    }
}