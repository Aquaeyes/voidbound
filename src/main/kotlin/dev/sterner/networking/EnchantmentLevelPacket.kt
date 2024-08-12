package dev.sterner.networking

import me.pepperbell.simplenetworking.SimpleChannel
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.world.item.enchantment.Enchantment
import team.lodestar.lodestone.network.interaction.UpdateLeftClickPacket
import team.lodestar.lodestone.systems.network.LodestoneServerNBTPacket

class EnchantmentLevelPacket(var nbt: CompoundTag) : LodestoneServerNBTPacket(nbt) {

    override fun encode(buf: FriendlyByteBuf?) {
        super.encode(buf)
    }

    fun decode(buf: FriendlyByteBuf): EnchantmentLevelPacket {
        return EnchantmentLevelPacket(buf.readNbt()!!)
    }

    override fun executeServer(
        server: MinecraftServer?,
        player: ServerPlayer?,
        listener: ServerGamePacketListenerImpl?,
        responseSender: PacketSender?,
        channel: SimpleChannel?
    ) {
        server?.execute {
            val enchantment: Enchantment = Enchantment.byId(nbt.getInt("Enchantment"))!!
            val level: Int = nbt.getInt("Level")
            val pos = BlockPos.of(nbt.getLong("Pos"))

            println("Hello ${enchantment}, ${level}, ${pos}")
        }

    }
}