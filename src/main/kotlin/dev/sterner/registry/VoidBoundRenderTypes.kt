package dev.sterner.registry

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.RenderStateShard
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry
import team.lodestar.lodestone.systems.rendering.LodestoneRenderType
import team.lodestar.lodestone.systems.rendering.StateShards
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeProvider
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken

object VoidBoundRenderTypes {

    val TRANSPARENT_GLOW_TEXTURE: RenderTypeProvider = RenderTypeProvider { token: RenderTypeToken ->
        LodestoneRenderTypeRegistry.createGenericRenderType(
            "transparent_glow_texture",
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
            VertexFormat.Mode.TRIANGLES,
            LodestoneRenderTypeRegistry.builder()
                .setShaderState(VoidBoundShaders.GLOW_TEXTURE)
                .setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY)
                .setLightmapState(LodestoneRenderTypeRegistry.LIGHTMAP)
                .setCullState(LodestoneRenderTypeRegistry.CULL)

                .setTextureState(token.get())
        )
    }

    val GRAVITY_VORTEX: RenderTypeProvider = RenderTypeProvider { token: RenderTypeToken ->
        LodestoneRenderTypeRegistry.createGenericRenderType(
            "gravity_vortex",
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
            VertexFormat.Mode.QUADS,
            LodestoneRenderTypeRegistry.builder()
                .setShaderState(VoidBoundShaders.GRAVITY_VORTEX)
                .setTransparencyState(LodestoneRenderTypeRegistry.ADDITIVE_TRANSPARENCY)
                .setLightmapState(LodestoneRenderTypeRegistry.LIGHTMAP)
                .setTextureState(token.get())
        )
    }
}