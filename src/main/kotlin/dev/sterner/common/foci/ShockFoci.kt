package dev.sterner.common.foci

import dev.sterner.api.wand.IWandFocus
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder
import team.lodestar.lodestone.systems.rendering.VFXBuilders.WorldVFXBuilder

class ShockFoci : IWandFocus {

    override fun onUsingFocusTick(stack: ItemStack, level: Level, player: Player) {

    }
}