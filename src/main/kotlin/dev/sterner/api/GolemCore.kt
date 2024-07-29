package dev.sterner.api

import dev.sterner.registry.VoidBoundItemRegistry
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item

enum class GolemCore : StringRepresentable {
    NONE,
    GATHER,
    FILL,
    EMPTY,
    HARVEST,
    GUARD,
    BUTCHER,
    LUMBER;

    override fun getSerializedName(): String {
        return this.name.lowercase()
    }

    companion object {

        fun getItem(core: GolemCore) : Item? {
            return when (core.serializedName) {
                "gather" -> VoidBoundItemRegistry.GOLEM_CORE_GATHER.get()
                "fill" -> VoidBoundItemRegistry.GOLEM_CORE_FILL.get()
                "empty" -> VoidBoundItemRegistry.GOLEM_CORE_EMPTY.get()
                "harvest" -> VoidBoundItemRegistry.GOLEM_CORE_HARVEST.get()
                "guard" -> VoidBoundItemRegistry.GOLEM_CORE_GUARD.get()
                "butcher" -> VoidBoundItemRegistry.GOLEM_CORE_BUTCHER.get()
                "lumber" -> VoidBoundItemRegistry.GOLEM_CORE_LUMBER.get()
                else -> null
            }
        }

        fun writeNbt(tag: CompoundTag, core: GolemCore) {
            tag.putString("name", core.name)
        }

        fun readNbt(tag: CompoundTag) : GolemCore {
            return valueOf(tag.getString("name"))
        }
    }

}