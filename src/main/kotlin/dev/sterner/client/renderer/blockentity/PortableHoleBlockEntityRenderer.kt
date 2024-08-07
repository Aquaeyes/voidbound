package dev.sterner.client.renderer.blockentity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import dev.sterner.common.blockentity.PortableHoleBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState
import org.joml.Matrix4f

class PortableHoleBlockEntityRenderer(ctx: BlockEntityRendererProvider.Context) :
    BlockEntityRenderer<PortableHoleBlockEntity> {


    override fun render(
        blockEntity: PortableHoleBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        this.renderSides(
            blockEntity,
            poseStack.last().pose(),
            buffer.getBuffer(RenderType.endPortal())
        )
    }


    private fun shouldDrawSide(direction: Direction, entity: PortableHoleBlockEntity): Boolean {
        val state: BlockState =
            Minecraft.getInstance().level!!.getBlockState(entity.blockPos.relative(direction.opposite))
        return !state.isAir && state.isSolid
    }

    private fun renderSides(entity: PortableHoleBlockEntity, matrix4f: Matrix4f, vertexConsumer: VertexConsumer) {
        this.renderSide(
            entity,
            matrix4f,
            vertexConsumer,
            0.0001f,
            0.9999f,
            0.0001f,
            0.9999f,
            0.9999f,
            0.9999f,
            0.9999f,
            0.9999f,
            Direction.NORTH
        )
        this.renderSide(
            entity,
            matrix4f,
            vertexConsumer,
            0.0001f,
            0.9999f,
            0.9999f,
            0.0001f,
            0.0001f,
            0.0001f,
            0.0001f,
            0.0001f,
            Direction.SOUTH
        )
        this.renderSide(
            entity,
            matrix4f,
            vertexConsumer,
            0.9999f,
            0.9999f,
            0.9999f,
            0.0001f,
            0.0001f,
            0.9999f,
            0.9999f,
            0.0001f,
            Direction.WEST
        )
        this.renderSide(
            entity,
            matrix4f,
            vertexConsumer,
            0.0001f,
            0.0001f,
            0.0001f,
            0.9999f,
            0.0001f,
            0.9999f,
            0.9999f,
            0.0001f,
            Direction.EAST
        )
        this.renderSide(
            entity,
            matrix4f,
            vertexConsumer,
            0.0001f,
            0.9999f,
            0.9999f,
            0.9999f,
            0.0001f,
            0.0001f,
            0.9999f,
            0.9999f,
            Direction.DOWN
        )
        this.renderSide(
            entity,
            matrix4f,
            vertexConsumer,
            0.0001f,
            0.9999f,
            0.0001f,
            0.0001f,
            0.9999f,
            0.9999f,
            0.0001f,
            0.0001f,
            Direction.UP
        )
    }

    private fun renderSide(
        entity: PortableHoleBlockEntity,
        model: Matrix4f,
        vertices: VertexConsumer,
        x1: Float,
        x2: Float,
        y1: Float,
        y2: Float,
        z1: Float,
        z2: Float,
        z3: Float,
        z4: Float,
        direction: Direction
    ) {
        if (shouldDrawSide(direction, entity)) {
            addVertex(vertices, model, x1, y1, z1, 0f, 0f)
            addVertex(vertices, model, x2, y1, z2, 1f, 0f)
            addVertex(vertices, model, x2, y2, z3, 1f, 1f)
            addVertex(vertices, model, x1, y2, z4, 0f, 1f)
            addVertex(vertices, model, x1, y2, z1, 0f, 1f)
            addVertex(vertices, model, x2, y2, z2, 1f, 1f)
            addVertex(vertices, model, x2, y1, z3, 1f, 0f)
            addVertex(vertices, model, x1, y1, z4, 0f, 0f)
        }
    }

    private fun addVertex(vertices: VertexConsumer, model: Matrix4f, x: Float, y: Float, z: Float, u: Float, v: Float) {
        vertices.vertex(model, x, y, z).uv(u, v).endVertex()
    }


}