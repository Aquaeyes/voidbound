package dev.sterner.client.renderer.entity

import dev.sterner.VoidBound
import dev.sterner.client.model.CrimsonKnightModel
import dev.sterner.common.entity.CrimsonKnightEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer
import net.minecraft.resources.ResourceLocation

class CrimsonKnightEntityRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<CrimsonKnightEntity, CrimsonKnightModel>(
        context, CrimsonKnightModel(context.bakeLayer(CrimsonKnightModel.LAYER_LOCATION)), 0.5f
    ) {

    init {
        this.addLayer(ItemInHandLayer(this, context.itemInHandRenderer))
    }

    override fun getTextureLocation(entity: CrimsonKnightEntity): ResourceLocation {
        return VoidBound.id("textures/entity/crimson_knight.png")
    }
}