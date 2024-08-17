package dev.sterner.client.screen.widget

import dev.sterner.client.screen.FociSelectionScreen
import dev.sterner.common.item.WandItem
import dev.sterner.networking.SelectFociPacket
import dev.sterner.registry.VoidBoundPacketRegistry
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class SelectFociWidget(var screen: FociSelectionScreen, x: Int, y: Int, width: Int, height: Int
) : AbstractWidget(x, y, width, height, Component.empty()) {

    var foci: ItemStack? = null

    override fun onClick(mouseX: Double, mouseY: Double) {

        var mainHandItem = screen.player?.mainHandItem
        if (mainHandItem?.item is WandItem && foci != null) {
            val wandItem = mainHandItem.item as WandItem
            wandItem.updateSelectedFoci(mainHandItem, foci!!)
           // wandItem.bindFoci(mainHandItem, foci!!)
            VoidBoundPacketRegistry.VOID_BOUND_CHANNEL.sendToServer(SelectFociPacket(screen.player!!.uuid, foci!!))
        }
        Minecraft.getInstance().setScreen(null)


        super.onClick(mouseX, mouseY)
    }

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        if (foci != null) {
            guiGraphics.renderItem(foci!!, x, y)
        }
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {

    }
}