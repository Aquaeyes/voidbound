package dev.sterner.blockentity

import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class DestabilizedSpiritRiftBlockEntity(pos: BlockPos, state: BlockState?) : SyncedBlockEntity(VoidBoundBlockEntityTypeRegistry.DESTABILIZED_SPIRIT_RIFT.get(), pos, state) {
}