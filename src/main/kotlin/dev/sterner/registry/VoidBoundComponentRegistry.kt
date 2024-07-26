package dev.sterner.registry

import com.sammy.malum.common.components.MalumComponents.*
import com.sammy.malum.common.components.MalumItemDataComponent
import com.sammy.malum.common.components.MalumLivingEntityDataComponent
import com.sammy.malum.common.components.MalumPlayerDataComponent
import dev.onyxstudios.cca.api.v3.component.ComponentFactory
import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer
import dev.sterner.VoidBound
import dev.sterner.components.VoidBoundEntityComponent
import dev.sterner.components.VoidBoundWorldComponent
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity


class VoidBoundComponentRegistry : WorldComponentInitializer, EntityComponentInitializer {

    override fun registerWorldComponentFactories(registry: WorldComponentFactoryRegistry) {
        registry.register(VOID_BOUND_WORLD_COMPONENT, ::VoidBoundWorldComponent)
    }

    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        registry.beginRegistration(LivingEntity::class.java, VOID_BOUND_ENTITY_COMPONENT)
            .respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end { livingEntity: LivingEntity ->
                VoidBoundEntityComponent(
                    livingEntity
                )
            }
    }


    companion object {
        val VOID_BOUND_WORLD_COMPONENT: ComponentKey<VoidBoundWorldComponent> = ComponentRegistry.getOrCreate(
            VoidBound.id("world"),
            VoidBoundWorldComponent::class.java
        )

        val VOID_BOUND_ENTITY_COMPONENT: ComponentKey<VoidBoundEntityComponent> = ComponentRegistry.getOrCreate(
            VoidBound.id("entity"),
            VoidBoundEntityComponent::class.java
        )
    }
}