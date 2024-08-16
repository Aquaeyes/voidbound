package dev.sterner.common.item.foci

import dev.sterner.api.wand.IWandFocus
import dev.sterner.registry.VoidBoundComponentRegistry
import net.minecraft.core.GlobalPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import java.util.*

class WardingFoci : IWandFocus {

    override fun onFocusRightClick(stack: ItemStack, level: Level, player: Player, hitResult: HitResult) {
        if (hitResult is BlockHitResult) {
            val comp = VoidBoundComponentRegistry.VOID_BOUND_WORLD_COMPONENT.get(level)
            val global = GlobalPos.of(level.dimension(), hitResult.blockPos)
            if (comp.hasBlockPos(player, global)) {
                comp.removePos(player, global)
                //println("Removed")
            } else {
                comp.addPos(UUID.randomUUID(), global)
                println("Added ${level.isClientSide()}")
                //comp.addPos(player, global)
                //println("Added")
            }
        }
    }
}