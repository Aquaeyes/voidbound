package dev.sterner.block

import dev.sterner.blockentity.SpiritBinderBlockEntity
import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.context.BlockPlaceContext
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
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class SpiritBinderBlock(properties: Properties) : BaseEntityBlock(
    properties.noOcclusion()
) {

    init {
        registerDefaultState(stateDefinition.any().setValue(MODIFIER, Modifier.NONE))
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
        return super.getStateForPlacement(context)!!.setValue(MODIFIER, Modifier.NONE)
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

    enum class Modifier(name: String) : StringRepresentable {
        NONE("none"),
        BRILLIANT("brilliant"),
        CTHONIC("cthonic"),
        HEX_ASH("hex_ash");

        override fun getSerializedName(): String {
            return this.name
        }
    }

    companion object {

        val MODIFIER = EnumProperty.create("modifier", Modifier::class.java)

        fun makeShape(): VoxelShape {
            var shape = Shapes.empty()
            shape =
                Shapes.join(shape, Shapes.box(1 / 16.0, 0.0, 1 / 16.0, 15 / 16.0, 4 / 16.0, 15 / 16.0), BooleanOp.OR)
            shape =
                Shapes.join(shape, Shapes.box(13 / 16.0, 0.0, 5 / 16.0, 16 / 16.0, 6 / 16.0, 11 / 16.0), BooleanOp.OR)
            shape = Shapes.join(shape, Shapes.box(5 / 16.0, 0.0, 0.0, 11 / 16.0, 6 / 16.0, 3 / 16.0), BooleanOp.OR)
            shape =
                Shapes.join(shape, Shapes.box(5 / 16.0, 0.0, 13 / 16.0, 11 / 16.0, 6 / 16.0, 16 / 16.0), BooleanOp.OR)
            shape = Shapes.join(shape, Shapes.box(0.0, 0.0, 5 / 16.0, 3 / 16.0, 6 / 16.0, 11 / 16.0), BooleanOp.OR)

            shape =
                Shapes.join(shape, Shapes.box(3 / 16.0, 0.0, 3 / 16.0, 13 / 16.0, 10 / 16.0, 13 / 16.0), BooleanOp.OR)


            shape = Shapes.join(
                shape,
                Shapes.box(1 / 16.0, 8 / 16.0, 1 / 16.0, 5 / 16.0, 14 / 16.0, 5 / 16.0),
                BooleanOp.OR
            )
            shape = Shapes.join(
                shape,
                Shapes.box(1 / 16.0, 8 / 16.0, 11 / 16.0, 5 / 16.0, 14 / 16.0, 15 / 16.0),
                BooleanOp.OR
            )

            shape = Shapes.join(
                shape,
                Shapes.box(11 / 16.0, 8 / 16.0, 1 / 16.0, 15 / 16.0, 14 / 16.0, 5 / 16.0),
                BooleanOp.OR
            )
            shape = Shapes.join(
                shape,
                Shapes.box(11 / 16.0, 8 / 16.0, 11 / 16.0, 15 / 16.0, 14 / 16.0, 15 / 16.0),
                BooleanOp.OR
            )
            return shape
        }
    }
}