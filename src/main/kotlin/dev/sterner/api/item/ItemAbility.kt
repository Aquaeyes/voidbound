package dev.sterner.api.item

import net.minecraft.util.StringRepresentable
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.*

import kotlin.reflect.KClass

enum class ItemAbility(private val equipmentSlot: EquipmentSlot?, private val clazz: KClass<out Item>?): StringRepresentable {
    AUTOSMELT(null, DiggerItem::class),
    VAMPIRISM(null, SwordItem::class),
    QUICKDRAW(null, ProjectileWeaponItem::class),
    DISPERSED_STRIKE(null, SwordItem::class),
    SLOW_FALL(EquipmentSlot.FEET, null);

    override fun getSerializedName(): String {
        return this.name.lowercase()
    }

    fun getAbilityFromItem(item: Item): List<ItemAbility> {
        val list = mutableListOf<ItemAbility>()
        for (ability in entries) {
            if (ability.equipmentSlot == equipmentSlot) {
                list.add(ability)
            } else if (ability.clazz?.isInstance(item) == true) {
                list.add(ability)
            }
        }
        return list
    }
}