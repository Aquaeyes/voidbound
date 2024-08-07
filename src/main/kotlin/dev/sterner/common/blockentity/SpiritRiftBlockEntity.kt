package dev.sterner.common.blockentity

import com.sammy.malum.client.SpiritBasedParticleBuilder
import dev.sterner.api.RiftType
import dev.sterner.api.blockentity.SyncedBlockEntity
import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import dev.sterner.registry.VoidBoundRiftTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.state.BlockState

class SpiritRiftBlockEntity(pos: BlockPos, state: BlockState?) :
    SyncedBlockEntity(VoidBoundBlockEntityTypeRegistry.DESTABILIZED_SPIRIT_RIFT.get(), pos, state) {

    var riftType: RiftType = VoidBoundRiftTypeRegistry.NORMAL.get()

    val x = (worldPosition.x.toFloat() + 0.5f).toDouble()
    val y = (worldPosition.y.toFloat() + 0.5f).toDouble()
    val z = (worldPosition.z.toFloat() + 0.5f).toDouble()

    override fun saveAdditional(tag: CompoundTag) {
        tag.putString("RiftType", riftType.toString())
        super.saveAdditional(tag)
    }

    override fun load(tag: CompoundTag) {

        if (tag.contains("RiftType")) {
            val op = VoidBoundRiftTypeRegistry.RIFT.getOptional(ResourceLocation.tryParse(tag.getString("RiftType")))
            if (op.isPresent) {
                riftType = op.get()
            }
        }

        super.load(tag)
    }

    fun tick() {

        if (level != null && level!!.isClientSide) {
            riftType.tick(level!!, blockPos)
        }
    }
}