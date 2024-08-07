package dev.sterner.common.block

import dev.sterner.common.blockentity.SpiritRiftBlockEntity
import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import net.minecraft.core.BlockPos
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

class DestabilizedSpiritRiftBlock(properties: Properties) :
    BaseEntityBlock(properties.noOcclusion().noCollission().lightLevel { 10 }) {

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return VoidBoundBlockEntityTypeRegistry.DESTABILIZED_SPIRIT_RIFT.get().create(pos, state)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return Shapes.box(5 / 16.0, 5 / 16.0, 5 / 16.0, 11 / 16.0, 11 / 16.0, 11 / 16.0)
    }

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T> {
        return BlockEntityTicker { _, _, _, blockEntity ->
            if (blockEntity is SpiritRiftBlockEntity) {
                (blockEntity as SpiritRiftBlockEntity).tick()
            }
        }
    }
}