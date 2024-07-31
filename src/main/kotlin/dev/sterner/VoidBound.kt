package dev.sterner

import com.sammy.malum.client.screen.codex.BookWidgetStyle
import com.sammy.malum.client.screen.codex.PlacedBookEntry
import com.sammy.malum.client.screen.codex.pages.recipe.SpiritInfusionPage
import com.sammy.malum.client.screen.codex.pages.text.HeadlineTextPage
import com.sammy.malum.client.screen.codex.screens.ArcanaProgressionScreen
import com.sammy.malum.common.events.MalumCodexEvents
import com.sammy.malum.registry.common.item.ItemRegistry
import dev.sterner.client.model.GolemCoreModel
import dev.sterner.client.model.SoulSteelGolemEntityModel
import dev.sterner.client.renderer.*
import dev.sterner.common.components.VoidBoundPlayerComponent
import dev.sterner.common.entity.SoulSteelGolemEntity
import dev.sterner.common.event.MalumCodexEvent
import dev.sterner.registry.*
import io.github.fabricators_of_create.porting_lib.event.client.InteractEvents
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
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


    val VOID_GILDED: BookWidgetStyle = BookWidgetStyle(
        BookWidgetStyle.WidgetStylePreset(modid, "void_frame"),
        BookWidgetStyle.WidgetStylePreset("dark_filling"),
        BookWidgetStyle.WidgetDesignType.GILDED
    )

    val MOD_GILDED: BookWidgetStyle = BookWidgetStyle(
        BookWidgetStyle.WidgetStylePreset(modid, "void_frame"),
        BookWidgetStyle.WidgetStylePreset("paper_filling"),
        BookWidgetStyle.WidgetDesignType.GILDED
    )

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

        VoidBoundCreativeTabRegistry.init()

        FabricDefaultAttributeRegistry.register(
            VoidBoundEntityTypeRegistry.SOUL_STEEL_GOLEM_ENTITY.get(),
            SoulSteelGolemEntity.createGolemAttributes()
        )

        MalumCodexEvents.EVENT.register(MalumCodexEvent::addVoidBoundEntries)
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
            ::DestabilizedSpiritRiftBlockEntityRenderer
        )

        EntityRendererRegistry.register(VoidBoundEntityTypeRegistry.PARTICLE_ENTITY.get(), ::ParticleEntityRenderer)
        EntityRendererRegistry.register(
            VoidBoundEntityTypeRegistry.SOUL_STEEL_GOLEM_ENTITY.get(),
            ::SoulSteelGolemEntityRenderer
        )

        EntityModelLayerRegistry.registerModelLayer(
            SoulSteelGolemEntityModel.LAYER_LOCATION,
            SoulSteelGolemEntityModel::createBodyLayer
        )
        EntityModelLayerRegistry.registerModelLayer(GolemCoreModel.LAYER_LOCATION, GolemCoreModel::createBodyLayer)

        ItemProperties.register(
            VoidBoundItemRegistry.CALL_OF_THE_VOID.get(),
            id("glowing")
        ) { itemStack, _, _, _ ->
            val v =
                if (itemStack.tag != null && itemStack.tag!!.contains("Glowing") && itemStack.tag!!.getBoolean("Glowing")) 1f else 0f
            return@register v
        }

        WorldRenderEvents.AFTER_TRANSLUCENT.register(VoidBoundPlayerComponent.Companion::renderCubeAtPos)
    }

    fun id(name: String): ResourceLocation {
        return ResourceLocation(modid, name)
    }
}