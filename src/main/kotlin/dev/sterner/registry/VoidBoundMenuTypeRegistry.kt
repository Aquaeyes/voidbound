package dev.sterner.registry

import dev.sterner.VoidBound
import dev.sterner.common.menu.OsmoticEnchanterMenu
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.core.registries.BuiltInRegistries

object VoidBoundMenuTypeRegistry {

    val MENU_TYPES = LazyRegistrar.create(BuiltInRegistries.MENU, VoidBound.modid)

    val OSMOTIC_ENCHANTER = MENU_TYPES
        .register(
            "osmotic_enchanter"
        ) {
            ExtendedScreenHandlerType { id, inv, _ -> OsmoticEnchanterMenu(id, inv) }
        }
}