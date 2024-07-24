package dev.sterner.data

import com.sammy.malum.MalumMod
import com.sammy.malum.registry.common.worldgen.StructureRegistry
import dev.sterner.VoidBound
import dev.sterner.registry.VoidBoundTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.structure.Structure
import java.util.concurrent.CompletableFuture

class VoidBoundTagProvider(output: FabricDataOutput?,
                           registriesFuture: CompletableFuture<HolderLookup.Provider>?
) : FabricTagProvider<Structure>(output, Registries.STRUCTURE, registriesFuture) {

    override fun addTags(arg: HolderLookup.Provider) {

        this.getOrCreateTagBuilder(VoidBoundTags.WEEPING_WELL).add(weep)
    }

    companion object {
        var weep = ResourceKey.create(Registries.STRUCTURE, MalumMod.malumPath("weeping_well"))
    }
}