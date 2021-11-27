package gg.moonflower.locksmith.client.lock;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.common.lock.LockManager;
import gg.moonflower.pollen.api.event.events.network.ClientNetworkEvent;
import gg.moonflower.pollen.api.event.events.world.ChunkEvent;
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
    private final ClientLevel level;

    private ClientLockManager(ClientLevel level) {
        this.level = level;
    }

    public static ClientLockManager getOrCreate(ClientLevel level) {
        return INSTANCES.computeIfAbsent(level.dimension(), levelResourceKey -> new ClientLockManager(level));
    }

    public static void init() {
        ClientNetworkEvent.LOGOUT.register((controller, player, connection) -> INSTANCES.clear());
        ChunkEvent.UNLOAD.register((level, chunk) -> {
            if (level instanceof Level) {
                ClientLockManager manager = INSTANCES.get(((Level) level).dimension());
                if (manager == null)
                    return;
                manager.locks.remove(chunk.getPos());
            }
        });
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
