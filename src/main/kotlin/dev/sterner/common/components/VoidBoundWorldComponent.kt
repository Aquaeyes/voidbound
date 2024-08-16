package dev.sterner.common.components

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import dev.sterner.registry.VoidBoundComponentRegistry
import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.nbt.*
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.CompassItem
import net.minecraft.world.level.Level
import java.util.*

class VoidBoundWorldComponent(val level: Level) : AutoSyncedComponent {

    private val playerWardPosMap = mutableMapOf<UUID, MutableSet<GlobalPos>>()

    fun isPosBoundToAnotherPlayer(player: Player, pos: GlobalPos): Boolean {
        val playerUuid = player.uuid

        // Iterate over all player sets in playerWardPosMap, excluding the provided player
        return playerWardPosMap.any { (uuid, posSet) ->
            uuid != playerUuid && posSet.contains(pos)
        }
    }

    fun hasBlockPos(player: Player, pos: GlobalPos) : Boolean{
        val playerUuid = player.uuid
        return playerWardPosMap.any { (uuid, posSet) ->
            uuid == playerUuid && posSet.contains(pos)
        }
    }

    fun addPos(uuid: UUID, pos: GlobalPos) {
        if (isPosOwned(pos)) {
            return
        }

        val playerPosSet = playerWardPosMap.getOrPut(uuid) { mutableSetOf() }

        playerPosSet.add(pos)
        VoidBoundComponentRegistry.VOID_BOUND_WORLD_COMPONENT.sync(level)
    }

    fun removePos(player: Player, pos: GlobalPos) {
        val uuid = player.uuid
        val playerPosSet = playerWardPosMap[uuid]

        // If the player has the position, remove it
        if (playerPosSet != null && playerPosSet.contains(pos)) {
            playerPosSet.remove(pos)

            // If the set is now empty, remove the player from the map
            if (playerPosSet.isEmpty()) {
                playerWardPosMap.remove(uuid)
            }
        }
        VoidBoundComponentRegistry.VOID_BOUND_WORLD_COMPONENT.sync(level)
    }

    private fun isPosOwned(pos: GlobalPos): Boolean {
        return playerWardPosMap.values.any { posSet -> posSet.contains(pos) }
    }

    override fun readFromNbt(tag: CompoundTag) {
        playerWardPosMap.clear()

        val list = tag.getList("playerWardPosMap", 10)

        for (i in 0 until list.size) {
            val playerTag = list.getCompound(i)
            val playerId = playerTag.getUUID("PlayerUUID")

            val posList = playerTag.getList("Positions", 10)
            val globalPosSet = mutableSetOf<GlobalPos>()

            for (j in 0 until posList.size) {
                val posTag = posList.getCompound(j) // Get the CompoundTag for this position
                val optionalDimension = getLodestoneDimension(posTag) // Get the dimension for this position
                if (optionalDimension.isPresent) {
                    val blockPos = NbtUtils.readBlockPos(posTag.getCompound("GlobalPos"))
                    val globalPos = GlobalPos.of(optionalDimension.get(), blockPos)
                    globalPosSet.add(globalPos)
                }
            }
            playerWardPosMap[playerId] = globalPosSet
        }
    }


    override fun writeToNbt(tag: CompoundTag) {
        val list = ListTag()  // This will hold all player data

        for ((uuid, posSet) in playerWardPosMap) {
            val playerTag = CompoundTag()  // Tag for each player
            playerTag.putUUID("PlayerUUID", uuid)

            val posListTag = ListTag()
            for (globalPos in posSet) {
                val posTag = CompoundTag()

                // Write the GlobalPos to NBT
                NbtUtils.writeBlockPos(globalPos.pos()).let { blockPosTag -> posTag.put("GlobalPos", blockPosTag) }

                // Write the dimension (LodestoneDimension) to NBT
                addLodestoneTags(globalPos.dimension(), posTag)

                // Add this position's tag to the list of positions
                posListTag.add(posTag)
            }
            playerTag.put("Positions", posListTag)
            list.add(playerTag)
        }

        // Attach the full list to the provided tag
        tag.put("playerWardPosMap", list)
    }


    private fun getLodestoneDimension(compoundTag: CompoundTag): Optional<ResourceKey<Level>> {
        return Level.RESOURCE_KEY_CODEC.parse(
            NbtOps.INSTANCE,
            compoundTag["Dimension"]
        ).result()
    }

    private fun addLodestoneTags(
        lodestoneDimension: ResourceKey<Level>,
        compoundTag: CompoundTag
    ) {
        Level.RESOURCE_KEY_CODEC
            .encodeStart(NbtOps.INSTANCE, lodestoneDimension)
            .resultOrPartial { _: String? -> }
            .ifPresent { tag: Tag ->
                compoundTag.put(
                    "Dimension",
                    tag
                )
            }
    }
}