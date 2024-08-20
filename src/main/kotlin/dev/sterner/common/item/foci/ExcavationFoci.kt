package dev.sterner.common.item.foci

import com.sammy.malum.registry.common.SpiritTypeRegistry
import com.sammy.malum.visual_effects.SpiritLightSpecs
import dev.sterner.api.util.VoidBoundPosUtils
import dev.sterner.api.wand.IWandFocus
import dev.sterner.networking.AxeOfTheStreamParticlePacket.Companion.getPlayerLookDirection
import dev.sterner.networking.ExcavationPacket
import dev.sterner.registry.VoidBoundPacketRegistry
import net.minecraft.client.Minecraft
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LevelEvent
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner


class ExcavationFoci : IWandFocus {

    private var timeToBreak: Int = 40
    private var breakTime: Int = 0
    private var breakProgress: Int = -1
    
    override fun onUsingFocusTick(stack: ItemStack, level: Level, player: Player) {
        val client = Minecraft.getInstance()
        val maxReach = 10.0 //The farthest target the cameraEntity can detect
        val tickDelta = 1.0f //Used for tracking animation progress; no tracking is 1.0F
        val includeFluids = false //Whether to detect fluids as blocks

        val hit: HitResult? = client.cameraEntity?.pick(maxReach, tickDelta, includeFluids)

        if (hit != null) {
            when (hit.type) {
                HitResult.Type.MISS -> {}
                HitResult.Type.BLOCK -> {
                    val blockHit = hit as BlockHitResult
                    val blockPos = blockHit.blockPos
                    val blockState: BlockState = client.level?.getBlockState(blockPos) ?: return

                    this.breakTime++
                    val progress: Int = (this.breakTime / this.timeToBreak.toFloat() * 10).toInt()

                    VoidBoundPacketRegistry.VOID_BOUND_CHANNEL.sendToServer(ExcavationPacket(player.id, blockPos, progress, breakProgress, breakTime))

                    if (progress != this.breakProgress) {
                        this.breakProgress = progress
                    }

                    if (this.breakTime >= this.timeToBreak) {
                        this.breakTime = 0
                        this.breakProgress = -1
                    }

                    level.playSound(player, blockPos, blockState.soundType.breakSound, SoundSource.BLOCKS)


                    for (i in 0..5) {
                        val coordPos =
                            VoidBoundPosUtils.getFaceCoords(level, blockState, blockPos, getPlayerLookDirection(client.player!!).opposite)
                        val lightSpecs: ParticleEffectSpawner =
                            SpiritLightSpecs.spiritLightSpecs(level, coordPos, SpiritTypeRegistry.EARTHEN_SPIRIT)
                        lightSpecs.builder.multiplyLifetime(1.5f)
                        lightSpecs.bloomBuilder.multiplyLifetime(1.5f)
                        lightSpecs.spawnParticles()
                        lightSpecs.spawnParticles()
                    }
                }

                HitResult.Type.ENTITY -> {
                    val entityHit = hit as EntityHitResult
                    val entity: Entity = entityHit.entity
                }

                null -> TODO()
            }
        }
    }
}