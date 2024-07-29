package dev.sterner.common.entity.ai

import dev.sterner.VoidBound
import dev.sterner.common.entity.SoulSteelGolemEntity
import dev.sterner.registry.VoidBoundMemoryTypeRegistry
import dev.sterner.registry.VoidBoundSensorTypeRegistry
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.ai.sensing.SensorType
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor
import net.tslat.smartbrainlib.api.core.sensor.PredicateSensor
import java.util.function.BiPredicate


class GolemSpecificSensor : PredicateSensor<BlockPos, SoulSteelGolemEntity>(BiPredicate { pos: BlockPos, entity: SoulSteelGolemEntity -> true }) {
        override fun memoriesUsed(): List<MemoryModuleType<*>> {
        return MEMORIES // Return our memory list
    }

    override fun type(): SensorType<out ExtendedSensor<*>> {
        return VoidBoundSensorTypeRegistry.GOLEM_SPECIFIC_SENSOR.get()
    }


    companion object {
        private val MEMORIES: List<MemoryModuleType<*>> =
            ObjectArrayList.of<MemoryModuleType<*>>(
                VoidBoundMemoryTypeRegistry.NEARBY_TREE_TRUNKS.get(),
                MemoryModuleType.NEAREST_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
            )
    }
}