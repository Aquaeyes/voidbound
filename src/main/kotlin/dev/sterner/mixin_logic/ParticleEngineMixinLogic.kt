package dev.sterner.mixin_logic

import com.sammy.malum.common.entity.nitrate.AbstractNitrateEntity
import com.sammy.malum.visual_effects.SpiritLightSpecs
import dev.sterner.api.VoidBoundApi.canPlayerBreakBlock
import dev.sterner.mixin.client.ParticleEngineMixin
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.spongepowered.asm.mixin.Unique
import team.lodestar.lodestone.helpers.RandomHelper
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry
import team.lodestar.lodestone.systems.easing.Easing
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder
import team.lodestar.lodestone.systems.particle.data.GenericParticleData
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType
import team.lodestar.lodestone.systems.particle.world.behaviors.components.DirectionalBehaviorComponent
import java.awt.Color
import kotlin.math.min

object ParticleEngineMixinLogic {

    private val AURIC_YELLOW: Color = Color(239, 175, 95)
    private val AURIC_WHITE: Color = Color(236, 200, 190)
    private val AURIC_COLOR_DATA: ColorParticleData =
        ColorParticleData.create(AURIC_YELLOW, AURIC_WHITE).setEasing(Easing.SINE_IN_OUT).setCoefficient(0.9f).build()

    fun logic(level: Level, pos: BlockPos, blockState: BlockState, random: RandomSource, side: Direction) : Boolean {
        if (Minecraft.getInstance().player != null && !canPlayerBreakBlock(
                level,
                Minecraft.getInstance().player!!, pos
            )
        ) {
            val i: Int = pos.x
            val j: Int = pos.y
            val k: Int = pos.z
            val f = 0.03f
            val aABB: AABB = blockState.getShape(level, pos).bounds()
            var d: Double = i.toDouble() + random.nextDouble() * (aABB.maxX - aABB.minX - 0.2f) + 0.1f + aABB.minX
            var e: Double = j.toDouble() + random.nextDouble() * (aABB.maxY - aABB.minY - 0.2f) + 0.1f + aABB.minY
            var g: Double = k.toDouble() + random.nextDouble() * (aABB.maxZ - aABB.minZ - 0.2f) + 0.1f + aABB.minZ
            if (side == Direction.DOWN) {
                e = j.toDouble() + aABB.minY - f
            }

            if (side == Direction.UP) {
                e = j.toDouble() + aABB.maxY + f
            }

            if (side == Direction.NORTH) {
                g = k.toDouble() + aABB.minZ - f
            }

            if (side == Direction.SOUTH) {
                g = k.toDouble() + aABB.maxZ + f
            }

            if (side == Direction.WEST) {
                d = i.toDouble() + aABB.minX - f
            }

            if (side == Direction.EAST) {
                d = i.toDouble() + aABB.maxX + f
            }

            val lightSpecs =
                SpiritLightSpecs.spiritLightSpecs(level, Vec3(d, e, g), AURIC_COLOR_DATA)
            lightSpecs.builder.multiplyLifetime(1.5f)
            lightSpecs.bloomBuilder.multiplyLifetime(1.5f)
            lightSpecs.spawnParticles()

            for (ix in 0..2) {
                val lifetime = (RandomHelper.randomBetween(random, 60, 80) * (1 - ix / 3f)).toInt()
                val spinData =
                    SpinParticleData.createRandomDirection(random, 0f, RandomHelper.randomBetween(random, 0f, 0.4f), 0f)
                        .randomSpinOffset(random).build()

                val scale =
                    GenericParticleData.create(0.05f, 0.1f, 0.15f).setEasing(Easing.QUINTIC_OUT, Easing.SINE_IN)
                        .build()
                val tans =
                    GenericParticleData.create(0.5f, 0.6f, 0f).setEasing(Easing.SINE_IN_OUT, Easing.SINE_IN).build()

                spawnWardParticles(level, d, e, g, side, tans, scale, spinData, lifetime)
            }

            val lifetime = (RandomHelper.randomBetween(random, 60, 80))
            val spinData =
                SpinParticleData.createRandomDirection(random, 0f, RandomHelper.randomBetween(random, 0f, 0.4f), 0f)
                    .randomSpinOffset(random).build()
            val scaleData =
                GenericParticleData.create(0.02f, 0.03f, 0.04f).setEasing(Easing.QUINTIC_OUT, Easing.SINE_IN).build()
            val trans = GenericParticleData.create(0.7f, 0.9f, 0f).setEasing(Easing.SINE_IN_OUT, Easing.SINE_IN).build()
            spawnWardParticles(level, d, e, g, side, trans, scaleData, spinData, lifetime)
            return false
        }

        return true
    }

    private fun spawnWardParticles(
        level: Level,
        x: Double,
        y: Double,
        z: Double,
        direction: Direction,
        transparencyData: GenericParticleData,
        scaleData: GenericParticleData,
        spinData: SpinParticleData,
        lifetime: Int
    ) {
        val v = direction.normal
        WorldParticleBuilder.create(
            LodestoneParticleRegistry.WISP_PARTICLE,
            DirectionalBehaviorComponent(Vec3(v.x.toDouble(), v.y.toDouble(), v.z.toDouble()))
        )
            .setTransparencyData(transparencyData)
            .setSpinData(spinData)
            .setScaleData(scaleData)
            .setColorData(
                ColorParticleData.create(
                    AURIC_YELLOW,
                    AbstractNitrateEntity.SECOND_SMOKE_COLOR
                ).setEasing(Easing.QUINTIC_OUT).build()
            )
            .setLifetime(min((6 + 3), lifetime))
            .setLifeDelay(1)
            .enableNoClip()
            .enableForcedSpawn()
            .setSpritePicker(SimpleParticleOptions.ParticleSpritePicker.WITH_AGE)
            .setRenderType(LodestoneWorldParticleRenderType.ADDITIVE)
            .spawn(level, x, y, z)
    }
}