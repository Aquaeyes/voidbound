package dev.sterner.api

import dev.sterner.common.item.CrimsonBookItem
import net.minecraft.client.Minecraft


object ClientTickHandler {

    var ticksWithCrimsonOpen: Int = 0
    var ticksInGame: Int = 0
    private var partialTicks: Float = 0f

    fun total(): Float {
        return ticksInGame + partialTicks
    }

    fun renderTick(renderTickTime: Float) {
        partialTicks = renderTickTime
    }

    fun clientTickEnd(mc: Minecraft) {
        if (!mc.isPaused) {
            ticksInGame++
            partialTicks = 0f
        }

        val ticksToOpen = 10
        if (CrimsonBookItem.isOpen()) {
            if (ticksWithCrimsonOpen < 0) {
                ticksWithCrimsonOpen = 0
            }
            if (ticksWithCrimsonOpen < ticksToOpen) {
                ticksWithCrimsonOpen++
            }
        } else {
            if (ticksWithCrimsonOpen > 0) {
                if (ticksWithCrimsonOpen > ticksToOpen) {
                    ticksWithCrimsonOpen = ticksToOpen
                }
                ticksWithCrimsonOpen--
            }
        }
    }
}