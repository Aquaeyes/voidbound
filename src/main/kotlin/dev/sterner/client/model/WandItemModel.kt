package dev.sterner.client.model

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.Model
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation


class WandItemModel(root: ModelPart) : Model(RenderType::entitySolid) {
    private val bone: ModelPart = root.getChild("bone")

    override fun renderToBuffer(
        poseStack: PoseStack,
        vertexConsumer: VertexConsumer,
        packedLight: Int,
        packedOverlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
    }

    companion object {
        // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
        val LAYER_LOCATION: ModelLayerLocation =
            ModelLayerLocation(ResourceLocation("modid", "rune_wood_hallowed_gold_capped_wand_converted"), "main")

        fun createBodyLayer(): LayerDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root

            val bone = partdefinition.addOrReplaceChild(
                "bone",
                CubeListBuilder.create().texOffs(16, 0)
                    .addBox(-9.0f, -16.0f, 7.0f, 2.0f, 16.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(4, 10).addBox(-9.5f, -2.0f, 6.5f, 3.0f, 3.0f, 3.0f, CubeDeformation(0.0f))
                    .texOffs(4, 10).addBox(-9.5f, -19.0f, 6.5f, 3.0f, 3.0f, 3.0f, CubeDeformation(0.0f))
                    .texOffs(4, 10).addBox(-9.5f, -15.0f, 6.5f, 3.0f, 1.0f, 3.0f, CubeDeformation(0.0f)),
                PartPose.offset(8.0f, 24.0f, -8.0f)
            )

            return LayerDefinition.create(meshdefinition, 32, 32)
        }
    }
}