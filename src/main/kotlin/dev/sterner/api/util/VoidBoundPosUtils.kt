package dev.sterner.api.util

import dev.sterner.common.entity.SoulSteelGolemEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.util.GoalUtils
import net.minecraft.world.entity.ai.util.LandRandomPos
import net.minecraft.world.entity.ai.util.RandomPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
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

    fun getFaceCoords(level: Level, blockState: BlockState, pos: BlockPos, side: Direction): Vec3 {
        var random = level.random
        val i: Int = pos.x
        val j: Int = pos.y
        val k: Int = pos.z
        val f = 0.03f
        val aABB: AABB = blockState.getShape(level, pos).bounds()
        var x: Double = i.toDouble() + random.nextDouble() * (aABB.maxX - aABB.minX - 0.2f) + 0.1f + aABB.minX
        var y: Double = j.toDouble() + random.nextDouble() * (aABB.maxY - aABB.minY - 0.2f) + 0.1f + aABB.minY
        var z: Double = k.toDouble() + random.nextDouble() * (aABB.maxZ - aABB.minZ - 0.2f) + 0.1f + aABB.minZ
        if (side == Direction.DOWN) {
            y = j.toDouble() + aABB.minY - f
        }

        if (side == Direction.UP) {
            y = j.toDouble() + aABB.maxY + f
        }

        if (side == Direction.NORTH) {
            z = k.toDouble() + aABB.minZ - f
        }

        if (side == Direction.SOUTH) {
            z = k.toDouble() + aABB.maxZ + f
        }

        if (side == Direction.WEST) {
            x = i.toDouble() + aABB.minX - f
        }

        if (side == Direction.EAST) {
            x = i.toDouble() + aABB.maxX + f
        }

        return Vec3(x, y, z)
    }
}