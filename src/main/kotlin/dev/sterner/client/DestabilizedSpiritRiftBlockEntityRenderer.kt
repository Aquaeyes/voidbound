package dev.sterner.client

import com.mojang.blaze3d.vertex.PoseStack
import dev.sterner.blockentity.DestabilizedSpiritRiftBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider

class DestabilizedSpiritRiftBlockEntityRenderer(ctx: BlockEntityRendererProvider.Context) : BlockEntityRenderer<DestabilizedSpiritRiftBlockEntity> {

    override fun render(
        blockEntity: DestabilizedSpiritRiftBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {

    }
}