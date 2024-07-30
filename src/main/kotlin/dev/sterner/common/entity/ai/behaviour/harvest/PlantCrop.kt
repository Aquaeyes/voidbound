package dev.sterner.common.entity.ai.behaviour.harvest

import com.mojang.datafixers.util.Pair
import dev.sterner.common.entity.SoulSteelGolemEntity
import dev.sterner.registry.VoidBoundMemoryTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.ai.behavior.BlockPosTracker
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.ai.memory.MemoryStatus
import net.minecraft.world.level.block.state.BlockState
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour
import net.tslat.smartbrainlib.util.BrainUtils

class PlantCrop : ExtendedBehaviour<SoulSteelGolemEntity>() {

    override fun getMemoryRequirements(): MutableList<Pair<MemoryModuleType<*>, MemoryStatus>> {
        return mutableListOf()
    }

    override fun start(entity: SoulSteelGolemEntity) {

    }
}