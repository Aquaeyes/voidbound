package dev.sterner.api.item

import io.github.fabricators_of_create.porting_lib.event.common.BlockEvents.BreakEvent
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.phys.BlockHitResult
import java.util.function.Consumer

interface HammerLikeItem {

    fun getRadius(): Int
    fun getDepth(): Int
    fun getTier(): Tier
    fun getBlockTags(): TagKey<Block>

    fun causeAoe(
        level: ServerLevel,
        blockPos: BlockPos,
        blockState: BlockState,
        itemStack: ItemStack,
        player: ServerPlayer
    ) {
        if (level.isClientSide || blockState.getDestroySpeed(level, blockPos) == 0.0f) {
            return
        }

        if (player.isCrouching) {
            return
        }

        val pick: BlockHitResult = player.pick(20.0, 0.0f, false) as? BlockHitResult ?: return


        this.findAndBreakNearBlocks(pick, blockPos, itemStack, level, player)
    }

    fun actualIsCorrectToolForDrops(state: BlockState): Boolean {
        val i = getTier().level
        return if (i < 3 && state.`is`(BlockTags.NEEDS_DIAMOND_TOOL)) {
            false
        } else if (i < 2 && state.`is`(BlockTags.NEEDS_IRON_TOOL)) {
            false
        } else {
            (i >= 1 || !state.`is`(BlockTags.NEEDS_STONE_TOOL)) && state.`is`(getBlockTags())
        }
    }


    private fun canDestroy(targetState: BlockState, level: Level, pos: BlockPos): Boolean {
        if (targetState.getDestroySpeed(level, pos) <= 0) {
            return false
        }

        return level.getBlockEntity(pos) == null
    }

    fun findAndBreakNearBlocks(
        pick: BlockHitResult,
        blockPos: BlockPos,
        hammerStack: ItemStack,
        level: Level,
        livingEntity: LivingEntity
    ) {
        if (livingEntity !is ServerPlayer) return

        val direction: Direction = pick.direction
        val boundingBox = getAreaOfEffect(blockPos, direction, getRadius(), getDepth())

        if (!livingEntity.isCreative && (hammerStack.damageValue >= hammerStack.maxDamage - 1)) {
            return
        }

        var damage = 0
        val iterator: Iterator<BlockPos> = BlockPos.betweenClosedStream(boundingBox).iterator()
        val removedPos: MutableSet<BlockPos> = HashSet()
        while (iterator.hasNext()) {
            val pos = iterator.next()

            if (!livingEntity.isCreative && (hammerStack.damageValue + (damage + 1)) >= hammerStack.maxDamage - 1) {
                break
            }

            val targetState: BlockState = level.getBlockState(pos)
            if (pos === blockPos || removedPos.contains(pos) || !canDestroy(targetState, level, pos)) {
                continue
            }

            if (!actualIsCorrectToolForDrops(targetState)) {
                continue
            }

            BreakEvent.BLOCK_BREAK.invoker().onBlockBreak(BreakEvent(level, pos, targetState, livingEntity))

            removedPos.add(pos)
            level.destroyBlock(pos, false, livingEntity)
            if (!livingEntity.isCreative) {
                val correctToolForDrops = hammerStack.isCorrectToolForDrops(targetState)
                if (correctToolForDrops) {
                    targetState.spawnAfterBreak(level as ServerLevel, pos, hammerStack, true)
                    val drops = Block.getDrops(
                        targetState,
                        level,
                        pos,
                        level.getBlockEntity(pos),
                        livingEntity,
                        hammerStack
                    )
                    drops.forEach(Consumer { e: ItemStack ->
                        Block.popResourceFromFace(
                            level,
                            pos,
                            pick.direction,
                            e
                        )
                    })
                }
            }

            damage++
        }

        if (damage != 0 && !livingEntity.isCreative) {
            hammerStack.hurtAndBreak<LivingEntity>(
                damage, livingEntity
            ) { livingEntityx: LivingEntity ->
                livingEntityx.broadcastBreakEvent(EquipmentSlot.MAINHAND)
            }
        }
    }

    fun getAreaOfEffect(blockPos: BlockPos, direction: Direction, radius: Int, depth: Int): BoundingBox {
        val size = (radius / 2)
        val offset = size - 1

        return when (direction) {
            Direction.DOWN, Direction.UP -> BoundingBox(
                blockPos.x - size,
                blockPos.y - (if (direction == Direction.UP) depth - 1 else 0),
                blockPos.z - size,
                blockPos.x + size,
                blockPos.y + (if (direction == Direction.DOWN) depth - 1 else 0),
                blockPos.z + size
            )

            Direction.NORTH, Direction.SOUTH -> BoundingBox(
                blockPos.x - size,
                blockPos.y - size + offset,
                blockPos.z - (if (direction == Direction.SOUTH) depth - 1 else 0),
                blockPos.x + size,
                blockPos.y + size + offset,
                blockPos.z + (if (direction == Direction.NORTH) depth - 1 else 0)
            )

            Direction.WEST, Direction.EAST -> BoundingBox(
                blockPos.x - (if (direction == Direction.EAST) depth - 1 else 0),
                blockPos.y - size + offset,
                blockPos.z - size,
                blockPos.x + (if (direction == Direction.WEST) depth - 1 else 0),
                blockPos.y + size + offset,
                blockPos.z + size
            )
        }
    }
}