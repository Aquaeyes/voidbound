package dev.sterner.common.blockentity

import com.sammy.malum.client.SpiritBasedParticleBuilder
import dev.sterner.api.blockentity.SyncedBlockEntity
import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import dev.sterner.registry.VoidBoundParticleTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Mth
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import team.lodestar.lodestone.handlers.RenderHandler
import team.lodestar.lodestone.helpers.RandomHelper
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry
import team.lodestar.lodestone.systems.easing.Easing
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder
import team.lodestar.lodestone.systems.particle.data.GenericParticleData
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle
import team.lodestar.lodestone.systems.particle.world.options.LodestoneTerrainParticleOptions
import java.awt.Color
import java.util.function.Consumer
import kotlin.math.cos
import kotlin.math.sin

class DestabilizedSpiritRiftBlockEntity(pos: BlockPos, state: BlockState?) :
    SyncedBlockEntity(VoidBoundBlockEntityTypeRegistry.DESTABILIZED_SPIRIT_RIFT.get(), pos, state) {

    val firstColor = Color(100, 100, 255, 255)
    val secondColor = Color(100, 100, 255, 255)


    val x = (worldPosition.x.toFloat() + 0.5f).toDouble()
    val y = (worldPosition.y.toFloat() + 0.5f).toDouble()
    val z = (worldPosition.z.toFloat() + 0.5f).toDouble()

    private var destabilized = false
        set(value) {
            field = value
            transformationTicks = 20 * 6
        }
    private var transformationTicks = 0

    override fun saveAdditional(tag: CompoundTag) {
        tag.putBoolean("Destabilized", destabilized)
        tag.putInt("TransformationTicks", transformationTicks)
        super.saveAdditional(tag)
    }

    override fun load(tag: CompoundTag) {
        destabilized = tag.getBoolean("Destabilized")
        transformationTicks = tag.getInt("TransformationTicks")
        super.load(tag)
    }

    fun tick() {

        if (level != null && level!!.isClientSide) {
            if (transformationTicks <= 0) {
                if (destabilized) {
                    tickDestabilized()
                } else {
                    tickNormal()
                }
            }

            if (transformationTicks > 0) {
                transformationTicks--
                tickTransformation(transformationTicks)
            }
        }
    }

    /**
     * Run particle effects during transformation between normal and destabilized
     */
    private fun tickTransformation(transformationTicks: Int) {

    }

    private fun tickDestabilized() {

    }

    private fun tickNormal() {
        if (level!!.gameTime % 16L == 0L) {

            WorldParticleBuilder.create(VoidBoundParticleTypeRegistry.RIFT_PARTICLE.get())
                .setRenderTarget(RenderHandler.LATE_DELAYED_RENDER)
                .setColorData(
                    ColorParticleData.create(firstColor, secondColor).setEasing(Easing.SINE_IN).setCoefficient(0.5f)
                        .build()
                )
                .setDiscardFunction(SimpleParticleOptions.ParticleDiscardFunctionType.ENDING_CURVE_INVISIBLE)
                .setScaleData(GenericParticleData.create(0.2f, 0.2f, 0.2f).build())
                .setTransparencyData(
                    GenericParticleData.create(0.0f, 1.0f, 0.0f).setEasing(Easing.CIRC_IN_OUT).build()
                )
                .setSpinData(SpinParticleData.create(0.0f, 0.05f).setEasing(Easing.QUARTIC_IN).build())
                .spawn(level, x, y, z)
        }

        if (level!!.gameTime % 16L == 8L) {

            WorldParticleBuilder.create(VoidBoundParticleTypeRegistry.RIFT_PARTICLE.get())
                .setRenderTarget(RenderHandler.LATE_DELAYED_RENDER)
                .setColorData(
                    ColorParticleData.create(firstColor, secondColor).setEasing(Easing.SINE_IN).setCoefficient(0.5f)
                        .build()
                )
                .setDiscardFunction(SimpleParticleOptions.ParticleDiscardFunctionType.ENDING_CURVE_INVISIBLE)
                .setScaleData(GenericParticleData.create(0.25f, 0.25f, 0.45f).build())
                .setTransparencyData(
                    GenericParticleData.create(0.0f, 1.0f, 0.0f).setEasing(Easing.CIRC_IN_OUT).build()
                )
                .setSpinData(SpinParticleData.create(0.0f, -0.05f).setEasing(Easing.QUARTIC_IN).build())
                .spawn(level, x, y, z)
        }


        if (level!!.gameTime % 2L == 0L) {

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
                .spawn(level, x, y, z)
        }
        // check all blocks in range
        val range = 8

        val discRad = (range * (1 / 3f) + level!!.getRandom().nextGaussian() / 5f)
        val builder = SpiritBasedParticleBuilder.createSpirit(
            LodestoneTerrainParticleOptions(
                LodestoneParticleRegistry.TERRAIN_PARTICLE,
                Blocks.GRASS_BLOCK.defaultBlockState(),
                blockPos
            )
        )
            .setRenderType(LodestoneWorldParticleRenderType.TERRAIN_SHEET)
            .setGravityStrength(0f)
            .setFrictionStrength(0.98f)
            .addTickActor {
                val speed = it.particleSpeed
                val time: Float = it.age / 6f

                it.setParticleSpeed(
                    cos(time) * speed.x,
                    speed.y,
                    sin(time) *speed.z
                )
            }
            .setScaleData(GenericParticleData.create(0.0625f).build())


        builder
            .setMotion(discRad / 6f, 0.0, discRad / 6f)
            .addTickActor {
                val speed = 0.1f // Adjust speed as necessary
                val time: Float = it.age / 6f

                // Update the particle's position to create an orbiting effect
                val newX = cos(time) * discRad
                val newZ = sin(time) * discRad

                it.setParticleSpeed(
                    newX * speed,
                    it.particleSpeed.y,
                    newZ * speed
                )
            }
            .setLifetime(RandomHelper.randomBetween(level!!.random, 40, 80))
            .spawn(level, blockPos.x + 0.5, blockPos.y + 0.5, blockPos.z + 0.5 - (discRad / 2))
    }
}