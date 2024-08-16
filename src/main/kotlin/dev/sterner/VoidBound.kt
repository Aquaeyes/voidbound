package dev.sterner

import com.sammy.malum.common.events.MalumCodexEvents
import dev.emi.trinkets.api.client.TrinketRendererRegistry
import dev.sterner.api.ClientTickHandler
import dev.sterner.api.VoidBoundApi
import dev.sterner.client.event.MalumCodexEvent
import dev.sterner.client.event.SpiritAltarHudRenderEvent
import dev.sterner.client.model.*
import dev.sterner.client.renderer.HallowedMonocleRenderer
import dev.sterner.client.renderer.WandItemRenderer
import dev.sterner.client.renderer.blockentity.*
import dev.sterner.client.renderer.entity.*
import dev.sterner.client.screen.OsmoticEnchanterScreen
import dev.sterner.common.components.VoidBoundPlayerComponent
import dev.sterner.listener.EnchantSpiritDataReloadListener
import dev.sterner.registry.*
import io.github.fabricators_of_create.porting_lib.event.common.BlockEvents
import io.github.fabricators_of_create.porting_lib.event.common.BlockEvents.BreakEvent
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.*
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import org.slf4j.LoggerFactory


object VoidBound : ModInitializer, ClientModInitializer {

    const val modid: String = "voidbound"
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
        VoidBoundMenuTypeRegistry.MENU_TYPES.register()

        VoidBoundCreativeTabRegistry.init()

        ResourceManagerHelper.get(PackType.SERVER_DATA)
            .registerReloadListener(EnchantSpiritDataReloadListenerFabricImpl())

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
        BlockEntityRenderers.register(
            VoidBoundBlockEntityTypeRegistry.OSMOTIC_ENCHANTER.get(),
            ::OsmoticEnchanterBlockEntityRenderer
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
        EntityRendererRegistry.register(
            VoidBoundEntityTypeRegistry.CRIMSON_ARCHER_ENTITY.get(),
            ::CrimsonArcherEntityRenderer
        )
        EntityRendererRegistry.register(
            VoidBoundEntityTypeRegistry.CRIMSON_CLERIC_ENTITY.get(),
            ::CrimsonClericEntityRenderer
        )
        EntityRendererRegistry.register(
            VoidBoundEntityTypeRegistry.CRIMSON_NECROMANCER_ENTITY.get(),
            ::CrimsonNecromancerEntityRenderer
        )
        EntityRendererRegistry.register(
            VoidBoundEntityTypeRegistry.CRIMSON_JESTER_ENTITY.get(),
            ::CrimsonJesterEntityRenderer
        )
        EntityRendererRegistry.register(
            VoidBoundEntityTypeRegistry.CRIMSON_HEAVY_KNIGHT_ENTITY.get(),
            ::CrimsonHeavyKnightEntityRenderer
        )

        EntityModelLayerRegistry.registerModelLayer(
            SoulSteelGolemEntityModel.LAYER_LOCATION,
            SoulSteelGolemEntityModel::createBodyLayer
        )

        EntityModelLayerRegistry.registerModelLayer(GolemCoreModel.LAYER_LOCATION, GolemCoreModel::createBodyLayer)
        EntityModelLayerRegistry.registerModelLayer(FociModel.LAYER_LOCATION, FociModel::createBodyLayer)
        EntityModelLayerRegistry.registerModelLayer(WandItemModel.LAYER_LOCATION) { WandItemModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(CrimsonKnightModel.LAYER_LOCATION) { CrimsonKnightModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(CrimsonArcherModel.LAYER_LOCATION) { CrimsonArcherModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(CrimsonClericModel.LAYER_LOCATION) { CrimsonClericModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(CrimsonJesterModel.LAYER_LOCATION) { CrimsonJesterModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(CrimsonHeavyKnightModel.LAYER_LOCATION) { CrimsonHeavyKnightModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(CrimsonNecromancerModel.LAYER_LOCATION) { CrimsonNecromancerModel.createBodyLayer() }

        EntityModelLayerRegistry.registerModelLayer(CrimsonBookModel.LAYER_LOCATION) { CrimsonBookModel.createBodyLayer() }

        //ArmorRenderer.register(HallowedGogglesRenderer(), VoidBoundItemRegistry.HALLOWED_GOGGLES.get())
        TrinketRendererRegistry.registerRenderer(
            VoidBoundItemRegistry.HALLOWED_MONOCLE.get(),
            HallowedMonocleRenderer()
        )

        ItemProperties.register(
            VoidBoundItemRegistry.CALL_OF_THE_VOID.get(),
            id("glowing")
        ) { itemStack, _, _, _ ->
            val v =
                if (itemStack.tag != null && itemStack.tag!!.contains("Glowing") && itemStack.tag!!.getBoolean("Glowing")) 1f else 0f
            return@register v
        }

        MalumCodexEvents.EVENT.register(MalumCodexEvent::addVoidBoundEntries)
        MalumCodexEvents.VOID_EVENT.register(MalumCodexEvent::addVoidBoundVoidEntries)
        WorldRenderEvents.AFTER_TRANSLUCENT.register(VoidBoundPlayerComponent.Companion::renderCubeAtPos)
        HudRenderCallback.EVENT.register(SpiritAltarHudRenderEvent::spiritAltarRecipeHud)
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickHandler::clientTickEnd)

        BuiltinItemRendererRegistry.INSTANCE.register(
            VoidBoundItemRegistry.HALLOWED_GOLD_CAPPED_RUNEWOOD_WAND.get(),
            WandItemRenderer()
        )

        MenuScreens.register(VoidBoundMenuTypeRegistry.OSMOTIC_ENCHANTER.get(), ::OsmoticEnchanterScreen)
    }

    fun id(name: String): ResourceLocation {
        return ResourceLocation(modid, name)
    }


    class EnchantSpiritDataReloadListenerFabricImpl : EnchantSpiritDataReloadListener(),
        IdentifiableResourceReloadListener {
        override fun getFabricId(): ResourceLocation {
            return id("enchanting_data")
        }
    }
}