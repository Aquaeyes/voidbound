package dev.sterner.api.item

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag

interface UpgradableAbilityItem {

    fun addExperience(experience: Int)

    fun removeExperience(experience: Int)

    fun writeNbt(abilities: List<ItemAbilityWithLevel>): CompoundTag {
        val listTag = ListTag()
        for (ability in abilities) {
            val ab = ability.writeNbt()
            listTag.add(ab)
        }
        val tag = CompoundTag()
        tag.put("abilities", listTag)
        return tag
    }

    fun readNbt(tag: CompoundTag): List<ItemAbilityWithLevel> {
        val tagList = tag.getList("abilities", 10)
        val list = mutableListOf<ItemAbilityWithLevel>()
        for (ability in tagList) {

            val a = ItemAbilityWithLevel.readNbt()
        }
    }
}