package dev.sterner.client.screen

import dev.sterner.VoidBound
import dev.sterner.client.screen.button.EnchantmentWidget
import dev.sterner.common.blockentity.OsmoticEnchanterBlockEntity
import dev.sterner.common.menu.OsmoticEnchanterMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantments


class OsmoticEnchanterScreen(menu: OsmoticEnchanterMenu,
                             playerInventory: Inventory, title: Component
) : AbstractContainerScreen<OsmoticEnchanterMenu>(menu, playerInventory, title) {

    private val enchantWidgets: Array<EnchantmentWidget?> = arrayOfNulls(16)
    private val stack: ItemStack? = null
    private var blockEntity: OsmoticEnchanterBlockEntity? = null

    init {
        imageWidth = 232
        imageHeight = 222
        blockEntity = getBlockEntity(playerInventory, menu.pos)
        initWidgets()
    }

    private fun initWidgets() {


        val widget = EnchantmentWidget(this, 32, 32)
        widget.enchantment = Enchantments.SHARPNESS
        this.addRenderableWidget(widget)
    }

    private fun getBlockEntity(playerInventory: Inventory, blockPos: BlockPos): OsmoticEnchanterBlockEntity {
        val blockEntity = playerInventory.player.level().getBlockEntity(blockPos)
        if (blockEntity is OsmoticEnchanterBlockEntity) {
            return blockEntity
        }
        throw IllegalStateException("Tile entity is not correct! $blockEntity")
    }

    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        val k = (this.width - this.imageWidth) / 2
        val l = (this.height - this.imageHeight) / 2
        guiGraphics.blit(CONTAINER_TEXTURE, k, l, 0, 0, this.imageWidth, this.imageHeight)
    }


    override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {

    }

    fun getTooltip(): MutableList<Component> {
        return mutableListOf()
    }

    companion object {
        private val CONTAINER_TEXTURE: ResourceLocation =
            VoidBound.id("textures/gui/enchanter.png")
    }
}