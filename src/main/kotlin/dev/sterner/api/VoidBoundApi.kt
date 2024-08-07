package dev.sterner.api

import dev.sterner.registry.VoidBoundItemRegistry
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
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
}