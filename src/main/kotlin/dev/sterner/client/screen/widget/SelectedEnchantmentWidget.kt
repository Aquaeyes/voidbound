package dev.sterner.client.screen.widget

import dev.sterner.VoidBound
import dev.sterner.client.screen.OsmoticEnchanterScreen
import dev.sterner.networking.EnchantmentLevelPacket
import dev.sterner.registry.VoidBoundPacketRegistry
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.util.Mth

class SelectedEnchantmentWidget(screen: OsmoticEnchanterScreen, x: Int, y: Int) : EnchantmentWidget(screen, x, y, 22, 33) {

    override fun onClick(mouseX: Double, mouseY: Double) {
        val area1XStart: Double = x + 6.0
        val area1XEnd: Double = x + 16.0
        val area1YStart: Double = y.toDouble()
        val area1YEnd: Double = y + 7.0

        val area2XStart: Double = x + 6.0
        val area2XEnd: Double = x + 16.0
        val area2YStart: Double = y + 24.0
        val area2YEnd: Double = y + 30.0

        val area3XStart: Double = x.toDouble()
        val area3XEnd: Double = x + 22.0
        val area3YStart: Double = y + 8.0
        val area3YEnd: Double = y + 22.0

        when {
            // Cast mouseX and mouseY to Int and check if the mouse is in area 1
            mouseX in area1XStart..area1XEnd && mouseY in area1YStart..area1YEnd -> {
                level += 1
                level = Mth.clamp(level, 0, enchantment!!.maxLevel)
                VoidBoundPacketRegistry.VOIDBOUND_CHANNEL.sendToServer(EnchantmentLevelPacket(enchantment!!, level, screen.menu.pos.asLong()))
            }

            // Cast mouseX and mouseY to Int and check if the mouse is in area 2
            mouseX in area2XStart..area2XEnd && mouseY in area2YStart..area2YEnd -> {
                level -= 1
                level = Mth.clamp(level, 1, enchantment!!.maxLevel)
                VoidBoundPacketRegistry.VOIDBOUND_CHANNEL.sendToServer(EnchantmentLevelPacket(enchantment!!, level, screen.menu.pos.asLong()))
            }

            // Cast mouseX and mouseY to Int and check if the mouse is in area 3
            mouseX in area3XStart..area3XEnd && mouseY in area3YStart..area3YEnd -> {
                super.onClick(mouseX, mouseY)
            }
        }
    }



    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        if (dontRender()) {
            return
        }

        val border = VoidBound.id("textures/gui/enchanter_widget.png")
        guiGraphics.blit(border, x, y, 0f,0f, width, height, width, height)

        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick)
    }
}