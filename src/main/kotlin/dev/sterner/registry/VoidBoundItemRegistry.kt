package dev.sterner.registry

import com.sammy.malum.common.block.curiosities.spirit_crucible.SpiritCrucibleCoreBlockEntity
import com.sammy.malum.common.item.curiosities.tools.TotemicStaffItem
import com.sammy.malum.registry.common.block.BlockRegistry
import com.sammy.malum.registry.common.item.ItemRegistry
import dev.sterner.VoidBound
import dev.sterner.api.entity.GolemCore
import dev.sterner.common.blockentity.EldritchObeliskBlockEntity
import dev.sterner.common.item.*
import dev.sterner.common.item.foci.*
import dev.sterner.common.item.tool.*
import dev.sterner.common.item.tool.ichor.*
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import io.github.fabricators_of_create.porting_lib.util.RegistryObject
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.SwordItem
import team.lodestar.lodestone.systems.multiblock.MultiBlockItem

object VoidBoundItemRegistry {

    val ITEMS: LazyRegistrar<Item> = LazyRegistrar.create(BuiltInRegistries.ITEM, VoidBound.modid)

    val HALLOWED_GOGGLES: RegistryObject<HallowedGogglesItem> = ITEMS.register("hallowed_goggles") {
        HallowedGogglesItem(Item.Properties())
    }

    val HALLOWED_MONOCLE: RegistryObject<HallowedMonocleItem> = ITEMS.register("hallowed_monocle") {
        HallowedMonocleItem(Item.Properties())
    }

    val HALLOWED_GOLD_CAPPED_RUNEWOOD_WAND: RegistryObject<WandItem> =
        ITEMS.register("hallowed_gold_capped_runewood_wand") {
            WandItem(Item.Properties())
        }

    val SOUL_STAINED_STEEL_CAPPED_SOULWOOD_WAND: RegistryObject<WandItem> =
        ITEMS.register("soul_stained_steel_capped_soulwood_wand") {
            WandItem(Item.Properties())
        }

    val CRYSTAL_FOCI: RegistryObject<Item> = ITEMS.register("crystal_foci") {
        Item(Item.Properties())
    }

    val PORTABLE_HOLE_FOCI: RegistryObject<Item> = ITEMS.register("portable_hole_foci") {
        PortableHoleFociItem(Item.Properties().stacksTo(1))
    }

    val WARDING_FOCI: RegistryObject<Item> = ITEMS.register("warding_foci") {
        WardingFociItem(Item.Properties().stacksTo(1))
    }

    val EXCAVATION_FOCI: RegistryObject<Item> = ITEMS.register("excavation_foci") {
        ExcavationFociItem(Item.Properties().stacksTo(1))
    }

    val SHOCK_FOCI: RegistryObject<Item> = ITEMS.register("shock_foci") {
        ShockFociItem(Item.Properties().stacksTo(1))
    }

    val FIRE_FOCI: RegistryObject<Item> = ITEMS.register("fire_foci") {
        FireFociItem(Item.Properties().stacksTo(1))
    }

    val EMPTY_SPIRIT_SHARD: RegistryObject<Item> = ITEMS.register("empty_spirit_shard") {
        Item(Item.Properties())
    }

    val DIVIDER: RegistryObject<Item> = ITEMS.register("divider") {
        DividerItem(FabricItemSettings().maxCount(1))
    }

    val SPIRIT_BINDER: RegistryObject<BlockItem> = ITEMS.register("spirit_binder") {
        UnimplementedBlockItem(VoidBoundBlockRegistry.SPIRIT_BINDER.get(), Item.Properties())
    }

    val OSMOTIC_ENCHANTER: RegistryObject<BlockItem> = ITEMS.register("osmotic_enchanter") {
        BlockItem(VoidBoundBlockRegistry.OSMOTIC_ENCHANTER.get(), Item.Properties())
    }

    val ELDRITCH_OBELISK: RegistryObject<Item> = ItemRegistry.register(
        "eldritch_obelisk", ItemRegistry.DEFAULT_PROPERTIES()
    ) { p: Item.Properties? ->
        MultiBlockItem(
            VoidBoundBlockRegistry.ELDRITCH_OBELISK.get(),
            p,
            EldritchObeliskBlockEntity.STRUCTURE
        )
    }

    val SPIRIT_STABILIZER: RegistryObject<UnimplementedBlockItem> = ITEMS.register("spirit_stabilizer") {
        UnimplementedBlockItem(VoidBoundBlockRegistry.SPIRIT_STABILIZER.get(), Item.Properties())
    }

    val SPIRIT_RIFT: RegistryObject<UnimplementedBlockItem> = ITEMS.register("spirit_rift") {
        UnimplementedBlockItem(VoidBoundBlockRegistry.SPIRIT_RIFT.get(), Item.Properties())
    }

    val CALL_OF_THE_VOID: RegistryObject<CallOfTheVoidItem> = ITEMS.register("call_of_the_void") {
        CallOfTheVoidItem()
    }

    val SOUL_STEEL_GOLEM: RegistryObject<GolemEntityItem> = ITEMS.register("soul_steel_golem") {
        GolemEntityItem()
    }

    val CORE_EMPTY: RegistryObject<Item> = ITEMS.register("core_empty") {
        Item(Item.Properties())
    }

    val GOLEM_CORE_GATHER: RegistryObject<GolemCoreItem> = ITEMS.register("golem_core_gather") {
        GolemCoreItem(GolemCore.GATHER, Item.Properties())
    }

    val GOLEM_CORE_HARVEST: RegistryObject<GolemCoreItem> = ITEMS.register("golem_core_harvest") {
        GolemCoreItem(GolemCore.HARVEST, Item.Properties())
    }

    val GOLEM_CORE_GUARD: RegistryObject<GolemCoreItem> = ITEMS.register("golem_core_guard") {
        GolemCoreItem(GolemCore.GUARD, Item.Properties())
    }

    val GOLEM_CORE_LUMBER: RegistryObject<GolemCoreItem> = ITEMS.register("golem_core_lumber") {
        GolemCoreItem(GolemCore.LUMBER, Item.Properties())
    }

    val GOLEM_CORE_BUTCHER: RegistryObject<GolemCoreItem> = ITEMS.register("golem_core_butcher") {
        GolemCoreItem(GolemCore.BUTCHER, Item.Properties())
    }

    val GOLEM_CORE_EMPTY: RegistryObject<GolemCoreItem> = ITEMS.register("golem_core_empty") {
        GolemCoreItem(GolemCore.EMPTY, Item.Properties())
    }

    val GOLEM_CORE_FILL: RegistryObject<GolemCoreItem> = ITEMS.register("golem_core_fill") {
        GolemCoreItem(GolemCore.FILL, Item.Properties())
    }

    val CRIMSON_KNIGHT_SWORD: RegistryObject<SwordItem> = ITEMS.register("crimson_knight_sword") {
        SwordItem(VoidBoundTiers.ELEMENTAL, 3, -2.4f, Item.Properties().stacksTo(1))
    }

    val PICKAXE_OF_THE_CORE: RegistryObject<PickaxeOfTheCoreItem> = ITEMS.register("pickaxe_of_the_core") {
        PickaxeOfTheCoreItem(VoidBoundTiers.ELEMENTAL, -2, 0f, 2f, Item.Properties().stacksTo(1))
    }

    val HOE_OF_GROWTH: RegistryObject<HoeOfGrowthItem> = ITEMS.register("hoe_of_growth") {
        HoeOfGrowthItem(VoidBoundTiers.ELEMENTAL, 0, -1.5f, 1f, Item.Properties().stacksTo(1))
    }

    val AXE_OF_THE_STREAM: RegistryObject<AxeOfTheStreamItem> = ITEMS.register("axe_of_the_stream") {
        AxeOfTheStreamItem(VoidBoundTiers.ELEMENTAL, -3f, 0f, 4f, Item.Properties())
    }

    val SHOVEL_OF_THE_EARTHMOVER: RegistryObject<ShovelOfEarthmoverItem> = ITEMS.register("shovel_of_the_earthmover") {
        ShovelOfEarthmoverItem(VoidBoundTiers.ELEMENTAL, 2, 0f, 2f, Item.Properties().stacksTo(1))
    }

    val SWORD_OF_THE_ZEPHYR: RegistryObject<SwordOfTheZephyrItem> = ITEMS.register("sword_of_the_zephyr") {
        SwordOfTheZephyrItem(VoidBoundTiers.ELEMENTAL, -3, 0f, 3f, Item.Properties().stacksTo(1))
    }

    val CRIMSON_RITES: RegistryObject<CrimsonBookItem> = ITEMS.register("crimson_rites") {
        CrimsonBookItem(Item.Properties().stacksTo(1))
    }

    val BOOTS_OF_THE_TRAVELLER: RegistryObject<BootsOfTheTraveller> = ITEMS.register("boots_of_the_traveller") {
        BootsOfTheTraveller(Item.Properties().stacksTo(1))
    }

    val SOULWOOD_TOTEMIC_STAFF: RegistryObject<Item> = ITEMS.register("soulwood_totemic_staff") {
        TotemicStaffItem(Item.Properties())
    }

    val ICHOR: RegistryObject<Item> = ITEMS.register("ichor") {
        IchorItem(Item.Properties())
    }

    val TEAR_OF_ENDER: RegistryObject<Item> = ITEMS.register("tear_of_ender") {
        Item(Item.Properties())
    }

    val TEAR_OF_CRIMSON: RegistryObject<Item> = ITEMS.register("tear_of_crimson") {
        Item(Item.Properties())
    }

    val STRANGE_MATTER: RegistryObject<Item> = ITEMS.register("strange_matter") {
        Item(Item.Properties())
    }

    val ICHORIUM_PICKAXE: RegistryObject<IchoriumPickaxeItem> = ITEMS.register("ichorium_pickaxe") {
        IchoriumPickaxeItem(VoidBoundTiers.ICHORIUM, -2, 1f, 2f, ichorProptery)
    }

    val ICHORIUM_HOE: RegistryObject<HoeOfGrowthItem> = ITEMS.register("ichorium_hoe") {
        IchoriumHoeItem(VoidBoundTiers.ICHORIUM, 0, -1.5f, 1f, ichorProptery)
    }

    val ICHORIUM_AXE: RegistryObject<AxeOfTheStreamItem> = ITEMS.register("ichorium_axe") {
        IchoriumAxeItem(VoidBoundTiers.ICHORIUM, -3f, 2f, 4f, ichorProptery)
    }

    val ICHORIUM_SHOVEL: RegistryObject<ShovelOfEarthmoverItem> = ITEMS.register("ichorium_shovel") {
        IchoriumShovelItem(VoidBoundTiers.ICHORIUM, 2, 0f, 2f, ichorProptery)
    }

    val ICHORIUM_SWORD: RegistryObject<SwordOfTheZephyrItem> = ITEMS.register("ichorium_sword") {
        IchoriumSwordItem(VoidBoundTiers.ICHORIUM, -1, 0f, 3f, ichorProptery)
    }

    val ICHORIUM_SCYTHE: RegistryObject<Item> = ITEMS.register("ichorium_scythe") {
        IchoriumScytheItem(VoidBoundTiers.ICHORIUM, -2.5f, 2.1f, ichorProptery)
    }

    val ichorProptery = FabricItemSettings().stacksTo(1).fireResistant().rarity(Rarity.RARE)
}