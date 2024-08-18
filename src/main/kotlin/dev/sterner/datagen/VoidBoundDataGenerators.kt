package dev.sterner.datagen

import dev.sterner.registry.VoidBoundPlacedFeatureRegistry
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries

class VoidBoundDataGenerators : DataGeneratorEntrypoint {

    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator?) {

    }

    override fun buildRegistry(registryBuilder: RegistrySetBuilder) {
        registryBuilder.add(Registries.CONFIGURED_FEATURE, VoidBoundPlacedFeatureRegistry::bootstrap)
        registryBuilder.add(Registries.PLACED_FEATURE, VoidBoundPlacedFeatureRegistry::placedBootStrap)
    }
}