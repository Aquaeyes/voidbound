package dev.sterner.common.blockentity

import com.sammy.malum.core.listeners.SpiritDataReloadListener
import com.sammy.malum.core.systems.recipe.SpiritWithCount
import com.sammy.malum.core.systems.spirit.MalumSpiritType
import com.sammy.malum.visual_effects.SpiritLightSpecs
import dev.sterner.api.SimpleSpiritCharge
import dev.sterner.api.blockentity.Modifier
import dev.sterner.api.blockentity.SyncedBlockEntity
import dev.sterner.common.block.SpiritBinderBlock
import dev.sterner.networking.SpiritBinderParticlePacket
import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import dev.sterner.registry.VoidBoundComponentRegistry
import dev.sterner.registry.VoidBoundPacketRegistry
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Mth
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder
import team.lodestar.lodestone.systems.particle.data.GenericParticleData
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier

class SpiritBinderBlockEntity(pos: BlockPos, blockState: BlockState) : SyncedBlockEntity(
    VoidBoundBlockEntityTypeRegistry.SPIRIT_BINDER.get(), pos, blockState
) {

    var color: Vector3f = Vector3f(0.8f, 0.07f, 0.8f)

    var rift: SpiritRiftBlockEntity? = null

    fun tick() {
        if (level != null) {

            if (level!!.getBlockState(blockPos).hasProperty(SpiritBinderBlock.MODIFIER)) {
                if (level!!.getBlockState(blockPos).getValue(SpiritBinderBlock.MODIFIER) == Modifier.BRILLIANT) {
                    tickBrilliantState()
                } else if (level!!.getBlockState(blockPos).getValue(SpiritBinderBlock.MODIFIER) == Modifier.HEX_ASH) {
                    tickHexAshState()
                } else {
                    tickNoneState()
                }
            }


        }
    }

    private fun tickHexAshState() {

    }

    private fun tickBrilliantState() {

    }

    private fun tickNoneState() {

    }


    override fun load(tag: CompoundTag) {
        super.load(tag)
    }

    override fun saveAdditional(tag: CompoundTag) {
        super.saveAdditional(tag)
    }
}