package dev.sterner.common.block

import dev.sterner.api.blockentity.SyncedBlockEntity
import dev.sterner.common.blockentity.OsmoticEnchanterBlockEntity
import dev.sterner.common.menu.OsmoticEnchanterMenu
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import team.lodestar.lodestone.systems.block.LodestoneEntityBlock

class OsmoticEnchanterBlock(properties: Properties?) : BaseEntityBlock(properties) {

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return OsmoticEnchanterBlockEntity(pos, state)
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

    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.MODEL
    }
}