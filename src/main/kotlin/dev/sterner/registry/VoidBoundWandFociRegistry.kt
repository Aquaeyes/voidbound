package dev.sterner.registry

import dev.sterner.VoidBound
import dev.sterner.api.wand.IWandFocus
import dev.sterner.common.foci.*
import dev.sterner.common.item.foci.*
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import io.github.fabricators_of_create.porting_lib.util.RegistryObject
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey


object VoidBoundWandFociRegistry {

    private val WAND_FOCUS_KEY: ResourceKey<Registry<IWandFocus>> =
        ResourceKey.createRegistryKey(VoidBound.id("wand_focus"))
    val WAND_FOCUS: Registry<IWandFocus> = FabricRegistryBuilder.createSimple(WAND_FOCUS_KEY).buildAndRegister()

    val WAND_FOCI: LazyRegistrar<IWandFocus> = LazyRegistrar.create(WAND_FOCUS, VoidBound.modid)

    val PORTABLE_HOLE: RegistryObject<PortableHoleFoci> = WAND_FOCI.register("portable_hole") {
        PortableHoleFoci()
    }

    val EXCAVATION: RegistryObject<ExcavationFoci> = WAND_FOCI.register("excavation") {
        ExcavationFoci()
    }

    val SHOCK: RegistryObject<ShockFoci> = WAND_FOCI.register("shock") {
        ShockFoci()
    }

    val FIRE: RegistryObject<FireFoci> = WAND_FOCI.register("fire") {
        FireFoci()
    }

    val WARDING: RegistryObject<WardingFoci> = WAND_FOCI.register("warding") {
        WardingFoci()
    }
}