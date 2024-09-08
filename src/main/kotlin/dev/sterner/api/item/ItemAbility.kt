package dev.sterner.api.item

import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.BowItem
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ProjectileWeaponItem
import net.minecraft.world.item.SwordItem

import kotlin.reflect.KClass

enum class ItemAbility(val clazz: KClass<out Item>): StringRepresentable {
    AUTOSMELT(DiggerItem::class),
    VAMPIRISM(SwordItem::class),
    QUICKDRAW(ProjectileWeaponItem::class),
    DISPERSED_STRIKE(SwordItem::class),
    SLOW_FALL(Sho);

    override fun getSerializedName(): String {
        return this.name.lowercase()
    }
}