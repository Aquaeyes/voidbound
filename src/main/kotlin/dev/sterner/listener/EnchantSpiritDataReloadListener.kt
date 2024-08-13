package dev.sterner.listener

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.sammy.malum.core.handlers.SpiritHarvestHandler
import com.sammy.malum.core.listeners.SpiritDataReloadListener
import com.sammy.malum.core.systems.recipe.SpiritWithCount
import com.sammy.malum.core.systems.spirit.EntitySpiritDropData
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.profiling.ProfilerFiller

open class EnchantSpiritDataReloadListener : SimpleJsonResourceReloadListener(GsonBuilder().create(), "enchanting_data") {

    override fun apply(
        objectIn: MutableMap<ResourceLocation, JsonElement>,
        resourceManager: ResourceManager,
        profiler: ProfilerFiller
    ) {
        ENCHANTING_DATA.clear()

        for (entry in objectIn.values) {
            val `object` = entry.asJsonObject
            val name = `object`.getAsJsonPrimitive("registry_name").asString
            val resourceLocation = ResourceLocation(name)
            if (!BuiltInRegistries.ENCHANTMENT.containsKey(resourceLocation)) {
                continue
            }

            val array = `object`.getAsJsonArray("spirits")

            ENCHANTING_DATA[resourceLocation] = getSpiritData(array)
        }
    }

    private fun getSpiritData(array: JsonArray): List<SpiritWithCount> {
        val spiritData: MutableList<SpiritWithCount> = ArrayList()
        for (spiritElement in array) {
            val spiritObject = spiritElement.asJsonObject
            val spiritName = spiritObject.getAsJsonPrimitive("spirit").asString
            val count = spiritObject.getAsJsonPrimitive("count").asInt
            spiritData.add(SpiritWithCount(SpiritHarvestHandler.getSpiritType(spiritName), count))
        }
        return spiritData
    }


    companion object {
        var ENCHANTING_DATA: MutableMap<ResourceLocation, List<SpiritWithCount>> = mutableMapOf()
    }
}