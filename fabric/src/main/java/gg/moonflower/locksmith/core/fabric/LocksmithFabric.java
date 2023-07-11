package gg.moonflower.locksmith.core.fabric;

import gg.moonflower.locksmith.core.Locksmith;
import net.fabricmc.api.ModInitializer;

public class LocksmithFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Locksmith.init();
    }
}
