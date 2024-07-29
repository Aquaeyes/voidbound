package dev.sterner.common.components

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.world.entity.LivingEntity

class VoidBoundEntityComponent(val livingEntity: LivingEntity) : AutoSyncedComponent {

    var spiritBinderPos: BlockPos? = null

    override fun readFromNbt(tag: CompoundTag) {
        spiritBinderPos = NbtUtils.readBlockPos(tag.getCompound("BlockPos"))
    }

    override fun writeToNbt(tag: CompoundTag) {
        if (spiritBinderPos != null) {
            val posTag: CompoundTag = NbtUtils.writeBlockPos(spiritBinderPos!!)
            tag.put("BlockPos", posTag)
        }
    }
}