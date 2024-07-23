package dev.sterner.api

import com.sammy.malum.core.systems.spirit.MalumSpiritType
import com.sammy.malum.registry.common.SpiritTypeRegistry
import net.minecraft.nbt.CompoundTag

data class SimpleSpiritCharge(
    var aqueousCharge: Int = 0,
    var aerialCharge: Int = 0,
    var arcaneCharge: Int = 0,
    var earthenCharge: Int = 0,
    var eldrichCharge: Int = 0,
    var infernalCharge: Int = 0,
    var sacredCharge: Int = 0,
    var wickedCharge: Int = 0,
    var umbralCharge: Int = 0
) {
    fun addToCharge(type: MalumSpiritType) {
        when (type) {
            SpiritTypeRegistry.AQUEOUS_SPIRIT -> aqueousCharge++
            SpiritTypeRegistry.AERIAL_SPIRIT -> aerialCharge++
            SpiritTypeRegistry.ARCANE_SPIRIT -> arcaneCharge++
            SpiritTypeRegistry.EARTHEN_SPIRIT -> earthenCharge++
            SpiritTypeRegistry.ELDRITCH_SPIRIT -> eldrichCharge++
            SpiritTypeRegistry.INFERNAL_SPIRIT -> infernalCharge++
            SpiritTypeRegistry.SACRED_SPIRIT -> sacredCharge++
            SpiritTypeRegistry.WICKED_SPIRIT -> wickedCharge++
            SpiritTypeRegistry.UMBRAL_SPIRIT -> umbralCharge++
        }
    }

    fun deserializeNBT(nbt: CompoundTag) : SimpleSpiritCharge {
        aqueousCharge = nbt.getInt("AqueousCharge")
        aerialCharge = nbt.getInt("AerialCharge")
        arcaneCharge = nbt.getInt("ArcaneCharge")
        earthenCharge = nbt.getInt("EarthenCharge")
        eldrichCharge = nbt.getInt("EldrichCharge")
        infernalCharge = nbt.getInt("InfernalCharge")
        sacredCharge = nbt.getInt("SacredCharge")
        wickedCharge = nbt.getInt("WickedCharge")
        umbralCharge = nbt.getInt("UmbralCharge")
        return this
    }

    fun serializeNBT(nbt: CompoundTag) {
        nbt.putInt("AqueousCharge", aqueousCharge)
        nbt.putInt("AerialCharge", aerialCharge)
        nbt.putInt("ArcaneCharge", arcaneCharge)
        nbt.putInt("EarthenCharge", earthenCharge)
        nbt.putInt("InfernalCharge", infernalCharge)
        nbt.putInt("EldrichCharge", eldrichCharge)
        nbt.putInt("SacredCharge", sacredCharge)
        nbt.putInt("WickedCharge", wickedCharge)
        nbt.putInt("UmbralCharge", umbralCharge)
    }

    fun getTotalCharge(): Int {
        return aqueousCharge + aerialCharge + arcaneCharge + earthenCharge + eldrichCharge +
                infernalCharge + sacredCharge + wickedCharge + umbralCharge
    }
}