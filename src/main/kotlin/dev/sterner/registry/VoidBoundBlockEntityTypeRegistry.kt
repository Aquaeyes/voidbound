package dev.sterner.registry

import dev.sterner.VoidBound
import dev.sterner.block.SpiritBinderBlock
import dev.sterner.blockentity.SpiritBinderBlockEntity
import dev.sterner.blockentity.SpiritBinderStabilizerBlockEntity
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour.Properties

object VoidBoundBlockEntityTypeRegistry {

    val BLOCK_ENTITY_TYPES: LazyRegistrar<BlockEntityType<*>> = LazyRegistrar.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, VoidBound.modid)

    var SPIRIT_BINDER = BLOCK_ENTITY_TYPES.register("spirit_binder") {
        BlockEntityType.Builder.of(
            { pos, state -> SpiritBinderBlockEntity(pos, state) },
            VoidBoundBlockRegistry.SPIRIT_BINDER.get(),
        )
            .build(null)
    }

    var SPIRIT_BINDER_STABILIZER = BLOCK_ENTITY_TYPES.register("spirit_binder_stabilizer") {
        BlockEntityType.Builder.of(
            { pos, state -> SpiritBinderStabilizerBlockEntity(pos, state) },
            VoidBoundBlockRegistry.SPIRIT_BINDER_STABILIZER.get(),
        )
            .build(null)
    }


}