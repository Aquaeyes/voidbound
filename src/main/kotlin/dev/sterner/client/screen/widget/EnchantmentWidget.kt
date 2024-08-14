package dev.sterner.client.screen.widget

import dev.sterner.api.util.VoidBoundUtils
import dev.sterner.client.screen.OsmoticEnchanterScreen
import dev.sterner.networking.EnchantmentLevelPacket
import dev.sterner.networking.RemoveEnchantPacket
import dev.sterner.registry.VoidBoundPacketRegistry
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.enchantment.Enchantment


open class EnchantmentWidget(var screen: OsmoticEnchanterScreen, x: Int, y: Int, width: Int, height: Int) : AbstractWidget(x, y, width, height,
    Component.empty()
) {

    var enchantment: Enchantment? = null

    var level: Int = 1

    override fun onClick(mouseX: Double, mouseY: Double) {
        val id = BuiltInRegistries.ENCHANTMENT.getId(enchantment)
        if (screen.selectedEnchants.contains(id)) {
            screen.selectedEnchants.remove(id)
            VoidBoundPacketRegistry.VOIDBOUND_CHANNEL.sendToServer(RemoveEnchantPacket(id, screen.menu.pos))

        } else {
            screen.selectedEnchants.add(BuiltInRegistries.ENCHANTMENT.getId(enchantment))
            VoidBoundPacketRegistry.VOIDBOUND_CHANNEL.sendToServer(EnchantmentLevelPacket(enchantment!!, level, screen.menu.pos.asLong()))
        }

        screen.refreshEnchants()
        super.onClick(mouseX, mouseY)
    }

    fun dontRender(): Boolean {
        return enchantment == null || !visible
    }

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        if (dontRender()) {
            return
        }

        val icon = VoidBoundUtils.getEnchantmentIcon(enchantment!!)
        var xx = x
        var yy = y

        if (this is SelectedEnchantmentWidget) {
            xx += 3
            yy += 10
        }

        guiGraphics.blit(icon, xx, yy, 0f,0f,16, 16, 16, 16)

        if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
            val tooltip: MutableList<Component> = ArrayList()
            if (enchantment != null) tooltip.add(enchantment!!.getFullname(level))
            setTooltip(Tooltip.create(enchantment!!.getFullname(level)))
        }
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {

    }
}