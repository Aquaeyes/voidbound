package dev.sterner.api.rift

import com.sammy.malum.core.systems.spirit.MalumSpiritType
import com.sammy.malum.registry.common.SpiritTypeRegistry
import net.minecraft.nbt.CompoundTag

data class SimpleSpiritCharge(
    private val charges: MutableMap<MalumSpiritType, Int> = mutableMapOf(
        SpiritTypeRegistry.AQUEOUS_SPIRIT to 0,
        SpiritTypeRegistry.AERIAL_SPIRIT to 0,
        SpiritTypeRegistry.ARCANE_SPIRIT to 0,
        SpiritTypeRegistry.EARTHEN_SPIRIT to 0,
        SpiritTypeRegistry.ELDRITCH_SPIRIT to 0,
        SpiritTypeRegistry.INFERNAL_SPIRIT to 0,
        SpiritTypeRegistry.SACRED_SPIRIT to 0,
        SpiritTypeRegistry.WICKED_SPIRIT to 0,
        SpiritTypeRegistry.UMBRAL_SPIRIT to 0
    )
) {

    fun setInfiniteCount() {
        charges.keys.forEach { charges[it] = 50 }
    }

    fun shouldBeInfinite(): Boolean {
        return charges.values.all { it >= 50 }
    }

    fun addToCharge(type: MalumSpiritType, count: Int) {
        charges[type] = (charges[type] ?: 0) + count
    }

    fun removeFromCharge(type: MalumSpiritType, count: Int): Boolean {
        val currentCount = charges[type] ?: return false
        return if (currentCount >= count) {
            charges[type] = currentCount - count
            true
        } else {
            false
        }
    }

    fun deserializeNBT(nbt: CompoundTag): SimpleSpiritCharge {
        charges.keys.forEach { charges[it] = nbt.getInt(it.identifier) }
        return this
    }

    fun serializeNBT(nbt: CompoundTag) {
        charges.forEach { (type, count) -> nbt.putInt(type.identifier, count) }
    }

    fun getTotalCharge(): Int {
        return charges.values.sum()
    }

    fun rechargeInfiniteCount() {
        charges.forEach { (type, count) ->
            if (count < 50) {
                charges[type] = count + 1
                return
            }
        }
    }

    fun addToCharge(type: MalumSpiritType) {

    }
}