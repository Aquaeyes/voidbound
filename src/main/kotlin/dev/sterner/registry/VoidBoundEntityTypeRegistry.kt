package dev.sterner.registry

import com.sammy.malum.MalumMod
import com.sammy.malum.common.entity.thrown.ThrownConcentratedGluttony
import dev.sterner.VoidBound
import dev.sterner.entity.ParticleEntity
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.Level

object VoidBoundEntityTypeRegistry {
    var ENTITY_TYPES = LazyRegistrar.create(BuiltInRegistries.ENTITY_TYPE, VoidBound.modid)

    var PARTICLE_ENTITY = ENTITY_TYPES.register("particle_entity",(
    {
        EntityType.Builder.of(
            { e: EntityType<ParticleEntity?>?, w: Level ->
                ParticleEntity(
                    w
                )
            }, MobCategory.MISC
        ).sized(0.05f, 0.05f).clientTrackingRange(50).build(VoidBound.id("particle_entity").toString())
    }))
}