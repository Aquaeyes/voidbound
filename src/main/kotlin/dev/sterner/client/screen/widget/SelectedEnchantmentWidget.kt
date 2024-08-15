package dev.sterner.client.screen.widget

import com.sammy.malum.core.systems.recipe.SpiritWithCount
import dev.sterner.VoidBound
import dev.sterner.api.VoidBoundApi
import dev.sterner.api.rift.SimpleSpiritCharge
import dev.sterner.client.screen.OsmoticEnchanterScreen
import dev.sterner.listener.EnchantSpiritDataReloadListener
import dev.sterner.networking.EnchantmentLevelPacket
import dev.sterner.registry.VoidBoundPacketRegistry
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentHelper
import java.awt.Color

class SelectedEnchantmentWidget(screen: OsmoticEnchanterScreen, x: Int, y: Int) : EnchantmentWidget(screen, x, y, 22, 33) {

    override fun onClick(mouseX: Double, mouseY: Double) {

        if (screen.menu.be?.activated == true) {
            return
        }

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
                if (canAddLevel()) {
                    level += 1
                    level = Mth.clamp(level, 0, enchantment!!.maxLevel)
                    VoidBoundPacketRegistry.VOIDBOUND_CHANNEL.sendToServer(EnchantmentLevelPacket(enchantment!!, level, screen.menu.pos.asLong()))
                    screen.menu.be!!.receiveScreenData(enchantment!!, level)
                }
            }

            // Cast mouseX and mouseY to Int and check if the mouse is in area 2
            mouseX in area2XStart..area2XEnd && mouseY in area2YStart..area2YEnd -> {
                level -= 1
                level = Mth.clamp(level, 1, enchantment!!.maxLevel)
                VoidBoundPacketRegistry.VOIDBOUND_CHANNEL.sendToServer(EnchantmentLevelPacket(enchantment!!, level, screen.menu.pos.asLong()))
                screen.menu.be!!.receiveScreenData(enchantment!!, level)
            }

            // Cast mouseX and mouseY to Int and check if the mouse is in area 3
            mouseX in area3XStart..area3XEnd && mouseY in area3YStart..area3YEnd -> {
                super.onClick(mouseX, mouseY)
            }
        }
    }

    private fun canAddLevel() : Boolean {
        val spirits: List<SpiritWithCount> = VoidBoundApi.getSpiritFromEnchant(enchantment!!, level)
        val toConsume: SimpleSpiritCharge = screen.menu.be!!.spiritsToConsume

        for (spirit in spirits) {
            val charge = toConsume.getChargeForType(spirit.type)
            if (charge + spirit.count > screen.maxSpiritCharge) {
                return false
            }
        }
        return true
    }

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        if (dontRender()) {
            return
        }

        val border = VoidBound.id("textures/gui/enchanter_widget.png")
        guiGraphics.blit(border, x, y, 0f,0f, width, height, width, height)

        if (screen.menu.be?.activated == false) {
            val l = Component.empty().append(Component.translatable("enchantment.level.$level"));
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, l, x + 15, y + 17, Color.WHITE.rgb)
        }

        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick)
    }
}