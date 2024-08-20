package dev.sterner.common.item.tool.ichor

import com.sammy.malum.common.enchantment.ReboundEnchantment
import com.sammy.malum.common.item.curiosities.weapons.scythe.MagicScytheItem
import com.sammy.malum.common.item.curiosities.weapons.scythe.MalumScytheItem
import net.minecraft.world.item.Tier

class IchoriumScytheItem(tier: Tier?, attackDamageIn: Float, attackSpeedIn: Float, builderIn: Properties?) : MagicScytheItem(
    tier,
    attackDamageIn + 3 + tier!!.attackDamageBonus,
    attackSpeedIn - 1.2f,
    4f,
    builderIn
) {

    init {

    }

}