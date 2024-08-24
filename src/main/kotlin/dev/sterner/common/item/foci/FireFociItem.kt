package dev.sterner.common.item.foci

import com.sammy.malum.registry.common.SpiritTypeRegistry
import dev.sterner.registry.VoidBoundWandFociRegistry
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import java.awt.Color

class FireFociItem(properties: Properties) : AbstractFociItem(VoidBoundWandFociRegistry.FIRE.get(), properties) {

    override fun color(): Color = Color(250, 154, 31)

    override fun endColor(): Color = Color(210, 39, 150)

    override fun appendHoverText(
        stack: ItemStack,
        level: Level?,
        tooltipComponents: MutableList<Component>,
        isAdvanced: TooltipFlag
    ) {
        tooltipComponents.add(
            Component.translatable("Not yet implemented").withStyle(ChatFormatting.ITALIC).withStyle(
                Style.EMPTY.withColor(Color.red.rgb)
            )
        )
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced)
    }
}