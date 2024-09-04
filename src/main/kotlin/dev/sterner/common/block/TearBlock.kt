package dev.sterner.common.block

import dev.sterner.VoidBound
import dev.sterner.api.util.VoidBoundUtils
import dev.sterner.common.components.VoidBoundRevelationComponent
import dev.sterner.registry.VoidBoundComponentRegistry
import dev.sterner.registry.VoidBoundItemRegistry
import dev.sterner.registry.VoidBoundPacketRegistry
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Containers
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class TearBlock(val type: Type, properties: Properties) : CloakedTearBlock(properties) {

    override fun playerDestroy(
        level: Level,
        player: Player,
        pos: BlockPos,
        state: BlockState,
        blockEntity: BlockEntity?,
        tool: ItemStack
    ) {

        val comp = VoidBoundComponentRegistry.VOID_BOUND_REVELATION_COMPONENT.get(player)
        if (comp.isTearKnowledgeComplete()) {

            var item = VoidBoundItemRegistry.TEAR_OF_ENDER.get()
            if (type == Type.ENDER) {
                item = VoidBoundItemRegistry.TEAR_OF_CRIMSON.get()
            }

            Containers.dropItemStack(level, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, ItemStack(item))
        }

        super.playerDestroy(level, player, pos, state, blockEntity, tool)
    }

    enum class Type {
        CRIMSON,
        ENDER
    }
}