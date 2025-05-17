package dev.hardaway.locksmith.api.lock;

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

    /**
     * Removes the current lock.
     *
     * @return The previous lock, or null if there is no lock
     */
    @Nullable Lock unlock();
}
