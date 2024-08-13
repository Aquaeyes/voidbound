package dev.sterner.common.block

import dev.sterner.common.blockentity.OsmoticEnchanterBlockEntity
import dev.sterner.common.blockentity.PortableHoleBlockEntity
import dev.sterner.common.menu.OsmoticEnchanterMenu
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Container
import net.minecraft.world.Containers
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class OsmoticEnchanterBlock(properties: Properties?) : BaseEntityBlock(properties) {

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return OsmoticEnchanterBlockEntity(pos, state)
    }

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        blockState: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T> {
        return BlockEntityTicker { _, _, _, blockEntity ->
            if (blockEntity is OsmoticEnchanterBlockEntity) {
                (blockEntity as OsmoticEnchanterBlockEntity).tick()
            }
        }
    }

    override fun use(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hit: BlockHitResult
    ): InteractionResult {

        if (level.getBlockEntity(pos) is OsmoticEnchanterBlockEntity) {
            val osmoticEnchanter = level.getBlockEntity(pos) as OsmoticEnchanterBlockEntity

            player.openMenu(object : ExtendedScreenHandlerFactory {
                override fun writeScreenOpeningData(player: ServerPlayer, buf: FriendlyByteBuf) {
                    buf.writeBlockPos(pos)
                }

                override fun getDisplayName(): Component {
                    return Component.translatable("osmotic_enchanter")
                }

                override fun createMenu(i: Int, inventory: Inventory, player: Player): AbstractContainerMenu {
                    return OsmoticEnchanterMenu(i, inventory, pos)
                }
            })
        }



        return super.use(state, level, pos, player, hand, hit)
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean
    ) {
        if (state.`is`(newState.block)) {
            return
        }
        val blockEntity = level.getBlockEntity(pos)
        if (blockEntity is OsmoticEnchanterBlockEntity) {
            Containers.dropContents(level, pos, blockEntity.inventory)
            level.updateNeighbourForOutputSignal(pos, this)
        }
        super.onRemove(state, level, pos, newState, movedByPiston)
    }

    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {

        val o = Shapes.box(0.0, 0.0, 0.0, 1.0, 2.0/16, 1.0)
        val i = Shapes.box(6.0/16, 2.0/16, 6.0/16, 10.0/16, 4.0/16, 10.0/16)
        val j = Shapes.box(2.0/16, 6.0/16, 2.0/16, 14.0/16, 8.0/16, 14.0/16)

        return Shapes.join(j, Shapes.join(o, i, BooleanOp.OR), BooleanOp.OR)
    }
}