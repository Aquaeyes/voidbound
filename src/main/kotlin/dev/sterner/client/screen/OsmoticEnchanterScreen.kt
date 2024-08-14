package dev.sterner.client.screen

import dev.sterner.VoidBound
import dev.sterner.client.screen.widget.EnchantmentWidget
import dev.sterner.client.screen.widget.SelectedEnchantmentWidget
import dev.sterner.client.screen.widget.StartEnchantingWidget
import dev.sterner.common.blockentity.OsmoticEnchanterBlockEntity
import dev.sterner.common.menu.OsmoticEnchanterMenu
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.enchantment.Enchantment


class OsmoticEnchanterScreen(menu: OsmoticEnchanterMenu,
                             playerInventory: Inventory, title: Component
) : AbstractContainerScreen<OsmoticEnchanterMenu>(menu, playerInventory, title) {

    private var blockEntity: OsmoticEnchanterBlockEntity? = null
    var selectedEnchants: MutableSet<Int> = mutableSetOf()

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
            selectedEnchants.clear()
            refreshEnchants()
            menu.shouldRefresh = false
        }
        super.containerTick()
    }

    override fun resize(minecraft: Minecraft, width: Int, height: Int) {
        refreshEnchants()
        super.resize(minecraft, width, height)
    }

    fun refreshEnchants() {
        clearWidgets()
        val xInMenu = (this.width - this.imageWidth) / 2
        val yInMenu = (this.height - this.imageHeight) / 2

        val filteredEnchantments = blockEntity?.cachedEnchantments?.filter { it !in selectedEnchants }.orEmpty()

        // Render available enchantments
        renderEnchantments(filteredEnchantments, xInMenu, yInMenu)

        // Render selected enchantments
        renderSelectedEnchantments(xInMenu, yInMenu)

        // Add the start enchanting button
        addStartEnchantingWidget(xInMenu, yInMenu)
    }

    private fun renderEnchantments(enchantments: List<Int>, xInMenu: Int, yInMenu: Int) {
        enchantments.forEachIndexed { index, enchantId ->
            val (xOffset, yOffset) = calculateWidgetPosition(index, 3, 166, 15, 17, 17)
            val widget = EnchantmentWidget(this, xInMenu + xOffset, yInMenu + yOffset, 16, 16)
            widget.enchantment = Enchantment.byId(enchantId)
            this.addRenderableWidget(widget)
        }
    }

    private fun renderSelectedEnchantments(xInMenu: Int, yInMenu: Int) {
        selectedEnchants.forEachIndexed { index, enchantId ->
            val (xOffset, yOffset) = calculateWidgetPosition(index, 3, 82, 5, 37, 23)
            val widget = SelectedEnchantmentWidget(this, xInMenu + xOffset, yInMenu + yOffset)
            widget.enchantment = Enchantment.byId(enchantId)
            this.addRenderableWidget(widget)
        }
    }

    private fun addStartEnchantingWidget(xInMenu: Int, yInMenu: Int) {
        val widgetX = xInMenu + 13 + 16 * 6
        val widgetY = yInMenu + 18 * 5 + 19
        this.addRenderableWidget(StartEnchantingWidget(this, widgetX, widgetY))
    }

    private fun calculateWidgetPosition(index: Int, itemsPerRow: Int, baseX: Int, baseY: Int, offsetY: Int, offsetX: Int): Pair<Int, Int> {
        val xOffset = (index % itemsPerRow) * offsetX + baseX
        val yOffset = (index / itemsPerRow) * offsetY + baseY
        return Pair(xOffset, yOffset)
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

    override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {

    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        this.renderTooltip(guiGraphics, mouseX, mouseY)
    }



    companion object {
        private val CONTAINER_TEXTURE: ResourceLocation =
            VoidBound.id("textures/gui/enchanter.png")
    }
}