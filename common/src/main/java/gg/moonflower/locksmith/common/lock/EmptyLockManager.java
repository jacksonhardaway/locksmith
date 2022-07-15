package gg.moonflower.locksmith.common.lock;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.api.lock.LockManager;
import gg.moonflower.locksmith.api.lock.position.LockPosition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

/**
 * Lock manager used for fake level instances
 */
public class EmptyLockManager implements LockManager {

    @Override
    public void addLock(AbstractLock data) {
    }

    @Override
    public void removeLock(BlockPos pos, BlockPos clickPos, boolean drop) {
    }

    @Override
    public void removeLock(Entity entity, boolean drop) {
    }

    @Override
    public @Nullable AbstractLock getLock(LockPosition pos) {
        return null;
    }
}
