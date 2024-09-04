package dev.sterner.mixin_logic

import com.sammy.malum.registry.common.block.BlockRegistry
import dev.sterner.registry.VoidBoundComponentRegistry.Companion.VOID_BOUND_REVELATION_COMPONENT
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

object ServerPlayerMixinLogic {

    fun logic1(player: ServerPlayer, state: BlockState) {
        if (state.`is`(BlockRegistry.PRIMORDIAL_SOUP.get())) {
            val comp = VOID_BOUND_REVELATION_COMPONENT[player]
            if (!comp.hasWellKnowledge) {
                comp.hasWellKnowledge = true
            }
        }
    }

    fun logic2(player: ServerPlayer, level: ServerLevel, resourceKey: ResourceKey<Level>, resourceKey2: ResourceKey<Level>) {
        val comp = VOID_BOUND_REVELATION_COMPONENT[player]

        if (resourceKey === Level.OVERWORLD && resourceKey2 === Level.NETHER) {
            if (comp.hasWellKnowledge) {
                comp.hasNetherKnowledge = true
                if (!comp.hasRecievedNetherMessage) {
                    player.sendSystemMessage(Component.translatable("voidbound.revelation.nether"))
                    comp.hasRecievedNetherMessage = true
                }

            }
        }
        if (resourceKey === Level.OVERWORLD && resourceKey2 === Level.END) {
            if (comp.hasWellKnowledge) {
                comp.hasEndKnowledge = true
                if (!comp.hasRecievedEndMessage) {
                    player.sendSystemMessage(Component.translatable("voidbound.revelation.nether"))
                    comp.hasRecievedEndMessage = true
                }

            }
        }
    }
}