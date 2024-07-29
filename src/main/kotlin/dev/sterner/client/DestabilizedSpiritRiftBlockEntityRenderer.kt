package dev.sterner.client

import com.mojang.blaze3d.vertex.PoseStack
import dev.sterner.VoidBound
import dev.sterner.common.blockentity.DestabilizedSpiritRiftBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken


class DestabilizedSpiritRiftBlockEntityRenderer(ctx: BlockEntityRendererProvider.Context) :
    BlockEntityRenderer<DestabilizedSpiritRiftBlockEntity> {

    val frameCount = 32
    val frameHeight = 64
    val texture = VoidBound.id("textures/misc/destabilized_spirit_rift.png")

    val RIFT_TYPE: RenderType = LodestoneRenderTypeRegistry.TRANSPARENT_TEXTURE.apply(
        RenderTypeToken.createCachedToken(
            texture
        )
    )


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