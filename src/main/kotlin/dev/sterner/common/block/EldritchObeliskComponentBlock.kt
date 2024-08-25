package dev.sterner.common.block

import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.BlockState
import team.lodestar.lodestone.systems.multiblock.MultiblockComponentBlock

class EldritchObeliskComponentBlock(properties: Properties?) : MultiblockComponentBlock(properties) {

    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.INVISIBLE
    }
}