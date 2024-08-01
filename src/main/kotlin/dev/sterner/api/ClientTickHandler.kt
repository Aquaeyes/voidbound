package dev.sterner.api

import net.minecraft.client.Minecraft


object ClientTickHandler {
    var ticksInGame: Int = 0
    var partialTicks: Float = 0f

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
    }
}