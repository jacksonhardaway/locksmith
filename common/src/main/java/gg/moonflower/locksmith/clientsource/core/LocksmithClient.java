package gg.moonflower.locksmith.clientsource.core;

import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import gg.moonflower.locksmith.clientsource.client.network.play.handler.LocksmithClientPlayPacketHandlerImpl;
import gg.moonflower.locksmith.clientsource.client.particle.LockSparkParticle;
import gg.moonflower.locksmith.clientsource.client.screen.KeyringScreen;
import gg.moonflower.locksmith.clientsource.client.screen.LockPickingScreen;
import gg.moonflower.locksmith.clientsource.client.screen.LocksmithingTableScreen;
import gg.moonflower.locksmith.clientsource.client.tooltip.ClientKeyringTooltip;
import gg.moonflower.locksmith.common.item.KeyringItem;
import gg.moonflower.locksmith.common.menu.LocksmithingTableMenu;
import gg.moonflower.locksmith.common.network.LocksmithMessages;
import gg.moonflower.locksmith.common.tooltip.KeyringTooltip;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import gg.moonflower.locksmith.core.registry.LocksmithMenus;
import gg.moonflower.locksmith.core.registry.LocksmithParticles;
import gg.moonflower.pollen.api.event.registry.v1.RegisterAtlasSpriteEvent;
import gg.moonflower.pollen.api.registry.tooltip.v1.ClientTooltipComponentRegistry;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class LocksmithClient {

    public static void init() {
        RegisterAtlasSpriteEvent.event(InventoryMenu.BLOCK_ATLAS).register((atlas, registry) -> registry.accept(LocksmithingTableMenu.EMPTY_SLOT_KEY));
        ParticleProviderRegistry.register(LocksmithParticles.LOCK_BREAK, SmokeParticle.Provider::new);
        ParticleProviderRegistry.register(LocksmithParticles.LOCK_SPARK, LockSparkParticle.Provider::new);

        LocksmithMessages.PLAY.setClientHandler(new LocksmithClientPlayPacketHandlerImpl());
        ClientTooltipComponentRegistry.register(KeyringTooltip.class, ClientKeyringTooltip::new);
    }

    public static void postInit() {
        MenuRegistry.registerScreenFactory(LocksmithMenus.LOCKSMITHING_TABLE_MENU.get(), LocksmithingTableScreen::new);
        MenuRegistry.registerScreenFactory(LocksmithMenus.KEYRING_MENU.get(), KeyringScreen::new);
        MenuRegistry.registerScreenFactory(LocksmithMenus.LOCK_PICKING_MENU.get(), LockPickingScreen::new);
        ItemPropertiesRegistry.register(LocksmithItems.KEYRING.get(), new ResourceLocation(Locksmith.MOD_ID, "keys"), (stack, level, livingEntity, i) -> KeyringItem.getKeys(stack).size() / (float) KeyringItem.MAX_KEYS);
    }
}
