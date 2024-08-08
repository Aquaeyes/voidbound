package dev.sterner.client.model

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import dev.sterner.VoidBound
import dev.sterner.common.entity.CrimsonArcherEntity
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.resources.ResourceLocation


class CrimsonArcherModel(root: ModelPart) : HumanoidModel<CrimsonArcherEntity>(root) {
    private val left_arm: ModelPart = root.getChild("left_arm")
    private val right_arm: ModelPart = root.getChild("right_arm")
    private val body: ModelPart = root.getChild("body")
    private val head: ModelPart = root.getChild("head")
    private val right_leg: ModelPart = root.getChild("right_leg")
    private val left_leg: ModelPart = root.getChild("left_leg")

    override fun setupAnim(
        entity: CrimsonArcherEntity,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
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
        left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
    }

    companion object {
        // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
        val LAYER_LOCATION: ModelLayerLocation =
            ModelLayerLocation(VoidBound.id("crimson_archer"), "main")

        fun createBodyLayer(): LayerDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root

            val left_arm = partdefinition.addOrReplaceChild(
                "left_arm",
                CubeListBuilder.create().texOffs(32, 48)
                    .addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offset(5.0f, 2.0f, 0.0f)
            )

            val LeftElbowPad = left_arm.addOrReplaceChild(
                "LeftElbowPad",
                CubeListBuilder.create().texOffs(59, 47).mirror()
                    .addBox(2.5f, 0.5f, -2.5f, 1.0f, 4.0f, 5.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val LeftShoulderPad = left_arm.addOrReplaceChild(
                "LeftShoulderPad",
                CubeListBuilder.create().texOffs(59, 40).mirror()
                    .addBox(-1.5f, -2.5f, -2.5f, 5.0f, 2.0f, 5.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val right_arm = partdefinition.addOrReplaceChild(
                "right_arm",
                CubeListBuilder.create().texOffs(40, 16)
                    .addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offset(-5.0f, 2.0f, 0.0f)
            )

            val RightShoulderPad = right_arm.addOrReplaceChild(
                "RightShoulderPad",
                CubeListBuilder.create().texOffs(59, 40)
                    .addBox(-3.5f, -2.5f, -2.5f, 5.0f, 2.0f, 5.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val RightElbowPad = right_arm.addOrReplaceChild(
                "RightElbowPad",
                CubeListBuilder.create().texOffs(59, 47)
                    .addBox(-3.5f, 0.5f, -2.5f, 1.0f, 4.0f, 5.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val body = partdefinition.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(16, 16)
                    .addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val ChestArmor = body.addOrReplaceChild(
                "ChestArmor",
                CubeListBuilder.create().texOffs(59, 27)
                    .addBox(-4.5f, -0.4f, -2.4f, 9.0f, 8.0f, 5.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val Leggings = body.addOrReplaceChild(
                "Leggings",
                CubeListBuilder.create().texOffs(0, 0)
                    .addBox(0.5f, 9.6f, -2.8f, 3.0f, 2.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val ClothThing = Leggings.addOrReplaceChild(
                "ClothThing",
                CubeListBuilder.create().texOffs(0, 32)
                    .addBox(-5.0f, -0.5f, -2.5f, 8.0f, 3.0f, 5.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-1.0f, 11.5f, 0.0f, 0.0f, 0.0f, -0.5236f)
            )

            val Cape = body.addOrReplaceChild(
                "Cape",
                CubeListBuilder.create().texOffs(0, 50)
                    .addBox(-3.5f, 0.0f, 0.0f, 7.0f, 14.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 2.0f, 0.0873f, 0.0f, 0.0f)
            )

            val head = partdefinition.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val HoodMain = head.addOrReplaceChild(
                "HoodMain",
                CubeListBuilder.create().texOffs(64, 0)
                    .addBox(-4.5f, -8.5f, -3.7f, 9.0f, 9.0f, 8.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0873f, 0.0f, 0.0f)
            )

            val HoodBack = head.addOrReplaceChild(
                "HoodBack",
                CubeListBuilder.create().texOffs(76, 17)
                    .addBox(-4.0f, -8.5f, 2.3f, 8.0f, 7.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -0.1396f, 0.0f, 0.0f)
            )

            val right_leg = partdefinition.addOrReplaceChild(
                "right_leg",
                CubeListBuilder.create().texOffs(0, 16)
                    .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offset(-1.9f, 12.0f, 0.0f)
            )

            val RightKneePad = right_leg.addOrReplaceChild(
                "RightKneePad",
                CubeListBuilder.create().texOffs(0, 4)
                    .addBox(-1.5f, 3.5f, -2.5f, 3.0f, 3.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val RightBoot = right_leg.addOrReplaceChild(
                "RightBoot",
                CubeListBuilder.create().texOffs(0, 40)
                    .addBox(-2.5f, 7.0f, -2.5f, 5.0f, 5.0f, 5.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val left_leg = partdefinition.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create().texOffs(0, 16).mirror()
                    .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offset(1.9f, 12.0f, 0.0f)
            )

            val LEftBoot = left_leg.addOrReplaceChild(
                "LEftBoot",
                CubeListBuilder.create().texOffs(0, 40)
                    .addBox(-2.5f, 7.0f, -2.5f, 5.0f, 5.0f, 5.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val RightKneePad_1 = left_leg.addOrReplaceChild(
                "RightKneePad_1",
                CubeListBuilder.create().texOffs(0, 4)
                    .addBox(-1.5f, 3.5f, -2.5f, 3.0f, 3.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            return LayerDefinition.create(meshdefinition, 128, 128)
        }
    }
}