package dev.sterner.common.item.foci

import dev.sterner.api.VoidBoundApi
import dev.sterner.api.wand.IWandFocus
import dev.sterner.registry.VoidBoundBlockRegistry
import dev.sterner.registry.VoidBoundTags
import eu.pb4.common.protection.api.CommonProtection
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class PortableHoleFoci : IWandFocus {

    override fun onFocusRightClick(stack: ItemStack, level: Level, player: Player, hitResult: HitResult) {
        if (hitResult.type == HitResult.Type.BLOCK) {
            val blockHit = hitResult as BlockHitResult

            var hx = blockHit.blockPos.x
            var hy = blockHit.blockPos.y
            var hz = blockHit.blockPos.z

            val maxDistance = 32
            var distance = 0
            for (d in 0 until maxDistance) {
                distance = d
                val block = level.getBlockState(BlockPos(hx, hy, hz))
                if (block.`is`(VoidBoundTags.PORTABLE_HOLE_BLACKLIST)) {
                    return
                }
                if (!VoidBoundApi.canBlockBreak(level, BlockPos(hx, hy, hz)) || !CommonProtection.canBreakBlock(level, BlockPos(hx, hy, hz), player.gameProfile, player)) {
                    return
                }
                if (block.isAir) {
                    break
                }

                when (blockHit.direction) {
                    Direction.DOWN -> hy--
                    Direction.UP -> hy++
                    Direction.NORTH -> hz--
                    Direction.SOUTH -> hz++
                    Direction.WEST -> hx--
                    Direction.EAST -> hx++
                }
            }

            val centerPos = blockHit.blockPos
            val direction = blockHit.direction
            val perpendiculars = getPerpendicularDirections(direction)

            for (x in -1..1) {
                for (y in -1..1) {
                    val offsetPos = centerPos
                        .relative(perpendiculars.first, x)
                        .relative(perpendiculars.second, y)

                    createHole(player, level, offsetPos, direction, distance)
                }
            }

            player.swing(InteractionHand.MAIN_HAND)
            if (!level.isClientSide) {
                level.playSound(
                    null,
                    hitResult.location.x + 0.5,
                    hitResult.location.y + 0.5,
                    hitResult.location.z + 0.5,
                    SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.BLOCKS,
                    1.0f,
                    1.0f
                )
            }
        }
    }

    companion object {
        private fun getPerpendicularDirections(direction: Direction): Pair<Direction, Direction> {
            return when (direction) {
                Direction.UP, Direction.DOWN -> Pair(Direction.NORTH, Direction.EAST)
                Direction.NORTH, Direction.SOUTH -> Pair(Direction.UP, Direction.EAST)
                Direction.WEST, Direction.EAST -> Pair(Direction.UP, Direction.NORTH)
                else -> throw IllegalArgumentException("Invalid direction: $direction")
            }
        }

        fun createHole(player: Player, level: Level, pos: BlockPos, direction: Direction, distance: Int) {
            val oldState = level.getBlockState(pos)
            if (oldState.`is`(VoidBoundTags.PORTABLE_HOLE_BLACKLIST) || !VoidBoundApi.canBlockBreak(level, pos) || !CommonProtection.canBreakBlock(level, pos, player.gameProfile, player)) {
                return
            }
            val oldEntity = level.getBlockEntity(pos)
            val holeState = VoidBoundBlockRegistry.PORTABLE_HOLE.get().defaultBlockState()


            level.removeBlockEntity(pos)
            level.setBlock(pos, holeState, 1 shl 3 or (1 shl 1))
            level.removeBlockEntity(pos)
            level.setBlockEntity(
                VoidBoundBlockRegistry.PORTABLE_HOLE.get().createWithData(
                    player,
                    pos,
                    oldState,
                    oldEntity,
                    direction,
                    distance
                )
            )
        }
    }
}