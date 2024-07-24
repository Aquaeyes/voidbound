package dev.sterner.registry

import com.sammy.malum.client.particles.cut.ScytheAttackParticle
import com.sammy.malum.client.particles.spiritflame.SpiritFlameParticleType
import com.sammy.malum.registry.client.ParticleRegistry
import dev.sterner.VoidBound
import dev.sterner.client.particle.RiftParticleType
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import io.github.fabricators_of_create.porting_lib.util.RegistryObject
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.minecraft.core.registries.BuiltInRegistries
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType

object VoidBoundParticleTypeRegistry {
    val PARTICLES = LazyRegistrar.create(BuiltInRegistries.PARTICLE_TYPE, VoidBound.modid)


    var RIFT_PARTICLE: RegistryObject<RiftParticleType> = PARTICLES.register(
        "rift"
    ) { RiftParticleType() }


    fun registerParticleFactory() {
        ParticleFactoryRegistry.getInstance().register(
            RIFT_PARTICLE.get()
        ) { sprite: FabricSpriteProvider? ->
            RiftParticleType.Factory(
                sprite!!
            )
        }
    }
}