package dev.sterner.common.entity.ai.goal

import dev.sterner.common.entity.AbstractCultistEntity
import dev.sterner.common.entity.CrimsonClericEntity
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.item.ItemStack


class HealAllyGoal(val cultist : CrimsonClericEntity) : Goal() {

    var targetHeal: AbstractCultistEntity? = null

    override fun canUse(): Boolean {

        val list: List<AbstractCultistEntity> = cultist.level().getEntitiesOfClass(
            AbstractCultistEntity::class.java, cultist.getBoundingBox().inflate(10.0)
        )

        if (!list.isEmpty()) {
            for (ally in list) {
                if (!ally.isInvisible && ally.isAlive) {
                    if (ally.health <= ally.maxHealth) {
                        targetHeal = ally
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun stop() {
        cultist.setIsHealing(false)
        super.stop()
    }

    override fun start() {
        this.healAlly()
    }

    override fun tick() {
        if (cultist.health < cultist.maxHealth) {
            this.healAlly()
        }
    }

    fun healAlly() {
        if (targetHeal != null) {
            cultist.getNavigation().moveTo(targetHeal!!, 0.5)
            if (cultist.distanceTo(targetHeal!!) <= 2.0) {
                targetHeal?.heal(15.0f)
            }
        }
    }
}