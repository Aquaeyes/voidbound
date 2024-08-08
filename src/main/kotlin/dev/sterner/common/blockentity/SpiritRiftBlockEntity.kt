package dev.sterner.common.blockentity

import com.sammy.malum.core.systems.spirit.MalumSpiritType
import dev.sterner.api.rift.RiftType
import dev.sterner.api.rift.SimpleSpiritCharge
import dev.sterner.api.blockentity.SyncedBlockEntity
import dev.sterner.api.util.VoidBoundUtils
import dev.sterner.common.rift.DestabilizedRiftType
import dev.sterner.common.rift.EldritchRiftType
import dev.sterner.common.rift.NormalRiftType
import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import dev.sterner.registry.VoidBoundRiftTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class SpiritRiftBlockEntity(pos: BlockPos, state: BlockState?) :
    SyncedBlockEntity(VoidBoundBlockEntityTypeRegistry.DESTABILIZED_SPIRIT_RIFT.get(), pos, state) {

    var riftType: RiftType = VoidBoundRiftTypeRegistry.NORMAL.get()
    var simpleSpiritCharge = SimpleSpiritCharge()

    val x = (worldPosition.x.toFloat() + 0.5f).toDouble()
    val y = (worldPosition.y.toFloat() + 0.5f).toDouble()
    val z = (worldPosition.z.toFloat() + 0.5f).toDouble()

    var alpha: Float = 0f
    var previousAlpha: Float = 0f
    var targetAlpha: Float = 0f
    var entity: PathfinderMob? = null

    var counter = 0
    private var rechargeCounter = 0

    private var infinite = false
        set(value) {
            field = value
            if (value) {
                simpleSpiritCharge.setInfiniteCount()
                notifyUpdate()
            }
        }

    fun onUse(player: Player, hand: InteractionHand, hit: BlockHitResult) {
        if (hand == InteractionHand.MAIN_HAND) {
            if (riftType is NormalRiftType) {
                riftType = VoidBoundRiftTypeRegistry.ELDRITCH.get()
                player.sendSystemMessage(Component.translatable("Eldritch"))
            } else if (riftType is EldritchRiftType) {
                riftType = VoidBoundRiftTypeRegistry.DESTABILIZED.get()
                player.sendSystemMessage(Component.translatable("Destabilized"))
            } else if (riftType is DestabilizedRiftType) {
                riftType = VoidBoundRiftTypeRegistry.NORMAL.get()
                player.sendSystemMessage(Component.translatable("Normal"))
            }
            notifyUpdate()
        }
    }


    fun tick() {

        if (level != null) {

            if (infinite) {
                rechargeCounter++
                if (rechargeCounter == 20 * 2) {
                    rechargeCounter = 0
                    simpleSpiritCharge.rechargeInfiniteCount()
                    notifyUpdate()
                }
            }

            riftType.tick(level!!, blockPos, this)

            // Update previousAlpha before changing alpha
            previousAlpha = alpha

            // Interpolate alpha towards targetAlpha
            alpha = Mth.lerp(0.05f, alpha, targetAlpha)
        }
    }

    fun removeSpiritFromCharge(type: MalumSpiritType, count: Int): Boolean {
        val bl = simpleSpiritCharge.removeFromCharge(type, count)
        notifyUpdate()
        return bl
    }

    fun addSpiritToCharge(entity: PathfinderMob) {
        val list = VoidBoundUtils.getSpiritData(entity)
        if (list.isPresent) {
            for (spirit in list.get()) {
                simpleSpiritCharge.addToCharge(spirit.type, spirit.count)
            }
            infinite = simpleSpiritCharge.shouldBeInfinite()
            notifyUpdate()
        }
    }

    override fun saveAdditional(tag: CompoundTag) {
        simpleSpiritCharge.serializeNBT(tag)
        tag.putString("RiftType", riftType.toString())
        tag.putBoolean("Infinite", infinite)
        if (alpha != null) {
            tag.putFloat("Alpha", alpha)
        }

        if (previousAlpha != null) {
            tag.putFloat("PrevAlpha", previousAlpha)
        }

        if (targetAlpha != null) {
            tag.putFloat("TargetAlpha", targetAlpha)
        }
        super.saveAdditional(tag)
    }

    override fun load(tag: CompoundTag) {
        simpleSpiritCharge = simpleSpiritCharge.deserializeNBT(tag)
        if (tag.contains("RiftType")) {
            val op = VoidBoundRiftTypeRegistry.RIFT.getOptional(ResourceLocation.tryParse(tag.getString("RiftType")))
            if (op.isPresent) {
                riftType = op.get()
            }
        }
        infinite = tag.getBoolean("Infinite")
        if (tag.contains("Alpha")) {
            alpha = tag.getFloat("Alpha")
        }
        if (tag.contains("PrevAlpha")) {
            previousAlpha = tag.getFloat("PrevAlpha")
        }
        if (tag.contains("TargetAlpha")) {
            targetAlpha = tag.getFloat("TargetAlpha")
        }
        super.load(tag)
    }

}