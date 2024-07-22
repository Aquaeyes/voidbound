package dev.sterner.registry

import dev.sterner.VoidBound
import dev.sterner.item.DividerItem
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import io.github.fabricators_of_create.porting_lib.util.RegistryObject
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item

object VoidBoundItemRegistry {

    val ITEMS: LazyRegistrar<Item> = LazyRegistrar.create(BuiltInRegistries.ITEM, VoidBound.modid)

    val DIVIDER: RegistryObject<Item> = ITEMS.register("divider") { DividerItem(FabricItemSettings().maxCount(1)) }

    val SPIRIT_BINDER = ITEMS.register("spirit_binder"){
        BlockItem(VoidBoundBlockRegistry.SPIRIT_BINDER.get(), Item.Properties())
    }

    val SPIRIT_BINDER_STABILIZER = ITEMS.register("spirit_binder_stabilizer"){
        BlockItem(VoidBoundBlockRegistry.SPIRIT_BINDER_STABILIZER.get(), Item.Properties())
    }
}