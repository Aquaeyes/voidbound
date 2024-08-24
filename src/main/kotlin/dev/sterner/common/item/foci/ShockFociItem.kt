package dev.sterner.common.item.foci

import dev.sterner.registry.VoidBoundWandFociRegistry
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import java.awt.Color

class ShockFociItem(properties: Properties) : AbstractFociItem(VoidBoundWandFociRegistry.SHOCK.get(), properties) {

    override fun color(): Color = Color(155, 255, 255)

    override fun endColor(): Color = Color(50, 125, 203)
}