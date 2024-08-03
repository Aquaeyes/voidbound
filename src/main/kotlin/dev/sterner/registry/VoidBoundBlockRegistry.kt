package dev.sterner.registry

import dev.sterner.VoidBound
import dev.sterner.common.block.DestabilizedSpiritRiftBlock
import dev.sterner.common.block.PortableHoleBlock
import dev.sterner.common.block.SpiritBinderBlock
import dev.sterner.common.block.SpiritBinderStabilizerBlock
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.material.MapColor

object VoidBoundBlockRegistry {

    val BLOCKS: LazyRegistrar<Block> = LazyRegistrar.create(BuiltInRegistries.BLOCK, VoidBound.modid)

    var SPIRIT_BINDER = BLOCKS.register("spirit_binder") {
        SpiritBinderBlock(
            Properties.of()
                .mapColor(MapColor.STONE)
                .requiresCorrectToolForDrops()
                .strength(1.5F, 8.0F)
        )
    }

    var SPIRIT_STABILIZER = BLOCKS.register("spirit_stabilizer") {
        SpiritBinderStabilizerBlock(Properties.of())
    }

    var DESTABILIZED_SPIRIT_RIFT = BLOCKS.register("destabilized_spirit_rift") {
        DestabilizedSpiritRiftBlock(Properties.of())
    }

    var PORTABLE_HOLE = BLOCKS.register("portable_hole") {
        PortableHoleBlock(FabricBlockSettings.copyOf(Blocks.AIR).luminance(5))
    }
}