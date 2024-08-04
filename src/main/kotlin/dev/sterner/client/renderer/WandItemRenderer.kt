package dev.sterner.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import com.sammy.malum.client.RenderUtils
import com.sammy.malum.client.renderer.block.MoteOfManaRenderer
import com.sammy.malum.common.block.storage.jar.SpiritJarBlockEntity
import dev.sterner.VoidBound
import dev.sterner.client.Tokens
import dev.sterner.client.model.WandItemModel
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry.DynamicItemRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.model.ShieldModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry
import team.lodestar.lodestone.systems.rendering.VFXBuilders

class WandItemRenderer : DynamicItemRenderer {

    var model: WandItemModel? = null

    override fun render(
        stack: ItemStack,
        mode: ItemDisplayContext,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int,
        overlay: Int
    ) {

        if (model == null) {
            model = WandItemModel(Minecraft.getInstance().entityModels.bakeLayer(WandItemModel.LAYER_LOCATION))
        }
        val cubeVertexData = RenderUtils.makeCubePositions(0.25f).applyWobble(0f, 0.35f, 0.01f)
        val builder = VFXBuilders.createWorld().setRenderType(LodestoneRenderTypeRegistry.ADDITIVE_TEXTURE.applyAndCache(Tokens.WARD_BORDER))
        //val builder2 = VFXBuilders.createWorld().setRenderType(LodestoneRenderTypeRegistry.ADDITIVE_TEXTURE.applyAndCache(Tokens.WARD_BORDER)).setColorRaw(0.9f,0.09f,0.9f)

        matrices.pushPose()

        matrices.translate(0f,0.75f,0f)
        //RenderUtils.drawCube(matrices, builder2, 1f, cubeVertexData)
        //RenderUtils.drawCube(matrices, builder, 1f, cubeVertexData)

        matrices.translate(0.5, 0.65, 0.5)
        matrices.scale(1f, -1f, -1f)
        model?.renderToBuffer(matrices, vertexConsumers.getBuffer(RenderType.entityTranslucent(VoidBound.id("textures/item/hallowed_gold_capped_runewood_wand.png"))), light, overlay, 1f,1f,1f,1f)
        matrices.translate(-0.5, -0.65, -0.5)
        //RenderUtils.drawCube(matrices, builder, 1f, cubeVertexData)
        matrices.popPose()
    }
}