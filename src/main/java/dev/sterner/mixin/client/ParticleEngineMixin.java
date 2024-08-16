package dev.sterner.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.sammy.malum.common.entity.nitrate.AbstractNitrateEntity;
import com.sammy.malum.registry.client.ParticleRegistry;
import com.sammy.malum.visual_effects.SpiritLightSpecs;
import dev.sterner.api.VoidBoundApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.helpers.RandomHelper;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
import team.lodestar.lodestone.systems.particle.world.behaviors.components.DirectionalBehaviorComponent;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;

import java.awt.*;
import java.util.function.Consumer;

import static com.sammy.malum.common.item.curiosities.weapons.staff.AuricFlameStaffItem.AURIC_COLOR_DATA;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {

    @Shadow protected ClientLevel level;

    @Shadow @Final private RandomSource random;
    @Unique
    private static final Color AURIC_YELLOW = new Color(239, 175, 95);
    @Unique
    private static final Color AURIC_WHITE = new Color(236, 200, 190);

    @Unique
    private static final ColorParticleData AURIC_COLOR_DATA = ColorParticleData.create(AURIC_YELLOW, AURIC_WHITE).setEasing(Easing.SINE_IN_OUT).setCoefficient(0.9f).build();

    @WrapWithCondition(method = "crack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;add(Lnet/minecraft/client/particle/Particle;)V"))
    private boolean voidbound$ward(ParticleEngine instance, Particle effect, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) Direction side, @Local BlockState blockState){
        if (Minecraft.getInstance().player != null && !VoidBoundApi.INSTANCE.canPlayerBreakBlock(level, Minecraft.getInstance().player, pos)) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            float f = 0.03F;
            AABB aABB = blockState.getShape(this.level, pos).bounds();
            double d = (double)i + this.random.nextDouble() * (aABB.maxX - aABB.minX - 0.2F) + 0.1F + aABB.minX;
            double e = (double)j + this.random.nextDouble() * (aABB.maxY - aABB.minY - 0.2F) + 0.1F + aABB.minY;
            double g = (double)k + this.random.nextDouble() * (aABB.maxZ - aABB.minZ - 0.2F) + 0.1F + aABB.minZ;
            if (side == Direction.DOWN) {
                e = (double)j + aABB.minY - f;
            }

            if (side == Direction.UP) {
                e = (double)j + aABB.maxY + f;
            }

            if (side == Direction.NORTH) {
                g = (double)k + aABB.minZ - f;
            }

            if (side == Direction.SOUTH) {
                g = (double)k + aABB.maxZ + f;
            }

            if (side == Direction.WEST) {
                d = (double)i + aABB.minX - f;
            }

            if (side == Direction.EAST) {
                d = (double)i + aABB.maxX + f;
            }

            var lightSpecs = SpiritLightSpecs.spiritLightSpecs(level, new Vec3(d, e, g), AURIC_COLOR_DATA);
            lightSpecs.getBuilder().multiplyLifetime(1.5f);
            lightSpecs.getBloomBuilder().multiplyLifetime(1.5f);
            lightSpecs.spawnParticles();

            for (int ix = 0; ix < 3; ix++) {
                int lifetime = (int) (RandomHelper.randomBetween(random, 60, 80) * (1 - ix / 3f));
                final SpinParticleData spinData = SpinParticleData.createRandomDirection(random, 0, RandomHelper.randomBetween(random, 0f, 0.4f), 0).randomSpinOffset(random).build();

                var scale = GenericParticleData.create(0.3f, 0.4f, 0.5f).setEasing(Easing.QUINTIC_OUT, Easing.SINE_IN).build();
                var tans = GenericParticleData.create(0.2f * 1, 0.4f * 1, 0f).setEasing(Easing.SINE_IN_OUT, Easing.SINE_IN).build();

                spawnWardParticles(d, e, g, side, tans, scale, spinData, lifetime);
            }

            int lifetime = (RandomHelper.randomBetween(random, 60, 80));
            final SpinParticleData spinData = SpinParticleData.createRandomDirection(random, 0, RandomHelper.randomBetween(random, 0f, 0.4f), 0).randomSpinOffset(random).build();
            var scaleData = GenericParticleData.create(0.05f, 0.1f, 0.15f).setEasing(Easing.QUINTIC_OUT, Easing.SINE_IN).build();
            var trans = GenericParticleData.create(0.7f * 1, 0.9f * 1, 0f).setEasing(Easing.SINE_IN_OUT, Easing.SINE_IN).build();
            spawnWardParticles(d, e, g, side, trans, scaleData, spinData, lifetime);
            return false;
        }

        return true;
    }

    @Unique
    private void spawnWardParticles(double x, double y, double z, Direction direction, GenericParticleData transparencyData, GenericParticleData scaleData, SpinParticleData spinData, int lifetime){
        var v = direction.getNormal();
        WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE, new DirectionalBehaviorComponent(new Vec3(v.getX(), v.getY(), v.getZ())))
                .setTransparencyData(transparencyData)
                .setSpinData(spinData)
                .setScaleData(scaleData)
                .setColorData(ColorParticleData.create(AURIC_YELLOW, AbstractNitrateEntity.SECOND_SMOKE_COLOR).setEasing(Easing.QUINTIC_OUT).build())
                .setLifetime(Math.min(6 + 3, lifetime))
                .setLifeDelay(1)
                .enableNoClip()
                .enableForcedSpawn()
                .setSpritePicker(SimpleParticleOptions.ParticleSpritePicker.WITH_AGE)
                .setRenderType(LodestoneWorldParticleRenderType.ADDITIVE)
                .spawn(level, x, y, z);
    }
}
