package dev.sterner.common.foci

import dev.sterner.api.wand.IWandFocus
import dev.sterner.common.entity.BoltEntity
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import team.lodestar.lodestone.handlers.ScreenshakeHandler.intensity
import kotlin.math.pow


class ShockFoci : IWandFocus {

    var cooldown = 0

    override fun onUsingFocusTick(stack: ItemStack, level: Level, player: Player) {
        if (cooldown == 0) {
            val distance: Double = getMaxDistance().pow(2.0)
            val vec3d: Vec3 = player.getEyePosition(1f)
            val vec3d2: Vec3 = player.getViewVector(1f)
            val vec3d3: Vec3 =
                vec3d.add(vec3d2.x * getMaxDistance(), vec3d2.y * getMaxDistance(), vec3d2.z * getMaxDistance())
            val blockHit: HitResult = level
                .clip(
                    ClipContext(
                        vec3d,
                        vec3d3,
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        player
                    )
                )
            val hit: EntityHitResult? = ProjectileUtil.getEntityHitResult(
                level, player, vec3d, vec3d3,
                player.getBoundingBox().expandTowards(vec3d2.multiply(distance, distance, distance)).inflate(1.0, 1.0, 1.0)
            ) { target -> !target.isSpectator() }

            if (!level.isClientSide && blockHit.location != null) {
                val bolt = BoltEntity(
                    player,
                    (if (hit?.entity != null) hit.entity.distanceTo(player)
                    else blockHit.location.distanceTo(player.position())).toDouble(),
                    123456
                )
                level.addFreshEntity(bolt)
            }
            cooldown = 20
            if (hit != null && blockHit.distanceTo(player) > hit.entity.distanceTo(player)) {
                val hitEntity = hit.entity

                hitEntity.hurt(player.damageSources().playerAttack(player), 3f)
            }

        }
        if (cooldown > 0) {
            cooldown--
        }
    }

    private fun getMaxDistance(): Double {
        return 24.0
    }
}