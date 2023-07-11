package gg.moonflower.locksmith.clientsource.core.fabric;

import gg.moonflower.locksmith.clientsource.core.LocksmithClient;
import net.fabricmc.api.ClientModInitializer;

public class LocksmithFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LocksmithClient.init();
        LocksmithClient.postInit();
    }
}
