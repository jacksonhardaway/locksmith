package dev.hardaway.locksmith.core;

import dev.hardaway.locksmith.client.particle.LockSparkParticle;
import dev.hardaway.locksmith.core.registry.LocksmithParticles;
import net.minecraft.client.particle.SmokeParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@Mod(value = Locksmith.MOD_ID, dist = Dist.CLIENT)
public class LocksmithClient {

    public LocksmithClient(IEventBus bus) {
        bus.addListener(this::registerMenuScreens);
        bus.addListener(this::registerParticleProviders);
    }

    private void setup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
//            ItemProperties.register(LocksmithItems.KEYRING.get(), Locksmith.path("keys"), (stack, level, livingEntity, i) -> KeyringItem.getKeys(stack).size() / (float) KeyringItem.MAX_KEYS);
        });
    }

    private void registerMenuScreens(RegisterMenuScreensEvent event) {
    }

    private void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(LocksmithParticles.LOCK_BREAK.get(), SmokeParticle.Provider::new);
        event.registerSpriteSet(LocksmithParticles.LOCK_SPARK.get(), LockSparkParticle.Provider::new);
    }
}
