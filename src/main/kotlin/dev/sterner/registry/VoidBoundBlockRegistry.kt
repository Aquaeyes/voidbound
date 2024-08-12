package dev.sterner.registry

import dev.sterner.VoidBound
import dev.sterner.common.block.*
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction

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

    var SPIRIT_RIFT = BLOCKS.register("spirit_rift") {
        SpiritRiftBlock(Properties.of())
    }

    var PORTABLE_HOLE = BLOCKS.register("portable_hole") {
        PortableHoleBlock(
            FabricBlockSettings.create()
                .luminance(5)
                .noCollission()
                .strength(-1.0F, 3600000.0F)
                .noLootTable()
                .pushReaction(PushReaction.BLOCK)
        )
    }

    var OSMOTIC_ENCHANTER = BLOCKS.register("osmotic_enchanter") {
        OsmoticEnchanterBlock(FabricBlockSettings.create())
    }
}