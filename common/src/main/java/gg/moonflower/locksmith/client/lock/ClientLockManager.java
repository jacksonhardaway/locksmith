package gg.moonflower.locksmith.client.lock;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.common.lock.LockManager;
import gg.moonflower.pollen.api.event.events.network.ClientNetworkEvents;
import gg.moonflower.pollen.api.event.events.world.ChunkEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class ClientLockManager implements LockManager {
    private static final Map<ResourceKey<Level>, ClientLockManager> INSTANCES = new HashMap<>();
    private final Map<ChunkPos, Set<AbstractLock>> locks = new HashMap<>();

    static {
        ClientNetworkEvents.LOGOUT.register((controller, player, connection) -> INSTANCES.clear());
        ChunkEvents.UNLOAD.register((level, chunk) -> {
            if (level instanceof Level) {
                ClientLockManager manager = INSTANCES.get(((Level) level).dimension());
                if (manager == null)
                    return;
                manager.locks.remove(chunk.getPos());
            }
        });
    }

    private ClientLockManager() {
    }

    public static ClientLockManager getOrCreate(ClientLevel level) {
        return INSTANCES.computeIfAbsent(level.dimension(), levelResourceKey -> new ClientLockManager());
    }

    @Override
    public Collection<AbstractLock> getLocks(ChunkPos chunkPos) {
        Set<AbstractLock> locks = this.locks.get(chunkPos);
        if (locks == null)
            return Collections.emptySet();
        return locks;
    }

    @Override
    @Nullable
    public AbstractLock getLock(BlockPos pos) {
        Set<AbstractLock> locks = this.locks.get(new ChunkPos(pos));
        if (locks == null)
            return null;
        for (AbstractLock lock : locks) {
            if (lock.getPos().equals(pos))
                return lock;
        }
        return null;
    }

    @Override
    public void addLock(AbstractLock data) {
        this.locks.computeIfAbsent(new ChunkPos(data.getPos()), chunkPos -> new HashSet<>()).add(data);
    }

    @Override
    public void removeLock(BlockPos pos) {
        Set<AbstractLock> locks = this.locks.get(new ChunkPos(pos));
        if (locks == null)
            return;

        locks.removeIf(lock -> lock.getPos().equals(pos));
    }

    public void clearLocks(ChunkPos chunkPos) {
        Set<AbstractLock> locks = this.locks.get(chunkPos);
        if (locks == null)
            return;
        locks.clear();
    }
}
