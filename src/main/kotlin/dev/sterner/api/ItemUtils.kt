package dev.sterner.api

import dev.sterner.common.entity.SoulSteelGolemEntity
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.ai.behavior.BehaviorUtils
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.phys.Vec3

object ItemUtils {

    fun throwItemsTowardPos(golem: SoulSteelGolemEntity, stacks: List<ItemStack>, pos: Vec3) {
        if (stacks.isNotEmpty()) {
            golem.swing(InteractionHand.OFF_HAND)

            for (itemStack in stacks) {
                BehaviorUtils.throwItem(golem, itemStack, pos.add(0.0, 1.0, 0.0))
            }
        }
    }

    fun removeOneItemFromItemEntity(itemEntity: ItemEntity): ItemStack {
        val itemStack = itemEntity.item
        val itemStack2 = itemStack.split(1)
        if (itemStack.isEmpty) {
            itemEntity.discard()
        } else {
            itemEntity.item = itemStack
        }

        return itemStack2
    }

    fun pickUpItem(golem: SoulSteelGolemEntity, itemEntity: ItemEntity) {
        val itemStack: ItemStack
        if (itemEntity.item.`is`(Items.IRON_AXE) && golem.mainHandItem.isEmpty) {
            itemStack = itemEntity.item
            golem.equipItemIfPossible(itemStack)
            itemEntity.discard()
        } else {
            golem.take(itemEntity, 1)
            itemStack = ItemUtils.removeOneItemFromItemEntity(itemEntity)
        }

        val bl = golem.equipItemIfPossible(itemStack) != ItemStack.EMPTY
        if (!bl) {
            putInInventory(golem, itemStack)
        }
    }

    private fun throwItemsTowardRandomPos(golem: SoulSteelGolemEntity, stacks: List<ItemStack>) {
        throwItemsTowardPos(golem, stacks, PosUtils.getRandomNearbyPos(golem))
    }

    private fun putInInventory(golem: SoulSteelGolemEntity, stack: ItemStack) {
        val itemStack = golem.inventory.addItem(stack)
        throwItemsTowardRandomPos(golem, listOf(itemStack))
    }

}