package gg.moonflower.locksmith.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import gg.moonflower.locksmith.common.menu.KeyringMenu;
import gg.moonflower.locksmith.common.menu.LockpickingMenu;
import gg.moonflower.locksmith.common.menu.LocksmithingTableMenu;
import gg.moonflower.locksmith.core.Locksmith;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class LocksmithMenus {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Locksmith.MOD_ID, Registry.MENU_REGISTRY);

    public static final Supplier<MenuType<LocksmithingTableMenu>> LOCKSMITHING_TABLE_MENU = REGISTRY.register("locksmithing", () -> new MenuType<>(LocksmithingTableMenu::new));
    public static final Supplier<MenuType<KeyringMenu>> KEYRING_MENU = REGISTRY.register("keyring", () -> new MenuType<>(KeyringMenu::new));
    public static final Supplier<MenuType<LockpickingMenu>> LOCK_PICKING_MENU = REGISTRY.register("lock_picking", () -> new MenuType<>(LockpickingMenu::new));
}
