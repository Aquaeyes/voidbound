package dev.sterner.api.util

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import com.sammy.malum.client.RenderUtils
import dev.sterner.VoidBound
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry
import team.lodestar.lodestone.systems.rendering.VFXBuilders
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken
import java.awt.Color


object VoidBoundRenderUtils {

    val CHECKMARK: ResourceLocation = VoidBound.id("textures/gui/check.png")

    fun renderMarker(resource: ResourceLocation, poseStack: PoseStack, x: Int, y: Int, alpha: Float) {
        val scale = 1f
        poseStack.pushPose()
        poseStack.scale(scale, scale, 1.0f)
        renderIcon(resource, poseStack, x, y, alpha)

        poseStack.popPose()
    }

    private fun renderIcon(icon: ResourceLocation, poseStack: PoseStack, x: Int, y: Int, alpha: Float) {
        renderIcon(icon, poseStack, x, y, 16, 16, 0f, 1f, 0f, 1f, alpha)
    }

    private fun renderIcon(
        icon: ResourceLocation,
        poseStack: PoseStack,
        x: Int,
        y: Int,
        w: Int,
        h: Int,
        u0: Float,
        u1: Float,
        v0: Float,
        v1: Float,
        alpha: Float
    ) {
        val matrix = poseStack.last().pose()

        Minecraft.getInstance().textureManager.getTexture(icon).setFilter(false, false)
        RenderSystem.setShaderTexture(0, icon)

        RenderSystem.setShader { GameRenderer.getPositionTexColorShader() }
        val bufferbuilder = Tesselator.getInstance().builder
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR)
        bufferbuilder.vertex(matrix, x.toFloat(), (y + h).toFloat(), 0f).uv(u0, v1).color(1.0f, 1.0f, 1.0f, alpha).endVertex()
        bufferbuilder.vertex(matrix, (x + w).toFloat(), (y + h).toFloat(), 0f).uv(u1, v1).color(1.0f, 1.0f, 1.0f, alpha).endVertex()
        bufferbuilder.vertex(matrix, (x + w).toFloat(), y.toFloat(), 0f).uv(u1, v0).color(1.0f, 1.0f, 1.0f, alpha).endVertex()
        bufferbuilder.vertex(matrix, x.toFloat(), y.toFloat(), 0f).uv(u0, v0).color(1.0f, 1.0f, 1.0f, alpha).endVertex()

        BufferUploader.drawWithShader(bufferbuilder.end())
    }

    fun drawIcon(matrixStack: PoseStack, icon: ResourceLocation) {
        matrixStack.pushPose()
        val matrix: Matrix4f = matrixStack.last().pose()
        val tessellator = Tesselator.getInstance()
        val bufferBuilder: BufferBuilder = tessellator.builder
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.disableDepthTest()
        matrixStack.translate(12.0, 12.0, 0.0)
        RenderSystem.setShaderTexture(0, icon)
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)
        bufferBuilder.vertex(matrix, -2f, 6f, 0f).uv(0f, 1f).endVertex()
        bufferBuilder.vertex(matrix, 6f, 6f, 0f).uv(1f, 1f).endVertex()
        bufferBuilder.vertex(matrix, 6f, -2f, 0f).uv(1f, 0f).endVertex()
        bufferBuilder.vertex(matrix, -2f, -2f, 0f).uv(0f, 0f).endVertex()
        tessellator.end()
        matrixStack.popPose()
    }

    fun drawRawIcon(matrixStack: PoseStack, icon: ResourceLocation) {
        matrixStack.pushPose()
        val matrix: Matrix4f = matrixStack.last().pose()
        val tessellator = Tesselator.getInstance()
        val bufferBuilder: BufferBuilder = tessellator.builder
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.setShaderTexture(0, icon)
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)
        bufferBuilder.vertex(matrix, 0f, 16f, 0f).uv(0f, 1f).endVertex()
        bufferBuilder.vertex(matrix, 16f, 16f, 0f).uv(1f, 1f).endVertex()
        bufferBuilder.vertex(matrix, 16f, 0f, 0f).uv(1f, 0f).endVertex()
        bufferBuilder.vertex(matrix, 0f, 0f, 0f).uv(0f, 0f).endVertex()
        tessellator.end()
        matrixStack.popPose()
    }

    fun renderCubeAtPos(
        camera: Camera,
        poseStack: PoseStack,
        blockPos: BlockPos,
        renderTypeToken: RenderTypeToken,
        ticksRemaining: Int,
        totalTicks: Int
    ) {
        val alpha = 0.5f * (ticksRemaining / totalTicks.toFloat())
        renderCubeAtPos(camera, poseStack, blockPos, renderTypeToken, Color(255, 200, 150), alpha, 1.08f)
    }

    fun renderCubeAtPos(
        camera: Camera,
        poseStack: PoseStack,
        blockPos: BlockPos,
        renderTypeToken: RenderTypeToken
    ) {
        renderCubeAtPos(camera, poseStack, blockPos, renderTypeToken, Color(255, 200, 150), 0.5f, 1.08f)
    }

    fun renderCubeAtPos(
        camera: Camera,
        poseStack: PoseStack,
        blockPos: BlockPos,
        renderTypeToken: RenderTypeToken,
        color: Color,
        alpha: Float,
        scale: Float
    ) {
        val targetPosition = Vec3(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble())
        val transformedPosition: Vec3 = targetPosition.subtract(camera.position)


        poseStack.pushPose()

        poseStack.translate(transformedPosition.x, transformedPosition.y, transformedPosition.z)

        val builder = VFXBuilders.createWorld()
            .setRenderType(LodestoneRenderTypeRegistry.ADDITIVE_TEXTURE.applyAndCache(renderTypeToken))
        val cubeVertexData = RenderUtils.makeCubePositions(1f)

        RenderUtils.drawCube(
            poseStack,
            builder.setColor(color, alpha),
            scale,
            cubeVertexData
        )


        poseStack.popPose()
    }
}