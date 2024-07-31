package dev.sterner.client

import dev.sterner.VoidBound
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken

object Tokens {
    val WARD_BORDER: RenderTypeToken =
        RenderTypeToken.createToken(VoidBound.id("textures/block/runeborder.png"))
}