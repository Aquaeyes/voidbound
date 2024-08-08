package dev.sterner.common.item.tool

import dev.sterner.api.util.VoidBoundBlockUtils
import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.joml.Vector3d
import team.lodestar.lodestone.systems.item.tools.magic.MagicAxeItem

class AxeOfTheStreamItem(material: Tier?, damage: Float, speed: Float, magicDamage: Float, properties: Properties?) : MagicAxeItem(material, damage, speed,
    magicDamage,
    properties
) {

    override fun getUseDuration(stack: ItemStack): Int {
        return 72000
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        player.startUsingItem(usedHand)
        return super.use(level, player, usedHand)
    }


    override fun onUseTick(level: Level, livingEntity: LivingEntity, stack: ItemStack, remainingUseDuration: Int) {
        var stuff = level.getEntitiesOfClass(ItemEntity::class.java, livingEntity.boundingBox.inflate(10.0))
        if (stuff != null && stuff.isNotEmpty()) {
            var iterator = stuff.iterator()
            while (iterator.hasNext()) {
                val e: ItemEntity = iterator.next() as ItemEntity
                if (!e.isRemoved) {
                    var d6: Double = e.x - livingEntity.x
                    var d8: Double = e.y - livingEntity.y + (livingEntity.bbHeight / 2.0f)
                    var d10: Double = e.z - livingEntity.z
                    val d11 = Mth.sqrt(d6.toFloat() * d6.toFloat() + d8.toFloat() * d8.toFloat() + d10.toFloat() * d10.toFloat())
                    d6 /= d11
                    d8 /= d11
                    d10 /= d11
                    val d13 = 0.3

                    val newMotionX = (e.deltaMovement.x - d6 * d13).coerceIn(-0.25, 0.25)
                    val newMotionY = (e.deltaMovement.y - (d8 * d13 - 0.1)).coerceIn(-0.25, 0.25)
                    val newMotionZ = (e.deltaMovement.z - d10 * d13).coerceIn(-0.25, 0.25)

                    e.deltaMovement = Vec3(newMotionX, newMotionY, newMotionZ)
                }
            }
        }


        super.onUseTick(level, livingEntity, stack, remainingUseDuration)
    }

    override fun onBlockStartBreak(itemstack: ItemStack?, pos: BlockPos, player: Player): Boolean {
        val level = player.level()
        val block = level.getBlockState(pos)
        if (!player.isShiftKeyDown && block.`is`(BlockTags.LOGS)) {
            VoidBoundBlockUtils.breakFurthestBlock(level, pos, block, player)

            return true
        } else {
            return super.onBlockStartBreak(itemstack, pos, player)
        }


    }
}