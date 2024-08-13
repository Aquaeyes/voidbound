package dev.sterner.networking

import dev.sterner.common.blockentity.OsmoticEnchanterBlockEntity
import me.pepperbell.simplenetworking.SimpleChannel
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.world.item.enchantment.Enchantment
import team.lodestar.lodestone.systems.network.LodestoneServerNBTPacket

class EnchantmentLevelPacket(nbt: CompoundTag) : LodestoneServerNBTPacket(nbt) {

    override fun encode(buf: FriendlyByteBuf?) {
        super.encode(buf)
    }

    constructor(buf: FriendlyByteBuf): this(buf.readNbt()!!)

    override fun executeServerNbt(
        server: MinecraftServer?,
        player: ServerPlayer?,
        listener: ServerGamePacketListenerImpl?,
        responseSender: PacketSender?,
        channel: SimpleChannel?,
        data: CompoundTag
    ) {
        server?.execute {
            val enchantment: Enchantment = Enchantment.byId(data.getInt("Enchantment"))!!
            val level: Int = data.getInt("Level")
            val pos = BlockPos.of(data.getLong("Pos"))

            if (player?.level()?.getBlockEntity(pos) is OsmoticEnchanterBlockEntity) {
                val osmotic = player.level().getBlockEntity(pos) as OsmoticEnchanterBlockEntity
                osmotic.recieveScreenData(enchantment, level)
            }
        }
    }
}