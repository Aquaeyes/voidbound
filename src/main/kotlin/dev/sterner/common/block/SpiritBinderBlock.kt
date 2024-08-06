package dev.sterner.common.block

import com.sammy.malum.registry.common.item.ItemRegistry
import dev.sterner.api.blockentity.Modifier
import dev.sterner.common.blockentity.SpiritBinderBlockEntity
import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.world.Containers
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class SpiritBinderBlock(properties: Properties) : BaseEntityBlock(
    properties.noOcclusion()
) {

    init {
        registerDefaultState(defaultBlockState().setValue(MODIFIER, Modifier.NONE))
    }

    override fun use(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hit: BlockHitResult
    ): InteractionResult {

        if (state.getValue(MODIFIER) == Modifier.NONE) {
            if (player.mainHandItem.`is`(ItemRegistry.BLOCK_OF_BRILLIANCE.get())) {
                level.setBlockAndUpdate(pos, state.setValue(MODIFIER, Modifier.BRILLIANT))
                player.mainHandItem.shrink(1)
                return InteractionResult.SUCCESS
            }
            if (player.mainHandItem.`is`(ItemRegistry.BLOCK_OF_HEX_ASH.get())) {
                level.setBlockAndUpdate(pos, state.setValue(MODIFIER, Modifier.HEX_ASH))
                player.mainHandItem.shrink(1)
                return InteractionResult.SUCCESS
            }
        } else {
            if (state.getValue(MODIFIER) == Modifier.BRILLIANT) {
                Containers.dropItemStack(
                    level,
                    pos.x + 0.5,
                    pos.y + 1.0,
                    pos.z + 0.5,
                    ItemStack(ItemRegistry.BLOCK_OF_BRILLIANCE.get())
                )
                level.setBlockAndUpdate(pos, state.setValue(MODIFIER, Modifier.NONE))
                return InteractionResult.SUCCESS
            } else if (state.getValue(MODIFIER) == Modifier.HEX_ASH) {
                Containers.dropItemStack(
                    level,
                    pos.x + 0.5,
                    pos.y + 1.0,
                    pos.z + 0.5,
                    ItemStack(ItemRegistry.BLOCK_OF_HEX_ASH.get())
                )
                level.setBlockAndUpdate(pos, state.setValue(MODIFIER, Modifier.NONE))
                return InteractionResult.SUCCESS
            }
        }

        return super.use(state, level, pos, player, hand, hit)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return VoidBoundBlockEntityTypeRegistry.SPIRIT_BINDER.get().create(pos, state)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(MODIFIER)
    }

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        blockState: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T> {
        return BlockEntityTicker { _, _, _, blockEntity ->
            if (blockEntity is SpiritBinderBlockEntity) {
                (blockEntity as SpiritBinderBlockEntity).tick()
            }
        }
    }

    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return makeShape()
    }


    companion object {

        var MODIFIER: EnumProperty<Modifier> = EnumProperty.create("modifier", Modifier::class.java)

        fun makeShape(): VoxelShape {
            var shape = Shapes.empty()
            shape = Shapes.join(shape, Shapes.box(0.0, 0.0, 0.0, 16 / 16.0, 5 / 16.0, 16 / 16.0), BooleanOp.OR)
            shape =
                Shapes.join(shape, Shapes.box(3 / 16.0, 0.0, 3 / 16.0, 13 / 16.0, 10 / 16.0, 13 / 16.0), BooleanOp.OR)

            return shape
        }
    }
}