package dev.sterner.blockentity

import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class SpiritBinderBlockEntity(pos: BlockPos, blockState: BlockState) : BlockEntity(VoidBoundBlockEntityTypeRegistry.SPIRIT_BINDER.get(), pos,
    blockState
) {
}