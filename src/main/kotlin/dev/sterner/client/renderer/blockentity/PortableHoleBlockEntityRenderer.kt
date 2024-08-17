package dev.sterner.client.renderer.blockentity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import dev.sterner.common.blockentity.PortableHoleBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.shapes.VoxelShape
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


        return !state.isAir
    }

    private fun renderSide(
        entity: PortableHoleBlockEntity,
        model: Matrix4f,
        vertices: VertexConsumer,
        aabb: AABB,
        camX: Float,
        camY: Float,
        camZ: Float,
        direction: Direction
    ) {
        if (shouldDrawSide(direction, entity)) {
            val x1 = (aabb.minX - camX).toFloat()
            val x2 = (aabb.maxX - camX).toFloat()
            val y1 = (aabb.minY - camY).toFloat()
            val y2 = (aabb.maxY - camY).toFloat()
            val z1 = (aabb.minZ - camZ).toFloat()
            val z2 = (aabb.maxZ - camZ).toFloat()

            when (direction) {
                Direction.SOUTH -> { // Rendering NORTH (minZ)
                    addVertex(vertices, model, x1, y1, z1, 0f, 0f)
                    addVertex(vertices, model, x1, y2, z1, 1f, 0f)
                    addVertex(vertices, model, x2, y2, z1, 1f, 1f)
                    addVertex(vertices, model, x2, y1, z1, 0f, 1f)
                }
                Direction.NORTH -> { // Rendering SOUTH (maxZ)
                    addVertex(vertices, model, x1, y1, z2, 0f, 0f)
                    addVertex(vertices, model, x2, y1, z2, 1f, 0f)
                    addVertex(vertices, model, x2, y2, z2, 1f, 1f)
                    addVertex(vertices, model, x1, y2, z2, 0f, 1f)
                }
                Direction.WEST -> { // Rendering EAST (maxX)
                    addVertex(vertices, model, x2, y1, z1, 0f, 0f)
                    addVertex(vertices, model, x2, y2, z1, 1f, 0f)
                    addVertex(vertices, model, x2, y2, z2, 1f, 1f)
                    addVertex(vertices, model, x2, y1, z2, 0f, 1f)
                }
                Direction.EAST -> { // Fix for WEST (minX)
                    addVertex(vertices, model, x1, y1, z1, 0f, 0f)
                    addVertex(vertices, model, x1, y1, z2, 1f, 0f)
                    addVertex(vertices, model, x1, y2, z2, 1f, 1f)
                    addVertex(vertices, model, x1, y2, z1, 0f, 1f)
                }
                Direction.DOWN -> { // Rendering UP (maxY)
                    addVertex(vertices, model, x1, y2, z1, 0f, 0f)
                    addVertex(vertices, model, x1, y2, z2, 1f, 0f)
                    addVertex(vertices, model, x2, y2, z2, 1f, 1f)
                    addVertex(vertices, model, x2, y2, z1, 0f, 1f)
                }
                Direction.UP -> { // Fix for DOWN (minY)
                    addVertex(vertices, model, x1, y1, z1, 0f, 0f)
                    addVertex(vertices, model, x2, y1, z1, 1f, 0f)
                    addVertex(vertices, model, x2, y1, z2, 1f, 1f)
                    addVertex(vertices, model, x1, y1, z2, 0f, 1f)
                }
            }
        }
    }

    private fun addVertex(vertices: VertexConsumer, model: Matrix4f, x: Float, y: Float, z: Float, u: Float, v: Float) {
        vertices.vertex(model, x, y, z).uv(u, v).endVertex()
    }

    private fun renderSides(entity: PortableHoleBlockEntity, matrix4f: Matrix4f, vertexConsumer: VertexConsumer) {
        val camX = entity.blockPos.x.toFloat()
        val camY = entity.blockPos.y.toFloat()
        val camZ = entity.blockPos.z.toFloat()

        // Loop through all neighboring positions in each direction
        for (direction in Direction.values()) {
            val neighborPos = entity.blockPos.relative(direction.opposite)
            val blockState = entity.level!!.getBlockState(neighborPos)

            if (!blockState.`is`(Blocks.AIR)) {
                val voxelShape = blockState.getShape(entity.level!!, neighborPos)

                // Iterate through each AABB part of the voxelShape
                for (aabb in voxelShape.toAabbs()) {
                    val aabb2 = aabb.move(neighborPos).inflate(0.002)
                    renderSide(entity, matrix4f, vertexConsumer, aabb2, camX, camY, camZ, direction.opposite)
                }
            }
        }
    }
}