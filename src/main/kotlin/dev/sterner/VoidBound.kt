package dev.sterner

import dev.sterner.client.DestabilizedSpiritRiftBlockEntityRenderer
import dev.sterner.client.ParticleEntityRenderer
import dev.sterner.client.SpiritBinderBlockEntityRenderer
import dev.sterner.client.SpiritStabilizerBlockEntityRenderer
import dev.sterner.registry.*
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.client.model.FabricModelPredicateProviderRegistry
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.resources.ResourceLocation
import org.slf4j.LoggerFactory

object VoidBound : ModInitializer, ClientModInitializer {
    val modid: String = "voidbound"
    private val logger = LoggerFactory.getLogger(modid)

    override fun onInitialize() {

        VoidBoundTags.init()

        VoidBoundItemRegistry.ITEMS.register()
        VoidBoundBlockRegistry.BLOCKS.register()
        VoidBoundBlockEntityTypeRegistry.BLOCK_ENTITY_TYPES.register()
        VoidBoundEntityTypeRegistry.ENTITY_TYPES.register()
        VoidBoundParticleTypeRegistry.PARTICLES.register()
        VoidBoundCreativeTabRegistry.init()


    }

    override fun onInitializeClient() {

        VoidBoundShaders.init()
        VoidBoundParticleTypeRegistry.registerParticleFactory()

        BlockEntityRenderers.register(VoidBoundBlockEntityTypeRegistry.SPIRIT_BINDER.get(), ::SpiritBinderBlockEntityRenderer)
        BlockEntityRenderers.register(VoidBoundBlockEntityTypeRegistry.SPIRIT_BINDER_STABILIZER.get(), ::SpiritStabilizerBlockEntityRenderer)
        BlockEntityRenderers.register(VoidBoundBlockEntityTypeRegistry.DESTABILIZED_SPIRIT_RIFT.get(), ::DestabilizedSpiritRiftBlockEntityRenderer)

        EntityRendererRegistry.register(VoidBoundEntityTypeRegistry.PARTICLE_ENTITY.get(), ::ParticleEntityRenderer)

        FabricModelPredicateProviderRegistry.register(
            VoidBoundItemRegistry.CALL_OF_THE_VOID.get(),
            id("glowing")
        ) { itemStack, _, _, _ ->
            val v = if(itemStack.tag != null && itemStack.tag!!.contains("Glowing") && itemStack.tag!!.getBoolean("Glowing")) 1f else 0f
            return@register v
        }

        /*
            ModelPredicateProviderRegistry.register(EXAMPLE_BOW, Identifier.ofVanilla("pulling"), (itemStack, clientWorld, livingEntity) -> {
      if (livingEntity == null) {
        return 0.0F;
      }
      return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
    });
         */
    }

    fun id(name: String): ResourceLocation {
        return ResourceLocation(modid, name)
    }
}