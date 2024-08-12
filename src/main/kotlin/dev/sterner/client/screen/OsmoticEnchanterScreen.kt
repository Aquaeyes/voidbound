package dev.sterner.client.screen

import dev.sterner.VoidBound
import dev.sterner.client.screen.button.EnchantmentWidget
import dev.sterner.common.blockentity.OsmoticEnchanterBlockEntity
import dev.sterner.common.menu.OsmoticEnchanterMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.enchantment.Enchantment


class OsmoticEnchanterScreen(menu: OsmoticEnchanterMenu,
                             playerInventory: Inventory, title: Component
) : AbstractContainerScreen<OsmoticEnchanterMenu>(menu, playerInventory, title) {

    private var blockEntity: OsmoticEnchanterBlockEntity? = null
    var tooltip: MutableList<String> = mutableListOf()

    init {
        imageWidth = 232
        imageHeight = 222
        blockEntity = getBlockEntity(playerInventory, menu.pos)

    }

    override fun init() {
        super.init()
        refreshEnchants()
    }

    override fun containerTick() {
        if (menu.shouldRefresh) {
            refreshEnchants()
            menu.shouldRefresh = false
        }
        super.containerTick()
    }

    private fun refreshEnchants(){
        clearWidgets()
        val xInMenu = (this.width - this.imageWidth) / 2
        val yInMenu = (this.height - this.imageHeight) / 2

        for ((index, enchant) in blockEntity!!.cachedEnchantments.withIndex()) {
            val widget = EnchantmentWidget(this, xInMenu + index * 18, yInMenu)
            widget.enchantment = (Enchantment.byId(enchant))

            this.addRenderableWidget(widget)
        }
    }


    private fun getBlockEntity(playerInventory: Inventory, blockPos: BlockPos): OsmoticEnchanterBlockEntity? {
        val blockEntity = playerInventory.player.level().getBlockEntity(blockPos)
        if (blockEntity is OsmoticEnchanterBlockEntity) {
            return blockEntity
        }
        return null
    }

    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        val k = (this.width - this.imageWidth) / 2
        val l = (this.height - this.imageHeight) / 2
        guiGraphics.blit(CONTAINER_TEXTURE, k, l, 0, 0, this.imageWidth, this.imageHeight)
    }

    override fun renderTooltip(guiGraphics: GuiGraphics, x: Int, y: Int) {
        super.renderTooltip(guiGraphics, x, y)
        guiGraphics.renderTooltip(
            this.font,
            Component.empty(), x, y
        )
    }

    override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {

    }

    companion object {
        private val CONTAINER_TEXTURE: ResourceLocation =
            VoidBound.id("textures/gui/enchanter.png")
    }
}