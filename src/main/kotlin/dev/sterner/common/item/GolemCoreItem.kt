package dev.sterner.common.item

import dev.sterner.api.GolemCore
import net.minecraft.ChatFormatting
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import team.lodestar.lodestone.helpers.ColorHelper
import java.awt.Color

class GolemCoreItem(val core: GolemCore, properties: Properties) : Item(properties.rarity(Rarity.UNCOMMON)) {

    val color = Color(-19164)
    val txtColor = TextColor.fromRgb(ColorHelper.darker(color, 1, 0.75f).rgb)

    override fun appendHoverText(
        stack: ItemStack,
        level: Level?,
        tooltipComponents: MutableList<Component>,
        isAdvanced: TooltipFlag
    ) {
        var coreName: String = core.name.lowercase()
        tooltipComponents.add(Component.translatable("tooltip.voidbound.$coreName").withStyle(ChatFormatting.ITALIC).withStyle(
            Style.EMPTY.withColor(txtColor)))

        super.appendHoverText(stack, level, tooltipComponents, isAdvanced)
    }
}