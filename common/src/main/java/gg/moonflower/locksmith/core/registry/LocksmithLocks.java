package gg.moonflower.locksmith.core.registry;

import gg.moonflower.locksmith.api.lock.LockType;
import gg.moonflower.locksmith.common.lock.types.KeyLock;
import gg.moonflower.locksmith.common.lock.types.LockButtonLock;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.pollen.api.registry.PollinatedRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class LocksmithLocks {
    public static final PollinatedRegistry<LockType> LOCKS = PollinatedRegistry.createDefaulted(LockType.class, new ResourceLocation(Locksmith.MOD_ID, "lock_types"), new ResourceLocation(Locksmith.MOD_ID, "key"));

    public static final Supplier<LockType> KEY = LOCKS.register("key", () -> LockType.of(KeyLock.CODEC));
    public static final Supplier<LockType> LOCK_BUTTON = LOCKS.register("lock_button", () -> LockType.of(LockButtonLock.CODEC));
}
