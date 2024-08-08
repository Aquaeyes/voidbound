package dev.sterner.client.renderer.entity

import dev.sterner.VoidBound
import dev.sterner.client.model.CrimsonArcherModel
import dev.sterner.client.model.CrimsonHeavyKnightModel
import dev.sterner.client.model.CrimsonKnightModel
import dev.sterner.client.model.CrimsonPaladinModel
import dev.sterner.common.entity.CrimsonArcherEntity
import dev.sterner.common.entity.CrimsonHeavyKnightEntity
import dev.sterner.common.entity.CrimsonKnightEntity
import dev.sterner.common.entity.CrimsonPaladinEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation

class CrimsonPaladinEntityRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<CrimsonPaladinEntity, CrimsonPaladinModel>(
        context, CrimsonPaladinModel(context.bakeLayer(CrimsonPaladinModel.LAYER_LOCATION)), 0.2f
    ) {

    override fun getTextureLocation(entity: CrimsonPaladinEntity): ResourceLocation {
        return VoidBound.id("textures/entity/crimson_paladin.png")
    }
}