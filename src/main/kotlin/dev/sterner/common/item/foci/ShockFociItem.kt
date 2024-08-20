package dev.sterner.common.item.foci

import dev.sterner.registry.VoidBoundWandFociRegistry
import java.awt.Color

class ShockFociItem(properties: Properties) : AbstractFociItem(VoidBoundWandFociRegistry.SHOCK.get(), properties) {

    override fun color(): Color = Color(155, 255, 255)

    override fun endColor(): Color = Color(50, 125, 203)
}