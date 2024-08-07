package dev.sterner.common.blockentity

import dev.sterner.api.blockentity.Modifier
import dev.sterner.api.blockentity.SyncedBlockEntity
import dev.sterner.common.block.SpiritBinderBlock
import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.state.BlockState
import org.joml.Vector3f

class SpiritBinderBlockEntity(pos: BlockPos, blockState: BlockState) : SyncedBlockEntity(
    VoidBoundBlockEntityTypeRegistry.SPIRIT_BINDER.get(), pos, blockState
) {

    var color: Vector3f = Vector3f(0.8f, 0.07f, 0.8f)

    var rift: SpiritRiftBlockEntity? = null
    private var needsSync = true


    fun tick() {
        if (needsSync) {
            init()
            needsSync = false
        }
        if (level != null) {

            if (level!!.getBlockState(blockPos).hasProperty(SpiritBinderBlock.MODIFIER)) {
                if (level!!.getBlockState(blockPos).getValue(SpiritBinderBlock.MODIFIER) == Modifier.BRILLIANT) {
                    tickBrilliantState()
                } else if (level!!.getBlockState(blockPos).getValue(SpiritBinderBlock.MODIFIER) == Modifier.HEX_ASH) {
                    tickHexAshState()
                } else {
                    tickNoneState()
                }
            }


        }
    }

    fun init(){
        val riftBe = level?.getBlockEntity(blockPos.above())
        if (rift != null) {
            rift = riftBe as SpiritRiftBlockEntity
        }
    }

    private fun tickHexAshState() {

    }

    private fun tickBrilliantState() {

    }

    private fun tickNoneState() {

    }


    override fun load(tag: CompoundTag) {
        super.load(tag)
    }

    override fun saveAdditional(tag: CompoundTag) {
        super.saveAdditional(tag)
    }
}