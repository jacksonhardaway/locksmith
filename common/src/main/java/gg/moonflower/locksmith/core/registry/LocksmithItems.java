package gg.moonflower.locksmith.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import gg.moonflower.locksmith.common.item.KeyItem;
import gg.moonflower.locksmith.common.item.KeyringItem;
import gg.moonflower.locksmith.common.item.LockItem;
import gg.moonflower.locksmith.common.item.LockPickItem;
import gg.moonflower.locksmith.core.Locksmith;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class LocksmithItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Locksmith.MOD_ID, Registry.ITEM_REGISTRY);

    public static final Supplier<Item> BLANK_LOCK = REGISTRY.register("blank_lock", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final Supplier<Item> BLANK_KEY = REGISTRY.register("blank_key", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final Supplier<Item> BLANK_LOCK_BUTTON = REGISTRY.register("blank_lock_button", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));

    public static final Supplier<Item> LOCK = REGISTRY.register("lock", () -> new LockItem(new Item.Properties()));
    public static final Supplier<Item> KEY = REGISTRY.register("key", () -> new KeyItem(new Item.Properties()));

    public static final Supplier<Item> KEYRING = REGISTRY.register("keyring", () -> new KeyringItem(new Item.Properties()));
    public static final Supplier<Item> LOCKPICK = REGISTRY.register("lockpick", () -> new LockPickItem(new Item.Properties().durability(3).tab(CreativeModeTab.TAB_TOOLS)));
}
