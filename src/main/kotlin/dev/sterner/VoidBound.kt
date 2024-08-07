package dev.sterner

import com.sammy.malum.common.events.MalumCodexEvents
import dev.emi.trinkets.api.client.TrinketRendererRegistry
import dev.sterner.api.ClientTickHandler
import dev.sterner.client.event.MalumCodexEvent
import dev.sterner.client.event.SpiritAltarHudRenderEvent
import dev.sterner.client.model.CrimsonKnightModel
import dev.sterner.client.model.GolemCoreModel
import dev.sterner.client.model.SoulSteelGolemEntityModel
import dev.sterner.client.model.WandItemModel
import dev.sterner.client.renderer.*
import dev.sterner.client.renderer.blockentity.SpiritRiftBlockEntityRenderer
import dev.sterner.client.renderer.blockentity.PortableHoleBlockEntityRenderer
import dev.sterner.client.renderer.blockentity.SpiritBinderBlockEntityRenderer
import dev.sterner.client.renderer.blockentity.SpiritStabilizerBlockEntityRenderer
import dev.sterner.client.renderer.entity.CrimsonKnightEntityRenderer
import dev.sterner.client.renderer.entity.ParticleEntityRenderer
import dev.sterner.client.renderer.entity.SoulSteelGolemEntityRenderer
import dev.sterner.common.components.VoidBoundPlayerComponent
import dev.sterner.common.entity.AbstractGolemEntity
import dev.sterner.registry.*
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.*
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.resources.ResourceLocation
import org.slf4j.LoggerFactory


object VoidBound : ModInitializer, ClientModInitializer {

    val modid: String = "voidbound"
    private val logger = LoggerFactory.getLogger(modid)

    override fun onInitialize() {

        VoidBoundTags.init()
        VoidBoundPacketRegistry.registerVoidBoundPackets()

        VoidBoundItemRegistry.ITEMS.register()
        VoidBoundBlockRegistry.BLOCKS.register()
        VoidBoundBlockEntityTypeRegistry.BLOCK_ENTITY_TYPES.register()
        VoidBoundEntityTypeRegistry.ENTITY_TYPES.register()
        VoidBoundParticleTypeRegistry.PARTICLES.register()
        VoidBoundMemoryTypeRegistry.MEMORY_TYPES.register()
        VoidBoundSensorTypeRegistry.SENSOR_TYPES.register()
        VoidBoundWandFociRegistry.WAND_FOCI.register()
        VoidBoundRiftTypeRegistry.RIFT_TYPES.register()

        VoidBoundCreativeTabRegistry.init()

        FabricDefaultAttributeRegistry.register(
            VoidBoundEntityTypeRegistry.SOUL_STEEL_GOLEM_ENTITY.get(),
            AbstractGolemEntity.createGolemAttributes()
        )

        UseBlockCallback.EVENT.register(VoidBoundPlayerComponent.Companion::useBlock)
        UseEntityCallback.EVENT.register(VoidBoundPlayerComponent.Companion::useEntity)
    }

    override fun onInitializeClient() {

        VoidBoundShaders.init()
        VoidBoundParticleTypeRegistry.registerParticleFactory()

        BlockEntityRenderers.register(
            VoidBoundBlockEntityTypeRegistry.SPIRIT_BINDER.get(),
            ::SpiritBinderBlockEntityRenderer
        )
        BlockEntityRenderers.register(
            VoidBoundBlockEntityTypeRegistry.SPIRIT_BINDER_STABILIZER.get(),
            ::SpiritStabilizerBlockEntityRenderer
        )
        BlockEntityRenderers.register(
            VoidBoundBlockEntityTypeRegistry.DESTABILIZED_SPIRIT_RIFT.get(),
            ::SpiritRiftBlockEntityRenderer
        )
        BlockEntityRenderers.register(
            VoidBoundBlockEntityTypeRegistry.PORTABLE_HOLE.get(),
            ::PortableHoleBlockEntityRenderer
        )

        EntityRendererRegistry.register(VoidBoundEntityTypeRegistry.PARTICLE_ENTITY.get(), ::ParticleEntityRenderer)

        EntityRendererRegistry.register(
            VoidBoundEntityTypeRegistry.SOUL_STEEL_GOLEM_ENTITY.get(),
            ::SoulSteelGolemEntityRenderer
        )

        EntityRendererRegistry.register(
            VoidBoundEntityTypeRegistry.CRIMSON_KNIGHT_ENTITY.get(),
            ::CrimsonKnightEntityRenderer
        )

        EntityModelLayerRegistry.registerModelLayer(
            SoulSteelGolemEntityModel.LAYER_LOCATION,
            SoulSteelGolemEntityModel::createBodyLayer
        )

        EntityModelLayerRegistry.registerModelLayer(GolemCoreModel.LAYER_LOCATION, GolemCoreModel::createBodyLayer)
        EntityModelLayerRegistry.registerModelLayer(WandItemModel.LAYER_LOCATION) { WandItemModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(CrimsonKnightModel.LAYER_LOCATION) { CrimsonKnightModel.createBodyLayer() }

        //ArmorRenderer.register(HallowedGogglesRenderer(), VoidBoundItemRegistry.HALLOWED_GOGGLES.get())
        TrinketRendererRegistry.registerRenderer(VoidBoundItemRegistry.HALLOWED_MONOCLE.get(), HallowedMonocleRenderer())

        ItemProperties.register(
            VoidBoundItemRegistry.CALL_OF_THE_VOID.get(),
            id("glowing")
        ) { itemStack, _, _, _ ->
            val v =
                if (itemStack.tag != null && itemStack.tag!!.contains("Glowing") && itemStack.tag!!.getBoolean("Glowing")) 1f else 0f
            return@register v
        }

        MalumCodexEvents.EVENT.register(MalumCodexEvent::addVoidBoundEntries)
        WorldRenderEvents.AFTER_TRANSLUCENT.register(VoidBoundPlayerComponent.Companion::renderCubeAtPos)
        HudRenderCallback.EVENT.register(SpiritAltarHudRenderEvent::spiritAltarRecipeHud)
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickHandler::clientTickEnd)

        BuiltinItemRendererRegistry.INSTANCE.register(
            VoidBoundItemRegistry.HALLOWED_GOLD_CAPPED_RUNEWOOD_WAND.get(),
            WandItemRenderer()
        )
    }

    fun id(name: String): ResourceLocation {
        return ResourceLocation(modid, name)
    }
}