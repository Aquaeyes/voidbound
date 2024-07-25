package dev.sterner.registry

import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer
import dev.sterner.VoidBound
import dev.sterner.components.VoidBoundWorldComponent

class VoidBoundComponentRegistry : WorldComponentInitializer {

    override fun registerWorldComponentFactories(registry: WorldComponentFactoryRegistry) {
        registry.register(VOID_BOUND_WORLD_COMPONENT, ::VoidBoundWorldComponent)
    }

    companion object {
        val VOID_BOUND_WORLD_COMPONENT: ComponentKey<VoidBoundWorldComponent> = ComponentRegistry.getOrCreate(
            VoidBound.id("world"),
            VoidBoundWorldComponent::class.java
        )
    }
}