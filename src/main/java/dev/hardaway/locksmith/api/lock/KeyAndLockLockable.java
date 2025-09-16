package dev.hardaway.locksmith.api.lock;

import dev.hardaway.locksmith.common.component.KeyData;
import dev.hardaway.locksmith.core.registry.*;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public abstract class KeyAndLockLockable implements Lockable{

    @Override
    public boolean canLock(ItemStack stack) {
        return getLock().isEmpty() && stack.is(LocksmithItems.LOCK.get()) && stack.has(LocksmithComponents.LOCK_DATA);
    }

    @Override
    public boolean canUnlock(ItemStack stack) {
        Optional<Lock> lock = this.getLock();
        if (lock.isEmpty())
            return true;

        if (!stack.is(LocksmithItems.KEY.get()) || !stack.has(LocksmithComponents.KEY_DATA))
            return false;

        KeyData data = stack.get(LocksmithComponents.KEY_DATA);
        return lock.get().getId().equals(data.id());
    }
}