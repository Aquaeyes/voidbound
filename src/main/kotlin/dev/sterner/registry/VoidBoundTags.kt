package dev.sterner.registry

import dev.sterner.VoidBound
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.structure.Structure

object VoidBoundTags {
    val WEEPING_WELL: TagKey<Structure> = structure("weeping_well")

    val PORTABLE_HOLE_BLACKLIST: TagKey<Block> = block("portable_hole_blacklist")

    private fun structure(name: String): TagKey<Structure> {
        return TagKey.create(Registries.STRUCTURE, VoidBound.id(name))
    }

    private fun block(name: String): TagKey<Block> {
        return TagKey.create(Registries.BLOCK, VoidBound.id(name))
    }

    fun init() {

    }
}