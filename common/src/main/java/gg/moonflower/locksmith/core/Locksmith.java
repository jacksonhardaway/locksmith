package gg.moonflower.locksmith.core;

import gg.moonflower.locksmith.client.lock.ClientLockManager;
import gg.moonflower.locksmith.client.screen.KeyringScreen;
import gg.moonflower.locksmith.client.screen.LockPickingScreen;
import gg.moonflower.locksmith.client.screen.LocksmithingTableScreen;
import gg.moonflower.locksmith.common.item.KeyringItem;
import gg.moonflower.locksmith.common.lock.LockInteractionManager;
import gg.moonflower.locksmith.common.lock.ServerLockManager;
import gg.moonflower.locksmith.common.menu.LocksmithingTableMenu;
import gg.moonflower.locksmith.common.network.LocksmithMessages;
import gg.moonflower.locksmith.core.registry.LocksmithBlocks;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import gg.moonflower.locksmith.core.registry.LocksmithLocks;
import gg.moonflower.locksmith.core.registry.LocksmithMenus;
import gg.moonflower.locksmith.core.registry.LocksmithParticles;
import gg.moonflower.locksmith.core.registry.LocksmithRecipes;
import gg.moonflower.locksmith.core.registry.LocksmithSounds;
import gg.moonflower.locksmith.core.registry.LocksmithStats;
import gg.moonflower.pollen.api.config.ConfigManager;
import gg.moonflower.pollen.api.config.PollinatedConfigType;
import gg.moonflower.pollen.api.event.events.entity.player.PlayerInteractionEvents;
import gg.moonflower.pollen.api.event.events.registry.client.RegisterAtlasSpriteEvent;
import gg.moonflower.pollen.api.platform.Platform;
import gg.moonflower.pollen.api.registry.client.ItemPredicateRegistry;
import gg.moonflower.pollen.api.registry.client.ScreenRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;

public class Locksmith {
    public static final String MOD_ID = "locksmith";
    public static final LocksmithConfig CONFIG = ConfigManager.register(MOD_ID, PollinatedConfigType.COMMON, LocksmithConfig::new);
    public static final Platform PLATFORM = Platform.builder(MOD_ID)
            .clientInit(Locksmith::onClientInit)
            .clientPostInit(Locksmith::onClientPostInit)
            .commonInit(Locksmith::onCommonInit)
            .commonPostInit(Locksmith::onCommonPostInit)
            .build();

    public static void onClientInit() {
        RegisterAtlasSpriteEvent.event(InventoryMenu.BLOCK_ATLAS).register((atlas, registry) -> registry.accept(LocksmithingTableMenu.EMPTY_SLOT_KEY));
        LocksmithParticles.setupClient();
    }

    public static void onClientPostInit(Platform.ModSetupContext ctx) {
        ctx.enqueueWork(() -> {
            ScreenRegistry.register(LocksmithMenus.LOCKSMITHING_TABLE_MENU.get(), LocksmithingTableScreen::new);
            ScreenRegistry.register(LocksmithMenus.KEYRING_MENU.get(), KeyringScreen::new);
            ScreenRegistry.register(LocksmithMenus.LOCK_PICKING_MENU.get(), LockPickingScreen::new);
            ItemPredicateRegistry.register(LocksmithItems.KEYRING.get(), new ResourceLocation(Locksmith.MOD_ID, "keys"), (stack, level, livingEntity) -> Mth.clamp(KeyringItem.getKeys(stack).size() / (float) KeyringItem.MAX_KEYS, 0, 1));
        });
    }

    public static void onCommonInit() {
        LocksmithBlocks.BLOCKS.register(Locksmith.PLATFORM);
        LocksmithItems.ITEMS.register(Locksmith.PLATFORM);
        LocksmithSounds.SOUNDS.register(Locksmith.PLATFORM);
        LocksmithParticles.PARTICLES.register(Locksmith.PLATFORM);
        LocksmithLocks.LOCKS.register(Locksmith.PLATFORM);
        LocksmithRecipes.RECIPES.register(Locksmith.PLATFORM);
        LocksmithMessages.init();
        ServerLockManager.init();

        PlayerInteractionEvents.RIGHT_CLICK_BLOCK.register(LockInteractionManager::onRightClickBlock);
        PlayerInteractionEvents.LEFT_CLICK_BLOCK.register(LockInteractionManager::onLeftClickBlock);
    }

    public static void onCommonPostInit(Platform.ModSetupContext ctx) {
        ctx.enqueueWork(() -> LocksmithStats.STATS.register(Locksmith.PLATFORM));
    }
}
