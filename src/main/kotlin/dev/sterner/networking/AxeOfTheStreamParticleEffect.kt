package dev.sterner.networking

import com.sammy.malum.registry.common.SpiritTypeRegistry
import com.sammy.malum.visual_effects.SpiritLightSpecs
import dev.sterner.api.util.VoidBoundPosUtils
import dev.sterner.registry.VoidBoundParticleTypeRegistry
import me.pepperbell.simplenetworking.SimpleChannel
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.level.block.state.BlockState
import org.joml.Vector3f
import team.lodestar.lodestone.handlers.RenderHandler
import team.lodestar.lodestone.helpers.RandomHelper
import team.lodestar.lodestone.systems.easing.Easing
import team.lodestar.lodestone.systems.network.LodestoneClientPacket
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder
import team.lodestar.lodestone.systems.particle.data.GenericParticleData
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType

class AxeOfTheStreamParticleEffect(private val pos: BlockPos, private val state: BlockState) : LodestoneClientPacket() {

    constructor(buf: FriendlyByteBuf) : this(buf.readBlockPos(), buf.readBlockHitResult().)

    override fun executeClient(
        client: Minecraft,
        listener: ClientPacketListener?,
        responseSender: PacketSender?,
        channel: SimpleChannel?
    ) {
        if (client.level != null) {
            val level = client.level!!
            val state = level.getBlockState(pos)
            val coordPos = VoidBoundPosUtils.getFaceCoords(level, state, pos,  client.player!!.direction.opposite)
            val lightSpecs: ParticleEffectSpawner = SpiritLightSpecs.spiritLightSpecs(level, coordPos, SpiritTypeRegistry.AQUEOUS_SPIRIT)
            lightSpecs.builder.multiplyLifetime(1.5f)
            lightSpecs.bloomBuilder.multiplyLifetime(1.5f)
            lightSpecs.spawnParticles()
            lightSpecs.spawnParticles()
        }
    }

    override fun encode(buf: FriendlyByteBuf) {
        buf.writeBlockPos(pos)
    }
}