package dev.sterner.registry

import dev.sterner.VoidBound
import dev.sterner.common.item.CallOfTheVoidItem
import dev.sterner.common.item.DividerItem
import dev.sterner.common.item.GolemEntityItem
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import io.github.fabricators_of_create.porting_lib.util.RegistryObject
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item

object VoidBoundItemRegistry {

    val ITEMS: LazyRegistrar<Item> = LazyRegistrar.create(BuiltInRegistries.ITEM, VoidBound.modid)

    val EMPTY_SPIRIT_SHARD = ITEMS.register("empty_spirit_shard") {
        Item(Item.Properties())
    }

    val DIVIDER: RegistryObject<Item> = ITEMS.register("divider") {
        DividerItem(FabricItemSettings().maxCount(1))
    }

    val SPIRIT_BINDER = ITEMS.register("spirit_binder") {
        BlockItem(VoidBoundBlockRegistry.SPIRIT_BINDER.get(), Item.Properties())
    }

    val SPIRIT_STABILIZER = ITEMS.register("spirit_stabilizer") {
        BlockItem(VoidBoundBlockRegistry.SPIRIT_STABILIZER.get(), Item.Properties())
    }

    val DESTABILIZED_SPIRIT_RIFT = ITEMS.register("destabilized_spirit_rift") {
        BlockItem(VoidBoundBlockRegistry.DESTABILIZED_SPIRIT_RIFT.get(), Item.Properties())
    }

    val CALL_OF_THE_VOID = ITEMS.register("call_of_the_void") {
        CallOfTheVoidItem()
    }

    val SOUL_STEEL_GOLEM = ITEMS.register("soul_steel_golem") {
        GolemEntityItem()
    }
}