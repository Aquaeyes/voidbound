package dev.sterner.api.item

interface UpgradableAbilityItem {

    fun addExperience(experience: Int)

    fun removeExperience(experience: Int)
}