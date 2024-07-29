package dev.sterner.common.entity.ai

import dev.sterner.api.GolemCore
import dev.sterner.common.entity.SoulSteelGolemEntity
import dev.sterner.registry.VoidBoundMemoryTypeRegistry
import dev.sterner.registry.VoidBoundSensorTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.ai.sensing.SensorType
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor
import net.tslat.smartbrainlib.api.core.sensor.PredicateSensor
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor
import net.tslat.smartbrainlib.`object`.SquareRadius
import net.tslat.smartbrainlib.util.BrainUtils
import net.tslat.smartbrainlib.util.EntityRetrievalUtil
import java.util.function.BiPredicate

class GolemGatherSensor : PredicateSensor<ItemEntity, SoulSteelGolemEntity>(
    BiPredicate{
        itemEntity: ItemEntity, entity: SoulSteelGolemEntity -> entity.getGolemCore() == GolemCore.GATHER &&entity.hasLineOfSight(itemEntity)
    }
) {

    private var radius: SquareRadius = SquareRadius(32.0, 16.0)

    override fun memoriesUsed(): MutableList<MemoryModuleType<*>> {
        return mutableListOf(VoidBoundMemoryTypeRegistry.NEARBY_ITEMS.get())
    }

    override fun type(): SensorType<out ExtendedSensor<*>> {
        return VoidBoundSensorTypeRegistry.GOLEM_GATHER_SENSOR.get()
    }

    fun setRadius(xz: Double, y: Double): GolemGatherSensor {
        this.radius = SquareRadius(xz, y)
        return this
    }

    override fun doTick(level: ServerLevel, entity: SoulSteelGolemEntity) {
        BrainUtils.setMemory(
            entity,
            VoidBoundMemoryTypeRegistry.NEARBY_ITEMS.get(),
            EntityRetrievalUtil.getEntities(
                level,
                this.radius.inflateAABB(entity.boundingBox)
            ) { obj ->
                obj is ItemEntity && predicate().test(obj as ItemEntity, entity)
            }
        )
    }
}