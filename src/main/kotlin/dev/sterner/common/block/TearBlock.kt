package dev.sterner.common.block

import de.dafuqs.revelationary.api.revelations.RevelationAware
import dev.sterner.VoidBound
import dev.sterner.registry.VoidBoundBlockRegistry
import dev.sterner.registry.VoidBoundItemRegistry
import io.github.fabricators_of_create.porting_lib.tags.Tags
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Tuple
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.*

class TearBlock(properties: Properties) : Block(properties), RevelationAware {

    init {
        RevelationAware.register(this)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return Shapes.box(6.0/16,13.0/16,6.0/16,10.0/16, 16.0/16, 10.0/16)
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        return level.getBlockState(pos.above()).block == Blocks.END_STONE || level.getBlockState(pos.above()).block == Blocks.NETHERRACK
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        return if (!state.canSurvive(level, pos)) Blocks.AIR.defaultBlockState() else super.updateShape(
            state,
            direction,
            neighborState,
            level,
            pos,
            neighborPos
        )
    }

    override fun onCloak() {
        println("Tear")
        super.onCloak()
    }

    override fun onUncloak() {
        println("UnTear")
        super.onUncloak()
    }

    override fun getCloakAdvancementIdentifier(): ResourceLocation {
        return VoidBound.id("revelationary/ichor_requirement_advancement")
    }

    override fun getBlockStateCloaks(): MutableMap<BlockState, BlockState> {
        val cloaks: MutableMap<BlockState, BlockState> = mutableMapOf()
        cloaks[this.defaultBlockState()] = VoidBoundBlockRegistry.TEAR_CLOAK.get().defaultBlockState()
        return cloaks
    }

    override fun getItemCloak(): Tuple<Item, Item> {
        return Tuple(this.asItem(), Items.STICK)
    }
}