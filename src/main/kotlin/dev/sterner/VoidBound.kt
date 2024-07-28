package dev.sterner

import com.sammy.malum.client.screen.codex.BookWidgetStyle
import com.sammy.malum.client.screen.codex.PlacedBookEntry
import com.sammy.malum.client.screen.codex.pages.text.HeadlineTextPage
import com.sammy.malum.client.screen.codex.screens.ArcanaProgressionScreen
import com.sammy.malum.common.events.MalumCodexEvents
import dev.sterner.client.*
import dev.sterner.client.model.SoulSteelGolemEntityModel
import dev.sterner.entity.SoulSteelGolemEntity
import dev.sterner.registry.*
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.client.model.geom.ModelLayers
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

    override fun onInitialize() {

        VoidBoundTags.init()
        VoidBoundPacketRegistry.registerVoidBoundPackets()

        VoidBoundItemRegistry.ITEMS.register()
        VoidBoundBlockRegistry.BLOCKS.register()
        VoidBoundBlockEntityTypeRegistry.BLOCK_ENTITY_TYPES.register()
        VoidBoundEntityTypeRegistry.ENTITY_TYPES.register()
        VoidBoundParticleTypeRegistry.PARTICLES.register()
        VoidBoundCreativeTabRegistry.init()

        FabricDefaultAttributeRegistry.register(VoidBoundEntityTypeRegistry.SOUL_STEEL_GOLEM_ENTITY.get(), SoulSteelGolemEntity.createGolemAttributes())

        MalumCodexEvents.EVENT.register(this::addVoidBoundEntries)
    }

    private fun addVoidBoundEntries(screen: ArcanaProgressionScreen?, entries: MutableList<PlacedBookEntry>?) {
        screen?.addEntry("call_of_the_void", 0, 12) {
            it.configureWidget {
                it.setIcon(VoidBoundItemRegistry.CALL_OF_THE_VOID.get()).setStyle(VOID_GILDED)
            }.addPage(HeadlineTextPage("call_of_the_void", "call_of_the_void.1"))
        }
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
        EntityRendererRegistry.register(VoidBoundEntityTypeRegistry.SOUL_STEEL_GOLEM_ENTITY.get(), ::SoulSteelGolemEntityRenderer)

        EntityModelLayerRegistry.registerModelLayer(SoulSteelGolemEntityModel.LAYER_LOCATION, SoulSteelGolemEntityModel::createBodyLayer)

        ItemProperties.register(
            VoidBoundItemRegistry.CALL_OF_THE_VOID.get(),
            id("glowing")
        ) { itemStack, _, _, _ ->
            val v =
                if (itemStack.tag != null && itemStack.tag!!.contains("Glowing") && itemStack.tag!!.getBoolean("Glowing")) 1f else 0f
            return@register v
        }
    }

    fun id(name: String): ResourceLocation {
        return ResourceLocation(modid, name)
    }
}