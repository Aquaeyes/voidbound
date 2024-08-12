package dev.sterner.client.screen.button

import dev.sterner.VoidBound
import dev.sterner.client.screen.OsmoticEnchanterScreen
import dev.sterner.networking.EnchantmentLevelPacket
import dev.sterner.registry.VoidBoundPacketRegistry
import io.netty.buffer.Unpooled
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.network.FriendlyByteBuf

class SelectedEnchantmentWidget(screen: OsmoticEnchanterScreen, x: Int, y: Int) : EnchantmentWidget(screen, x, y, 31, 23) {

    override fun onClick(mouseX: Double, mouseY: Double) {
        if (mouseX > x + (width - 10) && mouseX < x + width && mouseY > y + (height - 12) && mouseY < y + height - 4) {
            //TODO level down

            if (level > 0 && enchantment!!.maxLevel > level) {
                level--

                val tag = CompoundTag()

                tag.putInt("Enchantment", BuiltInRegistries.ENCHANTMENT.getId(enchantment))
                tag.putInt("Level", level)
                tag.putLong("Pos", screen.menu.pos.asLong())

                VoidBoundPacketRegistry.VOIDBOUND_CHANNEL.sendToServer(EnchantmentLevelPacket(tag))
            }

        } else if (mouseX > x + (width - 10) && mouseX < x + width && mouseY > y && mouseY < y + height - 4) {
            //TODO level up

            if (level > 0 && enchantment!!.maxLevel > level) {
                level++

                val tag = CompoundTag()

                tag.putInt("Enchantment", BuiltInRegistries.ENCHANTMENT.getId(enchantment))
                tag.putInt("Level", level)
                tag.putLong("Pos", screen.menu.pos.asLong())

                VoidBoundPacketRegistry.VOIDBOUND_CHANNEL.sendToServer(EnchantmentLevelPacket(tag))
            }
        } else {
            super.onClick(mouseX, mouseY)
        }
    }

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        if (dontRender()) {
            return
        }

        val border = VoidBound.id("textures/gui/enchanter_widget.png")
        guiGraphics.blit(border, x - 3, y - 3, 0f,0f,31, 23, 31, 23)

        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick)
    }
}