package dev.sterner.client.screen

import dev.sterner.VoidBound
import dev.sterner.common.menu.OsmoticEnchanterMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class OsmoticEnchanterScreen(menu: OsmoticEnchanterMenu,
                             playerInventory: Inventory, title: Component
) : AbstractContainerScreen<OsmoticEnchanterMenu>(menu, playerInventory, title) {

    init {
        imageWidth = 232
        imageHeight = 222
    }

    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        val k = (this.width - this.imageWidth) / 2
        val l = (this.height - this.imageHeight) / 2
        guiGraphics.blit(CONTAINER_TEXTURE, k, l, 0, 0, this.imageWidth, this.imageHeight)
    }

    override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {

    }

    companion object {
        private val CONTAINER_TEXTURE: ResourceLocation =
            VoidBound.id("textures/gui/enchanter.png")
    }
}