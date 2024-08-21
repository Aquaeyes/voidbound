package dev.sterner.common.foci

import dev.sterner.registry.VoidBoundWandFociRegistry
import java.awt.Color

class PortableHoleFociItem(properties: Properties) :
    AbstractFociItem(VoidBoundWandFociRegistry.PORTABLE_HOLE.get(), properties) {
    override fun color(): Color {
        return Color(0, 0, 0)
    }

    override fun endColor(): Color {
        return Color(0, 0, 0)
    }

    override fun isVoid(): Boolean {
        return true
    }
}