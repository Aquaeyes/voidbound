package dev.sterner.registry

import dev.sterner.VoidBound
import dev.sterner.common.blockentity.SpiritRiftBlockEntity
import dev.sterner.common.blockentity.PortableHoleBlockEntity
import dev.sterner.common.blockentity.SpiritBinderBlockEntity
import dev.sterner.common.blockentity.SpiritStabilizerBlockEntity
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType

object VoidBoundBlockEntityTypeRegistry {

    val BLOCK_ENTITY_TYPES: LazyRegistrar<BlockEntityType<*>> =
        LazyRegistrar.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, VoidBound.modid)

    var SPIRIT_BINDER = BLOCK_ENTITY_TYPES.register("spirit_binder") {
        BlockEntityType.Builder.of(
            { pos, state -> SpiritBinderBlockEntity(pos, state) },
            VoidBoundBlockRegistry.SPIRIT_BINDER.get(),
        )
            .build(null)
    }

    var SPIRIT_BINDER_STABILIZER = BLOCK_ENTITY_TYPES.register("spirit_stabilizer") {
        BlockEntityType.Builder.of(
            { pos, state -> SpiritStabilizerBlockEntity(pos, state) },
            VoidBoundBlockRegistry.SPIRIT_STABILIZER.get(),
        )
            .build(null)
    }

    var DESTABILIZED_SPIRIT_RIFT = BLOCK_ENTITY_TYPES.register("destabilized_spirit_rift") {
        BlockEntityType.Builder.of(
            { pos, state -> SpiritRiftBlockEntity(pos, state) },
            VoidBoundBlockRegistry.DESTABILIZED_SPIRIT_RIFT.get()
        )
            .build(null)
    }


    var PORTABLE_HOLE = BLOCK_ENTITY_TYPES.register("portable_hole") {
        BlockEntityType.Builder.of(
            { pos, state -> PortableHoleBlockEntity(pos, state) },
            VoidBoundBlockRegistry.PORTABLE_HOLE.get()
        )
            .build(null)
    }
}