package dev.sterner.registry

import dev.sterner.VoidBound
import dev.sterner.block.SpiritBinderBlock
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour.Properties

object VoidBoundBlockRegistry {

    val BLOCKS: LazyRegistrar<Block> = LazyRegistrar.create(BuiltInRegistries.BLOCK, VoidBound.modid)

    var SPIRIT_BINDER = BLOCKS.register("spirit_binder") {
        SpiritBinderBlock(Properties.of())
    }
}