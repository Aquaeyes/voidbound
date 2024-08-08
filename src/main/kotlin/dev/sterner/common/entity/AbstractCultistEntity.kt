package dev.sterner.common.entity

import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

abstract class AbstractCultistEntity(entityType: EntityType<out Monster>, level: Level) : Monster(entityType, level) {

    abstract val armPose: Any

    override fun createNavigation(level: Level): PathNavigation {
        return super.createNavigation(level)
    }

    override fun getAmbientSound(): SoundEvent {
        return SoundEvents.VINDICATOR_AMBIENT
    }

    override fun getDeathSound(): SoundEvent {
        return SoundEvents.VINDICATOR_DEATH
    }

    override fun getHurtSound(damageSource: DamageSource?): SoundEvent {
        return SoundEvents.VINDICATOR_HURT
    }

    override fun registerGoals() {
        super.registerGoals()
        goalSelector.addGoal(0, FloatGoal(this))
        goalSelector.addGoal(2, OpenDoorGoal(this, false))

        goalSelector.addGoal(7, WaterAvoidingRandomStrollGoal(this, 1.0))
        goalSelector.addGoal(8, LookAtPlayerGoal(this, Player::class.java, 8.0f))
        goalSelector.addGoal(8, RandomLookAroundGoal(this))

        targetSelector.addGoal(
            1, HurtByTargetGoal(
                this,
                AbstractCultistEntity::class.java
            ).setAlertOthers(*arrayOfNulls(0))
        )
        targetSelector.addGoal(
            2, NearestAttackableTargetGoal(
                this as Mob,
                Player::class.java, true
            )
        )
    }

    enum class CrimsonArmPose {
        ATTACKING,
        SPELLCASTING,
        BOW_AND_ARROW,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        NEUTRAL
    }
}