package dev.sterner.api.util

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import com.mojang.math.Axis
import com.sammy.malum.client.RenderUtils
import com.sammy.malum.client.renderer.block.TotemPoleRenderer
import dev.sterner.VoidBound
import dev.sterner.api.ClientTickHandler
import net.minecraft.client.Camera
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f
import org.joml.Vector3f
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry
import team.lodestar.lodestone.systems.easing.Easing
import team.lodestar.lodestone.systems.rendering.VFXBuilders
import team.lodestar.lodestone.systems.rendering.VFXBuilders.ScreenVFXBuilder
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken
import java.awt.Color


object VoidBoundRenderUtils {

    val CHECKMARK: ResourceLocation = VoidBound.id("textures/gui/check.png")

    /**
     * Render a texture on screen with ScreenVFXBuilder
     */
    fun drawScreenIcon(
        texture: ResourceLocation,
        poseStack: PoseStack,
        builder: ScreenVFXBuilder,
        x: Int,
        y: Float,
        u: Float,
        v: Float,
        width: Float,
        height: Float,
        textureWidth: Int,
        textureHeight: Int
    ) {
        RenderSystem.enableBlend()
        RenderSystem.enableDepthTest()
        builder.setPositionWithWidth(x.toFloat(), y, width, height)
            .setZLevel(0)
            .setShaderTexture(texture)
            .setUVWithWidth(u, v, width, height, textureWidth.toFloat(), textureHeight.toFloat())
            .draw(poseStack)
        RenderSystem.disableDepthTest()
        RenderSystem.disableBlend()
    }

    /**
     * Render a texture on screen with minecraft's BufferBuilder
     */
    fun drawScreenIcon(matrixStack: PoseStack, icon: ResourceLocation) {
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

    /**
     * Render a texture quad in world towards camera
     */
    fun renderWobblyWorldIcon(
        icon: ResourceLocation,
        poseStack: PoseStack,
        alpha: Float
    ) {
        poseStack.pushPose()
        poseStack.mulPose(Axis.ZP.rotationDegrees(180f))
        val renderType: RenderType =
            LodestoneRenderTypeRegistry.TRANSPARENT_TEXTURE.applyAndCache(RenderTypeToken.createCachedToken(icon))

        val pct: Float = ClientTickHandler.ticksInGame / 20f
        val ease = Easing.SINE_OUT.ease(pct, 0f, 1f, 1f)
        val wobbleStrength: Float = 0.1f - ease * 0.075f

        val positions = arrayOf(
            Vector3f(-0.025f, -0.025f, 1.01f),
            Vector3f(1.025f, -0.025f, 1.01f),
            Vector3f(1.025f, 1.025f, 1.01f),
            Vector3f(-0.025f, 1.025f, 1.01f)
        )

        TotemPoleRenderer.applyWobble(positions, wobbleStrength)

        VFXBuilders.createWorld()
            .setAlpha(alpha)
            .setRenderType(renderType)
            .renderQuad(poseStack, positions, 9f)
        poseStack.popPose()
    }

    /**
     * Uses the renderCubeAtPos function with a set alpha
     */
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

    /**
     * Renders a 1x1x1 block with a RenderTypeToken, scale and alpha ata blockPos in world
     */
    private fun renderCubeAtPos(
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