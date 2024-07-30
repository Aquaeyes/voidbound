package dev.sterner.common.entity.ai.behaviour

import com.mojang.datafixers.util.Pair
import dev.sterner.api.ItemUtils
import dev.sterner.common.entity.SoulSteelGolemEntity
import dev.sterner.registry.VoidBoundMemoryTypeRegistry
import net.minecraft.core.Direction
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.ai.memory.MemoryStatus
import net.minecraft.world.item.ItemStack
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour
import net.tslat.smartbrainlib.util.BrainUtils

class InsertItemsToStorage : ExtendedBehaviour<SoulSteelGolemEntity>() {

    private var insertCooldown = 0

    override fun getMemoryRequirements(): MutableList<Pair<MemoryModuleType<*>, MemoryStatus>> {
        return mutableListOf(
            Pair.of(VoidBoundMemoryTypeRegistry.STORAGE_LOCATION.get(), MemoryStatus.VALUE_PRESENT)
        )
    }

    override fun tick(entity: SoulSteelGolemEntity) {
        super.tick(entity)
        if (insertCooldown > 0) {
            insertCooldown--
        }

        if (!entity.inventory.isEmpty && insertCooldown <= 0) {
            val memory = BrainUtils.getMemory(entity, VoidBoundMemoryTypeRegistry.STORAGE_LOCATION.get())
            if (memory != null && memory.distToCenterSqr(entity.position()) < 2) {
                val be = entity.level().getBlockEntity(memory)
                if (be is WorldlyContainer) {
                    val container = be as WorldlyContainer

                    var insert: ItemStack? = null
                    var index = 0
                    for (item in entity.inventory.items) {
                        index++
                        if (!item.isEmpty) {
                            insert = item
                            break
                        }
                    }
                    if (insert != null) {
                        val remainder = ItemUtils.addItem(container, insert, Direction.DOWN)
                        entity.inventory.setItem(index, remainder)
                        insertCooldown = 20
                    }
                }
            }
        }
    }

    override fun start(entity: SoulSteelGolemEntity) {

        super.start(entity)
    }

}