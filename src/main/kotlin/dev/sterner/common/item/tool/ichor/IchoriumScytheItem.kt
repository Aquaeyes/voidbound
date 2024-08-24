package dev.sterner.common.item.tool.ichor

import com.sammy.malum.common.enchantment.ReboundEnchantment
import com.sammy.malum.common.item.curiosities.weapons.scythe.MagicScytheItem
import com.sammy.malum.common.item.curiosities.weapons.scythe.MalumScytheItem
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import java.awt.Color

class IchoriumScytheItem(tier: Tier?, attackDamageIn: Float, attackSpeedIn: Float, builderIn: Properties?) : MagicScytheItem(
    tier,
    attackDamageIn + 3 + tier!!.attackDamageBonus,
    attackSpeedIn - 1.2f,
    4f,
    builderIn
) {

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