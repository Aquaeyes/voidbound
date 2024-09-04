package dev.sterner.registry

import dev.sterner.client.renderer.blockentity.*
import dev.sterner.client.renderer.entity.*
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers

object VoidBoundEntityRenderers {

    fun init() {

        BlockEntityRenderers.register(
            VoidBoundBlockEntityTypeRegistry.ELDRITCH_OBELISK.get(),
            ::EldritchObeliskBlockEntityRenderer
        )
        BlockEntityRenderers.register(
            VoidBoundBlockEntityTypeRegistry.SPIRIT_BINDER.get(),
            ::SpiritBinderBlockEntityRenderer
        )
        BlockEntityRenderers.register(
            VoidBoundBlockEntityTypeRegistry.SPIRIT_BINDER_STABILIZER.get(),
            ::SpiritStabilizerBlockEntityRenderer
        )
        BlockEntityRenderers.register(
            VoidBoundBlockEntityTypeRegistry.SPIRIT_RIFT.get(),
            ::SpiritRiftBlockEntityRenderer
        )
        BlockEntityRenderers.register(
            VoidBoundBlockEntityTypeRegistry.PORTABLE_HOLE.get(),
            ::PortableHoleBlockEntityRenderer
        )
        BlockEntityRenderers.register(
            VoidBoundBlockEntityTypeRegistry.OSMOTIC_ENCHANTER.get(),
            ::OsmoticEnchanterBlockEntityRenderer
        )

        EntityRendererRegistry.register(
            VoidBoundEntityTypeRegistry.PARTICLE_ENTITY.get(),
            ::ParticleEntityRenderer
        )
        EntityRendererRegistry.register(
            VoidBoundEntityTypeRegistry.SOUL_STEEL_GOLEM_ENTITY.get(),
            ::SoulSteelGolemEntityRenderer
        )
        EntityRendererRegistry.register(
            VoidBoundEntityTypeRegistry.CRIMSON_KNIGHT_ENTITY.get(),
            ::CrimsonKnightEntityRenderer
        )
        EntityRendererRegistry.register(
            VoidBoundEntityTypeRegistry.CRIMSON_ARCHER_ENTITY.get(),
            ::CrimsonArcherEntityRenderer
        )
        EntityRendererRegistry.register(
            VoidBoundEntityTypeRegistry.CRIMSON_CLERIC_ENTITY.get(),
            ::CrimsonClericEntityRenderer
        )
        EntityRendererRegistry.register(
            VoidBoundEntityTypeRegistry.CRIMSON_NECROMANCER_ENTITY.get(),
            ::CrimsonNecromancerEntityRenderer
        )
        EntityRendererRegistry.register(
            VoidBoundEntityTypeRegistry.CRIMSON_JESTER_ENTITY.get(),
            ::CrimsonJesterEntityRenderer
        )
        EntityRendererRegistry.register(
            VoidBoundEntityTypeRegistry.CRIMSON_HEAVY_KNIGHT_ENTITY.get(),
            ::CrimsonHeavyKnightEntityRenderer
        )
        EntityRendererRegistry.register(
            VoidBoundEntityTypeRegistry.BOLT_ENTITY.get(),
            ::BoltEntityRenderer
        )
    }
}