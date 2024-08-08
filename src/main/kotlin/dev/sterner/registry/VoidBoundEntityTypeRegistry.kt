package dev.sterner.registry

import dev.sterner.VoidBound
import dev.sterner.common.entity.*
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityDimensions
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.Level

object VoidBoundEntityTypeRegistry {
    var ENTITY_TYPES = LazyRegistrar.create(BuiltInRegistries.ENTITY_TYPE, VoidBound.modid)

    var PARTICLE_ENTITY = ENTITY_TYPES.register("particle_entity") {
        EntityType.Builder.of(
            { _: EntityType<ParticleEntity?>?, w: Level ->
                ParticleEntity(
                    w
                )
            }, MobCategory.MISC
        ).sized(0.05f, 0.05f).clientTrackingRange(50).build(VoidBound.id("particle_entity").toString())
    }

    var SOUL_STEEL_GOLEM_ENTITY = ENTITY_TYPES.register("soul_steel_golem") {
        FabricEntityTypeBuilder.Mob.createMob<SoulSteelGolemEntity>()
            .entityFactory { _, w -> SoulSteelGolemEntity(w) }
            .dimensions(EntityDimensions(0.5f, 1.1f, true))
            .spawnGroup(MobCategory.CREATURE)
            .defaultAttributes { AbstractGolemEntity.createGolemAttributes() }
            .build()
    }

    var CRIMSON_KNIGHT_ENTITY = ENTITY_TYPES.register("crimson_knight") {
        FabricEntityTypeBuilder.Mob.createMob<CrimsonKnightEntity>()
            .entityFactory { _, w -> CrimsonKnightEntity(w) }
            .dimensions(EntityDimensions(0.5f, 1.85f, true))
            .spawnGroup(MobCategory.MONSTER)
            .defaultAttributes { CrimsonKnightEntity.createCrimsonAttributes() }
            .build()
    }

    var CRIMSON_ARCHER_ENTITY = ENTITY_TYPES.register("crimson_archer") {
        FabricEntityTypeBuilder.Mob.createMob<CrimsonArcherEntity>()
            .entityFactory { _, w -> CrimsonArcherEntity(w) }
            .dimensions(EntityDimensions(0.5f, 1.85f, true))
            .spawnGroup(MobCategory.MONSTER)
            .defaultAttributes { CrimsonArcherEntity.createCrimsonAttributes() }
            .build()
    }

    var CRIMSON_CLERIC_ENTITY = ENTITY_TYPES.register("crimson_cleric") {
        FabricEntityTypeBuilder.Mob.createMob<CrimsonClericEntity>()
            .entityFactory { _, w -> CrimsonClericEntity(w) }
            .dimensions(EntityDimensions(0.5f, 1.85f, true))
            .spawnGroup(MobCategory.MONSTER)
            .defaultAttributes { CrimsonClericEntity.createCrimsonAttributes() }
            .build()
    }

    var CRIMSON_NECROMANCER_ENTITY = ENTITY_TYPES.register("crimson_necromancer") {
        FabricEntityTypeBuilder.Mob.createMob<CrimsonNecromancerEntity>()
            .entityFactory { _, w -> CrimsonNecromancerEntity(w) }
            .dimensions(EntityDimensions(0.5f, 1.85f, true))
            .spawnGroup(MobCategory.MONSTER)
            .defaultAttributes { CrimsonNecromancerEntity.createCrimsonAttributes() }
            .build()
    }

    var CRIMSON_HEAVY_KNIGHT_ENTITY = ENTITY_TYPES.register("crimson_heavy_knight") {
        FabricEntityTypeBuilder.Mob.createMob<CrimsonHeavyKnightEntity>()
            .entityFactory { _, w -> CrimsonHeavyKnightEntity(w) }
            .dimensions(EntityDimensions(0.5f, 1.85f, true))
            .spawnGroup(MobCategory.MONSTER)
            .defaultAttributes { CrimsonHeavyKnightEntity.createCrimsonAttributes() }
            .build()
    }

    var CRIMSON_JESTER_ENTITY = ENTITY_TYPES.register("crimson_jester") {
        FabricEntityTypeBuilder.Mob.createMob<CrimsonJesterEntity>()
            .entityFactory { _, w -> CrimsonJesterEntity(w) }
            .dimensions(EntityDimensions(0.5f, 1.85f, true))
            .spawnGroup(MobCategory.MONSTER)
            .defaultAttributes { CrimsonJesterEntity.createCrimsonAttributes() }
            .build()
    }

    var CRIMSON_PALADIN_ENTITY = ENTITY_TYPES.register("crimson_paladin") {
        FabricEntityTypeBuilder.Mob.createMob<CrimsonPaladinEntity>()
            .entityFactory { _, w -> CrimsonPaladinEntity(w) }
            .dimensions(EntityDimensions(0.7f, 2.15f, true))
            .spawnGroup(MobCategory.MONSTER)
            .defaultAttributes { CrimsonPaladinEntity.createCrimsonAttributes() }
            .build()
    }
}