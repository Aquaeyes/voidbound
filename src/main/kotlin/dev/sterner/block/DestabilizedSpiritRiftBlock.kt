package dev.sterner.block

import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class DestabilizedSpiritRiftBlock(properties: Properties) : BaseEntityBlock(properties) {

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return VoidBoundBlockEntityTypeRegistry.DESTABILIZED_SPIRIT_RIFT.get().create(pos, state)
    }
}