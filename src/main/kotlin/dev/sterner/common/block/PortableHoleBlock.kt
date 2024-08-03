package dev.sterner.common.block

import dev.sterner.common.blockentity.PortableHoleBlockEntity
import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class PortableHoleBlock(properties: Properties) : BaseEntityBlock(properties) {

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return VoidBoundBlockEntityTypeRegistry.PORTABLE_HOLE.get().create(pos, state)
    }

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        blockState: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T> {
        return BlockEntityTicker { _, _, _, blockEntity ->
            if (blockEntity is PortableHoleBlockEntity) {
                (blockEntity as PortableHoleBlockEntity).tick()
            }
        }
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return Shapes.empty()
    }

    fun createWithData(
        pos: BlockPos,
        oldState: BlockState,
        oldEntity: BlockEntity?,
        direction: Direction,
        distance: Int
    ): BlockEntity {
        return PortableHoleBlockEntity(pos, oldState, oldEntity, direction, distance)
    }
}