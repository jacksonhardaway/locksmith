package gg.moonflower.locksmith.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import gg.moonflower.locksmith.core.Locksmith;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

public class LocksmithParticles {

    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Locksmith.MOD_ID, Registry.PARTICLE_TYPE_REGISTRY);

    public static final RegistrySupplier<SimpleParticleType> LOCK_BREAK = REGISTRY.register("lock_break", () -> new SimpleParticleType(true) {});
    public static final RegistrySupplier<SimpleParticleType> LOCK_SPARK = REGISTRY.register("lock_spark", () -> new SimpleParticleType(true) {});
}
