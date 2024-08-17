package dev.sterner.registry

import com.mojang.blaze3d.platform.InputConstants
import dev.sterner.client.screen.FociSelectionScreen
import dev.sterner.common.item.WandItem
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft

object VoidBoundKeyBindings {

    private var fociKeyBind: KeyMapping? = KeyBindingHelper.registerKeyBinding(
        KeyMapping("key.voidbound.foci_select", InputConstants.KEY_F, "category.voidbound")
    )

    fun init() {
        ClientTickEvents.END_CLIENT_TICK.register(::listenFociKey)
    }

    private fun listenFociKey(minecraft: Minecraft) {
        if (fociKeyBind?.isDown == true) {
            if (minecraft.screen == null && minecraft.player != null && minecraft.player!!.mainHandItem.item is WandItem) {
                minecraft.setScreen(FociSelectionScreen(minecraft.player!!))
            }
        }
    }
}