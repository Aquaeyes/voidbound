package dev.sterner.api

import com.sammy.malum.core.systems.recipe.SpiritWithCount
import dev.sterner.listener.EnchantSpiritDataReloadListener
import dev.sterner.registry.VoidBoundItemRegistry
import net.minecraft.client.Minecraft
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.enchantment.Enchantment
import team.lodestar.lodestone.helpers.TrinketsHelper

object VoidBoundApi {

    fun hasGoggles(): Boolean {
        val player = Minecraft.getInstance().player
        if (player != null) {
            val bl = TrinketsHelper.hasTrinketEquipped(player, VoidBoundItemRegistry.HALLOWED_MONOCLE.get())
            val bl2 = Minecraft.getInstance().player!!.getItemBySlot(EquipmentSlot.HEAD)
                .`is`(
                    VoidBoundItemRegistry.HALLOWED_GOGGLES.get()
                )
            return bl || bl2
        }
        return false
    }

    fun hasGoggles(player: Player): Boolean {
        val bl = TrinketsHelper.hasTrinketEquipped(player, VoidBoundItemRegistry.HALLOWED_MONOCLE.get())
        val bl2 = Minecraft.getInstance().player!!.getItemBySlot(EquipmentSlot.HEAD)
            .`is`(
                VoidBoundItemRegistry.HALLOWED_GOGGLES.get()
            )
        return bl || bl2
    }

    fun getSpiritFromEnchant(enchantment: Enchantment, level: Int): List<SpiritWithCount> {

        val reg = BuiltInRegistries.ENCHANTMENT.getKey(enchantment)
        val list = EnchantSpiritDataReloadListener.ENCHANTING_DATA[reg]
        val out = mutableListOf<SpiritWithCount>()
        if (list != null) {
            for (spiritIn in list) {
                out.add(SpiritWithCount(spiritIn.type, spiritIn.count * level))
            }
        }

        return out
    }
}