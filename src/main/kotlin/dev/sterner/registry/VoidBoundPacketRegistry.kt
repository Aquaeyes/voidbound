package dev.sterner.registry

import dev.sterner.VoidBound
import dev.sterner.networking.EnchantmentLevelPacket
import dev.sterner.networking.HeartParticlePacket
import dev.sterner.networking.SpiritBinderParticlePacket
import io.github.fabricators_of_create.porting_lib.util.EnvExecutor
import me.pepperbell.simplenetworking.SimpleChannel
import net.fabricmc.api.EnvType
import team.lodestar.lodestone.network.interaction.RightClickEmptyPacket
import team.lodestar.lodestone.registry.common.LodestonePacketRegistry


object VoidBoundPacketRegistry {

    const val PROTOCOL_VERSION: String = "1"
    val VOIDBOUND_CHANNEL: SimpleChannel = SimpleChannel(VoidBound.id("main"))

    init {

    }

    fun registerVoidBoundPackets() {
        VOIDBOUND_CHANNEL.initServerListener()
        EnvExecutor.runWhenOn(
            EnvType.CLIENT
        ) { Runnable { VOIDBOUND_CHANNEL.initClientListener() } }

        var index = 0
        VOIDBOUND_CHANNEL.registerS2CPacket(
            SpiritBinderParticlePacket::class.java, index++
        )

        VOIDBOUND_CHANNEL.registerS2CPacket(
            HeartParticlePacket::class.java, index++
        )

        //C2S
        VOIDBOUND_CHANNEL.registerC2SPacket(EnchantmentLevelPacket::class.java, index++)
    }

}