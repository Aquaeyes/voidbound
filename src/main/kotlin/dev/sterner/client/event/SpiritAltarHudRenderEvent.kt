package dev.sterner.client.event

import com.mojang.authlib.minecraft.client.MinecraftClient
import com.mojang.blaze3d.vertex.PoseStack
import com.sammy.malum.common.block.curiosities.spirit_altar.SpiritAltarBlockEntity
import com.sammy.malum.common.recipe.SpiritInfusionRecipe
import dev.sterner.VoidBound
import dev.sterner.api.utils.RenderUtils
import dev.sterner.registry.VoidBoundItemRegistry
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.BlockHitResult


object SpiritAltarHudRenderEvent {

    fun spiritAltarRecipeHud(guiGraphics: GuiGraphics, fl: Float) {
        val client: Minecraft = Minecraft.getInstance()
        if (client.player != null && client.player!!.getItemBySlot(EquipmentSlot.HEAD).`is`(VoidBoundItemRegistry.HALLOWED_GOGGLES.get())
        ) {
            if (client.level != null && client.hitResult is BlockHitResult) {
                val result = client.hitResult as BlockHitResult
                val pos: BlockPos = result.blockPos
                if (client.level!!.getBlockEntity(pos) is SpiritAltarBlockEntity) {
                    val se = client.level!!.getBlockEntity(pos) as SpiritAltarBlockEntity
                    val recipes: List<SpiritInfusionRecipe> = se.possibleRecipes
                    //println(recipes)
                    if (recipes.isNotEmpty()) {
                        val matrixStack = guiGraphics.pose()
                        matrixStack.pushPose()
                        matrixStack.translate(
                            if (client.window.guiScaledWidth % 2 !== 0) 0.5 else 0.0,
                            if (client.window.guiScaledHeight % 2 !== 0) 0.5 else 0.0,
                            0.0
                        )
                        var startX: Int = client.window.guiScaledWidth / 2
                        var centerY: Int = client.window.guiScaledHeight / 2
                        startX -= recipes.size / 2 * 16
                        centerY += 10
                        matrixStack.translate(startX.toDouble(), centerY.toDouble(), 0.0)
                        val recipe = recipes[0]
                        val spirits = recipe.spirits
                        val extras = recipe.extraItems
                        val inventory: ItemStack = se.extrasInventory.getItem(0)

                        for (extra in extras) {
                            //println(extra)
                            val renderStack = ItemStack(extra.item, extra.getCount())
                            val checked = extra.item === inventory.item
                            guiGraphics.renderItem(renderStack, 0, 0)
                            println("$checked, ${renderStack}")
                            if (!checked) {
                                guiGraphics.renderItemDecorations(client.font, renderStack, 0,0)
                            } else {
                                RenderUtils.drawIcon(matrixStack, RenderUtils.CHECKMARK)
                            }
                            matrixStack.translate(16.0, 0.0, 0.0)
                        }
                        matrixStack.popPose()
                    }
                }
            }
        }
    }
}