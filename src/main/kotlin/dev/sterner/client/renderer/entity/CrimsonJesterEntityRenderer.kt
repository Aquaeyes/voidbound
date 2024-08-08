package dev.sterner.client.renderer.entity

import dev.sterner.VoidBound
import dev.sterner.client.model.CrimsonArcherModel
import dev.sterner.client.model.CrimsonHeavyKnightModel
import dev.sterner.client.model.CrimsonJesterModel
import dev.sterner.client.model.CrimsonKnightModel
import dev.sterner.common.entity.CrimsonArcherEntity
import dev.sterner.common.entity.CrimsonHeavyKnightEntity
import dev.sterner.common.entity.CrimsonJesterEntity
import dev.sterner.common.entity.CrimsonKnightEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation

class CrimsonJesterEntityRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<CrimsonJesterEntity, CrimsonJesterModel>(
        context, CrimsonJesterModel(context.bakeLayer(CrimsonJesterModel.LAYER_LOCATION)), 0.2f
    ) {

    override fun getTextureLocation(entity: CrimsonJesterEntity): ResourceLocation {
        return VoidBound.id("textures/entity/crimson_jester.png")
    }
}