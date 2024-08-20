package dev.sterner.common.item.foci

import com.sammy.malum.common.item.curiosities.weapons.staff.AuricFlameStaffItem
import com.sammy.malum.registry.client.ParticleRegistry
import com.sammy.malum.registry.common.SpiritTypeRegistry
import com.sammy.malum.visual_effects.SpiritLightSpecs
import dev.sterner.api.util.VoidBoundPosUtils
import dev.sterner.api.wand.IWandFocus
import dev.sterner.networking.AxeOfTheStreamParticlePacket.Companion.getPlayerLookDirection
import dev.sterner.networking.ExcavationPacket
import dev.sterner.registry.VoidBoundPacketRegistry
import net.minecraft.client.Minecraft
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import team.lodestar.lodestone.handlers.RenderHandler
import team.lodestar.lodestone.helpers.RandomHelper
import team.lodestar.lodestone.systems.easing.Easing
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder
import team.lodestar.lodestone.systems.particle.data.GenericParticleData
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType
import team.lodestar.lodestone.systems.particle.world.behaviors.components.DirectionalBehaviorComponent
import kotlin.math.cos
import kotlin.math.sin


class ExcavationFoci : IWandFocus {

    private var timeToBreak: Int = 40
    private var breakTime: Int = 0
    private var breakProgress: Int = -1
    private var blockState: BlockState? = null
    
    override fun onUsingFocusTick(stack: ItemStack, level: Level, player: Player) {
        val client = Minecraft.getInstance()
        val maxReach = 10.0
        val tickDelta = 1.0f
        val includeFluids = false

        val hit: HitResult? = client.cameraEntity?.pick(maxReach, tickDelta, includeFluids)

        if (hit != null) {
            when (hit.type) {
                HitResult.Type.MISS -> {}
                HitResult.Type.BLOCK -> {
                    val blockHit = hit as BlockHitResult
                    val blockPos = blockHit.blockPos
                    val newState = client.level?.getBlockState(blockPos) ?: return



                    if (blockState != newState) {
                        this.breakTime = 0
                        this.breakProgress = -1
                    }

                    blockState = newState

                    this.breakTime++
                    val progress: Int = (this.breakTime / this.timeToBreak.toFloat() * 10).toInt()

                    VoidBoundPacketRegistry.VOID_BOUND_CHANNEL.sendToServer(ExcavationPacket(player.id, blockPos, progress, breakProgress, breakTime))
                    val pos = getProjectileSpawnPos(player, InteractionHand.MAIN_HAND, 1.5f, 0.6f)
                    spawnChargeParticles(player.level(), player, pos, 1f)


                    if (breakTime % 6 == 0) {
                        level.playSound(player, blockPos, blockState!!.soundType.breakSound, SoundSource.BLOCKS)
                    }

                    if (progress != this.breakProgress) {
                        this.breakProgress = progress
                    }

                    client.level!!.destroyBlockProgress(player.id, blockPos, progress)

                    if (this.breakTime >= this.timeToBreak) {
                        this.breakTime = 0
                        this.breakProgress = -1
                    }

                    for (i in 0..2) {
                        val coordPos = VoidBoundPosUtils.getFaceCoords(level, blockState!!, blockPos, getPlayerLookDirection(client.player!!).opposite)
                        val lightSpecs: ParticleEffectSpawner = SpiritLightSpecs.spiritLightSpecs(level, coordPos, SpiritTypeRegistry.EARTHEN_SPIRIT)
                        lightSpecs.builder.multiplyLifetime(1.5f)
                        lightSpecs.bloomBuilder.multiplyLifetime(1.5f)
                        lightSpecs.spawnParticles()
                        lightSpecs.spawnParticles()
                    }
                }

                HitResult.Type.ENTITY -> {

                }

                null -> {

                }
            }
        }
    }

    fun spawnChargeParticles(
        pLevel: Level,
        pLivingEntity: LivingEntity,
        pos: Vec3,
        pct: Float
    ) {
        val random = pLevel.random
        val spinData = SpinParticleData.createRandomDirection(random, 0.25f, 0.5f)
            .setSpinOffset(RandomHelper.randomBetween(random, 0f, 6.28f)).build()
        WorldParticleBuilder.create(
            ParticleRegistry.HEXAGON,
            DirectionalBehaviorComponent(pLivingEntity.lookAngle.normalize())
        )
            .setRenderTarget(RenderHandler.LATE_DELAYED_RENDER)
            .setTransparencyData(
                GenericParticleData.create(0.5f * pct, 0f).setEasing(Easing.SINE_IN_OUT, Easing.SINE_IN).build()
            )
            .setScaleData(GenericParticleData.create(0.35f * pct, 0f).setEasing(Easing.SINE_IN_OUT).build())
            .setSpinData(spinData)
            .setColorData(AuricFlameStaffItem.AURIC_COLOR_DATA)
            .setLifetime(5)
            .setMotion(pLivingEntity.lookAngle.normalize().scale(0.05))
            .enableNoClip()
            .enableForcedSpawn()
            .setLifeDelay(2)
            .spawn(pLevel, pos.x, pos.y, pos.z)
            .setRenderType(LodestoneWorldParticleRenderType.LUMITRANSPARENT)
            .spawn(pLevel, pos.x, pos.y, pos.z)
    }

    fun getProjectileSpawnPos(player: LivingEntity, hand: InteractionHand, distance: Float, spread: Float): Vec3 {
        val angle = if (hand == InteractionHand.MAIN_HAND) 225 else 90
        val radians = Math.toRadians((angle - player.yHeadRot).toDouble())
        return player.position().add(player.lookAngle.scale(distance.toDouble()))
            .add(spread * sin(radians), (player.bbHeight * 0.9f).toDouble(), spread * cos(radians))
    }
}