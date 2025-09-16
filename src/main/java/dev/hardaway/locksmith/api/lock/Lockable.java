package dev.hardaway.locksmith.api.lock;

import dev.hardaway.locksmith.common.component.KeyData;
import dev.hardaway.locksmith.core.registry.LocksmithComponents;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Lockable {

    Optional<Lock> getLock();

    /**
     * Locks the object with the provided lock.
     *
     * @param lock The lock
     * @return Whether the object was locked
     */
    boolean lock(Lock lock);

    boolean canLock(ItemStack stack);

    /**
     * Removes the current lock.
     *
     * @return The previous lock, or null if there is no lock
     */
    @Nullable Lock unlock();

    boolean canUnlock(ItemStack stack);
}
