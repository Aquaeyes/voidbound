package dev.sterner.common.rift

import dev.sterner.api.rift.RiftType
import dev.sterner.common.blockentity.SpiritRiftBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level

class PoolRiftType : RiftType() {

    var cooldown = 0

    override fun tick(level: Level, blockPos: BlockPos, blockEntity: SpiritRiftBlockEntity) {
        cooldown++
        if (cooldown >= 20 * 60) {
            blockEntity.simpleSpiritCharge.rechargeAllCount()
            blockEntity.notifyUpdate()
        }
        super.tick(level, blockPos, blockEntity)
    }
}