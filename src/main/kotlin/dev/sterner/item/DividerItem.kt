package dev.sterner.item

import com.sammy.malum.common.entity.FloatingItemEntity
import com.sammy.malum.common.item.IMalumEventResponderItem
import com.sammy.malum.common.item.spirit.SpiritShardItem
import com.sammy.malum.registry.common.DamageTypeRegistry
import com.sammy.malum.registry.common.SpiritTypeRegistry
import dev.sterner.entity.ParticleEntity
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingHurtEvent
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import java.util.*


class DividerItem(properties: Properties) : Item(properties), IMalumEventResponderItem {

    override fun hurtEvent(event: LivingHurtEvent?, attacker: LivingEntity?, target: LivingEntity?, stack: ItemStack?) {
        var level = attacker!!.level();
        val damage = event!!.amount * (0.5f + EnchantmentHelper.getSweepingDamageRatio(attacker))
        val entities = level.getEntities(attacker, target!!.boundingBox.inflate(1.0))
        entities.forEach { entity ->
            if (entity is LivingEntity) {
                if (entity.isAlive) {
                    entity.hurt(DamageTypeRegistry.create(level, DamageTypeRegistry.SCYTHE_SWEEP, attacker), damage)
                    entity.knockback(0.4, Mth.sin(attacker.yRot * (Math.PI.toFloat() / 180F)).toDouble(), -Mth.cos(attacker.yRot * (Math.PI.toFloat() / 180F)).toDouble())
                }
            }
        }
    }

    override fun getUseAnimation(stack: ItemStack): UseAnim {
        return UseAnim.BRUSH
    }

    override fun getUseDuration(stack: ItemStack): Int {
        return 200
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        player.startUsingItem(usedHand)
        return super.use(level, player, usedHand)
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val player = context.player
        player!!.startUsingItem(context.hand)
        return InteractionResult.CONSUME
    }

    override fun onUseTick(level: Level, livingEntity: LivingEntity, stack: ItemStack, remainingUseDuration: Int) {
        if (remainingUseDuration >= 0 && livingEntity is Player) {
            val spirits = getEntitiesInBox(level, livingEntity)
            for (spirit in spirits) {
                if (spirit.item.item is SpiritShardItem) {
                    for (spiritType in SpiritTypeRegistry.SPIRITS) {
                        val dest = ParticleEntity.getRandomOffset(spirit.position(), level.random)

                        val particle = ParticleEntity(level, dest)
                        particle.setSpirit(spiritType.value)
                        particle.moveTo(spirit.position())

                        level.addFreshEntity(particle)
                    }

                    spirit.discard()
                }
            }
        }

    }

    private fun getEntitiesInBox(level: Level, player: Player): Collection<FloatingItemEntity> {
        // Calculate the direction the player is looking at
        val lookDirection = player.lookAngle
        val position = player.position()

        // Calculate the center of the box 2 blocks in front of the player
        val centerX = position.x + lookDirection.x * 2
        val centerY = position.y + lookDirection.y * 2
        val centerZ = position.z + lookDirection.z * 2

        // Define the bounding box centered at the calculated position
        val boundingBox = AABB(centerX - 3, centerY - 3, centerZ - 3, centerX + 3, centerY + 3, centerZ + 3)

        // Retrieve entities in the defined bounding box
        val entitiesInBox = mutableListOf<FloatingItemEntity>()
        for (entity in level.getEntitiesOfClass(FloatingItemEntity::class.java, boundingBox)) {
            if (player.distanceToSqr(entity) < 9.0) {
                entitiesInBox.add(entity)
            }
        }

        return entitiesInBox
    }

    override fun canAttackBlock(state: BlockState, level: Level, pos: BlockPos, player: Player): Boolean {
        return false
    }

    override fun getDestroySpeed(stack: ItemStack, state: BlockState): Float {
        return 1f
    }

    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        return false
    }

    override fun mineBlock(
        stack: ItemStack,
        level: Level,
        state: BlockState,
        pos: BlockPos,
        miningEntity: LivingEntity
    ): Boolean {
        return false
    }
}