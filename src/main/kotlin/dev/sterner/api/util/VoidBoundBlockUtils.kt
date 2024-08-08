package dev.sterner.api.util

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
import kotlin.math.max

object VoidBoundBlockUtils {

    var lastPos: BlockPos? = null
    var lastdistance: Double = 0.0

    fun breakFurthestBlock(world: Level, pos: BlockPos, block: BlockState, player: Player): Boolean {
        lastPos = BlockPos(pos)
        lastdistance = 0.0
        val reach = if (world.getBlockState(pos).`is`(BlockTags.LOGS)) 2 else 1
        findBlocks(world, pos, block, reach)
        val worked: Boolean = harvestBlockSkipCheck(
            world,
            player,
            lastPos!!
        )
        world.markAndNotifyBlock(pos, world.getChunkAt(pos), block, block, 3, 20)
        if (worked && world.getBlockState(pos).`is`(BlockTags.LOGS)) {
            world.markAndNotifyBlock(pos, world.getChunkAt(pos), block, block, 3, 20)

            for (xx in -3..3) {
                for (yy in -3..3) {
                    for (zz in -3..3) {
                        world.scheduleTick(
                            lastPos!!.offset(xx, yy, zz),
                            world.getBlockState(lastPos!!.offset(xx, yy, zz)).block,
                            50 + world.random.nextInt(75)
                        )
                    }
                }
            }
        }

        return worked
    }

    fun harvestBlockSkipCheck(world: Level, player: Player, pos: BlockPos): Boolean {
        return harvestBlock(world, player, pos, false, false, 0, true)
    }

    fun harvestBlock(
        world: Level,
        p: Player,
        pos: BlockPos,
        alwaysDrop: Boolean,
        silkOverride: Boolean,
        fortuneOverride: Int,
        skipEvent: Boolean
    ): Boolean {
        if (p is ServerPlayer) {
            val player = p


            if (false) {
                return false
            } else {
                val iblockstate = world.getBlockState(pos)
                val tileentity = world.getBlockEntity(pos)
                val block: Block = iblockstate.block
                if (false) {

                } else {
                    //world.blockEvent(null, 2001, pos, Block.getStateId(iblockstate))
                    var flag1 = false
                    if (player.abilities.instabuild) {
                        flag1 = removeBlock(player, pos)
                        //player.interactionManager.player.connection.sendPacket(SPacketBlockChange(world, pos))
                    } else {
                        val itemstack1: ItemStack = player.mainHandItem
                        val flag = alwaysDrop || iblockstate.canEntityDestroy(world, pos, player)
                        flag1 = removeBlock(player, pos, flag)
                        if (flag1 && flag) {
                            var fakeStack: ItemStack = itemstack1.copy()
                            if (silkOverride || fortuneOverride > EnchantmentHelper.getEnchantmentLevel(
                                    Enchantments.BLOCK_FORTUNE,
                                    player
                                )
                            ) {

                                val enchMap: MutableMap<Enchantment, Int?> =
                                    EnchantmentHelper.getEnchantments(itemstack1)
                                if (silkOverride) {
                                    enchMap[Enchantments.SILK_TOUCH] = 1
                                }

                                val fort = max(
                                    fortuneOverride.toDouble(),
                                    (if (enchMap[Enchantments.BLOCK_FORTUNE] != null) enchMap[Enchantments.BLOCK_FORTUNE] else 0)!!.toDouble()
                                ).toInt()

                                if (fort > 0) {
                                    enchMap[Enchantments.BLOCK_FORTUNE] = fort
                                }

                                EnchantmentHelper.setEnchantments(enchMap, fakeStack)
                            }

                            iblockstate.block.playerDestroy(world, player, pos, iblockstate, tileentity, fakeStack)
                        }
                    }

                    return flag1
                }
            }
        } else {
            return false
        }
        return false
    }

    private fun removeBlock(player: Player, pos: BlockPos): Boolean {
        return removeBlock(player, pos, false)
    }


    private fun removeBlock(player: Player, pos: BlockPos, canHarvest: Boolean): Boolean {
        val iblockstate = player.level().getBlockState(pos)

        val flag: Boolean =
            iblockstate.onDestroyedByPlayer(player.level(), pos, player, canHarvest, iblockstate.fluidState)
        if (flag) {
            try {
                iblockstate.block.destroy(player.level(), pos, iblockstate)
            } catch (var6: Exception) {
            }
        }

        return flag
    }

    fun findBlocks(world: Level, pos: BlockPos, block: BlockState, reach: Int) {
        for (xx in -reach..reach) {
            for (yy in reach downTo -reach) {
                for (zz in -reach..reach) {
                    if (Math.abs(lastPos!!.x + xx - pos.x) > 24) {
                        return
                    }

                    if (Math.abs(lastPos!!.y + yy - pos.y) > 48) {
                        return
                    }

                    if (Math.abs(lastPos!!.z + zz - pos.z) > 24) {
                        return
                    }

                    val bs: BlockState = world.getBlockState(lastPos!!.offset(xx, yy, zz))
                    val same = bs.block == block.block

                    if (same && bs.block.properties.destroyTime >= 0.0f
                    ) {
                        val xd = (lastPos!!.x + xx - pos.x)
                        val yd = (lastPos!!.y + yy - pos.y)
                        val zd = (lastPos!!.z + zz - pos.z)
                        val d = xd * xd + yd * yd + zd * zd
                        if (d > lastdistance) {
                            lastdistance = d.toDouble()
                            lastPos =
                                lastPos!!.offset(xx, yy, zz)
                            findBlocks(world, pos, block, reach)
                            return
                        }
                    }
                }
            }
        }
    }

}