package gg.moonflower.locksmith.core.registry;

import gg.moonflower.locksmith.common.menu.LocksmithingTableMenu;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.pollen.api.registry.PollinatedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class LocksmithMenus {
    public static final PollinatedRegistry<MenuType<?>> MENUS = PollinatedRegistry.create(Registry.MENU, Locksmith.MOD_ID);

    public static final Supplier<MenuType<LocksmithingTableMenu>> LOCKSMITHING_TABLE_MENU = MENUS.register("locksmithing", () -> new MenuType<>(LocksmithingTableMenu::new));
}
