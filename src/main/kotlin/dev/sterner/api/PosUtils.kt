package dev.sterner.api

import dev.sterner.common.entity.SoulSteelGolemEntity
import net.minecraft.world.entity.ai.util.LandRandomPos
import net.minecraft.world.phys.Vec3

object PosUtils {

    fun getRandomNearbyPos(golem: SoulSteelGolemEntity): Vec3 {
        val vec3 = LandRandomPos.getPos(golem, 4, 2)
        return vec3 ?: golem.position()
    }

}