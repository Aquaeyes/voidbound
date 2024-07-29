package dev.sterner.common.entity

import dev.sterner.api.GolemCore
import dev.sterner.common.entity.ai.SetWalkTargetToItem
import dev.sterner.common.entity.ai.GolemGatherSensor
import dev.sterner.common.entity.ai.GolemHarvestSensor
import dev.sterner.common.entity.ai.GolemSpecificSensor
import dev.sterner.registry.VoidBoundEntityTypeRegistry
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.core.BlockPos
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.Brain
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.tslat.smartbrainlib.api.SmartBrainOwner
import net.tslat.smartbrainlib.api.core.BrainActivityGroup
import net.tslat.smartbrainlib.api.core.SmartBrainProvider
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor


class SoulSteelGolemEntity(level: Level) : PathfinderMob(VoidBoundEntityTypeRegistry.SOUL_STEEL_GOLEM_ENTITY.get(), level), SmartBrainOwner<SoulSteelGolemEntity> {

    private var attackAnimationTick = 0
    private var coreEntityData = SynchedEntityData.defineId(SoulSteelGolemEntity::class.java, EntityDataSerializers.STRING)

    init {
        this.setMaxUpStep(1.0f)
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        entityData.define(coreEntityData, GolemCore.NONE.name)
    }

    fun setGolemCore(core: GolemCore){
        entityData.set(coreEntityData, core.name)
    }

    fun getGolemCore() : GolemCore {
        return GolemCore.valueOf(entityData.get(coreEntityData))
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
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 0.75f, 1.25f)
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
            return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75)
                .add(Attributes.ARMOR)
                .add(Attributes.ARMOR_TOUGHNESS)
                .add(Attributes.FOLLOW_RANGE, 16.0)
                .add(Attributes.ATTACK_KNOCKBACK)
        }
    }

    //BRAINNNNZZZ

    override fun customServerAiStep() {
        tickBrain(this)
    }

    override fun brainProvider(): Brain.Provider<*> {
        return SmartBrainProvider(this) as Brain.Provider<*>
    }

    override fun getSensors(): MutableList<out ExtendedSensor<out SoulSteelGolemEntity>> {
        return ObjectArrayList.of(
            GolemGatherSensor(),
            GolemHarvestSensor(),
            GolemSpecificSensor(),
            NearbyPlayersSensor(),
            NearbyLivingEntitySensor(),
            HurtBySensor(),
        )
    }

    override fun getCoreTasks(): BrainActivityGroup<out SoulSteelGolemEntity> {
        return BrainActivityGroup.coreTasks(
            LookAtTargetSink(40, 300),
            SetWalkTargetToItem(),
            MoveToWalkTarget<SoulSteelGolemEntity>(),
        )
    }

    override fun getIdleTasks(): BrainActivityGroup<out SoulSteelGolemEntity> {
        return BrainActivityGroup.idleTasks(

            FirstApplicableBehaviour(
                SetRandomLookTarget(),
                SetPlayerLookTarget<SoulSteelGolemEntity?>().predicate { it.distanceToSqr(this.position()) < 6 }
            ),
            OneRandomBehaviour(
                SetRandomLookTarget(),
                Idle<SoulSteelGolemEntity>().runFor { it.random.nextInt(30, 60) }
            )
        )
    }

    override fun getFightTasks(): BrainActivityGroup<out SoulSteelGolemEntity> {
        return BrainActivityGroup.fightTasks(

        )
    }
}