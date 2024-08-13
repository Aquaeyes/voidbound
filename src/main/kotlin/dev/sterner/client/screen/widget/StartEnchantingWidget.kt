package dev.sterner.client.screen.widget

import dev.sterner.VoidBound
import dev.sterner.client.screen.OsmoticEnchanterScreen
import dev.sterner.networking.StartEnchantingPacket
import dev.sterner.registry.VoidBoundPacketRegistry
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component

class StartEnchantingWidget(var screen: OsmoticEnchanterScreen, x: Int, y: Int) : AbstractWidget(x, y, 18, 18, Component.empty()) {

    override fun onClick(mouseX: Double, mouseY: Double) {
        VoidBoundPacketRegistry.VOIDBOUND_CHANNEL.sendToServer(StartEnchantingPacket(screen.menu.pos))
        super.onClick(mouseX, mouseY)
    }

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {

        val icon = VoidBound.id("textures/gui/enchanter_check${if(!screen.menu.be!!.activated) "" else "_glowing"}.png")

        guiGraphics.blit(icon, x, y, 0f,0f,18, 18, 18, 18)
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {

    }
}