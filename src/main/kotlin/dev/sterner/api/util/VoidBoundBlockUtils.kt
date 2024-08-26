package dev.sterner.api.util

import com.sammy.malum.registry.common.block.BlockRegistry
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.BlockTags
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.abs
import kotlin.math.max

object VoidBoundBlockUtils {

    private var lastPos: BlockPos? = null
    private var lastDistance: Double = 0.0

    /**
     * Finds and breaks the furthest block of a connected set of logs
     */
    fun breakFurthestBlock(level: Level, pos: BlockPos, blockState: BlockState, player: Player): Boolean {
        lastPos = BlockPos(pos)
        lastDistance = 0.0
        val reach = if (level.getBlockState(pos).`is`(BlockTags.LOGS)) 2 else 1
        findBlocks(level, pos, blockState, reach)
        val worked: Boolean = harvestBlock(
            level,
            player,
            lastPos!!
        )
        level.markAndNotifyBlock(pos, level.getChunkAt(pos), blockState, blockState, 3, 20)
        if (worked && level.getBlockState(pos).`is`(BlockTags.LOGS)) {
            level.markAndNotifyBlock(pos, level.getChunkAt(pos), blockState, blockState, 3, 20)

            for (xx in -3..3) {
                for (yy in -3..3) {
                    for (zz in -3..3) {
                        level.scheduleTick(
                            lastPos!!.offset(xx, yy, zz),
                            level.getBlockState(lastPos!!.offset(xx, yy, zz)).block,
                            50 + level.random.nextInt(75)
                        )
                    }
                }
            }
        }

        return worked
    }

    /**
     * Applies fortune and restraints towards a block breaking action
     */
    private fun harvestBlock(
        level: Level,
        player: Player,
        pos: BlockPos
    ): Boolean {
        if (player is ServerPlayer) {
            val blockState = level.getBlockState(pos)
            val blockEntity = level.getBlockEntity(pos)
            val bl2: Boolean
            if (player.abilities.instabuild) {
                bl2 = removeBlock(player, pos)
            } else {
                val itemStack: ItemStack = player.mainHandItem
                val bl = blockState.canEntityDestroy(level, pos, player)
                bl2 = removeBlock(player, pos, bl)
                if (bl2 && bl) {
                    val fakeStack: ItemStack = itemStack.copy()
                    if (0 > EnchantmentHelper.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, player)) {

                        val enchantments: MutableMap<Enchantment, Int?> = EnchantmentHelper.getEnchantments(itemStack)

                        val fort = max(
                            0.toDouble(),
                            (if (enchantments[Enchantments.BLOCK_FORTUNE] != null) enchantments[Enchantments.BLOCK_FORTUNE] else 0)!!.toDouble()
                        ).toInt()

                        if (fort > 0) {
                            enchantments[Enchantments.BLOCK_FORTUNE] = fort
                        }

                        EnchantmentHelper.setEnchantments(enchantments, fakeStack)
                    }

                    blockState.block.playerDestroy(level, player, pos, blockState, blockEntity, fakeStack)
                }
            }

            return bl2
        } else {
            return false
        }
    }

    private fun removeBlock(player: Player, pos: BlockPos): Boolean {
        return removeBlock(player, pos, false)
    }


    private fun removeBlock(player: Player, pos: BlockPos, canHarvest: Boolean): Boolean {
        val blockState = player.level().getBlockState(pos)

        val flag: Boolean =
            blockState.onDestroyedByPlayer(player.level(), pos, player, canHarvest, blockState.fluidState)
        if (flag) {
            try {
                blockState.block.destroy(player.level(), pos, blockState)
            } catch (_: Exception) {
            }
        }

        return flag
    }

    private fun findBlocks(level: Level, pos: BlockPos, blockState: BlockState, reach: Int) {
        for (xx in -reach..reach) {
            for (yy in reach downTo -reach) {
                for (zz in -reach..reach) {
                    if (abs(lastPos!!.x + xx - pos.x) > 24) {
                        return
                    }

                    if (abs(lastPos!!.y + yy - pos.y) > 48) {
                        return
                    }

                    if (abs(lastPos!!.z + zz - pos.z) > 24) {
                        return
                    }

                    val bs: BlockState = level.getBlockState(lastPos!!.offset(xx, yy, zz))
                    val same = bs.block == blockState.block

                    if (same && bs.block.properties.destroyTime >= 0.0f) {
                        val xd = (lastPos!!.x + xx - pos.x)
                        val yd = (lastPos!!.y + yy - pos.y)
                        val zd = (lastPos!!.z + zz - pos.z)
                        val d = xd * xd + yd * yd + zd * zd
                        if (d > lastDistance) {
                            lastDistance = d.toDouble()
                            lastPos = lastPos!!.offset(xx, yy, zz)
                            findBlocks(level, pos, blockState, reach)
                            return
                        }
                    }
                }
            }
        }
    }

    /**
     * Collects all connected log blocks of the same type and breaks them
     */
    fun getLogsToBreak(
        level: Level,
        pos: BlockPos,
        logsToBreak: MutableList<BlockPos>,
        logType: Block
    ): List<BlockPos> {
        if (logsToBreak.size > 256) {
            return logsToBreak
        }

        val checkAround: MutableList<BlockPos> = ArrayList()

        val aroundLogs: MutableList<BlockPos> = ArrayList()
        for (aroundLog in BlockPos.betweenClosed(pos.x - 1, pos.y, pos.z - 1, pos.x + 1, pos.y + 1, pos.z + 1)) {
            aroundLogs.add(aroundLog.immutable())
        }

        for (aroundLogPos in aroundLogs) {
            if (logsToBreak.contains(aroundLogPos)) {
                continue
            }

            val log = level.getBlockState(aroundLogPos).block
            if (log == logType || isExposedLog(log, logType)) {
                checkAround.add(aroundLogPos)
                logsToBreak.add(aroundLogPos)
            }
        }

        if (checkAround.size == 0) {
            return logsToBreak
        }

        for (aroundPos in checkAround) {
            for (logPos in getLogsToBreak(level, aroundPos, logsToBreak, logType)) {
                if (!logsToBreak.contains(logPos)) {
                    logsToBreak.add(logPos.immutable())
                }
            }
        }

        val up = pos.above(2)
        return getLogsToBreak(level, up.immutable(), logsToBreak, logType)
    }

    /**
     * Special case for Malum's exposed log types
     */
    private fun isExposedLog(log1: Block, log2: Block): Boolean {
        if (log1.defaultBlockState().`is`(BlockRegistry.EXPOSED_RUNEWOOD_LOG.get())) {
            return true
        }
        if (log2.defaultBlockState().`is`(BlockRegistry.EXPOSED_RUNEWOOD_LOG.get())) {
            return true
        }
        if (log1.defaultBlockState().`is`(BlockRegistry.EXPOSED_SOULWOOD_LOG.get())) {
            return true
        }
        if (log2.defaultBlockState().`is`(BlockRegistry.EXPOSED_SOULWOOD_LOG.get())) {
            return true
        }
        return false
    }
}