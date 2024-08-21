package dev.sterner.common.foci

import dev.sterner.registry.VoidBoundWandFociRegistry
import java.awt.Color

class WardingFociItem(properties: Properties) : AbstractFociItem(VoidBoundWandFociRegistry.WARDING.get(), properties) {

    override fun color(): Color = Color(255, 255, 255)

    override fun endColor(): Color = Color(255, 155, 23)
}