package gg.moonflower.locksmith.core.registry;

import gg.moonflower.locksmith.common.item.KeyItem;
import gg.moonflower.locksmith.common.item.KeyringItem;
import gg.moonflower.locksmith.common.item.LockItem;
import gg.moonflower.locksmith.common.item.LockpickItem;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.pollen.api.registry.PollinatedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class LocksmithItems {
    public static final PollinatedRegistry<Item> ITEMS = PollinatedRegistry.create(Registry.ITEM, Locksmith.MOD_ID);

    public static final Supplier<Item> BLANK_LOCK = ITEMS.register("blank_lock", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final Supplier<Item> BLANK_KEY = ITEMS.register("blank_key", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));

    public static final Supplier<Item> LOCK = ITEMS.register("lock", () -> new LockItem(new Item.Properties()));
    public static final Supplier<Item> KEY = ITEMS.register("key", () -> new KeyItem(new Item.Properties()));

    public static final Supplier<Item> KEYRING = ITEMS.register("keyring", () -> new KeyringItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final Supplier<Item> LOCKPICK = ITEMS.register("lockpick", () -> new LockpickItem(new Item.Properties().durability(3).tab(CreativeModeTab.TAB_TOOLS)));
}
