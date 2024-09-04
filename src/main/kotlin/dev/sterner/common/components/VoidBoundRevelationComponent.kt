package dev.sterner.common.components

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent
import dev.sterner.registry.VoidBoundComponentRegistry
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player

class VoidBoundRevelationComponent(private val player: Player) : AutoSyncedComponent, CommonTickingComponent {

    var thoughtsQueue: MutableMap<Component, Int> = mutableMapOf()

    override fun tick() {
        // Ensure the queue does not exceed 16 elements
        while (thoughtsQueue.size > 16) {
            val firstEntry = thoughtsQueue.entries.firstOrNull()
            if (firstEntry != null) {
                thoughtsQueue.remove(firstEntry.key)
            }
        }

        val iterator = thoughtsQueue.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val remainingTime = entry.value - 1
            if (remainingTime <= 0) {
                iterator.remove() // Remove the thought if the time is up
            } else {
                thoughtsQueue[entry.key] = remainingTime
            }
        }
    }

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

    var hasReceivedNetherMessage: Boolean = false
        set(value) {
            field = value
            sync()
        }

    var hasReceivedPreWellNetherMessage: Boolean = false
        set(value) {
            field = value
            sync()
        }

    var hasReceivedEndMessage: Boolean = false
        set(value) {
            field = value
            sync()
        }

    var hasReceivedPreWellEndMessage: Boolean = false
        set(value) {
            field = value
            sync()
        }

    fun isTearKnowledgeComplete(): Boolean {
        return hasWellKnowledge && hasEndKnowledge && hasNetherKnowledge
    }

    fun addThought(thought: Component, durationTicks: Int) {
        thoughtsQueue[thought] = durationTicks
        sync()
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

        hasReceivedNetherMessage = tag.getBoolean("hasReceivedNetherMessage")
        hasReceivedEndMessage = tag.getBoolean("hasReceivedEndMessage")

        hasReceivedPreWellNetherMessage = tag.getBoolean("hasReceivedPreWellNetherMessage")
        hasReceivedPreWellEndMessage = tag.getBoolean("hasReceivedPreWellEndMessage")

        thoughtsQueue.clear() // Clear the existing queue
        val thoughtsList = tag.getList("ThoughtsQueue", 10) // 10 is the ID for CompoundTag
        for (i in 0 until thoughtsList.size) {
            val thoughtTag = thoughtsList.getCompound(i)
            val thought = Component.Serializer.fromJson(thoughtTag.getString("Text"))
            val duration = thoughtTag.getInt("Duration")
            if (thought != null) {
                thoughtsQueue[thought] = duration
            }
        }
    }

    override fun writeToNbt(tag: CompoundTag) {
        tag.putBoolean("hasWellKnowledge", hasWellKnowledge)
        tag.putBoolean("hasEndKnowledge", hasEndKnowledge)
        tag.putBoolean("hasNetherKnowledge", hasNetherKnowledge)
        tag.putBoolean("hasCrimsonKnowledge", hasCrimsonKnowledge)
        tag.putBoolean("hasIchorKnowledge", hasIchorKnowledge)

        tag.putBoolean("hasReceivedNetherMessage", hasReceivedNetherMessage)
        tag.putBoolean("hasReceivedEndMessage", hasReceivedEndMessage)

        tag.putBoolean("hasReceivedPreWellNetherMessage", hasReceivedPreWellNetherMessage)
        tag.putBoolean("hasReceivedPreWellEndMessage", hasReceivedPreWellEndMessage)

        val thoughtsList = ListTag()
        thoughtsQueue.forEach { (thought, duration) ->
            val thoughtTag = CompoundTag()
            thoughtTag.putString("Text", Component.Serializer.toJson(thought))
            thoughtTag.putInt("Duration", duration)
            thoughtsList.add(thoughtTag)
        }
        tag.put("ThoughtsQueue", thoughtsList)
    }
}