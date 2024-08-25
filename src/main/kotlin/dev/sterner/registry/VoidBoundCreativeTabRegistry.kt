package dev.sterner.registry

import dev.sterner.VoidBound
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters

object VoidBoundCreativeTabRegistry {

    private var GROUP: CreativeModeTab? = null

    fun init() {
        GROUP = FabricItemGroup.builder().title(Component.translatable("itemGroup." + VoidBound.modid))
            .icon {
                val item = VoidBoundItemRegistry.CALL_OF_THE_VOID.get().defaultInstance
                val tag = CompoundTag()
                tag.putBoolean("Glowing", true)
                item.tag = tag
                item
            }
            .displayItems { _: ItemDisplayParameters?, entries: CreativeModeTab.Output ->
                entries.accept(VoidBoundItemRegistry.HALLOWED_GOLD_CAPPED_RUNEWOOD_WAND.get())
                entries.accept(VoidBoundItemRegistry.SOUL_STAINED_STEEL_CAPPED_SOULWOOD_WAND.get())
                entries.accept(VoidBoundItemRegistry.HALLOWED_GOGGLES.get())
                entries.accept(VoidBoundItemRegistry.HALLOWED_MONOCLE.get())
                entries.accept(VoidBoundItemRegistry.CALL_OF_THE_VOID.get())
                entries.accept(VoidBoundItemRegistry.CRYSTAL_FOCI.get())
                entries.accept(VoidBoundItemRegistry.FIRE_FOCI.get())
                entries.accept(VoidBoundItemRegistry.SHOCK_FOCI.get())
                entries.accept(VoidBoundItemRegistry.EXCAVATION_FOCI.get())
                entries.accept(VoidBoundItemRegistry.PORTABLE_HOLE_FOCI.get())
                entries.accept(VoidBoundItemRegistry.WARDING_FOCI.get())
                entries.accept(VoidBoundItemRegistry.EMPTY_SPIRIT_SHARD.get())
                entries.accept(VoidBoundItemRegistry.SOULWOOD_TOTEMIC_STAFF.get())
                entries.accept(VoidBoundItemRegistry.ELDRITCH_OBELISK.get())
                //entries.accept(VoidBoundItemRegistry.DIVIDER.get())
                entries.accept(VoidBoundItemRegistry.CORE_EMPTY.get())
                entries.accept(VoidBoundItemRegistry.GOLEM_CORE_GATHER.get())
                entries.accept(VoidBoundItemRegistry.GOLEM_CORE_GUARD.get())
                entries.accept(VoidBoundItemRegistry.GOLEM_CORE_HARVEST.get())
                entries.accept(VoidBoundItemRegistry.GOLEM_CORE_BUTCHER.get())
                entries.accept(VoidBoundItemRegistry.GOLEM_CORE_FILL.get())
                entries.accept(VoidBoundItemRegistry.GOLEM_CORE_EMPTY.get())
                entries.accept(VoidBoundItemRegistry.GOLEM_CORE_LUMBER.get())
                entries.accept(VoidBoundItemRegistry.SOUL_STEEL_GOLEM.get())
                entries.accept(VoidBoundItemRegistry.SPIRIT_BINDER.get())
                entries.accept(VoidBoundItemRegistry.SPIRIT_STABILIZER.get())
                entries.accept(VoidBoundItemRegistry.OSMOTIC_ENCHANTER.get())
                //entries.accept(VoidBoundItemRegistry.DESTABILIZED_SPIRIT_RIFT.get())
                entries.accept(VoidBoundItemRegistry.CRIMSON_RITES.get())
                entries.accept(VoidBoundItemRegistry.CRIMSON_KNIGHT_SWORD.get())
                entries.accept(VoidBoundItemRegistry.PICKAXE_OF_THE_CORE.get())
                entries.accept(VoidBoundItemRegistry.SWORD_OF_THE_ZEPHYR.get())
                entries.accept(VoidBoundItemRegistry.AXE_OF_THE_STREAM.get())
                entries.accept(VoidBoundItemRegistry.HOE_OF_GROWTH.get())
                entries.accept(VoidBoundItemRegistry.SHOVEL_OF_THE_EARTHMOVER.get())
                entries.accept(VoidBoundItemRegistry.BOOTS_OF_THE_TRAVELLER.get())
                entries.accept(VoidBoundItemRegistry.TEAR_OF_CRIMSON.get())
                entries.accept(VoidBoundItemRegistry.TEAR_OF_ENDER.get())
                entries.accept(VoidBoundItemRegistry.ICHOR.get())
                entries.accept(VoidBoundItemRegistry.ICHORIUM_SCYTHE.get())
                entries.accept(VoidBoundItemRegistry.ICHORIUM_PICKAXE.get())
                entries.accept(VoidBoundItemRegistry.ICHORIUM_AXE.get())
                entries.accept(VoidBoundItemRegistry.ICHORIUM_SWORD.get())
                entries.accept(VoidBoundItemRegistry.ICHORIUM_SHOVEL.get())

            }.build()
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation(VoidBound.modid, "main"), GROUP!!)
    }
}