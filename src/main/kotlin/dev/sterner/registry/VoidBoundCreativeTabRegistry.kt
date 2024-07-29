package dev.sterner.registry

import dev.sterner.VoidBound
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters

object VoidBoundCreativeTabRegistry {

    var GROUP: CreativeModeTab? = null

    fun init() {
        GROUP = FabricItemGroup.builder().title(Component.translatable("itemGroup." + VoidBound.modid))
            .icon { VoidBoundItemRegistry.DIVIDER.get().defaultInstance }
            .displayItems { _: ItemDisplayParameters?, entries: CreativeModeTab.Output ->
                entries.accept(VoidBoundItemRegistry.EMPTY_SPIRIT_SHARD.get())
                entries.accept(VoidBoundItemRegistry.DIVIDER.get())
                entries.accept(VoidBoundItemRegistry.SPIRIT_BINDER.get())
                entries.accept(VoidBoundItemRegistry.SPIRIT_STABILIZER.get())
                entries.accept(VoidBoundItemRegistry.CALL_OF_THE_VOID.get())
                entries.accept(VoidBoundItemRegistry.SOUL_STEEL_GOLEM.get())
                entries.accept(VoidBoundItemRegistry.DESTABILIZED_SPIRIT_RIFT.get())

            }.build()
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation(VoidBound.modid, "main"), GROUP!!)
    }
}