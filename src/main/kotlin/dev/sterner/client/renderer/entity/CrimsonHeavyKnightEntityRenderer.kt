package dev.sterner.client.renderer.entity

import dev.sterner.VoidBound
import dev.sterner.client.model.CrimsonHeavyKnightModel
import dev.sterner.client.model.CrimsonKnightModel
import dev.sterner.common.entity.CrimsonHeavyKnightEntity
import dev.sterner.common.entity.CrimsonKnightEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation

class CrimsonHeavyKnightEntityRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<CrimsonHeavyKnightEntity, CrimsonHeavyKnightModel>(
        context, CrimsonHeavyKnightModel(context.bakeLayer(CrimsonHeavyKnightModel.LAYER_LOCATION)), 0.2f
    ) {

    override fun getTextureLocation(entity: CrimsonHeavyKnightEntity): ResourceLocation {
        return VoidBound.id("textures/entity/crimson_heavy_knight.png")
    }
}