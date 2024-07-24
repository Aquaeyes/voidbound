package dev.sterner.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.levelgen.structure.Structure

object VoidBoundTags {
    val WEEPING_WELL: TagKey<Structure> = create("weeping_well")

    private fun create(name: String): TagKey<Structure> {
        return TagKey.create(Registries.STRUCTURE, ResourceLocation(name))
    }

    fun init() {

    }
}