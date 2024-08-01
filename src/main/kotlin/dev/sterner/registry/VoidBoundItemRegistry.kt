package dev.sterner.registry

import dev.sterner.VoidBound
import dev.sterner.api.GolemCore
import dev.sterner.common.item.*
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import io.github.fabricators_of_create.porting_lib.util.RegistryObject
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item

object VoidBoundItemRegistry {

    val ITEMS: LazyRegistrar<Item> = LazyRegistrar.create(BuiltInRegistries.ITEM, VoidBound.modid)

    val HALLOWED_GOGGLES = ITEMS.register("hallowed_goggles") {
        HallowedGogglesItem(Item.Properties())
    }

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
        UnimplementedBlockItem(VoidBoundBlockRegistry.SPIRIT_STABILIZER.get(), Item.Properties())
    }

    val DESTABILIZED_SPIRIT_RIFT = ITEMS.register("destabilized_spirit_rift") {
        UnimplementedBlockItem(VoidBoundBlockRegistry.DESTABILIZED_SPIRIT_RIFT.get(), Item.Properties())
    }

    val CALL_OF_THE_VOID = ITEMS.register("call_of_the_void") {
        CallOfTheVoidItem()
    }

    val SOUL_STEEL_GOLEM = ITEMS.register("soul_steel_golem") {
        GolemEntityItem()
    }

    val CORE_EMPTY = ITEMS.register("core_empty") {
        Item(Item.Properties())
    }

    val GOLEM_CORE_GATHER = ITEMS.register("golem_core_gather") {
        GolemCoreItem(GolemCore.GATHER, Item.Properties())
    }

    val GOLEM_CORE_HARVEST = ITEMS.register("golem_core_harvest") {
        GolemCoreItem(GolemCore.HARVEST, Item.Properties())
    }

    val GOLEM_CORE_GUARD = ITEMS.register("golem_core_guard") {
        GolemCoreItem(GolemCore.GUARD, Item.Properties())
    }

    val GOLEM_CORE_LUMBER = ITEMS.register("golem_core_lumber") {
        GolemCoreItem(GolemCore.LUMBER, Item.Properties())
    }

    val GOLEM_CORE_BUTCHER = ITEMS.register("golem_core_butcher") {
        GolemCoreItem(GolemCore.BUTCHER, Item.Properties())
    }

    val GOLEM_CORE_EMPTY = ITEMS.register("golem_core_empty") {
        GolemCoreItem(GolemCore.EMPTY, Item.Properties())
    }

    val GOLEM_CORE_FILL = ITEMS.register("golem_core_fill") {
        GolemCoreItem(GolemCore.FILL, Item.Properties())
    }
}