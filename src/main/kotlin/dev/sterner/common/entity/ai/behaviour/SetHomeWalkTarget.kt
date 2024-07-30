package dev.sterner.common.entity.ai.behaviour

import com.mojang.datafixers.util.Pair
import dev.sterner.api.PosUtils
import dev.sterner.common.entity.SoulSteelGolemEntity
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.ai.memory.MemoryStatus
import net.minecraft.world.entity.ai.memory.WalkTarget
import net.minecraft.world.phys.Vec3
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour
import net.tslat.smartbrainlib.util.BrainUtils
import java.util.function.BiFunction
import java.util.function.BiPredicate

class SetHomeWalkTarget : ExtendedBehaviour<SoulSteelGolemEntity>() {
    private var speedModifier: BiFunction<SoulSteelGolemEntity, Vec3, Float> =
        BiFunction { entity: SoulSteelGolemEntity, targetPos: Vec3? -> 1f }
    private var positionPredicate: BiPredicate<SoulSteelGolemEntity, Vec3?> =
        BiPredicate { entity: SoulSteelGolemEntity, pos: Vec3? -> true }

    override fun getMemoryRequirements(): List<Pair<MemoryModuleType<*>, MemoryStatus>> {
        return MEMORY_REQUIREMENTS
    }

    fun speedModifier(modifier: Float): SetHomeWalkTarget {
        return speedModifier { entity: SoulSteelGolemEntity, targetPos: Vec3? -> modifier }
    }

    fun speedModifier(function: BiFunction<SoulSteelGolemEntity, Vec3, Float>): SetHomeWalkTarget {
        this.speedModifier = function

        return this
    }

    override fun start(entity: SoulSteelGolemEntity) {
        var targetPos = getTargetPos(entity)

        if (!positionPredicate.test(entity, targetPos)) targetPos = null

        if (targetPos == null) {
            BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET)
        } else {
            BrainUtils.setMemory(
                entity, MemoryModuleType.WALK_TARGET, WalkTarget(
                    targetPos,
                    speedModifier.apply(entity, targetPos), 0
                )
            )
        }
    }

    private fun getTargetPos(entity: SoulSteelGolemEntity): Vec3? {
        val pos = BrainUtils.getMemory(entity, MemoryModuleType.HOME)
        return PosUtils.getPos(if (pos != null) pos.pos() else entity.onPos, entity, 16, 8)
    }


    companion object {
        private val MEMORY_REQUIREMENTS: List<Pair<MemoryModuleType<*>, MemoryStatus>> =
            ObjectArrayList.of(
                Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                Pair.of(MemoryModuleType.HOME, MemoryStatus.VALUE_PRESENT)
            )
    }
}