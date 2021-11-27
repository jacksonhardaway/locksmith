package gg.moonflower.locksmith.common.lock;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.client.lock.ClientLockManager;
import gg.moonflower.locksmith.common.world.lock.ServerLockManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface LockManager {

    static LockManager get(Level level) {
        return level.isClientSide() ? ClientLockManager.getOrCreate((ClientLevel) level) : ServerLockManager.getOrCreate((ServerLevel) level);
    }

    Collection<AbstractLock> getLocks(ChunkPos chunkPos);

    @Nullable
    AbstractLock getLock(BlockPos pos);

    void addLock(AbstractLock data);

    void removeLock(BlockPos pos);

}
