package gg.moonflower.locksmith.core;

import gg.moonflower.locksmith.client.lock.ClientLockManager;
import gg.moonflower.locksmith.common.world.lock.LockManager;
import gg.moonflower.pollen.api.platform.Platform;

public class Locksmith {
    public static final String MOD_ID = "locksmith";
    public static final Platform PLATFORM = Platform.builder(MOD_ID)
            .clientInit(Locksmith::onClientInit)
            .clientPostInit(Locksmith::onClientPostInit)
            .commonInit(Locksmith::onCommonInit)
            .commonPostInit(Locksmith::onCommonPostInit)
            .build();

    public static void onClientInit() {
        ClientLockManager.INSTANCE.init();
    }

    public static void onClientPostInit(Platform.ModSetupContext ctx) {
    }

    public static void onCommonInit() {
        LockManager.INSTANCE.init();
    }

    public static void onCommonPostInit(Platform.ModSetupContext ctx) {
    }
}
