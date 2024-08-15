package dev.sterner.client.screen.widget

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import com.sammy.malum.core.systems.spirit.MalumSpiritType
import dev.sterner.VoidBound
import dev.sterner.api.util.VoidBoundRenderUtils
import dev.sterner.client.screen.OsmoticEnchanterScreen
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import org.joml.Matrix4f

class SpiritBarWidget(screen: OsmoticEnchanterScreen, x: Int, y: Int) : AbstractWidget(x, y, 8, 45,
    Component.empty()
) {

    var spirit_type: MalumSpiritType? = null
    val icon = VoidBound.id("textures/gui/spirit_bar.png")

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        //spirit_type.primaryColor
        if (spirit_type != null) {
            val rgba = unpackIntToRGBA(spirit_type!!.primaryColor.rgb)
            VoidBoundRenderUtils.blit(guiGraphics, icon, x, y, width, height, width, height, 1 - rgba[0] ,1 - rgba[1], 1 - rgba[2], 1f)

            if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
                tooltip = Tooltip.create(Component.translatable(spirit_type!!.identifier))
            }
        }
    }

    fun unpackIntToRGBA(color: Int): FloatArray {
        val r = (color shr 24) and 0xFF  // Extract red component (8 most significant bits)
        val g = (color shr 16) and 0xFF  // Extract green component
        val b = (color shr 8) and 0xFF   // Extract blue component
        val a = color and 0xFF           // Extract alpha component (8 least significant bits)

        // Convert the components to floats (0.0 to 1.0)
        return floatArrayOf(r / 255f, g / 255f, b / 255f, a / 255f)
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {

    }
}