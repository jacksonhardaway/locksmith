package gg.moonflower.locksmith.core;

import gg.moonflower.locksmith.client.lock.ClientLockManager;
import gg.moonflower.locksmith.client.screen.LocksmithingTableScreen;
import gg.moonflower.locksmith.common.network.LocksmithMessages;
import gg.moonflower.locksmith.common.world.lock.LockManager;
import gg.moonflower.locksmith.core.registry.LocksmithBlocks;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import gg.moonflower.locksmith.core.registry.LocksmithMenus;
import gg.moonflower.pollen.api.platform.Platform;
import gg.moonflower.pollen.api.registry.ClientRegistries;

public class Locksmith {
    public static final String MOD_ID = "locksmith";
    public static final Platform PLATFORM = Platform.builder(MOD_ID)
            .clientInit(Locksmith::onClientInit)
            .clientPostInit(Locksmith::onClientPostInit)
            .commonInit(Locksmith::onCommonInit)
            .commonPostInit(Locksmith::onCommonPostInit)
            .build();

    public static void onClientInit() {
        ClientRegistries.registerScreenFactory(LocksmithMenus.LOCKSMITHING_TABLE_MENU.get(), LocksmithingTableScreen::new);
        ClientLockManager.INSTANCE.init();
    }

    public static void onClientPostInit(Platform.ModSetupContext ctx) {
    }

    public static void onCommonInit() {
        LocksmithBlocks.BLOCKS.register(Locksmith.PLATFORM);
        LocksmithItems.ITEMS.register(Locksmith.PLATFORM);
        LocksmithMessages.init();
        LockManager.init();
    }

    public static void onCommonPostInit(Platform.ModSetupContext ctx) {
    }
}
