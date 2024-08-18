package dev.sterner.registry

import dev.sterner.registry.VoidBoundPlacedFeatureRegistry.TEAR_OF_ENDER
import dev.sterner.registry.VoidBoundPlacedFeatureRegistry.TEAR_OF_ENDER_FEATURE
import dev.sterner.registry.VoidBoundPlacedFeatureRegistry.TEAR_OF_ENDER_ID
import net.fabricmc.fabric.api.biome.v1.*
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.GenerationStep

object VoidBoundWorldGenerations {

    fun init(){
        Registry.register(BuiltInRegistries.FEATURE, TEAR_OF_ENDER_ID, TEAR_OF_ENDER)

        BiomeModifications.addFeature(
            BiomeSelectors.foundInTheEnd(),
            GenerationStep.Decoration.VEGETAL_DECORATION,
            TEAR_OF_ENDER_FEATURE
        )
    }

}