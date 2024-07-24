package dev.sterner

import dev.sterner.client.DestabilizedSpiritRiftBlockEntityRenderer
import dev.sterner.client.ParticleEntityRenderer
import dev.sterner.client.SpiritBinderBlockEntityRenderer
import dev.sterner.client.SpiritStabilizerBlockEntityRenderer
import dev.sterner.registry.*
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.resources.ResourceLocation
import org.slf4j.LoggerFactory

object VoidBound : ModInitializer, ClientModInitializer {
    val modid: String = "voidbound"
    private val logger = LoggerFactory.getLogger(modid)

    override fun onInitialize() {

        VoidBoundItemRegistry.ITEMS.register()
        VoidBoundBlockRegistry.BLOCKS.register()
        VoidBoundBlockEntityTypeRegistry.BLOCK_ENTITY_TYPES.register()
        VoidBoundEntityTypeRegistry.ENTITY_TYPES.register()

        VoidBoundCreativeTabRegistry.init()
    }

    override fun onInitializeClient() {

        VoidBoundShaders.init()

        BlockEntityRenderers.register(VoidBoundBlockEntityTypeRegistry.SPIRIT_BINDER.get(), ::SpiritBinderBlockEntityRenderer)
        BlockEntityRenderers.register(VoidBoundBlockEntityTypeRegistry.SPIRIT_BINDER_STABILIZER.get(), ::SpiritStabilizerBlockEntityRenderer)
        BlockEntityRenderers.register(VoidBoundBlockEntityTypeRegistry.DESTABILIZED_SPIRIT_RIFT.get(), ::DestabilizedSpiritRiftBlockEntityRenderer)

        EntityRendererRegistry.register(VoidBoundEntityTypeRegistry.PARTICLE_ENTITY.get(), ::ParticleEntityRenderer)
    }

    fun id(name: String): Any {
        return ResourceLocation(modid, name)
    }
}