package dev.sterner.registry

import com.sammy.malum.registry.common.item.ItemRegistry
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient


enum class VoidBoundMaterials (

    val materialName: String,
    private val durabilityMultiplier: Int,
    private val damageReduction: IntArray,
    val enchantability: Int,
    private val equipSound: SoundEvent,
    repairItem: Item,
    private val toughness: Float
) :
    ArmorMaterial {
    HALLOWED(
        "malum:hallowed",
        16,
        intArrayOf(1, 3, 4, 2),
        25,
        SoundEvents.ARMOR_EQUIP_GOLD,
        ItemRegistry.HALLOWED_GOLD_INGOT.get(),
        0.0f
    );

    private val repairItem: Item = repairItem

    override fun getDurabilityForType(type: ArmorItem.Type): Int {
        return this.durabilityMultiplier * MAX_DAMAGE_ARRAY[type.slot.index]
    }

    override fun getDefenseForType(type: ArmorItem.Type): Int {
        return damageReduction[type.slot.index]
    }

    override fun getEquipSound(): SoundEvent {
        return this.equipSound
    }

    override fun getEnchantmentValue(): Int {
        return enchantability
    }

    override fun getRepairIngredient(): Ingredient {
        return Ingredient.of(this.repairItem)
    }

    override fun getName(): String {
        return this.materialName
    }

    override fun getToughness(): Float {
        return this.toughness
    }

    override fun getKnockbackResistance(): Float {
        return 0.0f
    }

    companion object {
        private val MAX_DAMAGE_ARRAY = intArrayOf(13, 15, 16, 11)
    }
}