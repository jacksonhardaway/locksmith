package gg.moonflower.locksmith.core.registry;

import gg.moonflower.locksmith.client.particle.LockSparkParticle;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.pollen.api.event.events.registry.client.ParticleFactoryRegistryEvent;
import gg.moonflower.pollen.api.registry.PollinatedRegistry;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

public class LocksmithParticles {

    public static final PollinatedRegistry<ParticleType<?>> PARTICLES = PollinatedRegistry.create(Registry.PARTICLE_TYPE, Locksmith.MOD_ID);

    public static final Supplier<SimpleParticleType> LOCK_BREAK = PARTICLES.register("lock_break", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> LOCK_SPARK = PARTICLES.register("lock_spark", () -> new SimpleParticleType(true));

    public static void setupClient() {
        ParticleFactoryRegistryEvent.EVENT.register(registry -> {
            registry.register(LOCK_BREAK.get(), SmokeParticle.Provider::new);
            registry.register(LOCK_SPARK.get(), LockSparkParticle.Provider::new);
        });
    }
}
