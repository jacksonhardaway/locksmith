package gg.moonflower.locksmith.core.registry;

import com.mojang.serialization.Lifecycle;
import gg.moonflower.locksmith.api.lock.LockType;
import gg.moonflower.locksmith.common.lock.types.KeyLock;
import gg.moonflower.locksmith.common.lock.types.LockButtonLock;
import gg.moonflower.locksmith.core.Locksmith;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class LocksmithLocks {

    public static final ResourceKey<Registry<LockType>> REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Locksmith.MOD_ID, "lock_types"));
    public static final Registry<LockType> LOCK_TYPES = new DefaultedRegistry<>("locksmith:key", REGISTRY_KEY, Lifecycle.stable(), null);

    public static final LockType KEY = Registry.register(LOCK_TYPES, new ResourceLocation(Locksmith.MOD_ID, "key"), new LockType(KeyLock.CODEC));
    public static final LockType LOCK_BUTTON = Registry.register(LOCK_TYPES, new ResourceLocation(Locksmith.MOD_ID, "lock_button"), new LockType(LockButtonLock.CODEC));

    public static void register() {}
}
