package dev.sterner.client.renderer.entity

import dev.sterner.VoidBound
import dev.sterner.client.model.CrimsonClericModel
import dev.sterner.client.model.CrimsonHeavyKnightModel
import dev.sterner.client.model.CrimsonKnightModel
import dev.sterner.common.entity.CrimsonClericEntity
import dev.sterner.common.entity.CrimsonHeavyKnightEntity
import dev.sterner.common.entity.CrimsonKnightEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation

class CrimsonClericEntityRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<CrimsonClericEntity, CrimsonClericModel>(
        context, CrimsonClericModel(context.bakeLayer(CrimsonClericModel.LAYER_LOCATION)), 0.5f
    ) {

    override fun getTextureLocation(entity: CrimsonClericEntity): ResourceLocation {
        return VoidBound.id("textures/entity/crimson_cleric.png")
    }
}