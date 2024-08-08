package dev.sterner.client.renderer.entity

import dev.sterner.VoidBound
import dev.sterner.client.model.CrimsonArcherModel
import dev.sterner.client.model.CrimsonHeavyKnightModel
import dev.sterner.client.model.CrimsonKnightModel
import dev.sterner.common.entity.CrimsonArcherEntity
import dev.sterner.common.entity.CrimsonHeavyKnightEntity
import dev.sterner.common.entity.CrimsonKnightEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.IllagerRenderer
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer
import net.minecraft.resources.ResourceLocation

class CrimsonArcherEntityRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<CrimsonArcherEntity, CrimsonArcherModel>(
        context, CrimsonArcherModel(context.bakeLayer(CrimsonArcherModel.LAYER_LOCATION)), 0.5f
    ) {

        init {
            this.addLayer(ItemInHandLayer(this, context.itemInHandRenderer))
        }

    override fun getTextureLocation(entity: CrimsonArcherEntity): ResourceLocation {
        return VoidBound.id("textures/entity/crimson_archer.png")
    }
}