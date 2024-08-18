package dev.sterner.common.item.tool

import dev.sterner.api.item.HammerLikeItem
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import team.lodestar.lodestone.systems.item.tools.magic.MagicPickaxeItem
import java.awt.Color


class PickaxeOfTheCoreItem(material: Tier?, damage: Int, speed: Float, magicDamage: Float, properties: Properties?) :
    MagicPickaxeItem(
        material, damage, speed,
        magicDamage,
        properties
    ), HammerLikeItem, UpgradableTool {

    override fun getDestroySpeed(stack: ItemStack, state: BlockState): Float {
        return super.getDestroySpeed(stack, state) + getExtraMiningSpeed(stack)
    }

    override fun getRadius(): Int {
        return 3
    }

    override fun getDepth(): Int {
        return 1
    }

    override fun getBlockTags(): TagKey<Block> {
        return BlockTags.MINEABLE_WITH_PICKAXE
    }

    override fun appendHoverText(
        stack: ItemStack,
        level: Level?,
        tooltipComponents: MutableList<Component>,
        isAdvanced: TooltipFlag
    ) {
        val tool = stack.item as UpgradableTool
        if (tool.getNetherited(stack)) {
            tooltipComponents.add(
                Component.translatable("Netherited").withStyle(ChatFormatting.ITALIC).withStyle(
                Style.EMPTY.withColor(Color(90, 65, 0).rgb)
            ))
        }
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced)
    }
}