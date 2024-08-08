package dev.sterner.common.item.tool

import dev.sterner.api.HammerLikeItem
import io.github.fabricators_of_create.porting_lib.event.common.BlockEvents
import io.github.fabricators_of_create.porting_lib.event.common.BlockEvents.BreakEvent
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.phys.BlockHitResult
import team.lodestar.lodestone.systems.item.tools.magic.MagicPickaxeItem
import java.util.function.Consumer


class PickaxeOfTheCoreItem(material: Tier?, damage: Int, speed: Float, magicDamage: Float, properties: Properties?) : MagicPickaxeItem(material, damage, speed,
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
       return BlockTags.MINEABLE_WITH_PICKAXE
    }


}