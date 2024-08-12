package dev.sterner.client.screen.button

import dev.sterner.api.util.VoidBoundRenderUtils
import dev.sterner.api.util.VoidBoundUtils
import dev.sterner.client.screen.OsmoticEnchanterScreen
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.world.item.enchantment.Enchantment


class EnchantmentWidget(val screen: OsmoticEnchanterScreen, x: Int, y: Int) : AbstractWidget(x, y, 16, 16,
    Component.empty()
) {

    var enchantment: Enchantment? = null

    var level: Int = 1

    protected fun dontRender(): Boolean {
        return enchantment == null || !visible
    }

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        if (dontRender())
            return

        val icon = VoidBoundUtils.getEnchantmentIcon(enchantment!!)

        VoidBoundRenderUtils.drawScreenIcon(guiGraphics.pose(), icon)

        if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
            val tooltip: MutableList<Component> = ArrayList()
            if (enchantment != null) tooltip.add(enchantment!!.getFullname(level))
            screen.getTooltip().addAll(tooltip)
        }
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {

    }
}