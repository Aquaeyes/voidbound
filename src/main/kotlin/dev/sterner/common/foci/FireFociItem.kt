package dev.sterner.common.foci

import com.sammy.malum.registry.common.SpiritTypeRegistry
import dev.sterner.registry.VoidBoundWandFociRegistry
import java.awt.Color

class FireFociItem(properties: Properties) : AbstractFociItem(VoidBoundWandFociRegistry.FIRE.get(), properties) {

    override fun color(): Color = Color(250, 154, 31)

    override fun endColor(): Color = Color(210, 39, 150)
}