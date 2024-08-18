package dev.sterner.common.item.tool

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import team.lodestar.lodestone.systems.item.tools.magic.MagicHoeItem
import java.awt.Color

class HoeOfGrowthItem(material: Tier?, damage: Int, speed: Float, magicDamage: Float, properties: Properties?) :
    MagicHoeItem(
        material, damage, speed,
        magicDamage,
        properties
    ), UpgradableTool {

    override fun getDestroySpeed(stack: ItemStack, state: BlockState): Float {
        return super.getDestroySpeed(stack, state) + getExtraMiningSpeed(stack)
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val player = context.player

        if (player!!.isShiftKeyDown) {
            return super.useOn(context)
        }

        for (xx in -1..1) {
            for (zz in -1..1) {
                super.useOn(
                    UseOnContext(
                        player, player.usedItemHand,
                        BlockHitResult(
                            context.clickLocation,
                            context.horizontalDirection,
                            context.clickedPos.offset(xx, 0, zz),
                            context.isInside
                        )
                    )
                )
            }
        }
        return InteractionResult.SUCCESS
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