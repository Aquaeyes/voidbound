package dev.sterner.client.screen.widget

import dev.sterner.VoidBound
import dev.sterner.client.screen.OsmoticEnchanterScreen
import dev.sterner.networking.EnchantmentLevelPacket
import dev.sterner.registry.VoidBoundPacketRegistry
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.util.Mth

class SelectedEnchantmentWidget(screen: OsmoticEnchanterScreen, x: Int, y: Int) : EnchantmentWidget(screen, x, y, 31, 20) {

    override fun onClick(mouseX: Double, mouseY: Double) {
        if (mouseX > x + (width - 9)){

            if (mouseX < x + width && mouseY > y + (height - 12) && mouseY < y + height - 3) {
                level -= 1
                level = Mth.clamp(level, 1, enchantment!!.maxLevel)
                VoidBoundPacketRegistry.VOIDBOUND_CHANNEL.sendToServer(EnchantmentLevelPacket(enchantment!!, level, screen.menu.pos.asLong()))

            } else if (mouseX < x + width && mouseY > y && mouseY < y + 9 && mouseY > y + 1) {
                level += 1
                level = Mth.clamp(level, 0, enchantment!!.maxLevel)
                VoidBoundPacketRegistry.VOIDBOUND_CHANNEL.sendToServer(EnchantmentLevelPacket(enchantment!!, level, screen.menu.pos.asLong()))
            }
        }

        if (mouseX <= x + (width - 11)) {
            super.onClick(mouseX, mouseY)
        }
    }



    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        if (dontRender()) {
            return
        }

        val border = VoidBound.id("textures/gui/enchanter_widget.png")
        guiGraphics.blit(border, x, y, 0f,0f,31, 23, 31, 23)

        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick)
    }
}