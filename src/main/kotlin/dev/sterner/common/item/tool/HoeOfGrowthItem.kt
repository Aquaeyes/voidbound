package dev.sterner.common.item.tool

import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Tier
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import team.lodestar.lodestone.systems.item.tools.magic.MagicHoeItem
import java.util.function.Consumer

class HoeOfGrowthItem(material: Tier?, damage: Int, speed: Float, magicDamage: Float, properties: Properties?) : MagicHoeItem(material, damage, speed,
    magicDamage,
    properties
) {

    override fun useOn(context: UseOnContext): InteractionResult {
        val player = context.player

        if (player!!.isShiftKeyDown) {
            return super.useOn(context)
        }

        for (xx in -1..1) {
            for (zz in -1..1) {
                super.useOn(UseOnContext(player, player.usedItemHand,
                    BlockHitResult(context.clickLocation, context.horizontalDirection, context.clickedPos.offset(xx, 0, zz), context.isInside))
                )
            }
        }
        return InteractionResult.SUCCESS
    }
}