package dev.sterner.block

import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class SpiritBinderBlock(properties: Properties) : BaseEntityBlock(properties.lightLevel { 4 }) {

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return VoidBoundBlockEntityTypeRegistry.SPIRIT_BINDER.get().create(pos, state)
    }


}