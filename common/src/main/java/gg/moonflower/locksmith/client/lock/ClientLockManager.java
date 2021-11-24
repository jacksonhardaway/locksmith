package gg.moonflower.locksmith.client.lock;

import gg.moonflower.locksmith.api.lock.LockData;
import gg.moonflower.pollen.api.event.events.network.ClientNetworkEvent;
import gg.moonflower.pollen.api.event.events.world.ChunkEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ClientLockManager {
    public static final ClientLockManager INSTANCE = new ClientLockManager();
    private final Map<ChunkPos, Set<LockData>> locks = new HashMap<>();

    private ClientLockManager() {
    }

    public void init() {
        ClientNetworkEvent.LOGOUT.register((controller, player, connection) -> this.locks.clear());
        ChunkEvent.UNLOAD.register((level, chunk) -> {
            if (level.isClientSide())
                this.locks.remove(chunk.getPos());
        });
    }

    public Map<ChunkPos, Set<LockData>> getLockMap() {
        return locks;
    }

    @Nullable
    public LockData getLock(BlockPos pos) {
        for (Set<LockData> lock : this.locks.values()) {
            for (LockData data : lock) {
                if (data.getPos().equals(pos))
                    return data;
            }
        }
        return null;
    }
}
