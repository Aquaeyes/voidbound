package dev.sterner.components

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import dev.sterner.VoidBound
import dev.sterner.registry.VoidBoundComponentRegistry
import net.minecraft.commands.Commands
import net.minecraft.core.BlockPos
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class VoidBoundWorldComponent(val level: Level) : AutoSyncedComponent {

    private var weepingWellList = mutableListOf<BlockPos>()

    fun addWeepingWell(pos: BlockPos) {
        weepingWellList.add(pos)
        VoidBoundComponentRegistry.VOID_BOUND_WORLD_COMPONENT.sync(level)

    }

    override fun readFromNbt(tag: CompoundTag) {
        weepingWellList.clear()
        val listTag = tag.getList("WellList", 10);
        for (i in listTag.indices) {
            val compoundTag = listTag.getCompound(i)
            val pos = NbtUtils.readBlockPos(compoundTag)
            weepingWellList.add(pos)
        }
    }

    override fun writeToNbt(tag: CompoundTag) {
        val listTag = ListTag()
        for (pos in weepingWellList) {
            val tag = CompoundTag()
            tag.put("WellPos", NbtUtils.writeBlockPos(pos))
            listTag.add(tag)
        }
        if (listTag.isNotEmpty()) {
            tag.put("WellList", listTag)
        }
    }
}