package dev.sterner.client.model

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import dev.sterner.VoidBound
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
import net.minecraft.util.Mth
import java.util.function.Function


class CrimsonBookModel(modelPart: ModelPart) :
    Model(Function<ResourceLocation, RenderType> { location: ResourceLocation ->
        RenderType.entitySolid(
            location
        )
    }) {
    private val root: ModelPart = modelPart.getChild("root")
    private val flipPage2: ModelPart = root.getChild("flipPage2")
    private val rightPages: ModelPart = root.getChild("rightPages")
    private val flipPage1: ModelPart = root.getChild("flipPage1")
    private val leftPages: ModelPart = root.getChild("leftPages")
    private val leftLid: ModelPart = root.getChild("leftLid")
    private val rightLid: ModelPart = root.getChild("rightLid")

    fun setupAnim(time: Float, rightPageFlipAmount: Float, leftPageFlipAmount: Float, bookOpenAmount: Float) {
        val f = (Mth.sin(time * 0.02f) * 0.1f + 1.25f) * bookOpenAmount
        leftLid.zRot = -f
        rightLid.zRot = f
        leftPages.zRot = -f
        rightPages.zRot = f
        flipPage1.zRot = f - 0.2f
        flipPage2.zRot = -f + 0.2f
        leftPages.x = Mth.sin(f)
        rightPages.x = Mth.sin(f)
        flipPage1.x = Mth.sin(f)
        flipPage2.x = Mth.sin(f)
    }

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
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
    }

    companion object {
        // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
        val LAYER_LOCATION: ModelLayerLocation =
            ModelLayerLocation(VoidBound.id("crimson_rites"), "main")

        fun createBodyLayer(): LayerDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root

            val root = partdefinition.addOrReplaceChild(
                "root",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0f, 24.0f, -4.0f, -1.5708f, 0.0f, 0.0f)
            )

            val flipPage2 = root.addOrReplaceChild(
                "flipPage2",
                CubeListBuilder.create().texOffs(24, 19)
                    .addBox(0.0f, -7.0f, -5.5f, 0.0f, 7.0f, 11.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, -1.0f, 0.0f)
            )

            val rightPages = root.addOrReplaceChild(
                "rightPages",
                CubeListBuilder.create().texOffs(28, 0)
                    .addBox(0.0f, -7.0f, 2.5f, 1.0f, 7.0f, 11.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, -1.0f, -8.0f)
            )

            val flipPage1 = root.addOrReplaceChild(
                "flipPage1",
                CubeListBuilder.create().texOffs(24, 26)
                    .addBox(0.0f, -7.0f, -5.5f, 0.0f, 7.0f, 11.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, -1.0f, 0.0f)
            )

            val leftPages = root.addOrReplaceChild(
                "leftPages",
                CubeListBuilder.create().texOffs(0, 21)
                    .addBox(-1.0f, -7.0f, -5.5f, 1.0f, 7.0f, 11.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, -1.0f, 0.0f)
            )

            val leftLid = root.addOrReplaceChild(
                "leftLid",
                CubeListBuilder.create().texOffs(34, 32)
                    .addBox(-1.0f, 0.0f, -6.0f, 1.0f, 1.0f, 12.0f, CubeDeformation(0.0f))
                    .texOffs(0, 0).addBox(-2.0f, -8.0f, -6.0f, 1.0f, 9.0f, 12.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, -1.0f, 0.0f)
            )

            val rightLid = root.addOrReplaceChild(
                "rightLid",
                CubeListBuilder.create().texOffs(0, 39)
                    .addBox(0.0f, 0.0f, 2.0f, 1.0f, 1.0f, 12.0f, CubeDeformation(0.0f))
                    .texOffs(14, 9).addBox(1.0f, -8.0f, 2.0f, 1.0f, 9.0f, 12.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, -1.0f, -8.0f)
            )

            return LayerDefinition.create(meshdefinition, 64, 64)
        }
    }
}