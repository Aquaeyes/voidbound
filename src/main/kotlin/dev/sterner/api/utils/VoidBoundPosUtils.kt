package dev.sterner.api.utils

import dev.sterner.common.entity.SoulSteelGolemEntity
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.util.GoalUtils
import net.minecraft.world.entity.ai.util.LandRandomPos
import net.minecraft.world.entity.ai.util.RandomPos
import net.minecraft.world.phys.Vec3

object VoidBoundPosUtils {

    fun getRandomNearbyPos(golem: SoulSteelGolemEntity): Vec3 {
        val vec3 = LandRandomPos.getPos(golem, 4, 2)
        return vec3 ?: golem.position()
    }

    fun getPos(originPos: BlockPos, mob: PathfinderMob, radius: Int, verticalDistance: Int): Vec3? {
        val bl = GoalUtils.mobRestricted(mob, radius)
        return RandomPos.generateRandomPos(mob) {
            val blockPos = generateRandomDirection(originPos, mob.random, radius, verticalDistance)
            generateRandomPosTowardDirection(mob, radius, bl, blockPos)
        }
    }

    private fun generateRandomDirection(
        pos: BlockPos,
        random: RandomSource,
        horizontalDistance: Int,
        verticalDistance: Int
    ): BlockPos {
        val i = random.nextInt(2 * horizontalDistance + 1) - horizontalDistance
        val j = random.nextInt(2 * verticalDistance + 1) - verticalDistance
        val k = random.nextInt(2 * horizontalDistance + 1) - horizontalDistance
        return pos.offset(i, j, k)
    }

    private fun generateRandomPosTowardDirection(
        mob: PathfinderMob,
        radius: Int,
        shortCircuit: Boolean,
        pos: BlockPos
    ): BlockPos? {
        val blockPos = RandomPos.generateRandomPosTowardDirection(mob, radius, mob.random, pos)
        return if (!GoalUtils.isOutsideLimits(blockPos, mob)
            && !GoalUtils.isRestricted(shortCircuit, mob, blockPos)
            && !GoalUtils.isNotStable(mob.navigation, blockPos)
            && !GoalUtils.hasMalus(mob, blockPos)
        ) blockPos
        else null
    }
}