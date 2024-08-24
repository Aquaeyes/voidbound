package dev.sterner.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import dev.sterner.VoidBound
import dev.sterner.client.model.FociModel
import dev.sterner.client.model.WandItemModel
import dev.sterner.registry.VoidBoundWandFociRegistry
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry.DynamicItemRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f
import org.joml.Quaternionf
import team.lodestar.lodestone.handlers.RenderHandler
import team.lodestar.lodestone.systems.rendering.VFXBuilders
import java.awt.Color


class WandItemRenderer(val texture: String) : DynamicItemRenderer {

    var model: WandItemModel? = null

    var fociModel: FociModel? = null


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
        if (fociModel == null) {
            fociModel = FociModel(Minecraft.getInstance().entityModels.bakeLayer(FociModel.LAYER_LOCATION))
        }

        matrices.pushPose()

        matrices.translate(0f, 0.75f, 0f)

        matrices.translate(0.5, 0.65, 0.5)

        matrices.scale(1f, -1f, -1f)
        model?.renderToBuffer(
            matrices,
            vertexConsumers.getBuffer(RenderType.entityTranslucent(VoidBound.id("textures/item/$texture.png"))),
            light,
            overlay,
            1f,
            1f,
            1f,
            1f
        )

        val focusName = stack.tag?.getString("FocusName")
        val focus = VoidBoundWandFociRegistry.WAND_FOCUS.getOptional(focusName?.let { ResourceLocation.tryParse(it) })
        if (focus.isPresent) {

            fociModel?.renderToBuffer(
                matrices,
                vertexConsumers.getBuffer(
                    RenderType.entityTranslucent(
                        VoidBound.id(
                            "textures/models/${
                                ResourceLocation.tryParse(
                                    focusName!!
                                )!!.path
                            }.png"
                        )
                    )
                ),
                light,
                overlay,
                1f,
                1f,
                1f,
                1f
            )
        }

        matrices.popPose()
    }
}