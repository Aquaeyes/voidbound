package dev.sterner.common.block

import de.dafuqs.revelationary.api.revelations.RevelationAware
import dev.sterner.VoidBound
import dev.sterner.registry.VoidBoundBlockRegistry
import dev.sterner.registry.VoidBoundItemRegistry
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Tuple
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.state.BlockState

class TearBlock(val type: Type, properties: Properties) : CloakedTearBlock(properties), RevelationAware {

    init {
        RevelationAware.register(this)
    }

    override fun getCloakAdvancementIdentifier(): ResourceLocation {
        return VoidBound.id("revelationary/ichor_requirement_advancement")
    }

    override fun getBlockStateCloaks(): MutableMap<BlockState, BlockState> {
        val cloaks: MutableMap<BlockState, BlockState> = mutableMapOf()
        cloaks[this.defaultBlockState()] = VoidBoundBlockRegistry.TEAR_CLOAK.get().defaultBlockState()
        return cloaks
    }

    override fun getItemCloak(): Tuple<Item, Item> {
        val end = Tuple(VoidBoundItemRegistry.TEAR_OF_ENDER.get(), VoidBoundItemRegistry.STRANGE_MATTER.get())
        val crim = Tuple(VoidBoundItemRegistry.TEAR_OF_CRIMSON.get(), VoidBoundItemRegistry.STRANGE_MATTER.get())

        return if (type == Type.CRIMSON) crim else end
    }

    /*

    override fun getCloakedItemTranslation(): Tuple<Item, MutableComponent> {
        val crim = VoidBoundItemRegistry.TEAR_OF_CRIMSON.get()
        val end = VoidBoundItemRegistry.TEAR_OF_ENDER.get()

        return Tuple(if(type == Type.CRIMSON) crim else end, Component.translatable("item.voidbound.strange_matter"))
    }


     */
    enum class Type {
        CRIMSON,
        ENDER
    }
}