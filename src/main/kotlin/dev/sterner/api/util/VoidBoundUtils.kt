package dev.sterner.api.util

import com.sammy.malum.core.listeners.SpiritDataReloadListener
import com.sammy.malum.core.systems.recipe.SpiritWithCount
import com.sammy.malum.core.systems.spirit.MalumSpiritType
import com.sammy.malum.visual_effects.SpiritLightSpecs
import dev.sterner.VoidBound
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.phys.Vec3
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder
import team.lodestar.lodestone.systems.particle.data.GenericParticleData
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier

object VoidBoundUtils {

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
                MobCategory.AXOLOTLS -> Optional.of(SpiritDataReloadListener.DEFAULT_AXOLOTL_SPIRIT_DATA.dataEntries)
                MobCategory.UNDERGROUND_WATER_CREATURE -> Optional.of(SpiritDataReloadListener.DEFAULT_UNDERGROUND_WATER_CREATURE_SPIRIT_DATA.dataEntries)
                MobCategory.WATER_CREATURE -> Optional.of(SpiritDataReloadListener.DEFAULT_WATER_CREATURE_SPIRIT_DATA.dataEntries)
                MobCategory.WATER_AMBIENT -> Optional.of(SpiritDataReloadListener.DEFAULT_WATER_AMBIENT_SPIRIT_DATA.dataEntries)

                else -> Optional.empty()
            }
        }
    }

    fun spawnSpiritParticle(level: ClientLevel, blockPos: BlockPos, entity: LivingEntity, type: MalumSpiritType) {
        val behavior =
            Consumer<WorldParticleBuilder> { b: WorldParticleBuilder ->
                b.addTickActor { p: LodestoneWorldParticle ->
                    val particlePos = Vec3(p.x, p.y, p.z)
                    val targetPos =
                        Vec3(
                            blockPos.x + 0.5,
                            blockPos.y + 0.5,
                            blockPos.z + 0.5
                        )
                    val direction = targetPos.subtract(particlePos).normalize()
                    p.particleSpeed = direction.scale(0.05)
                }
            }
        val rand = level.random
        val xRand = entity.x + rand.nextDouble() - 0.5
        val yRand = entity.y + entity.bbHeight / 1.5 + (rand.nextDouble() - 0.5)
        val zRand = entity.z + rand.nextDouble() - 0.5

        val startPos = Vec3(xRand, yRand, zRand)

        val distance = startPos.distanceTo(Vec3(blockPos.x + 0.5, blockPos.y + 1.5, blockPos.z + 0.5))
        val baseLifetime = 10
        val speed = 0.05
        val adjustedLifetime = (distance / speed).toInt().coerceAtLeast(baseLifetime) - 20

        val lightSpecs: ParticleEffectSpawner =
            SpiritLightSpecs.spiritLightSpecs(level, Vec3(xRand, yRand, zRand), type)
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

    fun getEnchantmentIcon(enchantment: Enchantment): ResourceLocation {
        return VoidBound.id("textures/gui/enchantment/${BuiltInRegistries.ENCHANTMENT.getKey(enchantment)?.path}.png")
    }
}