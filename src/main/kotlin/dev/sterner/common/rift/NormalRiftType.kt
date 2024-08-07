package dev.sterner.common.rift

import dev.sterner.api.RiftType
import dev.sterner.common.blockentity.SpiritRiftBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level

class NormalRiftType : RiftType() {


    override fun tick(level: Level, blockPos: BlockPos, blockEntity: SpiritRiftBlockEntity) {
        super.tick(level, blockPos, blockEntity)
    }
}