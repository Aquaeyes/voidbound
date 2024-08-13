package dev.sterner.common.blockentity

import com.sammy.malum.common.block.MalumBlockEntityInventory
import dev.sterner.registry.VoidBoundBlockEntityTypeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
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

    var enchantments: MutableList<Int> = mutableListOf()
    val levels: MutableList<Int> = mutableListOf()

    var cachedEnchantments: MutableList<Int> = mutableListOf()
    private var progress = 0
    private val cooldown = 0

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

    fun recieveScreenData(enchantment: Enchantment, level: Int) {

    }

    fun refreshEnchants() {
        var enchantmentObjects = getAvailableEnchants(enchantments.stream().map(Enchantment::byId).collect(Collectors.toList()))
        enchantmentObjects = enchantmentObjects.filter { !it.isCurse }.toMutableList()
        cachedEnchantments = enchantmentObjects.stream().map(BuiltInRegistries.ENCHANTMENT::getId).toList()
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
        compound.putIntArray("Enchants", enchantments.stream().mapToInt { i -> i }.toArray())
        compound.putIntArray("Levels", levels.stream().mapToInt { i -> i }.toArray())
        compound.putIntArray("Cache", cachedEnchantments.stream().mapToInt {i -> i}.toArray())
        compound.putInt("Progress", progress)
        super.saveAdditional(compound)
    }

    override fun load(compound: CompoundTag) {
        if (compound.contains("Enchants")) {
            enchantments.clear()
            levels.clear()
            cachedEnchantments.clear()

            val enchantmentArray = compound.getIntArray("Enchants")
            Arrays.stream(enchantmentArray).forEach { i -> enchantments.add(i) }
            val levelsArray = compound.getIntArray("Levels")
            Arrays.stream(levelsArray).forEach{i -> levels.add(i)}
            val cachedEnchantmentArray = compound.getIntArray("Cache")
            Arrays.stream(cachedEnchantmentArray).forEach{i -> cachedEnchantments.add(i)}
        }
        if (compound.contains("Progress")) {
            progress = compound.getInt("Progress")
        }

        super.load(compound)
    }
}