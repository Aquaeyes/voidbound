package dev.sterner.common.components

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.Level

class VoidBoundWorldComponent(val level: Level) : AutoSyncedComponent {

    override fun readFromNbt(tag: CompoundTag) {

    }

    override fun writeToNbt(tag: CompoundTag) {

    }
}