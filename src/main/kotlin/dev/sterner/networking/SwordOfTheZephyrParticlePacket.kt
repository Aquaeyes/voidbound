package dev.sterner.networking

import com.sammy.malum.client.MalumModelLoaderPlugin
import com.sammy.malum.client.SpiritBasedParticleBuilder
import com.sammy.malum.common.entity.nitrate.AbstractNitrateEntity
import com.sammy.malum.common.item.curiosities.nitrate.AbstractNitrateItem
import com.sammy.malum.common.item.curiosities.nitrate.EthericNitrateItem
import com.sammy.malum.registry.common.SpiritTypeRegistry
import com.sammy.malum.visual_effects.SpiritLightSpecs
import dev.sterner.api.util.VoidBoundPosUtils
import dev.sterner.registry.VoidBoundPacketRegistry
import dev.sterner.registry.VoidBoundParticleTypeRegistry
import me.pepperbell.simplenetworking.SimpleChannel
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.util.Mth
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import org.joml.Vector3d
import team.lodestar.lodestone.handlers.RenderHandler
import team.lodestar.lodestone.helpers.RandomHelper
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry
import team.lodestar.lodestone.systems.easing.Easing
import team.lodestar.lodestone.systems.network.LodestoneClientNBTPacket
import team.lodestar.lodestone.systems.network.LodestoneClientPacket
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder
import team.lodestar.lodestone.systems.particle.data.GenericParticleData
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType
import team.lodestar.lodestone.systems.particle.world.options.LodestoneTerrainParticleOptions
import team.lodestar.lodestone.systems.rendering.VFXBuilders
import java.util.UUID
import kotlin.math.cos
import kotlin.math.sin

class SwordOfTheZephyrParticlePacket(val uuid: UUID) : LodestoneClientPacket() {

    constructor(buf: FriendlyByteBuf) : this(buf.readUUID())

    override fun encode(buf: FriendlyByteBuf) {
        buf.writeUUID(uuid)
    }

    override fun executeClient(
        client: Minecraft,
        listener: ClientPacketListener?,
        responseSender: PacketSender?,
        channel: SimpleChannel?
    ) {
        if (client.level != null) {
            val level = client.level!!
            val player = level.getPlayerByUUID(uuid)
            for (i in 1..4) {
                genParticleOrbit(level, player!!.position(), 4, Blocks.GRASS_BLOCK.defaultBlockState(), i)
            }
        }
    }

    private fun genParticleOrbit(level: Level, pos: Vec3, range: Int, state: BlockState, direction: Int) {
        val clampedDir = Mth.clamp(direction, 1, 4)
        val discRad = (range * (1 / 3f) + level.getRandom().nextGaussian() / 5f)
        val yRand = (level.getRandom().nextGaussian() - 0.5) / 4


        val builder = WorldParticleBuilder.create(VoidBoundParticleTypeRegistry.SMOKE_PARTICLE)

        builder
            .setRenderTarget(RenderHandler.LATE_DELAYED_RENDER)
            .setTransparencyData(GenericParticleData.create(0f, 0.2f, 0f).setEasing(Easing.SINE_IN, Easing.QUAD_IN).setCoefficient(3.5f).build())
            .setRenderType(LodestoneWorldParticleRenderType.TRANSPARENT)
            .setGravityStrength(0f)
            .setFrictionStrength(0.98f)
            .setScaleData(GenericParticleData.create(0.425f).build())
            .setMotion(discRad, 0.01, discRad)
            .setDiscardFunction(SimpleParticleOptions.ParticleDiscardFunctionType.ENDING_CURVE_INVISIBLE)
            .addTickActor {
                val baseSpeed = 0.3f
                val speedFactor = 3f   // Speed factor that increases the speed of motion without affecting radius
                val time: Float = it.age / 6f * speedFactor   // Multiply time progression by speedFactor

                // Calculate new positions based on time, but keep the radius fixed
                val (newX, newZ) = when (clampedDir) {
                    1 -> Pair(cos(time) * discRad, sin(time) * discRad)
                    2 -> Pair(cos(time) * discRad, -sin(time) * discRad)
                    3 -> Pair(-cos(time) * discRad, sin(time) * discRad)
                    4 -> Pair(-cos(time) * discRad, -sin(time) * discRad)
                    else -> Pair(0f, 0f)
                }

                it.setParticleSpeed(
                    newX.toDouble() * baseSpeed,   // Multiply by base speed for motion scaling
                    it.particleSpeed.y,
                    newZ.toDouble() * baseSpeed
                )
            }
            .setLifetime(RandomHelper.randomBetween(level.random, 40, 80))
            .spawn(
                level,
                pos.x,
                pos.y + yRand,
                pos.z + if (direction % 2 == 0) discRad / 2 else -discRad / 2
            )
    }
}