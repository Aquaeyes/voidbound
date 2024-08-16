package dev.sterner.common.item.foci

import com.sammy.malum.common.item.IVoidItem
import com.sammy.malum.core.systems.spirit.MalumSpiritType
import dev.sterner.api.wand.IWandFocus
import dev.sterner.registry.VoidBoundWandFociRegistry
import net.minecraft.world.item.Item

class BaseFociItem(val foci: IWandFocus, val type: MalumSpiritType, properties: Properties) : Item(properties), IVoidItem {


}