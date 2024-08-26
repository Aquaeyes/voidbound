package dev.sterner.common.item.tool.ichor

import dev.sterner.common.item.tool.AxeOfTheStreamItem
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import java.awt.Color

class IchoriumAxeItem(material: Tier?, damage: Float, speed: Float, magicDamage: Float, properties: Properties?) : AxeOfTheStreamItem(material, damage, speed,
    magicDamage,
    properties
) {
}