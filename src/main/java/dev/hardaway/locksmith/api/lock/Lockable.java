package dev.hardaway.locksmith.api.lock;

import dev.hardaway.locksmith.common.component.KeyData;
import dev.hardaway.locksmith.core.registry.LocksmithComponents;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
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

    /**
     * Removes the current lock.
     *
     * @return The previous lock, or null if there is no lock
     */
    @Nullable Lock unlock();

    default boolean canLock(ItemStack stack) {
        return this.getLock().isEmpty() && stack.has(LocksmithComponents.LOCK_DATA);
    }

    default boolean canUnlock(ItemStack stack) {
        Optional<Lock> lock = this.getLock();
        if (lock.isEmpty())
            return true;

        if (!stack.has(LocksmithComponents.KEY_DATA))
            return false;

        KeyData data = stack.get(LocksmithComponents.KEY_DATA);
        return Objects.equals(lock.get().getId(), data.id());
    }
}
