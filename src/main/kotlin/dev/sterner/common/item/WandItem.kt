package dev.sterner.common.item

import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity

class WandItem(properties: Properties) : Item(
    properties
        .stacksTo(1)
        .rarity(Rarity.RARE)
) {

}