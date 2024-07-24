package dev.sterner.blockentity

import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class SpiritStabilizerBlockEntity(pos: BlockPos, blockState: BlockState) : BlockEntity(
    VoidBoundBlockEntityTypeRegistry.SPIRIT_BINDER_STABILIZER.get(), pos,
    blockState
)