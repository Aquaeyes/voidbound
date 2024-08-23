package dev.sterner

import com.mojang.blaze3d.vertex.PoseStack
import com.sammy.malum.client.MalumModelLoaderPlugin
import dev.emi.trinkets.api.client.TrinketRendererRegistry
import dev.sterner.api.util.VoidBoundRenderUtils
import dev.sterner.api.util.VoidBoundRenderUtils.renderType
import dev.sterner.client.VoidBoundModelLoaderPlugin
import dev.sterner.client.renderer.HallowedMonocleRenderer
import dev.sterner.client.renderer.WandItemRenderer
import dev.sterner.client.screen.OsmoticEnchanterScreen
import dev.sterner.listener.EnchantSpiritDataReloadListenerFabricImpl
import dev.sterner.registry.*
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f
import org.slf4j.LoggerFactory
import team.lodestar.lodestone.handlers.RenderHandler
import team.lodestar.lodestone.systems.rendering.VFXBuilders
import java.awt.Color


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
        VoidBoundEvents.init()
        VoidBoundWorldGenerations.init()

        ResourceManagerHelper.get(PackType.SERVER_DATA)
            .registerReloadListener(EnchantSpiritDataReloadListenerFabricImpl())
    }

    override fun onInitializeClient() {

        VoidBoundShaders.init()
        VoidBoundParticleTypeRegistry.init()
        VoidBoundKeyBindings.init()
        VoidBoundEntityRenderers.init()
        VoidBoundModelLayers.init()
        VoidBoundEvents.clientInit()

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

        BuiltinItemRendererRegistry.INSTANCE.register(
            VoidBoundItemRegistry.HALLOWED_GOLD_CAPPED_RUNEWOOD_WAND.get(),
            WandItemRenderer("hallowed_gold_capped_runewood_wand")
        )
        BuiltinItemRendererRegistry.INSTANCE.register(
            VoidBoundItemRegistry.SOUL_STAINED_STEEL_CAPPED_SOULWOOD_WAND.get(),
            WandItemRenderer("soul_stained_steel_capped_soulwood_wand")
        )

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
            VoidBoundBlockRegistry.TEAR_OF_ENDER.get(),
            VoidBoundBlockRegistry.TEAR_OF_CRIMSON.get(),
            VoidBoundBlockRegistry.TEAR_CLOAK.get(),
            VoidBoundBlockRegistry.OSMOTIC_ENCHANTER.get()
        )

        ModelLoadingPlugin.register(VoidBoundModelLoaderPlugin)

        MenuScreens.register(VoidBoundMenuTypeRegistry.OSMOTIC_ENCHANTER.get(), ::OsmoticEnchanterScreen)

        WorldRenderEvents.AFTER_TRANSLUCENT.register(::renderBeamRTest)
    }

    private fun renderBeamRTest(worldRenderContext: WorldRenderContext) {
        val matrices = worldRenderContext.matrixStack()
        val hit: HitResult? = Minecraft.getInstance().cameraEntity?.pick(10.0, 1f, false)
        if (hit?.type == HitResult.Type.BLOCK) {
            matrices.pushPose()

            //renderBeam(matrices, hit.location.x, hit.location.y, hit.location.z)
            matrices.popPose()
        }

    }
    private fun renderBeam(matrices: PoseStack, x: Double, y: Double, z: Double) {

        val builder = VFXBuilders.createWorld()
        builder.replaceBufferSource(RenderHandler.LATE_DELAYED_RENDER.target)
            .setRenderType(renderType)
            .setColor(Color(255, 255, 255))
            .setAlpha(1.0f)

        matrices.pushPose()

        val camera = Minecraft.getInstance().gameRenderer.mainCamera
        matrices.translate(-camera.position.x, -camera.position.y, -camera.position.z)

        val matrix4f: Matrix4f = matrices.last().pose()

        val entity = Minecraft.getInstance().player!!

        val startPos = Vec3(entity.x + 0.1, entity.eyeY - 0.2,  entity.z)
        val endPos = Vec3(x, y , z)

        builder.renderBeam(matrix4f, startPos, endPos, 1f, camera.position)

        matrices.popPose()
    }

    fun id(name: String): ResourceLocation {
        return ResourceLocation(modid, name)
    }
}