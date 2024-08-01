package dev.sterner.common.item

import dev.sterner.registry.VoidBoundMaterials
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial

class HallowedGogglesItem(properties: Properties) : ArmorItem(
    VoidBoundMaterials.HALLOWED, Type.HELMET,
    properties
) {
}