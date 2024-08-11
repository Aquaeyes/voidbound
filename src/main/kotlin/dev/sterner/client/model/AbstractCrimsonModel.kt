package dev.sterner.client.model

import dev.sterner.common.entity.AbstractCultistEntity
import net.minecraft.client.model.AnimationUtils
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.util.Mth
import net.minecraft.world.entity.LivingEntity

abstract class AbstractCrimsonModel<T : AbstractCultistEntity>(root: ModelPart) : HumanoidModel<T>(root) {

}