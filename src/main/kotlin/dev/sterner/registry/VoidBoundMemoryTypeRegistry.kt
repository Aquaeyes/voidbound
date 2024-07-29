package dev.sterner.registry

import dev.sterner.VoidBound
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.block.state.BlockState
import java.util.*


object VoidBoundMemoryTypeRegistry {

    var MEMORY_TYPES = LazyRegistrar.create(BuiltInRegistries.MEMORY_MODULE_TYPE, VoidBound.modid)

    var NEARBY_TREE_TRUNKS = MEMORY_TYPES.register("tree_trunk") {
        MemoryModuleType<List<com.mojang.datafixers.util.Pair<BlockPos, BlockState>>>(Optional.empty())
    }

    var NEARBY_ITEMS = MEMORY_TYPES.register("nearby_items") {
        MemoryModuleType<List<ItemEntity>>(Optional.empty())
    }

    var NEARBY_CROPS = MEMORY_TYPES.register("nearby_items") {
        MemoryModuleType<List<com.mojang.datafixers.util.Pair<BlockPos, BlockState>>>(Optional.empty())
    }
}