package dev.sterner.api

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.sammy.malum.client.SpiritBasedParticleBuilder
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import team.lodestar.lodestone.handlers.RenderHandler
import team.lodestar.lodestone.helpers.RandomHelper
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry
import team.lodestar.lodestone.systems.easing.Easing
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder
import team.lodestar.lodestone.systems.particle.data.GenericParticleData
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType
import team.lodestar.lodestone.systems.particle.world.options.LodestoneTerrainParticleOptions
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin

abstract class RiftType {

    open fun tick(level: Level, blockPos: BlockPos){

    }

    class NormalRiftType : RiftType() {

        val firstColor = Color(100, 100, 255, 255)
        val secondColor = Color(100, 100, 255, 255)

        override fun tick(level: Level, blockPos: BlockPos) {
            if (level.gameTime % 2L == 0L) {
                WorldParticleBuilder.create(LodestoneParticleRegistry.TWINKLE_PARTICLE)
                    .setRenderTarget(RenderHandler.LATE_DELAYED_RENDER)
                    .setScaleData(GenericParticleData.create(0.2f, 0f).build())
                    .setTransparencyData(GenericParticleData.create(0.2f, 0.8f).build())
                    .setColorData(
                        ColorParticleData.create(firstColor, secondColor).setEasing(Easing.SINE_IN).setCoefficient(0.5f)
                            .build()
                    )
                    .setSpinData(SpinParticleData.create(0f, 0.4f).setEasing(Easing.QUARTIC_IN).build())
                    .setLifetime(20)
                    .enableNoClip()
                    .spawn(level, blockPos.x + 0.5, blockPos.y + 0.5, blockPos.z + 0.5)
            }
        }
    }

    class DestabilizedRiftType : RiftType() {

        override fun tick(level: Level, blockPos: BlockPos){
            genParticleOrbit(level, blockPos,8,  Blocks.GRASS_BLOCK.defaultBlockState(), 1)
            genParticleOrbit(level, blockPos,8,  Blocks.GRASS_BLOCK.defaultBlockState(), 2)
            genParticleOrbit(level, blockPos,8,  Blocks.GRASS_BLOCK.defaultBlockState(), 3)
            genParticleOrbit(level, blockPos,8,  Blocks.GRASS_BLOCK.defaultBlockState(), 4)
        }

        private fun genParticleOrbit(level: Level, blockPos: BlockPos, range: Int, state: BlockState, direction: Int) {
            val discRad = (range * (1 / 3f) + level.getRandom().nextGaussian() / 5f)
            val yRand = (level.getRandom().nextGaussian() - 0.5) / 4

            val builder = SpiritBasedParticleBuilder.createSpirit(
                LodestoneTerrainParticleOptions(
                    LodestoneParticleRegistry.TERRAIN_PARTICLE,
                    state,
                    blockPos
                )
            )
                .setRenderType(LodestoneWorldParticleRenderType.TERRAIN_SHEET)
                .setGravityStrength(0f)
                .setFrictionStrength(0.98f)
                .setScaleData(GenericParticleData.create(0.0625f).build())

            builder
                .setMotion(discRad / 6f, 0.0, discRad / 6f)
                .addTickActor {
                    val speed = 0.1f
                    val time: Float = it.age / 6f

                    val (newX, newZ) = when (direction) {
                        1 -> Pair(cos(time) * discRad, sin(time) * discRad)
                        2 -> Pair(cos(time) * discRad, -sin(time) * discRad)
                        3 -> Pair(-cos(time) * discRad, sin(time) * discRad)
                        4 -> Pair(-cos(time) * discRad, -sin(time) * discRad)
                        else -> Pair(0f, 0f)
                    }

                    it.setParticleSpeed(
                        newX.toDouble() * speed,
                        it.particleSpeed.y,
                        newZ.toDouble() * speed
                    )
                }
                .setLifetime(RandomHelper.randomBetween(level.random, 40, 80))
                .spawn(level, blockPos.x + 0.5, blockPos.y + 0.5 + yRand, blockPos.z + 0.5 + if (direction % 2 == 0) discRad / 2 else -discRad / 2)
        }
    }

    class EldritchRiftType : RiftType() {}
}