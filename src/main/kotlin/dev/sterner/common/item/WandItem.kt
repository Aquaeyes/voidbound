package dev.sterner.common.item

import dev.sterner.registry.VoidBoundWandFociRegistry
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.UseAnim
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult

class WandItem(properties: Properties) : Item(
    properties
        .stacksTo(1)
        .rarity(Rarity.RARE)
) {

    override fun useOn(context: UseOnContext): InteractionResult {
        val player = context.player
        val level = context.level
        val stack = context.itemInHand
        if (stack.tag == null) {
            stack.tag = CompoundTag()
        }

        val id = VoidBoundWandFociRegistry.PORTABLE_HOLE.id.toString()
        stack.tag!!.putString("FocusName", id)//TODO remove

        val focusName = stack.tag?.getString("FocusName")
        val focus = VoidBoundWandFociRegistry.WAND_FOCUS.getOptional(focusName?.let { ResourceLocation.tryParse(it) })

        if (focus.isPresent && player != null) {
            val blockHit =
                BlockHitResult(context.clickLocation, context.clickedFace.opposite, context.clickedPos, false)
            focus.get().onFocusRightClick(stack, level, player, blockHit)
        }

        return super.useOn(context)
    }

    override fun onUseTick(level: Level, livingEntity: LivingEntity, stack: ItemStack, remainingUseDuration: Int) {
        if (stack.tag == null) {
            stack.tag = CompoundTag()
        }

        val id = VoidBoundWandFociRegistry.PORTABLE_HOLE.id.toString()
        stack.tag!!.putString("FocusName", id)//TODO remove

        val focusName = stack.tag?.getString("FocusName")
        val focus = VoidBoundWandFociRegistry.WAND_FOCUS.getOptional(focusName?.let { ResourceLocation.tryParse(it) })

        if (focus.isPresent && livingEntity is Player) {
            focus.get().onUsingFocusTick(stack, level, livingEntity)
        }
        super.onUseTick(level, livingEntity, stack, remainingUseDuration)
    }

    override fun releaseUsing(stack: ItemStack, level: Level, livingEntity: LivingEntity, timeCharged: Int) {
        if (stack.tag == null) {
            stack.tag = CompoundTag()
        }
        val id = VoidBoundWandFociRegistry.PORTABLE_HOLE.id.toString()
        stack.tag!!.putString("FocusName", id)//TODO remove

        val focusName = stack.tag?.getString("FocusName")
        val focus = VoidBoundWandFociRegistry.WAND_FOCUS.getOptional(focusName?.let { ResourceLocation.tryParse(it) })

        if (focus.isPresent && livingEntity is Player) {
            focus.get().onPlayerStopUsingFocus(stack, level, livingEntity)
        }
        super.releaseUsing(stack, level, livingEntity, timeCharged)
    }

    override fun use(pLevel: Level?, pPlayer: Player, pHand: InteractionHand?): InteractionResultHolder<ItemStack> {
        val itemstack = pPlayer.getItemInHand(pHand)
        if (pPlayer.cooldowns.isOnCooldown(itemstack.item)) {
            return InteractionResultHolder.fail(itemstack)
        } else {
            pPlayer.startUsingItem(pHand)
            return InteractionResultHolder.consume(itemstack)
        }
    }

    override fun getUseDuration(pStack: ItemStack?): Int {
        return 72000
    }

    override fun getUseAnimation(pStack: ItemStack?): UseAnim {
        return UseAnim.BOW
    }
}