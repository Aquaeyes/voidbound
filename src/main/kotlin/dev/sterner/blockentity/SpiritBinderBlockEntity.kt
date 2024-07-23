package dev.sterner.blockentity

import com.sammy.malum.core.listeners.SpiritDataReloadListener
import com.sammy.malum.core.systems.recipe.SpiritWithCount
import com.sammy.malum.core.systems.spirit.MalumSpiritType
import com.sammy.malum.registry.common.SpiritTypeRegistry
import com.sammy.malum.visual_effects.SpiritLightSpecs
import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import team.lodestar.lodestone.helpers.RandomHelper
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder
import team.lodestar.lodestone.systems.particle.data.GenericParticleData
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier

class SpiritBinderBlockEntity(pos: BlockPos, blockState: BlockState) : SyncedBlockEntity(
    VoidBoundBlockEntityTypeRegistry.SPIRIT_BINDER.get(), pos,
    blockState
) {

    var aqueousCharge = 0
    var aerialCharge = 0
    var arcaneCharge = 0
    var earthenCharge = 0
    var eldrichCharge = 0
    var infernalCharge = 0
    var sacredCharge = 0
    var wickedCharge = 0
    var umbralCharge = 0

    var counter = 0
    var entity: PathfinderMob? = null

    fun tick() {
        if (level != null) {
            if (entity == null) {
                val list = level!!.getEntitiesOfClass(PathfinderMob::class.java, AABB(blockPos).inflate(5.0)).filter { it.health / it.maxHealth <= 0.25 }
                if (list.isNotEmpty()) {
                    entity = list.first()
                }
            } else {
                val spiritDataOptional = getSpiritData(entity!!)
                if (spiritDataOptional.isPresent) {
                    counter++
                    for (spirit in spiritDataOptional.get()) {
                        spawnSpiritParticle(entity!!, spirit.type)
                    }
                    if (counter > 20 * 5) {
                        counter = 0
                        addSpiritToCharge(entity!!)
                        entity!!.hurt(level!!.damageSources().magic(), 20f)
                        entity = null
                    }
                }
            }
        }
    }

    private fun addSpiritToCharge(entity: PathfinderMob) {
        val list = getSpiritData(entity)
        if (list.isPresent) {
            for (spirit in list.get()) {
                when (spirit.type){
                    SpiritTypeRegistry.AQUEOUS_SPIRIT -> aqueousCharge++
                    SpiritTypeRegistry.AERIAL_SPIRIT -> aerialCharge++
                    SpiritTypeRegistry.ARCANE_SPIRIT -> arcaneCharge++
                    SpiritTypeRegistry.EARTHEN_SPIRIT -> earthenCharge++
                    SpiritTypeRegistry.ELDRITCH_SPIRIT -> eldrichCharge++
                    SpiritTypeRegistry.INFERNAL_SPIRIT -> infernalCharge++
                    SpiritTypeRegistry.SACRED_SPIRIT -> sacredCharge++
                    SpiritTypeRegistry.WICKED_SPIRIT -> wickedCharge++
                    SpiritTypeRegistry.UMBRAL_SPIRIT -> umbralCharge++
                }
            }
            notifyUpdate()
        }
    }

    override fun deserializeNBT(nbt: CompoundTag) {
        super.deserializeNBT(nbt)
        aqueousCharge = nbt.getInt("AqueousCharge")
        aerialCharge = nbt.getInt("AerialCharge")
        arcaneCharge = nbt.getInt("ArcaneCharge")
        earthenCharge = nbt.getInt("EarthenCharge")
        eldrichCharge = nbt.getInt("EldrichCharge")
        infernalCharge = nbt.getInt("InfernalCharge")
        sacredCharge = nbt.getInt("SacredCharge")
        wickedCharge = nbt.getInt("WickedCharge")
        umbralCharge = nbt.getInt("UmbralCharge")
    }

    override fun serializeNBT(): CompoundTag {
        var tag = super.serializeNBT()
        tag.putInt("AqueousCharge", aqueousCharge)
        tag.putInt("AerialCharge", aerialCharge)
        tag.putInt("ArcaneCharge", arcaneCharge)
        tag.putInt("EarthenCharge", earthenCharge)
        tag.putInt("InfernalCharge", infernalCharge)
        tag.putInt("EldrichCharge", eldrichCharge)
        tag.putInt("SacredCharge", sacredCharge)
        tag.putInt("WickedCharge", wickedCharge)
        tag.putInt("UmbralCharge", umbralCharge)

        return tag
    }

    fun spawnSpiritParticle(entity: LivingEntity, type: MalumSpiritType){
        val behavior =
            Consumer<WorldParticleBuilder> { b: WorldParticleBuilder ->
                b.addTickActor { p: LodestoneWorldParticle ->
                    val particlePos = Vec3(p.x, p.y, p.z)
                    val targetPos = Vec3(blockPos.x + 0.5, blockPos.y + 1.5, blockPos.z + 0.5) // Target is the center of the block
                    val direction = targetPos.subtract(particlePos).normalize()
                    p.particleSpeed = direction.scale(0.05) //
                }
            }
        val rand = level!!.random
        val xRand = entity.x + rand.nextDouble() - 0.5
        val yRand = entity.y + entity.bbHeight / 1.5 + (rand.nextDouble() - 0.5)
        val zRand = entity.z + rand.nextDouble() - 0.5

        val startPos = Vec3(xRand, yRand, zRand)

        val distance = startPos.distanceTo(Vec3(blockPos.x + 0.5, blockPos.y + 1.5, blockPos.z + 0.5))
        val baseLifetime = 10 // Base lifetime in ticks
        val speed = 0.05 // Speed of the particle
        val adjustedLifetime = (distance / speed).toInt().coerceAtLeast(baseLifetime) - 20 // Adjusted lifetime based on distance

        val lightSpecs: ParticleEffectSpawner = SpiritLightSpecs.spiritLightSpecs(level, Vec3(xRand, yRand , zRand), type)
        lightSpecs.builder.act { b: WorldParticleBuilder ->
            b.act(behavior)
                .modifyColorData { d: ColorParticleData ->
                    d.multiplyCoefficient(0.35f)
                }
                .modifyData(
                Supplier { b.scaleData },
                Consumer { d: GenericParticleData ->
                    d.multiplyValue(2.0f).multiplyCoefficient(0.9f)
                })
                .modifyData(
                Supplier { b.transparencyData },
                Consumer { d: GenericParticleData ->
                    d.multiplyCoefficient(0.9f)
                }).multiplyLifetime(1.5f)
                .setLifetime(b.particleOptions.lifetimeSupplier.get() as Int + adjustedLifetime)
        }

        lightSpecs.bloomBuilder.act { b: WorldParticleBuilder ->
            b.act(behavior).modifyColorData { d: ColorParticleData ->
                    d.multiplyCoefficient(0.35f)
            }
                .modifyData(
                Supplier { b.scaleData },
                Consumer { d: GenericParticleData ->
                    d.multiplyValue(1.6f).multiplyCoefficient(0.9f)
                })
                .modifyData(
                Supplier { b.transparencyData },
                Consumer { d: GenericParticleData ->
                    d.multiplyCoefficient(0.9f)
                })
                .setLifetime(((b.particleOptions.lifetimeSupplier.get() as Int).toFloat() + adjustedLifetime).toInt())
        }

        lightSpecs.spawnParticles()
    }

    companion object {
        fun getSpiritData(entity: LivingEntity): Optional<MutableList<SpiritWithCount>> {
            val key = BuiltInRegistries.ENTITY_TYPE.getKey(entity.type)
            if (SpiritDataReloadListener.HAS_NO_DATA.contains(key)) {
                return Optional.empty()
            } else {
                val spiritData = SpiritDataReloadListener.SPIRIT_DATA[key]
                if (spiritData != null) {
                    return Optional.of(spiritData.dataEntries)
                }

                return when (entity.type.category) {
                    MobCategory.MONSTER -> Optional.of(SpiritDataReloadListener.DEFAULT_MONSTER_SPIRIT_DATA.dataEntries)
                    MobCategory.CREATURE -> Optional.of(SpiritDataReloadListener.DEFAULT_CREATURE_SPIRIT_DATA.dataEntries)
                    MobCategory.AMBIENT -> Optional.of(SpiritDataReloadListener.DEFAULT_AMBIENT_SPIRIT_DATA.dataEntries)
                    MobCategory.AXOLOTLS ->  Optional.of(SpiritDataReloadListener.DEFAULT_AXOLOTL_SPIRIT_DATA.dataEntries)
                    MobCategory.UNDERGROUND_WATER_CREATURE ->  Optional.of(SpiritDataReloadListener.DEFAULT_UNDERGROUND_WATER_CREATURE_SPIRIT_DATA.dataEntries)
                    MobCategory.WATER_CREATURE ->  Optional.of(SpiritDataReloadListener.DEFAULT_WATER_CREATURE_SPIRIT_DATA.dataEntries)
                    MobCategory.WATER_AMBIENT ->  Optional.of(SpiritDataReloadListener.DEFAULT_WATER_AMBIENT_SPIRIT_DATA.dataEntries)

                    else -> Optional.empty()
                }
            }
        }
    }
}