package dev.hardaway.locksmith.core.registry;

import dev.hardaway.locksmith.core.Locksmith;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LocksmithParticles {

    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Registries.PARTICLE_TYPE, Locksmith.MOD_ID);

    public static final Supplier<SimpleParticleType> LOCK_BREAK = REGISTRY.register("lock_break", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> LOCK_SPARK = REGISTRY.register("lock_spark", () -> new SimpleParticleType(true));
}