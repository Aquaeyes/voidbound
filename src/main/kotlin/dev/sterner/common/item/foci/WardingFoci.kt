package dev.sterner.common.item.foci

import dev.sterner.api.wand.IWandFocus
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult

class WardingFoci : IWandFocus {

    override fun onFocusRightClick(stack: ItemStack, level: Level, player: Player, hitResult: HitResult) {

    }
}