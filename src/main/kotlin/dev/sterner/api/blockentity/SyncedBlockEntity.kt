package dev.sterner.api.blockentity

import io.github.fabricators_of_create.porting_lib.block.CustomDataPacketHandlingBlockEntity
import io.github.fabricators_of_create.porting_lib.block.CustomUpdateTagHandlingBlockEntity
import net.minecraft.MethodsReturnNonnullByDefault
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Connection
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState


@MethodsReturnNonnullByDefault
abstract class SyncedBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    BlockEntity(type, pos, state), CustomDataPacketHandlingBlockEntity, CustomUpdateTagHandlingBlockEntity {

    override fun getUpdateTag(): CompoundTag {
        return writeClient(CompoundTag())
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener>? {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    override fun handleUpdateTag(tag: CompoundTag) {
        readClient(tag)
    }

    override fun onDataPacket(connection: Connection, packet: ClientboundBlockEntityDataPacket) {
        val tag = packet.tag
        readClient(tag ?: CompoundTag())
    }

    // Special handling for client update packets
    fun readClient(tag: CompoundTag) {
        load(tag)
    }

    // Special handling for client update packets
    fun writeClient(tag: CompoundTag): CompoundTag {
        saveAdditional(tag)
        return tag
    }

    fun sendData() {
        if (level is ServerLevel) (level!! as ServerLevel).chunkSource.blockChanged(blockPos)
    }

    fun notifyUpdate() {
        setChanged()
        sendData()
    }

    override fun deserializeNBT(state: BlockState, nbt: CompoundTag) {
        this.load(nbt)
    }
}