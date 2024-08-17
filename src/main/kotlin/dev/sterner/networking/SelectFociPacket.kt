package dev.sterner.networking

import dev.sterner.common.blockentity.OsmoticEnchanterBlockEntity
import dev.sterner.common.item.WandItem
import me.pepperbell.simplenetworking.SimpleChannel
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import team.lodestar.lodestone.helpers.NBTHelper
import team.lodestar.lodestone.systems.network.LodestoneServerNBTPacket
import team.lodestar.lodestone.systems.network.LodestoneServerPacket
import java.util.UUID

class SelectFociPacket(data: CompoundTag?) : LodestoneServerNBTPacket(data) {

    override fun encode(buf: FriendlyByteBuf?) {
        super.encode(buf)
    }

    constructor(buf: FriendlyByteBuf) : this(buf.readNbt()!!)

    constructor(uuid: UUID, foci: ItemStack) : this(CompoundTag().apply {
        put("Foci", foci.save(CompoundTag()))
        putUUID("UUID", uuid)
    })

    override fun executeServerNbt(
        server: MinecraftServer?,
        player: ServerPlayer?,
        listener: ServerGamePacketListenerImpl?,
        responseSender: PacketSender?,
        channel: SimpleChannel?,
        data: CompoundTag
    ) {
        server?.execute {
            val foci = ItemStack.of(data.getCompound("Foci"))

            if (player!!.uuid == data.getUUID("UUID")) {
                val wandItem = player.mainHandItem.item as WandItem
                wandItem.updateSelectedFoci(player.mainHandItem, foci)
            }
        }
    }
}