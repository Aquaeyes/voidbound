package dev.sterner.client.model

import dev.sterner.common.entity.AbstractCultistEntity
import net.minecraft.client.model.AnimationUtils
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.util.Mth
import net.minecraft.world.entity.LivingEntity

abstract class AbstractCrimsonModel<T : AbstractCultistEntity>(root: ModelPart) : HumanoidModel<T>(root) {

    override fun setupAnim(
        entity: T,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch)

        val armPose = (entity as AbstractCultistEntity).getArmPose()

        if (armPose == AbstractCultistEntity.CrimsonArmPose.SPELLCASTING) {
            rightArm.z = 0.0f
            rightArm.x = -5.0f
            leftArm.z = 0.0f
            leftArm.x = 5.0f
            rightArm.xRot = Mth.cos(ageInTicks * 0.6662f) * 0.25f
            leftArm.xRot = Mth.cos(ageInTicks * 0.6662f) * 0.25f
            rightArm.zRot = 2.3561945f
            leftArm.zRot = -2.3561945f
            rightArm.yRot = 0.0f
            leftArm.yRot = 0.0f
        } else if (armPose == AbstractCultistEntity.CrimsonArmPose.BOW_AND_ARROW) {
            rightArm.yRot = -0.1f + head.yRot
            rightArm.xRot = -1.5707964f + head.xRot
            leftArm.xRot = -0.9424779f + head.xRot
            leftArm.yRot = head.yRot - 0.4f
            leftArm.zRot = 1.5707964f
        } else if (armPose == AbstractCultistEntity.CrimsonArmPose.CROSSBOW_HOLD) {
            AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true)
        } else if (armPose == AbstractCultistEntity.CrimsonArmPose.CROSSBOW_CHARGE) {
            AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, entity, true)
        } else if (armPose == AbstractCultistEntity.CrimsonArmPose.BLOCK) {
            this.rightArm.xRot = this.rightArm.xRot * 0.5f - 0.9424779f
            this.rightArm.yRot = -0.5235988f
        }
    }
}