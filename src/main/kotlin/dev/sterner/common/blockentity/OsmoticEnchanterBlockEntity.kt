package dev.sterner.common.blockentity

import com.sammy.malum.common.block.MalumBlockEntityInventory
import com.sammy.malum.registry.common.SpiritTypeRegistry
import dev.sterner.api.VoidBoundApi
import dev.sterner.api.rift.SimpleSpiritCharge
import dev.sterner.networking.UpdateSpiritAmountPacket
import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import dev.sterner.registry.VoidBoundPacketRegistry
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.block.state.BlockState
import team.lodestar.lodestone.helpers.BlockHelper
import team.lodestar.lodestone.systems.blockentity.ItemHolderBlockEntity
import java.util.*
import java.util.stream.Collectors
import kotlin.streams.toList


class OsmoticEnchanterBlockEntity(pos: BlockPos, state: BlockState?) : ItemHolderBlockEntity(VoidBoundBlockEntityTypeRegistry.OSMOTIC_ENCHANTER.get(), pos,
    state
) {

    data class EnchantmentData(val enchantment: Enchantment, val level: Int)
    
    var enchantments: MutableList<EnchantmentData> = mutableListOf()

    var cachedEnchantments: MutableList<Int>? = mutableListOf()
    var activated = false

    var spiritsToConsume: SimpleSpiritCharge = SimpleSpiritCharge()
    var consumedSpirits: SimpleSpiritCharge = SimpleSpiritCharge()

    var cooldown: Int = 0
    
    init {
        inventory = object : MalumBlockEntityInventory(1, 64) {
            public override fun onContentsChanged(slot: Int) {
                spiritsToConsume = SimpleSpiritCharge()
                consumedSpirits = SimpleSpiritCharge()
                cachedEnchantments = mutableListOf()
                enchantments = mutableListOf()
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

    fun calculateSpiritRequired() : Boolean {
        var bl = false
        val spirits = SimpleSpiritCharge()

        for (enchantmentInfo in this.enchantments) {
            val sc = VoidBoundApi.getSpiritFromEnchant(enchantmentInfo.enchantment, enchantmentInfo.level)
            for (spirit in sc) {
                spirits.addToCharge(spirit.type, spirit.count)
            }
            bl = true
        }

        spiritsToConsume = spirits
        if (level is ServerLevel) {
            VoidBoundPacketRegistry.VOIDBOUND_CHANNEL.sendToClientsTracking(UpdateSpiritAmountPacket(spiritsToConsume, blockPos.asLong()), this)
        }
        return bl
    }

    fun receiveScreenData(enchantment: Enchantment, level: Int) {
        enchantments.removeIf { it.enchantment == enchantment }
        enchantments.add(EnchantmentData(enchantment, level))
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
        if (activated) {
            cooldown++

            // Check if cooldown has passed 2 ticks
            if (cooldown > 2) {
                // Variable to track if a spirit has been consumed
                var spiritConsumed = false

                //TODO this whole segment needs to change to accept spirits from a source, now the source is infinite

                // Iterate through each spirit type, but stop after one spirit is processed
                for (spiritType in SpiritTypeRegistry.SPIRITS) {
                    val requiredCharge = spiritsToConsume.getChargeForType(spiritType.value)
                    val currentCharge = consumedSpirits.getChargeForType(spiritType.value)

                    // Check if more charge needs to be added for this spirit type
                    if (currentCharge < requiredCharge) {
                        consumedSpirits.addToCharge(spiritType.value, 1)
                        spiritConsumed = true  // Mark that a spirit was consumed

                        // Exit the loop after processing this spirit type
                        break
                    }
                }

                // Reset cooldown if a spirit was consumed
                if (spiritConsumed) {
                    cooldown = 0
                }
            }

            // If all the spirits are consumed, perform the enchantment
            if (consumedSpirits.getTotalCharge() >= spiritsToConsume.getTotalCharge()) {
                doEnchant()
            }
        }

        super.tick()
    }

    private fun doEnchant() {
        for (enchantment in this.enchantments) {
            inventory.getStackInSlot(0).enchant(enchantment.enchantment, enchantment.level)
        }
        enchantments.clear()
        refreshEnchants()
        spiritsToConsume = SimpleSpiritCharge()
        consumedSpirits = SimpleSpiritCharge()
        activated = false
        cooldown = 0
        notifyUpdate()
    }

    fun refreshEnchants() {
        var enchantmentObjects = getAvailableEnchants(enchantments.stream().map {
            enchantmentInfo: EnchantmentData -> enchantmentInfo.enchantment
        }.collect(Collectors.toList()))

        enchantmentObjects = enchantmentObjects.filter { !it.isCurse }.toMutableList()
        cachedEnchantments = enchantmentObjects.stream().map(BuiltInRegistries.ENCHANTMENT::getId).toList().toMutableList()
        notifyUpdate()
    }

    private fun canApply(itemStack: ItemStack, enchantment: Enchantment, currentEnchants: List<Enchantment?>): Boolean {
        return canApply(itemStack, enchantment, currentEnchants, true)
    }

    private fun canApply(
        itemStack: ItemStack,
        enchantment: Enchantment,
        currentEnchants: List<Enchantment?>,
        checkConflicts: Boolean
    ): Boolean {
        if (!enchantment.canEnchant(itemStack) || currentEnchants.contains(enchantment)) {
            return false
        }

        if (EnchantmentHelper.getEnchantments(itemStack).keys.contains(enchantment)) {
            return false
        }

        if (checkConflicts) {
            for (curEnchant in currentEnchants) {
                if (!curEnchant!!.isCompatibleWith(enchantment)) {
                    return false
                }
            }
        }
        return true
    }

    private fun getValidEnchantments(): List<Enchantment> {
        val enchantments: MutableList<Enchantment> = ArrayList()
        if (inventory.getStackInSlot(0) == ItemStack.EMPTY) return enchantments
        val item = inventory.getStackInSlot(0)
        for (enchantment in BuiltInRegistries.ENCHANTMENT) {
            if (enchantment.canEnchant(item) && item.isEnchantable && canApply(item, enchantment, enchantments, false)) {
                enchantments.add(enchantment)
            }
        }
        return enchantments
    }

    private fun getAvailableEnchants(currentEnchants: List<Enchantment?>): MutableList<Enchantment> {
        val enchantments: MutableList<Enchantment> = ArrayList()
        if (inventory.getStackInSlot(0) == ItemStack.EMPTY) return enchantments
        val item: ItemStack = inventory.getStackInSlot(0)
        val valid: List<Enchantment> = getValidEnchantments()
        for (validEnchant in valid) {
            if (item.item.enchantmentValue != 0 && canApply(
                    item,
                    validEnchant,
                    enchantments,
                    false
                ) && canApply(item, validEnchant, currentEnchants)
            ) enchantments.add(validEnchant)
        }
        return enchantments
    }

    override fun saveAdditional(compound: CompoundTag) {
        compound.putIntArray("Enchants", enchantments.stream().mapToInt { i -> BuiltInRegistries.ENCHANTMENT.getId(i.enchantment) }.toArray())
        compound.putIntArray("Level", enchantments.stream().mapToInt { i -> i.level }.toArray())

        cachedEnchantments?.stream()?.mapToInt {i -> i}?.toList()?.toMutableList()?.let { compound.putIntArray("Cache", it) }
        spiritsToConsume.serializeNBT(compound)
        val consumedNbt = CompoundTag()
        consumedSpirits.serializeNBT(consumedNbt)
        compound.put("Consumed", consumedNbt)
        compound.putBoolean("Activated", activated)
        super.saveAdditional(compound)
    }

    override fun load(compound: CompoundTag) {
        if (compound.contains("Enchants")) {
            enchantments.clear()

            val enchantmentArray = compound.getIntArray("Enchants")
            val levelArray = compound.getIntArray("Level")

            for ((index, enchantment) in enchantmentArray.withIndex()) {
                enchantments.add(EnchantmentData(Enchantment.byId(enchantment)!!, levelArray[index]))
            }
        }
        if (compound.contains("Cache")) {
            cachedEnchantments?.clear()
            val cachedEnchantmentArray = compound.getIntArray("Cache")
            Arrays.stream(cachedEnchantmentArray).forEach{i -> cachedEnchantments?.add(i)}
        }
        spiritsToConsume = spiritsToConsume.deserializeNBT(compound)
        if (compound.contains("Consumed")) {
            val c = compound.get("Consumed") as CompoundTag
            consumedSpirits = consumedSpirits.deserializeNBT(c)
        }
        activated = compound.getBoolean("Activated")

        super.load(compound)
    }



}