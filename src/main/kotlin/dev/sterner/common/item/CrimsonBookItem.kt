package dev.sterner.common.item

import dev.sterner.VoidBound
import net.minecraft.advancements.Advancement
import net.minecraft.client.Minecraft
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level


class CrimsonBookItem(properties: Properties) : Item(properties) {

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val item = player.getItemInHand(usedHand)
        if (!item.hasTag()) {
            val tag = CompoundTag()
            item.tag = tag
        }
        if (player is ServerPlayer) {
            grantAdvancementCriterion(player, VoidBound.id("revelationary/ichor_requirement_advancement"), "opened_crimson_rites")
        }
        if (item.tag!!.contains("open")) {
            if (item.tag!!.getBoolean("open")) {
                player.getItemInHand(usedHand).getOrCreateTag().putBoolean("open", false)
            } else {
                player.getItemInHand(usedHand).getOrCreateTag().putBoolean("open", true)
            }

        } else {
            player.getItemInHand(usedHand).getOrCreateTag().putBoolean("open", true)
        }

        return super.use(level, player, usedHand)
    }

    companion object {

        fun grantAdvancementCriterion(
            serverPlayerEntity: ServerPlayer,
            advancementIdentifier: ResourceLocation,
            criterion: String
        ) {
            if (serverPlayerEntity.getServer() == null) {
                return
            }
            val sal = serverPlayerEntity.getServer()!!.advancements
            val tracker = serverPlayerEntity.advancements

            val advancement: Advancement? = sal.getAdvancement(advancementIdentifier)
            if (advancement != null) {
                if (!tracker.getOrStartProgress(advancement).isDone()) {
                    tracker.award(advancement, criterion)
                }
            }
        }

        fun isOpen(): Boolean {
            val v: Boolean? =
                Minecraft.getInstance().player?.getItemInHand(InteractionHand.MAIN_HAND)?.tag?.getBoolean("open")
            return v == true
        }
    }
}