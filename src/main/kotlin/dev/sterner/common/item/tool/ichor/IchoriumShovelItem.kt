package dev.sterner.common.item.tool.ichor

import dev.sterner.common.item.tool.ShovelOfEarthmoverItem
import net.minecraft.world.item.Tier

class IchoriumShovelItem(material: Tier?, damage: Int, speed: Float, magicDamage: Float, properties: Properties?) : ShovelOfEarthmoverItem(material, damage, speed,
    magicDamage,
    properties
) {

    override fun getRadius(): Int {
        return 5
    }

    override fun getDepth(): Int {
        return 1
    }
}