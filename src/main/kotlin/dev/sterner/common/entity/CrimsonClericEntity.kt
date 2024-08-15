package dev.sterner.common.entity

import dev.sterner.common.entity.ai.goal.HealAllyGoal
import dev.sterner.registry.VoidBoundEntityTypeRegistry
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.level.Level

class CrimsonClericEntity(level: Level) :
    AbstractCultistEntity(VoidBoundEntityTypeRegistry.CRIMSON_CLERIC_ENTITY.get(), level) {

    private var healCooldown = 0

    override fun addAdditionalSaveData(compound: CompoundTag) {
        compound.putInt("HealCooldown", this.healCooldown)
        super.addAdditionalSaveData(compound)
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        this.healCooldown = compound.getInt("HealCooldown")
        super.readAdditionalSaveData(compound)
    }

    override fun baseTick() {
        if (this.healCooldown > 0) {
            --this.healCooldown
        }

        super.baseTick()
    }

    override fun registerGoals() {
        super.registerGoals()
        this.goalSelector.addGoal(2, HealAllyGoal(this, 1.0, 100, 0, 10.0F))
    }

    override fun getArmPose(): CrimsonArmPose {
        if (this.isHealing()) {
            return CrimsonArmPose.SPELLCASTING
        }

        return super.getArmPose()
    }


    override fun defineSynchedData() {
        super.defineSynchedData()
        entityData.define(IS_HEALING, false)
    }

    fun setIsHealing(b: Boolean) {
        entityData.set(IS_HEALING, b)
    }

    fun isHealing(): Boolean {
        return entityData.get(IS_HEALING)
    }

    companion object {

        val IS_HEALING: EntityDataAccessor<Boolean> = SynchedEntityData.defineId(
            AbstractCultistEntity::class.java, EntityDataSerializers.BOOLEAN
        )

        fun createCrimsonAttributes(): AttributeSupplier.Builder? {
            return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.ARMOR)
                .add(Attributes.ARMOR_TOUGHNESS)
                .add(Attributes.FOLLOW_RANGE, 16.0)
                .add(Attributes.ATTACK_KNOCKBACK)
        }
    }
}