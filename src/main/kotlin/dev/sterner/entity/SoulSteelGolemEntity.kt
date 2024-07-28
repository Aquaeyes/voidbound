package dev.sterner.entity

import dev.sterner.registry.VoidBoundEntityTypeRegistry
import io.github.fabricators_of_create.porting_lib.attributes.PortingLibAttributes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class SoulSteelGolemEntity(level: Level) : PathfinderMob(VoidBoundEntityTypeRegistry.SOUL_STEEL_GOLEM_ENTITY.get(), level) {

    private var attackAnimationTick = 0

    init {
        this.setMaxUpStep(1.0f)
    }

    override fun registerGoals() {
        goalSelector.addGoal(1, MeleeAttackGoal(this, 1.0, true))
        goalSelector.addGoal(2, MoveTowardsTargetGoal(this, 0.9, 32.0f))
        goalSelector.addGoal(2, MoveBackToVillageGoal(this, 0.6, false))
        goalSelector.addGoal(4, GolemRandomStrollInVillageGoal(this, 0.6))
        goalSelector.addGoal(
            7, LookAtPlayerGoal(
                this,
                Player::class.java, 6.0f
            )
        )
        goalSelector.addGoal(8, RandomLookAroundGoal(this))
    }

    override fun aiStep() {
        super.aiStep()
        if (this.attackAnimationTick > 0) {
            attackAnimationTick--
        }
    }

    override fun handleEntityEvent(id: Byte) {
        if (id.toInt() == 4) {
            this.attackAnimationTick = 10
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0f, 1.0f)
        } else {
            super.handleEntityEvent(id)
        }
    }

    fun getAttackAnimationTick(): Int {
        return this.attackAnimationTick
    }

    override fun getHurtSound(damageSource: DamageSource): SoundEvent {
        return SoundEvents.IRON_GOLEM_HURT
    }

    override fun getDeathSound(): SoundEvent {
        return SoundEvents.IRON_GOLEM_DEATH
    }

    override fun playStepSound(pos: BlockPos, state: BlockState) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 0.5f, 1.25f)
    }



    companion object {
        fun createGolemAttributes(): AttributeSupplier.Builder {
            return AttributeSupplier.builder()
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75)
                .add(Attributes.ARMOR)
                .add(Attributes.ARMOR_TOUGHNESS)
                .add(Attributes.FOLLOW_RANGE, 16.0)
                .add(Attributes.ATTACK_KNOCKBACK)
                .add(PortingLibAttributes.STEP_HEIGHT_ADDITION, 1.0)
        }
    }

}