package dev.sterner.networking

import dev.sterner.common.blockentity.OsmoticEnchanterBlockEntity
import me.pepperbell.simplenetworking.SimpleChannel
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LevelEvent
import net.minecraft.world.level.block.state.BlockState
import team.lodestar.lodestone.systems.network.LodestoneServerNBTPacket
import team.lodestar.lodestone.systems.network.LodestoneServerPacket
import java.sql.Time

class ExcavationPacket(data: CompoundTag?) : LodestoneServerNBTPacket(data) {

    override fun encode(buf: FriendlyByteBuf?) {
        super.encode(buf)
    }

    constructor(buf: FriendlyByteBuf) : this(buf.readNbt()!!)

    constructor(id: Int, blockPos: BlockPos, progress: Int, breakProgress: Int, breakTime: Int) : this(CompoundTag().apply {
        putInt("Id", id)
        put("Pos", NbtUtils.writeBlockPos(blockPos))
        putInt("Progress", progress)
        putInt("BreakProgress", breakProgress)
        putInt("BreakTime", breakTime)
    })

    override fun executeServerNbt(
        server: MinecraftServer?,
        player: ServerPlayer,
        listener: ServerGamePacketListenerImpl?,
        responseSender: PacketSender?,
        channel: SimpleChannel?,
        data: CompoundTag
    ) {
        server?.execute {

            val playerId = data.getInt("Id")
            val blockPos = NbtUtils.readBlockPos(data.getCompound("Pos"))
            val progress = data.getInt("Progress")
            var breakProgress = data.getInt("BreakProgress")
            var breakTime = data.getInt("BreakTime")

            if (progress != breakProgress) {
                //player.level().destroyBlockProgress(playerId, blockPos, progress)
            }

            if (breakTime >= 40) {
                player.level().destroyBlock(blockPos, true)
                player.level().levelEvent(
                    LevelEvent.PARTICLES_DESTROY_BLOCK,
                    blockPos, Block.getId(player.level().getBlockState(blockPos))
                )
            }
        }
    }
}