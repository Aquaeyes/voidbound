package dev.sterner.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class TearOfEnderBlock(properties: Properties) : Block(properties) {

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return Shapes.box(6.0/16,12.0/16,6.0/16,10.0/16, 16.0/16, 10.0/16)
    }
}