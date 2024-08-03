package dev.sterner.common.item

import dev.sterner.registry.VoidBoundWandFociRegistry
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.phys.BlockHitResult

class WandItem(properties: Properties) : Item (
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
            val blockHit = BlockHitResult(context.clickLocation, context.clickedFace.opposite, context.clickedPos, false)
            focus.get().onFocusRightClick(stack, level, player, blockHit)
        }
        return super.useOn(context)
    }
}