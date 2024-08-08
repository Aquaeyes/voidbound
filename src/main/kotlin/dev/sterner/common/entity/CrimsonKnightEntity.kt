package dev.sterner.common.entity

import dev.sterner.registry.VoidBoundEntityTypeRegistry
import dev.sterner.registry.VoidBoundItemRegistry
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.RandomSource
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.SpawnGroupData
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor

class CrimsonKnightEntity(level: Level) : AbstractCultistEntity(VoidBoundEntityTypeRegistry.CRIMSON_KNIGHT_ENTITY.get(), level) {

    override fun registerGoals() {
        super.registerGoals()
        goalSelector.addGoal(2, MeleeAttackGoal(this, 1.0, false))
    }

    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance?,
        reason: MobSpawnType?,
        spawnData: SpawnGroupData?,
        dataTag: CompoundTag?
    ): SpawnGroupData? {
        val spawnGroupData = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag)
        (getNavigation() as GroundPathNavigation).setCanOpenDoors(true)
        val randomSource = level.random
        this.populateDefaultEquipmentSlots(randomSource, difficulty!!)
        this.populateDefaultEquipmentEnchantments(randomSource, difficulty)
        return spawnGroupData
    }

    override fun populateDefaultEquipmentSlots(random: RandomSource, difficulty: DifficultyInstance) {
        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack(VoidBoundItemRegistry.CRIMSON_KNIGHT_SWORD.get()))
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