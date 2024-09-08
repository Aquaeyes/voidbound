package dev.sterner.api.item

import net.minecraft.nbt.CompoundTag

data class ItemAbilityWithLevel(val itemAbility: ItemAbility, val level: Int) {

    fun writeNbt(): CompoundTag {
        val compoundTag = CompoundTag()
        compoundTag.putString("itemAbility", itemAbility.name)
        compoundTag.putInt("level", level)
        return compoundTag
    }

    companion object {
        fun readNbt(nbt: CompoundTag): ItemAbilityWithLevel {
            val ability = ItemAbility.valueOf(nbt.getString("itemAbility"))
            val level = nbt.getInt("level")

            return ItemAbilityWithLevel(ability, level)
        }
    }


}
