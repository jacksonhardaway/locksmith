package dev.hardaway.locksmith.core.registry;

import dev.hardaway.locksmith.common.component.Keychain;
import dev.hardaway.locksmith.common.item.KeyItem;
import dev.hardaway.locksmith.common.item.KeyringItem;
import dev.hardaway.locksmith.common.item.LockItem;
import dev.hardaway.locksmith.core.Locksmith;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LocksmithItems {

    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(Locksmith.MOD_ID);

    public static final Supplier<Item> BLANK_KEY = REGISTRY.registerSimpleItem("blank_key");
    public static final Supplier<Item> BLANK_LOCK = REGISTRY.registerSimpleItem("blank_lock");

    public static final Supplier<Item> KEY = REGISTRY.registerItem("key", KeyItem::new, new Item.Properties()
            .stacksTo(1)
    );
    public static final Supplier<Item> LOCK = REGISTRY.registerItem("lock", LockItem::new);
    public static final Supplier<Item> KEYRING = REGISTRY.registerItem("keyring", p -> new KeyringItem(p.stacksTo(1).component(LocksmithComponents.KEYCHAIN, Keychain.EMPTY)));
}
