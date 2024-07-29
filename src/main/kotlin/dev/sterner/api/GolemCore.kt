package dev.sterner.api

import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.StringRepresentable

enum class GolemCore : StringRepresentable {
    NONE,
    GATHER,
    FILL,
    EMPTY,
    HARVEST,
    GUARD,
    BUTCHER,
    CHOP;

    override fun getSerializedName(): String {
        return this.name.lowercase()
    }

    companion object {
        fun writeNbt(tag: CompoundTag, core: GolemCore) {
            tag.putString("name", core.name)
        }

        fun readNbt(tag: CompoundTag) : GolemCore {
            return valueOf(tag.getString("name"))
        }
    }

}