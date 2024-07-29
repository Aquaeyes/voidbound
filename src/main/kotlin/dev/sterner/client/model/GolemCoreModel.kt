package dev.sterner.client.model

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import dev.sterner.VoidBound
import net.minecraft.client.model.Model
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.*
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import java.util.function.Function

class GolemCoreModel(root: ModelPart) : Model(Function { location: ResourceLocation ->
    RenderType.entityCutoutNoCull(
        location
    )
}) {

    private val golemCore: ModelPart = root.getChild("golemCore")

    override fun renderToBuffer(
        poseStack: PoseStack,
        buffer: VertexConsumer,
        packedLight: Int,
        packedOverlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        golemCore.render(poseStack, buffer, packedLight, packedOverlay)
    }

    companion object {

        val LAYER_LOCATION: ModelLayerLocation = ModelLayerLocation(VoidBound.id("golem_core"), "main")

        fun createBodyLayer(): LayerDefinition {
            val meshDefinition: MeshDefinition = MeshDefinition()
            val root: PartDefinition = meshDefinition.root

            val golemCore: PartDefinition = root.addOrReplaceChild(
                "golemCore",
                CubeListBuilder.create().texOffs(1, 1)
                    .addBox(-3.0f, -3.0f, -0.5f, 6.0f, 6.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 14.5f, -2.6f)
            )

            return LayerDefinition.create(meshDefinition, 16, 16)
        }
    }
}