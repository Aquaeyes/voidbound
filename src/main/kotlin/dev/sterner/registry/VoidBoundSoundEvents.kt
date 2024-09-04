package dev.sterner.registry

import com.sammy.malum.MalumMod
import com.sammy.malum.registry.common.SoundRegistry
import dev.sterner.VoidBound
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import io.github.fabricators_of_create.porting_lib.util.RegistryObject
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvent

object VoidBoundSoundEvents {

    val SOUNDS = LazyRegistrar.create(BuiltInRegistries.SOUND_EVENT, VoidBound.modid)


    val SOUL_SPEAK: RegistryObject<SoundEvent> =
        SoundRegistry.register(SoundEvent.createVariableRangeEvent(VoidBound.id("soul_speak")))

}