package gg.moonflower.locksmith.core.forge;

import dev.architectury.platform.forge.EventBuses;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.locksmith.clientsource.core.LocksmithClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Locksmith.MOD_ID)
public class LocksmithForge {
    public LocksmithForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Locksmith.MOD_ID, eventBus);
        eventBus.addListener(this::clientInit);

        Locksmith.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> LocksmithClient::init);
    }

    private void clientInit(FMLClientSetupEvent event) {
        LocksmithClient.postInit();
    }
}
