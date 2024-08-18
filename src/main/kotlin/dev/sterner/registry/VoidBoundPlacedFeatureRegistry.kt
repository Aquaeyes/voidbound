package dev.sterner.registry

import com.google.common.collect.ImmutableList
import com.sammy.malum.MalumMod
import com.sammy.malum.registry.common.worldgen.ConfiguredFeatureRegistry
import dev.sterner.VoidBound
import dev.sterner.common.worldgen.TearOfEnderFeature
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.data.worldgen.features.FeatureUtils
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration
import net.minecraft.world.level.levelgen.placement.*

object VoidBoundPlacedFeatureRegistry {

    val TEAR_OF_ENDER = TearOfEnderFeature(NoneFeatureConfiguration.CODEC)
    val TEAR_OF_ENDER_ID = VoidBound.id("tear_of_ender")
    val TEAR_OF_ENDER_FEATURE: ResourceKey<PlacedFeature> = registerKey("tear_of_ender")

    val TEAR_OF_ENDER_CONFIGURED_KEY: ResourceKey<ConfiguredFeature<*, *>> =
        ResourceKey.create(Registries.CONFIGURED_FEATURE, VoidBound.id("tear_of_ender"))

    fun registerKey(name: String): ResourceKey<PlacedFeature> {
        return ResourceKey.create(Registries.PLACED_FEATURE, VoidBound.id(name))
    }


    fun placedBootStrap(context: BootstapContext<PlacedFeature>){
        val features = context.lookup(Registries.CONFIGURED_FEATURE)

        val tearOFEnder = features.getOrThrow(TEAR_OF_ENDER_CONFIGURED_KEY)
        PlacementUtils.register(context, TEAR_OF_ENDER_FEATURE, tearOFEnder,
            listOf(RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())
        )

    }

    fun bootstrap(context: BootstapContext<ConfiguredFeature<*, *>>){
        FeatureUtils.register(context, TEAR_OF_ENDER_CONFIGURED_KEY, TEAR_OF_ENDER, FeatureConfiguration.NONE)
    }
}