package dev.sterner.api

import dev.sterner.registry.VoidBoundItemRegistry
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player

object VoidBoundApi {

    fun hasGoggles(): Boolean {
        return Minecraft.getInstance().player != null && Minecraft.getInstance().player!!.getItemBySlot(EquipmentSlot.HEAD).`is`(
            VoidBoundItemRegistry.HALLOWED_GOGGLES.get()
        )
    }

    fun hasGoggles(player: Player) : Boolean {
        return player.getItemBySlot(EquipmentSlot.HEAD).`is`(
            VoidBoundItemRegistry.HALLOWED_GOGGLES.get()
        )
    }
}