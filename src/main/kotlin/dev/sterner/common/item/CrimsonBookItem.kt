package dev.sterner.common.item

import com.sammy.malum.client.VoidRevelationHandler
import com.sammy.malum.client.VoidRevelationHandler.RevelationType
import dev.sterner.VoidBound
import dev.sterner.api.util.VoidBoundUtils
import dev.sterner.registry.VoidBoundBlockRegistry
import net.minecraft.client.Minecraft
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level


class CrimsonBookItem(properties: Properties) : BlockItem(VoidBoundBlockRegistry.CRIMSON_RITES.get(), properties) {

    override fun useOn(context: UseOnContext): InteractionResult {
        if (context.player?.isShiftKeyDown == true) {
            return super.useOn(context)
        }
        return InteractionResult.PASS
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {

        if (player.isShiftKeyDown) {
            return super.use(level, player, usedHand)
        }

        val item = player.getItemInHand(usedHand)
        if (!item.hasTag()) {
            val tag = CompoundTag()
            item.tag = tag
        }
        var giveAdvancement = false

        if (item.tag!!.contains("open")) {
            if (item.tag!!.getBoolean("open")) {
                player.getItemInHand(usedHand).getOrCreateTag().putBoolean("open", false)
            } else {
                player.getItemInHand(usedHand).getOrCreateTag().putBoolean("open", true)
                giveAdvancement = true
            }

        } else {
            player.getItemInHand(usedHand).getOrCreateTag().putBoolean("open", true)
            giveAdvancement = true
        }

        if (giveAdvancement && player is ServerPlayer) {
            VoidBoundUtils.grantAdvancementCriterion(
                player,
                VoidBound.id("revelationary/ichor_requirement_advancement"),
                "opened_crimson_rites"
            )
        }
        if (giveAdvancement && level.isClientSide) {
            VoidRevelationHandler.seeTheRevelation(RevelationType.ICHOR)
        }

        return super.use(level, player, usedHand)
    }

    companion object {

        fun isOpen(): Boolean {
            val v: Boolean? =
                Minecraft.getInstance().player?.getItemInHand(InteractionHand.MAIN_HAND)?.tag?.getBoolean("open")
            return v == true
        }
    }
}