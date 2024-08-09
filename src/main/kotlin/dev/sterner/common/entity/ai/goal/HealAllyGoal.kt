package dev.sterner.common.entity.ai.goal

import dev.sterner.common.entity.AbstractCultistEntity
import dev.sterner.common.entity.CrimsonClericEntity
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.entity.ai.goal.Goal


class HealAllyGoal(val cultist : CrimsonClericEntity) : Goal() {

    var targetHeal: AbstractCultistEntity? = null
    var healProgress = 0

    override fun canUse(): Boolean {

        if (cultist.healCooldown > 0) {
            return false
        }

        val list: List<AbstractCultistEntity> = cultist.level().getEntitiesOfClass(
            AbstractCultistEntity::class.java, cultist.getBoundingBox().inflate(10.0)
        )

        if (list.isNotEmpty()) {
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

    override fun canContinueToUse(): Boolean {
        if (targetHeal != null) {
            if (targetHeal!!.health == targetHeal!!.maxHealth) {
                return false
            }
        } else {
            return false
        }
        return super.canContinueToUse()
    }

    override fun stop() {
        cultist.setIsHealing(false)
        super.stop()
    }

    override fun start() {
        this.healAlly()
        cultist.setIsHealing(true)
    }

    override fun tick() {
        if (targetHeal != null && targetHeal!!.health < targetHeal!!.maxHealth) {
            healProgress++
            if (healProgress > 20 * 2) {
                this.healAlly()
                healProgress = 0
            }
        }
    }

    fun healAlly() {
        if (targetHeal != null) {
            cultist.getNavigation().moveTo(targetHeal!!, 0.5)
            if (cultist.distanceTo(targetHeal!!) <= 5.0) {
                targetHeal?.heal(15.0f)
                cultist.healCooldown = 20 * 5
            }
        }
    }
}