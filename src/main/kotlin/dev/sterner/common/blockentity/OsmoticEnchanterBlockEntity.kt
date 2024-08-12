package dev.sterner.common.blockentity

import dev.sterner.api.blockentity.SyncedBlockEntity
import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class OsmoticEnchanterBlockEntity(pos: BlockPos, state: BlockState?) : SyncedBlockEntity(VoidBoundBlockEntityTypeRegistry.OSMOTIC_ENCHANTER.get(), pos,
    state
) {

}