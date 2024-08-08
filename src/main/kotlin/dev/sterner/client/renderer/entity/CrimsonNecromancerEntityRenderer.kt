package dev.sterner.client.renderer.entity

import dev.sterner.VoidBound
import dev.sterner.client.model.CrimsonHeavyKnightModel
import dev.sterner.client.model.CrimsonKnightModel
import dev.sterner.client.model.CrimsonNecromancerModel
import dev.sterner.common.entity.CrimsonHeavyKnightEntity
import dev.sterner.common.entity.CrimsonKnightEntity
import dev.sterner.common.entity.CrimsonNecromancerEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation

class CrimsonNecromancerEntityRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<CrimsonNecromancerEntity, CrimsonNecromancerModel>(
        context, CrimsonNecromancerModel(context.bakeLayer(CrimsonNecromancerModel.LAYER_LOCATION)), 0.2f
    ) {

    override fun getTextureLocation(entity: CrimsonNecromancerEntity): ResourceLocation {
        return VoidBound.id("textures/entity/crimson_necromancer.png")
    }
}