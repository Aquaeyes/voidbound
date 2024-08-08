package dev.sterner.common.item.tool

import dev.sterner.api.util.VoidBoundEntityUtils
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import team.lodestar.lodestone.systems.item.tools.magic.MagicSwordItem

class SwordOfTheZephyrItem(material: Tier?, attackDamage: Int, attackSpeed: Float, magicDamage: Float,
                           properties: Properties?
) : MagicSwordItem(material, attackDamage,
    attackSpeed,
    magicDamage, properties
) {

    override fun getUseDuration(stack: ItemStack): Int {
        return 72000
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        player.startUsingItem(usedHand)
        return super.use(level, player, usedHand)
    }

    override fun onUseTick(level: Level, player: LivingEntity, stack: ItemStack, remainingUseDuration: Int) {

        val ticks: Int = this.getUseDuration(stack) - remainingUseDuration
        var newMotionY = player.deltaMovement.y

        if (newMotionY < 0.0) {
            newMotionY /= 1.2000000476837158
            player.fallDistance /= 1.2f
        }

        newMotionY += 0.07999999821186066
        if (newMotionY > 0.5) {
            newMotionY = 0.20000000298023224
        }

        player.deltaMovement = Vec3(player.deltaMovement.x, newMotionY, player.deltaMovement.z)

        if (player is ServerPlayer) {
            VoidBoundEntityUtils.resetFloatCounter(player as ServerPlayer?)
        }

        val targets: List<*> =
            player.level().getEntities(player, player.boundingBox.inflate(2.5, 2.5, 2.5))
        var miny: Int
        if (targets.size > 0) {
            miny = 0
            while (miny < targets.size) {
                val entity: Entity = targets[miny] as Entity
                if (entity !is Player && entity is LivingEntity && !entity.isDeadOrDying && (player.passengers == null || player.passengers !== entity)) {
                    val p = Vec3(player.x, player.y, player.z)
                    val t = Vec3(entity.x, entity.y, entity.z)
                    val distance: Double = p.distanceTo(t) + 0.1
                    val r = Vec3(t.x - p.x, t.y - p.y, t.z - p.z)

                    val prevDeltaMovement = entity.deltaMovement
                    entity.deltaMovement = Vec3(
                        prevDeltaMovement.x + r.x / 2.5 / distance,
                        prevDeltaMovement.y + r.y / 2.5 / distance,
                        prevDeltaMovement.z + r.z / 2.5 / distance
                    )

                }
                ++miny
            }
        }

        if (player.level().isClientSide) {

            if (player.onGround()) {
                val r1: Float = player.level().random.nextFloat() * 360.0f
                val mx: Float = -Mth.sin(r1 / 180.0f * 3.1415927f) / 5.0f
                val mz: Float = Mth.cos(r1 / 180.0f * 3.1415927f) / 5.0f
                player.level().addParticle(
                    ParticleTypes.SMOKE,
                    false,
                    player.x,
                    player.boundingBox.minY + 0.10000000149011612,
                    player.z,
                    mx.toDouble(),
                    0.0,
                    mz.toDouble()
                )
            }
        }

        if (ticks % 20 == 0) {

            stack.hurtAndBreak(1, player
            ) { player: LivingEntity ->
                player.broadcastBreakEvent(
                    player.usedItemHand
                )
            }
        }

        super.onUseTick(level, player, stack, remainingUseDuration)
    }
}