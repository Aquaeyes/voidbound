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

    fun setInfiniteCount(){
        aqueousCharge = 50
        aerialCharge = 50
        earthenCharge = 50
        arcaneCharge = 50
        infernalCharge = 50
        eldrichCharge = 50
        wickedCharge = 50
        sacredCharge = 50
        umbralCharge = 50
    }

    fun shouldBeInfinite(): Boolean{
        if (aqueousCharge < 50) {
            return false
        }
        if (aerialCharge < 50) {
            return false
        }
        if (arcaneCharge < 50) {
            return false
        }
        if (earthenCharge < 50) {
            return false
        }
        if (eldrichCharge < 50) {
            return false
        }
        if (infernalCharge < 50) {
            return false
        }
        if (sacredCharge < 50) {
            return false
        }
        if (wickedCharge < 50) {
            return false
        }
        if (umbralCharge < 50) {
            return false
        }

        return true
    }

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

    fun removeFromCharge(type: MalumSpiritType, count: Int): Boolean {
        when (type) {
            SpiritTypeRegistry.AQUEOUS_SPIRIT -> if (aqueousCharge >= count) {
                aqueousCharge -= count
            } else {
                return false
            }

            SpiritTypeRegistry.AERIAL_SPIRIT -> if (aerialCharge >= count) {
                aerialCharge -= count
            } else {
                return false
            }

            SpiritTypeRegistry.ARCANE_SPIRIT -> if (arcaneCharge >= count) {
                arcaneCharge -= count
            } else {
                return false
            }

            SpiritTypeRegistry.EARTHEN_SPIRIT -> if (earthenCharge >= count) {
                earthenCharge -= count
            } else {
                return false
            }

            SpiritTypeRegistry.ELDRITCH_SPIRIT -> if (eldrichCharge >= count) {
                eldrichCharge -= count
            } else {
                return false
            }

            SpiritTypeRegistry.INFERNAL_SPIRIT -> if (infernalCharge >= count) {
                infernalCharge -= count
            } else {
                return false
            }

            SpiritTypeRegistry.SACRED_SPIRIT -> if (sacredCharge >= count) {
                sacredCharge -= count
            } else {
                return false
            }

            SpiritTypeRegistry.WICKED_SPIRIT -> if (wickedCharge >= count) {
                wickedCharge -= count
            } else {
                return false
            }

            SpiritTypeRegistry.UMBRAL_SPIRIT -> if (umbralCharge >= count) {
                umbralCharge -= count
            } else {
                return false
            }
        }
        return false
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

    fun rechargeInfiniteCount() {
        if (aqueousCharge < 50) {
            aqueousCharge++
            return
        }
        if (aerialCharge < 50) {
            aerialCharge++
            return
        }
        if (arcaneCharge < 50) {
            arcaneCharge++
            return
        }
        if (earthenCharge < 50) {
            earthenCharge++
            return
        }
        if (infernalCharge < 50) {
            infernalCharge++
            return
        }
        if (eldrichCharge < 50) {
            eldrichCharge++
            return
        }
        if (sacredCharge < 50) {
            sacredCharge++
            return
        }
        if (wickedCharge < 50) {
            wickedCharge++
            return
        }
        if (umbralCharge < 50) {
            umbralCharge++
            return
        }
    }
}