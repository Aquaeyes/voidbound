package dev.sterner.common.blockentity

import com.sammy.malum.common.block.MalumBlockEntityInventory
import dev.sterner.api.VoidBoundApi
import dev.sterner.api.rift.SimpleSpiritCharge
import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
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


class OsmoticEnchanterBlockEntity(pos: BlockPos, state: BlockState?) : ItemHolderBlockEntity(VoidBoundBlockEntityTypeRegistry.OSMOTIC_ENCHANTER.get(), pos,
    state
) {

    data class EnchantmentInfo(val enchantment: Enchantment, val level: Int) {}
    
    var enchantments: MutableList<EnchantmentInfo> = mutableListOf()

    var cachedEnchantments: MutableList<Int> = mutableListOf()
    var progress = 0
    var targetProgress: Int? = null
    var activated = false
    
    
    init {
        inventory = object : MalumBlockEntityInventory(1, 64) {
            public override fun onContentsChanged(slot: Int) {
                this.setChanged()
                refreshEnchants()
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

    private fun calculateSpiritRequired() : Boolean {
        var bl = false
        val spirits = SimpleSpiritCharge()

        for (enchantmentInfo in this.enchantments) {
            val sc = VoidBoundApi.getSpiritFromEnchant(enchantmentInfo.enchantment, enchantmentInfo.level)
            spirits.addToCharge(sc.type, sc.count)
            bl = true
        }

        targetProgress = spirits.getTotalCharge()

        return bl
    }


    fun receiveScreenData(enchantment: Enchantment, level: Int) {
        enchantments.removeIf { it.enchantment == enchantment }
        enchantments.add(EnchantmentInfo(enchantment, level))
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

        if (activated && targetProgress != null) {
            progress++
            if (progress >= 20 * 5) {
                for (enchantment in this.enchantments) {
                    inventory.getStackInSlot(0).enchant(enchantment.enchantment, enchantment.level)
                }
                enchantments.clear()
                refreshEnchants()
                progress = 0
                activated = false
                notifyUpdate()
            }

        } else {
            progress = 0
        }

        super.tick()
    }

    fun refreshEnchants() {
        var enchantmentObjects = getAvailableEnchants(enchantments.stream().map {
            enchantmentInfo: EnchantmentInfo -> enchantmentInfo.enchantment
        }.collect(Collectors.toList()))

        enchantmentObjects = enchantmentObjects.filter { !it.isCurse }.toMutableList()
        cachedEnchantments = enchantmentObjects.stream().map(BuiltInRegistries.ENCHANTMENT::getId).toList() as MutableList<Int>
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

        compound.putIntArray("Cache", cachedEnchantments.stream().mapToInt {i -> i}.toArray())
        compound.putInt("Progress", progress)
        compound.putBoolean("Activated", activated)
        super.saveAdditional(compound)
    }

    override fun load(compound: CompoundTag) {
        if (compound.contains("Enchants")) {
            enchantments.clear()

            val enchantmentArray = compound.getIntArray("Enchants")
            val levelArray = compound.getIntArray("Level")

            for ((index, enchantment) in enchantmentArray.withIndex()) {
                enchantments.add(EnchantmentInfo(Enchantment.byId(enchantment)!!, levelArray[index]))
            }
        }
        if (compound.contains("Cache")) {
            cachedEnchantments.clear()
            val cachedEnchantmentArray = compound.getIntArray("Cache")
            Arrays.stream(cachedEnchantmentArray).forEach{i -> cachedEnchantments.add(i)}
        }
        if (compound.contains("Progress")) {
            progress = compound.getInt("Progress")
        }
        activated = compound.getBoolean("Activated")

        super.load(compound)
    }



}