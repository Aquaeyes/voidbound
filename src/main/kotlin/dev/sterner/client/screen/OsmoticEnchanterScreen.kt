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
            refreshEnchants()
            menu.shouldRefresh = false
        }
        super.containerTick()
    }

    override fun resize(minecraft: Minecraft, width: Int, height: Int) {
        refreshEnchants()
        super.resize(minecraft, width, height)
    }

    fun refreshEnchants(){
        clearWidgets()
        val xInMenu = (this.width - this.imageWidth) / 2
        val yInMenu = (this.height - this.imageHeight) / 2

        val filteredList = blockEntity!!.cachedEnchantments?.filter { enchantId -> enchantId !in selectedEnchants }

        for ((index, enchant) in filteredList!!.withIndex()) {
            val widget = if (index < 8) {
                EnchantmentWidget(this, xInMenu + index * 18 + 24 + 24 + 8, yInMenu + 15,16, 16)
            } else {
                EnchantmentWidget(this, xInMenu + (index - 8) * 18 + 24 + 24 + 8, yInMenu + 15 + 16, 16, 16)
            }
            widget.enchantment = (Enchantment.byId(enchant))

            this.addRenderableWidget(widget)
        }

        for ((index, enchant) in selectedEnchants.withIndex()) {
            val widget = SelectedEnchantmentWidget(this, xInMenu + 128 + 32 + 32 + 18 + 7, yInMenu + index * 22 + 18)
            widget.enchantment = (Enchantment.byId(enchant))
            this.addRenderableWidget(widget)
        }

        this.addRenderableWidget(StartEnchantingWidget(this, xInMenu + 13, yInMenu + 18 * 5 + 9))
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