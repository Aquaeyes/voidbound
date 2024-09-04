package dev.sterner.common.item

import dev.sterner.registry.VoidBoundComponentRegistry
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class IchorItem(properties: Properties) : Item(properties) {

    override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {
        if (entity is ServerPlayer) {
            val comp = VoidBoundComponentRegistry.VOID_BOUND_REVELATION_COMPONENT.get(entity)

            if (!comp.hasIchorKnowledge) {
                comp.hasIchorKnowledge = true
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected)
    }
}