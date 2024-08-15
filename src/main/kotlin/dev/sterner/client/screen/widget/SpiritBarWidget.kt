package dev.sterner.client.screen.widget

import com.sammy.malum.core.systems.spirit.MalumSpiritType
import dev.sterner.VoidBound
import dev.sterner.api.util.VoidBoundRenderUtils
import dev.sterner.client.screen.OsmoticEnchanterScreen
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth

class SpiritBarWidget(var screen: OsmoticEnchanterScreen, x: Int, y: Int) : AbstractWidget(x, y, 8, 45,
    Component.empty()
) {

    var isScy: Boolean = false
    var spirit_type: MalumSpiritType? = null
    private val icon = VoidBound.id("textures/gui/spirit_bar.png")

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        if (spirit_type != null) {
            val targetSpirits = screen.menu.be?.spiritsToConsume
            var normalizer = 0f
            if (targetSpirits != null) {
                normalizer = targetSpirits.getChargeForType(spirit_type!!) / 256f
                normalizer = Mth.clamp(normalizer, 0.0f, 1.0f)
            }
            println(normalizer)
            val rgba = unpackIntToRGBA(spirit_type!!.primaryColor.rgb)

            // Full height of the bar (when completely filled)
            val maxBarHeight = height
            val fillPercentage = normalizer

            // Calculate the height of the filled portion of the bar
            val fillHeight = (maxBarHeight * fillPercentage).toInt()

            // Calculate the Y position for the top of the filled portion
            val adjustedY = y + (maxBarHeight - fillHeight)

            // Calculate texture coordinates for cropping
            val minU = 0f
            val maxU = width.toFloat() / width.toFloat()
            val minV = (maxBarHeight - fillHeight).toFloat() / height.toFloat()  // Top of the portion to display
            val maxV = 1f  // Bottom of the portion to display

            VoidBoundRenderUtils.blit(guiGraphics, icon, x, adjustedY, width, fillHeight, width, height, rgba[0], rgba[1], rgba[2], 1f, minU, maxU, minV, maxV)

            if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + maxBarHeight) {
                tooltip = Tooltip.create(Component.translatable(spirit_type!!.identifier))
            }
        }
    }

    private fun unpackIntToRGBA(color: Int): FloatArray {
        val a = (color shr 24) and 0xFF
        val r = (color shr 16) and 0xFF
        val g = (color shr 8) and 0xFF
        val b = color and 0xFF

        return floatArrayOf(r / 255f, g / 255f, b / 255f, a / 255f)
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {

    }
}