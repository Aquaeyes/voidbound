package dev.sterner.networking

import dev.sterner.api.rift.SimpleSpiritCharge
import dev.sterner.common.blockentity.OsmoticEnchanterBlockEntity
import dev.sterner.registry.VoidBoundParticleTypeRegistry
import me.pepperbell.simplenetworking.SimpleChannel
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.item.enchantment.Enchantment
import org.joml.Vector3f
import team.lodestar.lodestone.handlers.RenderHandler
import team.lodestar.lodestone.helpers.RandomHelper
import team.lodestar.lodestone.systems.easing.Easing
import team.lodestar.lodestone.systems.network.LodestoneClientNBTPacket
import team.lodestar.lodestone.systems.network.LodestoneClientPacket
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder
import team.lodestar.lodestone.systems.particle.data.GenericParticleData
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType

class UpdateSpiritAmountPacket(nbt: CompoundTag) : LodestoneClientNBTPacket(nbt) {

    constructor(buf: FriendlyByteBuf): this(buf.readNbt()!!)

    constructor(simpleSpiritCharge: SimpleSpiritCharge, asLong: Long) : this(createTag(simpleSpiritCharge, asLong))

    override fun executeClientNbt(
        client: Minecraft,
        listener: ClientPacketListener?,
        responseSender: PacketSender?,
        channel: SimpleChannel?,
        data: CompoundTag
    ) {
        if (client.level != null) {
            val pos = BlockPos.of(data.getLong("Pos"))
            var spiritCharge = SimpleSpiritCharge()
            spiritCharge = spiritCharge.deserializeNBT(data)

            if (client.level!!.getBlockEntity(pos) is OsmoticEnchanterBlockEntity) {
                val be = client.level!!.getBlockEntity(pos) as OsmoticEnchanterBlockEntity
                be.spiritsToConsume = spiritCharge
            }
        }
    }

    companion object {
        private fun createTag(simpleSpiritCharge: SimpleSpiritCharge, asLong: Long): CompoundTag {
            val tag = CompoundTag()
            tag.putLong("Pos", asLong)
            simpleSpiritCharge.serializeNBT(tag)
            return tag
        }
    }
}