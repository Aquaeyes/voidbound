package dev.sterner.registry

import dev.sterner.VoidBound
import dev.sterner.common.entity.SoulSteelGolemEntity
import dev.sterner.common.entity.ai.GolemGatherSensor
import dev.sterner.common.entity.ai.GolemGuardSensor
import dev.sterner.common.entity.ai.GolemHarvestSensor
import dev.sterner.common.entity.ai.GolemSpecificSensor
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.ai.sensing.Sensor
import net.minecraft.world.entity.ai.sensing.SensorType
import java.util.function.Supplier

object VoidBoundSensorTypeRegistry {

    var SENSOR_TYPES = LazyRegistrar.create(BuiltInRegistries.SENSOR_TYPE, VoidBound.modid)

    var GOLEM_SPECIFIC_SENSOR = SENSOR_TYPES.register("golem_specific") {
        SensorType( ::GolemSpecificSensor )
    }

    var GOLEM_GATHER_SENSOR = SENSOR_TYPES.register("golem_gather") {
        SensorType( ::GolemGatherSensor )
    }

    var GOLEM_HARVEST_SENSOR = SENSOR_TYPES.register("golem_harvest") {
        SensorType( ::GolemHarvestSensor )
    }

    var GOLEM_GUARD_SENSOR = SENSOR_TYPES.register("golem_guard") {
        SensorType( ::GolemGuardSensor )
    }
}