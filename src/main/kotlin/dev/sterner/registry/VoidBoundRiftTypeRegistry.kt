package dev.sterner.registry

import dev.sterner.VoidBound
import dev.sterner.api.RiftType
import dev.sterner.common.rift.DestabilizedRiftType
import dev.sterner.common.rift.EldritchRiftType
import dev.sterner.common.rift.NormalRiftType
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

object VoidBoundRiftTypeRegistry {

    val RIFT_KEY: ResourceKey<Registry<RiftType>> = ResourceKey.createRegistryKey(VoidBound.id("rift"))
    val RIFT: Registry<RiftType> = FabricRegistryBuilder.createSimple(RIFT_KEY).buildAndRegister()

    val RIFT_TYPES = LazyRegistrar.create(RIFT, VoidBound.modid)

    val NORMAL = RIFT_TYPES.register("normal") {
        NormalRiftType()
    }

    val DESTABILIZED = RIFT_TYPES.register("destabilized") {
        DestabilizedRiftType()
    }

    val ELDRITCH = RIFT_TYPES.register("eldritch") {
        EldritchRiftType()
    }
}