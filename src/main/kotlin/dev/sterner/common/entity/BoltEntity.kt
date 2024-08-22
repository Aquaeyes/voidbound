package dev.sterner.common.entity

import dev.sterner.registry.VoidBoundEntityTypeRegistry
import net.fabricmc.api.EnvType
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Entity.RemovalReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3


class BoltEntity(entityType: EntityType<BoltEntity>, world: Level?) : Entity(entityType, world) {
    var seed: Long
    var color: FloatArray = floatArrayOf(1f, 0f, 0f, 1f)
    private var ambientTick: Int

    constructor(caster: LivingEntity, length: Double, color: Int) : this(VoidBoundEntityTypeRegistry.BOLT_ENTITY.get(), caster.level()) {
        yRot = caster.getYHeadRot()
        xRot = caster.xRot
        setPos(caster.x, caster.eyeY, caster.z)
        this.length = length.toFloat()
        setColor(color)
    }

    init {
        this.noCulling = true
        this.ambientTick = 6
        this.seed = this.random.nextLong()
    }

    constructor(world: Level?, pos: Vec3, direction: Vec3, color: Int) : this(VoidBoundEntityTypeRegistry.BOLT_ENTITY.get(), world) {
        lookAt(EntityAnchorArgument.Anchor.FEET, direction)
        setPos(pos.x(), pos.y(), pos.z())
        length = direction.length().toFloat()
        setColor(color)
    }


    var length: Float
        get() = entityData.get(LENGTH)
        set(length) {
            entityData.set(LENGTH, length)
        }

    fun getColor(): Int {
        return entityData.get(COLOR)
    }

    fun setColor(color: Int) {
        entityData.set(COLOR, color)
    }

    override fun defineSynchedData() {
        entityData.define(LENGTH, 0f)
        entityData.define(COLOR, 0)
    }

    override fun tick() {
        ambientTick--
        if (ambientTick < 0) {
            remove(RemovalReason.DISCARDED)
        }
        super.tick()
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {

    }

    override fun addAdditionalSaveData(compound: CompoundTag) {

    }

    companion object {
        protected val LENGTH: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(BoltEntity::class.java, EntityDataSerializers.FLOAT)
        protected val COLOR: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(BoltEntity::class.java, EntityDataSerializers.INT)
    }
}