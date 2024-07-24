package dev.sterner.blockentity

import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import dev.sterner.registry.VoidBoundParticleTypeRegistry
import net.minecraft.core.BlockPos
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
import java.awt.Color

class DestabilizedSpiritRiftBlockEntity(pos: BlockPos, state: BlockState?) : SyncedBlockEntity(VoidBoundBlockEntityTypeRegistry.DESTABILIZED_SPIRIT_RIFT.get(), pos, state) {

    val firstColor = Color(100, 100, 255, 255)
    val secondColor = Color(100, 100, 255, 255)

    val firstColor2 = Color(180, 180, 255, 255)
    val secondColor2 = Color(70, 70, 255, 255)

    fun tick(){
        if (level != null && level!!.isClientSide) {
            if (level!!.gameTime % 16L == 0L) {

                WorldParticleBuilder.create(VoidBoundParticleTypeRegistry.RIFT_PARTICLE.get())
                    .setRenderTarget(RenderHandler.LATE_DELAYED_RENDER)
                    .setColorData(ColorParticleData.create(firstColor, secondColor).setEasing(Easing.SINE_IN).setCoefficient(0.5f).build())
                    .setDiscardFunction(SimpleParticleOptions.ParticleDiscardFunctionType.ENDING_CURVE_INVISIBLE)
                    .setScaleData(GenericParticleData.create(0.2f, 0.2f, 0.2f).build())
                    .setTransparencyData(GenericParticleData.create(0.0f, 1.0f, 0.0f).setEasing(Easing.CIRC_IN_OUT).build())
                    .setSpinData(SpinParticleData.create(0.0f, 0.05f).setEasing(Easing.QUARTIC_IN).build())
                    .spawn(level, blockPos.x + 0.5, blockPos.y + 0.5, blockPos.z + 0.5)
            }

            if (level!!.gameTime % 16L == 8L) {

                WorldParticleBuilder.create(VoidBoundParticleTypeRegistry.RIFT_PARTICLE.get())
                    .setRenderTarget(RenderHandler.LATE_DELAYED_RENDER)
                    .setColorData(ColorParticleData.create(firstColor, secondColor).setEasing(Easing.SINE_IN).setCoefficient(0.5f).build())
                    .setDiscardFunction(SimpleParticleOptions.ParticleDiscardFunctionType.ENDING_CURVE_INVISIBLE)
                    .setScaleData(GenericParticleData.create(0.25f, 0.25f, 0.45f).build())
                    .setTransparencyData(GenericParticleData.create(0.0f, 1.0f, 0.0f).setEasing(Easing.CIRC_IN_OUT).build())
                    .setSpinData(SpinParticleData.create(0.0f, -0.05f).setEasing(Easing.QUARTIC_IN).build())
                    .spawn(level, blockPos.x + 0.5, blockPos.y + 0.5, blockPos.z + 0.5)
            }


            if (level!!.gameTime % 8L == 0L) {
                val x = (worldPosition.x.toFloat() + 0.5f).toDouble()
                val y = (worldPosition.y.toFloat() + 0.5f).toDouble()
                val z = (worldPosition.z.toFloat() + 0.5f).toDouble()

                val lifeTime = 20 * 2
                WorldParticleBuilder.create(LodestoneParticleRegistry.STAR_PARTICLE)
                    .setRenderTarget(RenderHandler.LATE_DELAYED_RENDER)
                    .setScaleData(GenericParticleData.create(0.5f, 0.0f).build())
                    .setTransparencyData(GenericParticleData.create(0.2f, 0.0f).build())
                    .setColorData(ColorParticleData.create(firstColor2, secondColor2).setEasing(Easing.SINE_IN).setCoefficient(0.5f).build())
                    .setSpinData(SpinParticleData.create(0.0f, 0.05f).setEasing(Easing.QUARTIC_IN).build())
                    .setLifetime(lifeTime)
                    .enableNoClip()
                    .spawn(this.level, x, y, z)
            }
        }
    }
}