package dev.sterner.registry

import com.sammy.malum.registry.common.item.ItemRegistry
import net.minecraft.tags.ItemTags
import net.minecraft.util.LazyLoadedValue
import net.minecraft.world.item.Items
import net.minecraft.world.item.Tier
import net.minecraft.world.item.crafting.Ingredient
import java.util.function.Supplier

enum class VoidBoundTiers(
    private val level: Int,
    private val uses: Int,
    private val speed: Float,
    private val damage: Float,
    private val enchantmentValue: Int,
    repairIngredient: Supplier<Ingredient>
) :
    Tier {
    ELEMENTAL(4, 2000, 8.0f, 3.0f, 16, Supplier { Ingredient.of(ItemRegistry.MALIGNANT_PEWTER_INGOT.get()) });

    private val repairIngredient = LazyLoadedValue(repairIngredient)

    override fun getUses(): Int {
        return this.uses
    }

    override fun getSpeed(): Float {
        return this.speed
    }

    override fun getAttackDamageBonus(): Float {
        return this.damage
    }

    override fun getLevel(): Int {
        return this.level
    }

    override fun getEnchantmentValue(): Int {
        return this.enchantmentValue
    }

    override fun getRepairIngredient(): Ingredient {
        return repairIngredient.get()
    }
}
