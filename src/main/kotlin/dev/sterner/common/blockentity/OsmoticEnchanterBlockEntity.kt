package dev.sterner.common.blockentity

import com.sammy.malum.common.block.MalumBlockEntityInventory
import com.sammy.malum.core.systems.recipe.SpiritWithCount
import dev.sterner.api.VoidBoundApi
import dev.sterner.api.rift.SimpleSpiritCharge
import dev.sterner.networking.UpdateSpiritAmountPacket
import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import dev.sterner.registry.VoidBoundPacketRegistry
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import team.lodestar.lodestone.helpers.BlockHelper
import team.lodestar.lodestone.systems.blockentity.ItemHolderBlockEntity
import java.util.*
import kotlin.math.PI


class OsmoticEnchanterBlockEntity(pos: BlockPos, state: BlockState?) : ItemHolderBlockEntity(
    VoidBoundBlockEntityTypeRegistry.OSMOTIC_ENCHANTER.get(), pos,
    state
) {

    data class EnchantmentData(val enchantment: Enchantment, val level: Int, val active: Boolean)

    var time: Int = 0
    var rot: Float = 0f
    var oRot: Float = 0f
    private var tRot: Float = 0f

    var activeEnchantments: MutableList<EnchantmentData> = mutableListOf()

    var activated = false

    var spiritsToConsume: SimpleSpiritCharge = SimpleSpiritCharge()
    var consumedSpirits: SimpleSpiritCharge = SimpleSpiritCharge()

    private var cooldown: Int = 0

    private var foundRiftPos: BlockPos? = null

    init {
        inventory = object : MalumBlockEntityInventory(1, 64) {
            public override fun onContentsChanged(slot: Int) {
                spiritsToConsume = SimpleSpiritCharge()
                consumedSpirits = SimpleSpiritCharge()
                activeEnchantments = mutableListOf()
                refreshEnchants()
                this.setChanged()
                needsSync = true
                BlockHelper.updateAndNotifyState(level, worldPosition)
                updateData()
                super.onContentsChanged(slot)
            }
        }
    }

    fun startEnchanting() {
        if (calculateSpiritRequired()) {
            activated = true
            notifyUpdate()
        }
    }

    fun calculateSpiritRequired(): Boolean {
        var bl = false
        val spirits = SimpleSpiritCharge()

        for (enchantmentInfo in this.activeEnchantments) {
            val sc = VoidBoundApi.getSpiritFromEnchant(enchantmentInfo.enchantment, enchantmentInfo.level)
            for (spirit in sc) {
                spirits.addToCharge(spirit.type, spirit.count)
            }
            bl = true
        }

        spiritsToConsume = spirits
        if (level is ServerLevel) {
            VoidBoundPacketRegistry.VOID_BOUND_CHANNEL.sendToClientsTracking(
                UpdateSpiritAmountPacket(
                    spiritsToConsume,
                    blockPos.asLong()
                ), this
            )
        }
        return bl
    }

    fun updateEnchantmentData(enchantment: Enchantment, level: Int, active: Boolean) {
        activeEnchantments.removeIf { it.enchantment == enchantment }
        activeEnchantments.add(EnchantmentData(enchantment, level, active))
        calculateSpiritRequired()
        notifyUpdate()
    }

    override fun onUse(player: Player, hand: InteractionHand?): InteractionResult {
        if (player.mainHandItem.isEnchantable) {
            inventory.interact(
                this, player.level(), player, hand
            ) { s: ItemStack? -> true }
        }
        return InteractionResult.SUCCESS
    }

    override fun tick() {

        if (level!!.isClientSide) {
            animationTick()
        }
        super.tick()

        if (activated) {
            cooldown++

            if (cooldown > 1) {
                // Variable to track if a spirit has been consumed
                var spiritConsumed = false

                //Look for a nearby Rift
                tickFindRift()

                //Try to consume spirit from saved rift
                spiritConsumed = tickCloseRift()

                //If no spirit was consumed, try to consume from other source
                if (!spiritConsumed) {
                    spiritConsumed = tickOtherSpiritSource()
                }

                // Reset cooldown if a spirit was consumed
                if (spiritConsumed) {
                    cooldown = 0
                }

                // If all the spirits are consumed, perform the enchantment
                if (consumedSpirits.getTotalCharge() >= spiritsToConsume.getTotalCharge()) {
                    doEnchant()
                }

                notifyUpdate()
            }
        }
    }

    //TODO add another way to generate spirit from shards
    private fun tickOtherSpiritSource(): Boolean {
        return false
    }

    /**
     * Called in the tick function, looks for a rift close by with a default of 3 blocks in radius
     */
    private fun tickFindRift(range: Int = 3) {
        val pos = blockPos
        for (aroundPos in BlockPos.betweenClosed(pos.x - range, pos.y, pos.z - range, pos.x + range, pos.y + range, pos.z + range)) {
            if (level?.getBlockEntity(aroundPos) is SpiritRiftBlockEntity) {
                foundRiftPos = aroundPos
                break
            }
        }
    }

    /**
     * If foundRiftPos has been found, get its charge and start transfer spirits to the enchanter.
     * Returns true if a transaction was successful
     */
    private fun tickCloseRift(): Boolean {
        if (foundRiftPos != null && level!!.getBlockEntity(foundRiftPos!!) is SpiritRiftBlockEntity) {
            val rift = level!!.getBlockEntity(foundRiftPos!!) as SpiritRiftBlockEntity
            val riftSpirits: MutableList<SpiritWithCount> = rift.simpleSpiritCharge.getNonEmptyMutableList()

            var i = 0
            while (i < spiritsToConsume.getNonEmptyMutableList().size) {
                val spiritType = spiritsToConsume.getNonEmptyMutableList()[i]
                val requiredCharge = spiritsToConsume.getChargeForType(spiritType.type)
                val currentCharge = consumedSpirits.getChargeForType(spiritType.type)

                if (currentCharge < requiredCharge) {
                    // Find the corresponding spirit in the rift
                    val riftSpirit = riftSpirits.find { it.type == spiritType.type }

                    if (riftSpirit != null && riftSpirit.count > 0) {
                        // Transfer one spirit at a time
                        consumedSpirits.addToCharge(spiritType.type, 1)
                        rift.simpleSpiritCharge.removeFromCharge(riftSpirit.type, 1)
                        rift.notifyUpdate()

                        // Return true after transferring one spirit to prevent jittering
                        return true
                    }
                } else {
                    // If the current spirit type's charge is full, move to the next one
                    i++
                }
            }
        }
        return false
    }

    /**
     * Handles the book model rotating towards the player withing 3 blocks
     */
    private fun animationTick() {
        oRot = rot
        val player = level!!.getNearestPlayer(
            blockPos.x.toDouble() + 0.5,
            blockPos.y.toDouble() + 0.5,
            blockPos.z.toDouble() + 0.5,
            3.0,
            false
        )

        val dir = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)

        if (player != null) {

            val d: Double = player.x - (blockPos.x.toDouble() + 0.5)
            val e: Double = player.z - (blockPos.z.toDouble() + 0.5)
            tRot = Mth.atan2(e, d).toFloat() - (PI / 2f).toFloat()

        } else {

            tRot = when (dir) {
                Direction.NORTH -> 180f
                Direction.SOUTH -> 0f
                Direction.WEST -> 90f
                else -> -90f
            } * (PI / 180f).toFloat()
        }

        while (rot >= Math.PI.toFloat()) {
            rot -= (Math.PI * 2).toFloat()
        }

        while (rot < -Math.PI.toFloat()) {
            rot += (Math.PI * 2).toFloat()
        }

        while (tRot >= Math.PI.toFloat()) {
            tRot -= (Math.PI * 2).toFloat()
        }

        while (tRot < -Math.PI.toFloat()) {
            tRot += (Math.PI * 2).toFloat()
        }

        var g: Float = tRot - rot

        while (g >= Math.PI.toFloat()) {
            g -= (Math.PI * 2).toFloat()
        }

        while (g < -Math.PI.toFloat()) {
            g += (Math.PI * 2).toFloat()
        }

        rot += g * 0.4f
        time++
    }

    /**
     * Enchant the item and reset the enchanter
     */
    private fun doEnchant() {
        for (enchantment in this.activeEnchantments) {
            inventory.getStackInSlot(0).enchant(enchantment.enchantment, enchantment.level)
        }
        activeEnchantments.clear()
        refreshEnchants()
        spiritsToConsume = SimpleSpiritCharge()
        consumedSpirits = SimpleSpiritCharge()
        activated = false
        cooldown = 0
        notifyUpdate()
    }

    fun refreshEnchants() {
        activeEnchantments = getValidEnchantments()
        notifyUpdate()
    }

    private fun canApply(
        itemStack: ItemStack,
        enchantment: Enchantment,
        currentEnchants: List<Enchantment?>
    ): Boolean {
        if (!enchantment.canEnchant(itemStack) || currentEnchants.contains(enchantment)) {
            return false
        }

        if (EnchantmentHelper.getEnchantments(itemStack).keys.contains(enchantment)) {
            return false
        }
        return true
    }

    private fun getValidEnchantments(): MutableList<EnchantmentData> {
        val enchantments: MutableList<EnchantmentData> = ArrayList()
        if (inventory.getStackInSlot(0) == ItemStack.EMPTY) return enchantments
        val item = inventory.getStackInSlot(0)
        for (enchantment in BuiltInRegistries.ENCHANTMENT) {
            if (enchantment.canEnchant(item) && item.isEnchantable && canApply(item, enchantment, enchantments.map { it.enchantment }) && !enchantment.isCurse) {
                enchantments.add(EnchantmentData(enchantment, 1, false))
            }
        }
        return enchantments
    }

    override fun saveAdditional(compound: CompoundTag) {
        compound.putIntArray(
            "Enchants",
            activeEnchantments.stream().mapToInt { i -> BuiltInRegistries.ENCHANTMENT.getId(i.enchantment) }.toArray()
        )
        compound.putIntArray("Level", activeEnchantments.stream().mapToInt { i -> i.level }.toArray())
        compound.putIntArray("ActiveEnchant", activeEnchantments.stream().mapToInt { i -> if (i.active) 1 else 0 }.toArray())

        spiritsToConsume.serializeNBT(compound)
        val consumedNbt = CompoundTag()
        consumedSpirits.serializeNBT(consumedNbt)
        compound.put("Consumed", consumedNbt)
        compound.putBoolean("Activated", activated)

        if (foundRiftPos != null) {
            compound.put("RiftPos", NbtUtils.writeBlockPos(foundRiftPos!!))
        }

        super.saveAdditional(compound)
    }

    override fun load(compound: CompoundTag) {
        if (compound.contains("Enchants") && compound.contains("ActiveEnchant")) {
            activeEnchantments.clear()

            val enchantmentArray = compound.getIntArray("Enchants")
            val levelArray = compound.getIntArray("Level")
            val activeArray = compound.getIntArray("ActiveEnchant")

            for ((index, enchantment) in enchantmentArray.withIndex()) {
                activeEnchantments.add(EnchantmentData(Enchantment.byId(enchantment)!!, levelArray[index], activeArray[index] == 1))
            }
        }

        spiritsToConsume = spiritsToConsume.deserializeNBT(compound)
        if (compound.contains("Consumed")) {
            val c = compound.get("Consumed") as CompoundTag
            consumedSpirits = consumedSpirits.deserializeNBT(c)
        }
        activated = compound.getBoolean("Activated")

        if (compound.contains("RiftPos")) {
            foundRiftPos = NbtUtils.readBlockPos(compound.getCompound("RiftPos"))
        }

        super.load(compound)
    }
}