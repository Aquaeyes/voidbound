package dev.sterner.common.components

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import dev.sterner.registry.VoidBoundComponentRegistry
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.player.Player

class VoidBoundRevelationComponent(private val player: Player) : AutoSyncedComponent {

    var hasWellKnowledge: Boolean = false
        set(value) {
            field = value
            sync()
        }
    var hasEndKnowledge: Boolean = false
        set(value) {
            field = value
            sync()
        }
    var hasNetherKnowledge: Boolean = false
        set(value) {
            field = value
            sync()
        }
    var hasCrimsonKnowledge: Boolean = false
        set(value) {
            field = value
            sync()
        }
    var hasIchorKnowledge: Boolean = false
        set(value) {
            field = value
            sync()
        }

    var hasRecievedNetherMessage: Boolean = false
        set(value) {
            field = value
            sync()
        }

    var hasRecievedEndMessage: Boolean = false
        set(value) {
            field = value
            sync()
        }

    fun isTearKnowledgeComplete(): Boolean {
        return hasWellKnowledge && hasEndKnowledge && hasNetherKnowledge
    }

    fun sync(){
        VoidBoundComponentRegistry.VOID_BOUND_REVELATION_COMPONENT.sync(player)
    }

    override fun readFromNbt(tag: CompoundTag) {
        hasWellKnowledge = tag.getBoolean("hasWellKnowledge")
        hasEndKnowledge = tag.getBoolean("hasEndKnowledge")
        hasNetherKnowledge = tag.getBoolean("hasNetherKnowledge")
        hasCrimsonKnowledge = tag.getBoolean("hasCrimsonKnowledge")
        hasIchorKnowledge = tag.getBoolean("hasIchorKnowledge")

        hasRecievedNetherMessage = tag.getBoolean("hasRecievedNetherMessage")
        hasRecievedEndMessage = tag.getBoolean("hasRecievedEndMessage")

    }

    override fun writeToNbt(tag: CompoundTag) {
        tag.putBoolean("hasWellKnowledge", hasWellKnowledge)
        tag.putBoolean("hasEndKnowledge", hasEndKnowledge)
        tag.putBoolean("hasNetherKnowledge", hasNetherKnowledge)
        tag.putBoolean("hasCrimsonKnowledge", hasCrimsonKnowledge)
        tag.putBoolean("hasIchorKnowledge", hasIchorKnowledge)

        tag.putBoolean("hasRecievedNetherMessage", hasRecievedNetherMessage)
        tag.putBoolean("hasRecievedEndMessage", hasRecievedEndMessage)
    }
}