package dev.sterner.registry

import dev.sterner.VoidBound
import dev.sterner.common.worldgen.TearOfEnderFeature
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration
import net.minecraft.world.level.levelgen.placement.*

object VoidBoundPlacedFeatureRegistry {

    val TEAR_OF_ENDER = TearOfEnderFeature(NoneFeatureConfiguration.CODEC)
    val TEAR_OF_ENDER_ID = VoidBound.id("tear_of_ender")
    val TEAR_OF_ENDER_FEATURE: ResourceKey<PlacedFeature> = registerKey("tear_of_ender")

    fun registerKey(name: String): ResourceKey<PlacedFeature> {
        return ResourceKey.create(Registries.PLACED_FEATURE, VoidBound.id(name))
    }
}