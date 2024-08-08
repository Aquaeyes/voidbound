package dev.sterner.client.model

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import dev.sterner.VoidBound
import dev.sterner.common.entity.CrimsonPaladinEntity
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


class CrimsonPaladinModel(root: ModelPart) : HumanoidModel<CrimsonPaladinEntity>(root) {
    private val head: ModelPart = root.getChild("head")
    private val right_arm: ModelPart = root.getChild("right_arm")
    private val right_leg: ModelPart = root.getChild("right_leg")
    private val left_leg: ModelPart = root.getChild("left_leg")
    private val left_arm: ModelPart = root.getChild("left_arm")
    private val body: ModelPart = root.getChild("body")

    override fun setupAnim(
        entity: CrimsonPaladinEntity,
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
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
    }

    companion object {
        // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
        val LAYER_LOCATION: ModelLayerLocation =
            ModelLayerLocation(VoidBound.id("crimson_paladin"), "main")

        fun createBodyLayer(): LayerDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root

            val head = partdefinition.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-6.5f, -13.0f, -6.5f, 13.0f, 13.0f, 13.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, -12.0f, 0.0f)
            )

            val RightHelmWing = head.addOrReplaceChild(
                "RightHelmWing",
                CubeListBuilder.create().texOffs(39, 0)
                    .addBox(-7.0f, -3.0f, 0.0f, 7.0f, 6.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-6.5f, -6.5f, 0.0f, 0.0f, 1.2217f, 0.0f)
            )

            val NeckArmorLeft = head.addOrReplaceChild(
                "NeckArmorLeft",
                CubeListBuilder.create().texOffs(0, 26).mirror()
                    .addBox(-1.3f, -0.6f, -6.0f, 10.0f, 3.0f, 12.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.2269f)
            )

            val LeftHelmWing = head.addOrReplaceChild(
                "LeftHelmWing",
                CubeListBuilder.create().texOffs(39, 0).mirror()
                    .addBox(0.0f, -3.0f, 0.0f, 7.0f, 6.0f, 0.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(6.5f, -6.5f, 0.0f, 0.0f, -1.2217f, 0.0f)
            )

            val NeckArmorRight = head.addOrReplaceChild(
                "NeckArmorRight",
                CubeListBuilder.create().texOffs(0, 26)
                    .addBox(-8.7f, -0.6f, -6.0f, 10.0f, 3.0f, 12.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.2269f)
            )

            val right_arm = partdefinition.addOrReplaceChild(
                "right_arm",
                CubeListBuilder.create().texOffs(78, 14)
                    .addBox(-4.5f, 0.0f, -3.0f, 6.0f, 18.0f, 6.0f, CubeDeformation(0.0f)),
                PartPose.offset(-8.4f, -11.0f, 1.0f)
            )

            val RightUpperArmPlate = right_arm.addOrReplaceChild(
                "RightUpperArmPlate",
                CubeListBuilder.create().texOffs(60, 27)
                    .addBox(-5.5f, -9.5f, -3.5f, 2.0f, 6.0f, 7.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 12.0f, 0.0f)
            )

            val SwordGrip = right_arm.addOrReplaceChild(
                "SwordGrip",
                CubeListBuilder.create().texOffs(102, 14)
                    .addBox(-2.0f, 17.0f, -7.0f, 1.0f, 1.0f, 12.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val SwordBlade2 = SwordGrip.addOrReplaceChild(
                "SwordBlade2",
                CubeListBuilder.create().texOffs(104, 48)
                    .addBox(-1.5f, 15.0f, -33.0f, 1.0f, 5.0f, 11.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val SwordBlade3 = SwordGrip.addOrReplaceChild(
                "SwordBlade3",
                CubeListBuilder.create().texOffs(120, 64)
                    .addBox(-1.5f, 16.0f, -36.0f, 1.0f, 3.0f, 3.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val SwordGuard = SwordGrip.addOrReplaceChild(
                "SwordGuard",
                CubeListBuilder.create().texOffs(120, 15)
                    .addBox(-2.0f, 13.0f, -8.0f, 2.0f, 9.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val SwordBlade1 = SwordGrip.addOrReplaceChild(
                "SwordBlade1",
                CubeListBuilder.create().texOffs(98, 27)
                    .addBox(-1.5f, 14.0f, -22.0f, 1.0f, 7.0f, 14.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val RightShoulderPad = right_arm.addOrReplaceChild(
                "RightShoulderPad",
                CubeListBuilder.create().texOffs(86, 0)
                    .addBox(-8.0f, -2.0f, -4.5f, 10.0f, 5.0f, 9.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.2731f)
            )

            val RightGlove = right_arm.addOrReplaceChild(
                "RightGlove",
                CubeListBuilder.create().texOffs(60, 40)
                    .addBox(-5.0f, 12.0f, -3.5f, 7.0f, 7.0f, 7.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val right_leg = partdefinition.addOrReplaceChild(
                "right_leg",
                CubeListBuilder.create().texOffs(48, 54)
                    .addBox(-3.5f, 0.0f, -3.5f, 7.0f, 16.0f, 7.0f, CubeDeformation(0.0f)),
                PartPose.offset(-3.0f, 6.0f, 0.0f)
            )

            val RightBoot = right_leg.addOrReplaceChild(
                "RightBoot",
                CubeListBuilder.create().texOffs(36, 88)
                    .addBox(-4.0f, 10.0f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val RightBootSmall = RightBoot.addOrReplaceChild(
                "RightBootSmall",
                CubeListBuilder.create().texOffs(36, 104)
                    .addBox(-2.0f, 14.0f, -5.0f, 4.0f, 4.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val RightLegPlateBack = right_leg.addOrReplaceChild(
                "RightLegPlateBack",
                CubeListBuilder.create().texOffs(22, 88)
                    .addBox(-3.0f, 1.0f, 2.9f, 6.0f, 8.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val RightLegPlate = right_leg.addOrReplaceChild(
                "RightLegPlate",
                CubeListBuilder.create().texOffs(22, 88)
                    .addBox(-3.0f, 1.0f, -3.9f, 6.0f, 8.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val RightFauld = right_leg.addOrReplaceChild(
                "RightFauld",
                CubeListBuilder.create().texOffs(0, 88)
                    .addBox(-4.0f, 0.0f, -4.0f, 3.0f, 8.0f, 8.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.2731f)
            )

            val left_leg = partdefinition.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create().texOffs(48, 54)
                    .addBox(-3.5f, 0.0f, -3.5f, 7.0f, 16.0f, 7.0f, CubeDeformation(0.0f)),
                PartPose.offset(3.0f, 6.0f, 0.0f)
            )

            val LeftLegPlate = left_leg.addOrReplaceChild(
                "LeftLegPlate",
                CubeListBuilder.create().texOffs(22, 88)
                    .addBox(-3.0f, 1.0f, -3.9f, 6.0f, 8.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val LeftBoot = left_leg.addOrReplaceChild(
                "LeftBoot",
                CubeListBuilder.create().texOffs(36, 88)
                    .addBox(-4.0f, 10.0f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val LeftBootSmall = LeftBoot.addOrReplaceChild(
                "LeftBootSmall",
                CubeListBuilder.create().texOffs(36, 104)
                    .addBox(1.0f, 32.0f, -5.0f, 4.0f, 4.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offset(-3.0f, -18.0f, 0.0f)
            )

            val LeftLegPlateBack = left_leg.addOrReplaceChild(
                "LeftLegPlateBack",
                CubeListBuilder.create().texOffs(22, 88)
                    .addBox(-3.0f, -11.0f, 2.9f, 6.0f, 8.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 12.0f, 0.0f)
            )

            val LeftFauld = left_leg.addOrReplaceChild(
                "LeftFauld",
                CubeListBuilder.create().texOffs(0, 88).mirror()
                    .addBox(1.0f, 0.0f, -4.0f, 3.0f, 8.0f, 8.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.2731f)
            )

            val left_arm = partdefinition.addOrReplaceChild(
                "left_arm",
                CubeListBuilder.create().texOffs(78, 14).mirror()
                    .addBox(-1.5f, 0.0f, -3.0f, 6.0f, 18.0f, 6.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offset(8.4f, -11.0f, 1.0f)
            )

            val LeftUpperArmPlate = left_arm.addOrReplaceChild(
                "LeftUpperArmPlate",
                CubeListBuilder.create().texOffs(60, 27).mirror()
                    .addBox(3.5f, -9.5f, -3.5f, 2.0f, 6.0f, 7.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offset(0.0f, 12.0f, 0.0f)
            )

            val LeftShoulderPad = left_arm.addOrReplaceChild(
                "LeftShoulderPad",
                CubeListBuilder.create().texOffs(86, 0).mirror()
                    .addBox(-2.0f, -2.0f, -4.5f, 10.0f, 5.0f, 9.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.2731f)
            )

            val LeftGlove = left_arm.addOrReplaceChild(
                "LeftGlove",
                CubeListBuilder.create().texOffs(60, 40).mirror()
                    .addBox(-2.0f, 12.0f, -3.5f, 7.0f, 7.0f, 7.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val body = partdefinition.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 41)
                    .addBox(-7.0f, 0.0f, -5.0f, 14.0f, 16.0f, 10.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, -12.0f, 0.0f)
            )

            val ChestPlateBack = body.addOrReplaceChild(
                "ChestPlateBack",
                CubeListBuilder.create().texOffs(52, 11)
                    .addBox(-6.0f, 1.0f, 5.0f, 12.0f, 10.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val ScarfLeft = body.addOrReplaceChild(
                "ScarfLeft",
                CubeListBuilder.create().texOffs(0, 67).mirror()
                    .addBox(-7.2f, 0.0f, -0.6f, 4.0f, 18.0f, 1.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(0.0f, 0.0f, -5.6f, -0.0456f, 0.0f, 0.0f)
            )

            val ScarfRight = body.addOrReplaceChild(
                "ScarfRight",
                CubeListBuilder.create().texOffs(0, 67)
                    .addBox(3.2f, 0.0f, -0.6f, 4.0f, 18.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, -5.6f, -0.0456f, 0.0f, 0.0f)
            )

            val Belt = body.addOrReplaceChild(
                "Belt",
                CubeListBuilder.create().texOffs(0, 75)
                    .addBox(-7.5f, 16.0f, -5.5f, 15.0f, 2.0f, 11.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val BeltBuckle = body.addOrReplaceChild(
                "BeltBuckle",
                CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-2.0f, 15.5f, -6.0f, 4.0f, 3.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val ChestPlateUpper = body.addOrReplaceChild(
                "ChestPlateUpper",
                CubeListBuilder.create().texOffs(53, 0)
                    .addBox(-5.5f, 0.5f, -6.3f, 11.0f, 10.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val ChestPlateLeft = ChestPlateUpper.addOrReplaceChild(
                "ChestPlateLeft",
                CubeListBuilder.create().texOffs(32, 26)
                    .addBox(1.5f, -1.5f, -6.4f, 7.0f, 4.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 12.0f, 0.0f, 0.0f, 0.0f, 0.0524f)
            )

            val ChestPlateRight = ChestPlateUpper.addOrReplaceChild(
                "ChestPlateRight",
                CubeListBuilder.create().texOffs(32, 26).mirror()
                    .addBox(-8.5f, -1.5f, -6.4f, 7.0f, 4.0f, 1.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(0.0f, 12.0f, 0.0f, 0.0f, 0.0f, -0.0524f)
            )

            val ChestPlateBack_1 = body.addOrReplaceChild(
                "ChestPlateBack_1",
                CubeListBuilder.create().texOffs(52, 22)
                    .addBox(-4.0f, 11.5f, 4.5f, 8.0f, 4.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val Cape = body.addOrReplaceChild(
                "Cape",
                CubeListBuilder.create().texOffs(76, 64)
                    .addBox(-7.0f, 0.0f, -0.5f, 14.0f, 24.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 6.0f, 0.0873f, 0.0f, 0.0f)
            )

            val ChestPlateLower = body.addOrReplaceChild(
                "ChestPlateLower",
                CubeListBuilder.create().texOffs(77, 0)
                    .addBox(-4.0f, 10.0f, -5.9f, 8.0f, 6.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            return LayerDefinition.create(meshdefinition, 128, 128)
        }
    }
}