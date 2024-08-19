package dev.sterner.common.item

import de.dafuqs.revelationary.api.revelations.RevelationAware
import dev.sterner.VoidBound
import dev.sterner.registry.VoidBoundItemRegistry
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Tuple
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.state.BlockState

class IchorItem(properties: Properties) : Item(properties), RevelationAware {

    init {
        RevelationAware.register(this)
    }

    override fun getCloakAdvancementIdentifier(): ResourceLocation {
        return VoidBound.id("revelationary/ichor_requirement_advancement")
    }

    override fun getBlockStateCloaks(): MutableMap<BlockState, BlockState> {
       return mutableMapOf()
    }

    override fun getItemCloak(): Tuple<Item, Item> {
        return Tuple(this, VoidBoundItemRegistry.STRANGE_MATTER.get())
    }
}