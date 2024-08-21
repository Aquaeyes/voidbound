package dev.sterner.common.foci

import dev.sterner.registry.VoidBoundWandFociRegistry
import java.awt.Color

class ExcavationFociItem(properties: Properties) : AbstractFociItem(VoidBoundWandFociRegistry.EXCAVATION.get(), properties) {

    override fun color(): Color = Color(135, 255, 135)

    override fun endColor(): Color = Color(0, 225, 0)
}