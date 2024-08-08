package dev.sterner.common.item.tool

import dev.sterner.api.HammerLikeItem
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Tier
import net.minecraft.world.level.block.Block
import team.lodestar.lodestone.systems.item.tools.magic.MagicShovelItem

class ShovelOfEarthmoverItem(material: Tier?, damage: Int, speed: Float, magicDamage: Float, properties: Properties?) :
    MagicShovelItem(
        material, damage, speed,
        magicDamage,
        properties
    ), HammerLikeItem {
    override fun getRadius(): Int {
        return 3
    }

    override fun getDepth(): Int {
        return 1
    }

    override fun getBlockTags(): TagKey<Block> {
        return BlockTags.MINEABLE_WITH_SHOVEL
    }
}