package dev.sterner.common.item.tool.ichor

import dev.sterner.common.item.tool.AxeOfTheStreamItem
import net.minecraft.world.item.Tier

class IchoriumAxeItem(material: Tier?, damage: Float, speed: Float, magicDamage: Float, properties: Properties?) :
    AxeOfTheStreamItem(
        material, damage, speed,
        magicDamage,
        properties
    )