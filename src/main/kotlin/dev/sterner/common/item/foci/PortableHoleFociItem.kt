package dev.sterner.common.item.foci

import dev.sterner.registry.VoidBoundWandFociRegistry

class PortableHoleFociItem(properties: Properties) : AbstractFociItem(VoidBoundWandFociRegistry.PORTABLE_HOLE.get(), properties) {

    override fun isVoid(): Boolean {
        return true
    }
}