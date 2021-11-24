package gg.moonflower.locksmith.client.lock;

import gg.moonflower.locksmith.api.lock.LockData;
import gg.moonflower.pollen.api.event.events.entity.player.server.PlayerTrackingEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class ClientLockManager {
    public static final ClientLockManager INSTANCE = new ClientLockManager();
    private final Map<ChunkPos, LockData> locks = new HashMap<>();

    private ClientLockManager() {
    }

    public void init() {
        PlayerTrackingEvent.STOP_TRACKING_CHUNK.register((player, chunkPos) -> this.locks.remove(chunkPos));
    }

    public Map<ChunkPos, LockData> getLocks() {
        return locks;
    }

    @Nullable
    public LockData getLock(BlockPos pos) {
        for (LockData lock : this.locks.values()) {
            if (lock.getPos().equals(pos))
                return lock;
        }
        return null;
    }
}
