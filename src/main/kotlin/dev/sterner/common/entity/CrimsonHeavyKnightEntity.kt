package dev.sterner.common.entity

import dev.sterner.common.entity.ai.goal.RaiseShieldGoal
import dev.sterner.registry.VoidBoundEntityTypeRegistry
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level


class CrimsonHeavyKnightEntity(level: Level) :
    AbstractCultistEntity(VoidBoundEntityTypeRegistry.CRIMSON_HEAVY_KNIGHT_ENTITY.get(), level) {

    var shieldCoolDown: Int = 0

    override fun registerGoals() {
        super.registerGoals()
        goalSelector.addGoal(2, MeleeAttackGoal(this, 1.0, false))
        goalSelector.addGoal(3, RaiseShieldGoal(this))

        goalSelector.addGoal(2, object : MeleeAttackGoal(this, 1.0, false) {
            override fun checkAndPerformAttack(enemy: LivingEntity, distToEnemySqr: Double) {
                val d0: Double = this.getAttackReachSqr(enemy)
                if (distToEnemySqr <= d0 && this.ticksUntilNextAttack <= 0) {
                    this.resetAttackCooldown()
                    this.mob.stopUsingItem()
                    val cultist = mob as CrimsonHeavyKnightEntity
                    if (cultist.shieldCoolDown == 0) cultist.shieldCoolDown = 8
                    this.mob.swing(InteractionHand.MAIN_HAND)
                    this.mob.doHurtTarget(enemy)
                }
            }
        })
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        compound.putInt("ShieldCooldown", this.shieldCoolDown);
        super.addAdditionalSaveData(compound)
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        this.shieldCoolDown = compound.getInt("ShieldCoolDown")
        super.readAdditionalSaveData(compound)
    }

    override fun baseTick() {
        if (this.shieldCoolDown > 0) {
            --this.shieldCoolDown
        }

        super.baseTick()
    }

    override fun blockUsingShield(attacker: LivingEntity) {
        super.blockUsingShield(attacker)
        if (attacker.mainHandItem.getItem() is AxeItem) this.disableShield(true)
    }


    fun disableShield(increase: Boolean) {
        var chance = 0.25f + EnchantmentHelper.getBlockEfficiency(this).toFloat() * 0.05f
        if (increase) chance += 0.75.toFloat()
        if (random.nextFloat() < chance) {
            this.shieldCoolDown = 100
            this.stopUsingItem()
            level().broadcastEntityEvent(this, 30.toByte())
        }
    }


    companion object {
        fun createCrimsonAttributes(): AttributeSupplier.Builder? {
            return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.ARMOR)
                .add(Attributes.ARMOR_TOUGHNESS)
                .add(Attributes.FOLLOW_RANGE, 16.0)
                .add(Attributes.ATTACK_KNOCKBACK)
        }
    }
}