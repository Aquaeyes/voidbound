package dev.sterner.api.util

import com.sammy.malum.core.listeners.SpiritDataReloadListener
import com.sammy.malum.core.systems.recipe.SpiritWithCount
import com.sammy.malum.core.systems.spirit.MalumSpiritType
import com.sammy.malum.visual_effects.SpiritLightSpecs
import dev.sterner.VoidBound
import dev.sterner.listener.EnchantSpiritDataReloadListener
import net.minecraft.advancements.Advancement
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
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

    fun grantAdvancementCriterion(
        serverPlayerEntity: ServerPlayer,
        advancementIdentifier: ResourceLocation,
        criterion: String
    ) {
        if (serverPlayerEntity.getServer() == null) {
            return
        }
        val sal = serverPlayerEntity.getServer()!!.advancements
        val tracker = serverPlayerEntity.advancements

        val advancement: Advancement? = sal.getAdvancement(advancementIdentifier)
        if (advancement != null) {
            if (!tracker.getOrStartProgress(advancement).isDone) {
                tracker.award(advancement, criterion)
            }
        }
    }

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

    fun spawnSpiritParticle(level: ClientLevel, to: Vec3, from: Vec3, yOffset: Float, type: MalumSpiritType, randStart: Boolean) {
        val behavior =
            Consumer<WorldParticleBuilder> { b: WorldParticleBuilder ->
                b.addTickActor { p: LodestoneWorldParticle ->
                    val particlePos = Vec3(p.x, p.y, p.z)
                    val direction = to.subtract(particlePos).normalize()
                    p.particleSpeed = direction.scale(0.05)
                }
            }
        val rand = level.random

        val xRand: Double?
        val yRand: Double?
        val zRand: Double?

        if (randStart) {
            xRand = from.x + rand.nextDouble() - 0.5
            yRand = from.y + yOffset + (rand.nextDouble() - 0.5)
            zRand = from.z + rand.nextDouble() - 0.5
        } else {
            xRand = from.x - 0.5
            yRand = from.y + yOffset - 0.5
            zRand = from.z - 0.5
        }

        val startPos = Vec3(xRand, yRand, zRand)

        val distance = startPos.distanceTo(Vec3(to.x + 0.5, to.y + 1.5, to.z + 0.5))
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
        val reg = BuiltInRegistries.ENCHANTMENT.getKey(enchantment)
        val list: EnchantSpiritDataReloadListener.EnchantingData? = EnchantSpiritDataReloadListener.ENCHANTING_DATA[reg]

        return list?.texture ?: VoidBound.id(
            "textures/gui/enchantment/${
                BuiltInRegistries.ENCHANTMENT.getKey(
                    enchantment
                )?.path
            }.png"
        )
    }
}