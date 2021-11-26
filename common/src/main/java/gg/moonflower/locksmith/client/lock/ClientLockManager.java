package gg.moonflower.locksmith.client.lock;

import gg.moonflower.locksmith.api.lock.LockData;
import gg.moonflower.pollen.api.event.events.network.ClientNetworkEvent;
import gg.moonflower.pollen.api.event.events.world.ChunkEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class ClientLockManager {
    public static final ClientLockManager INSTANCE = new ClientLockManager();
    private final Map<ResourceKey<Level>, Map<ChunkPos, Set<LockData>>> locks = new HashMap<>();

    private ClientLockManager() {
    }

    public void init() {
        ClientNetworkEvent.LOGOUT.register((controller, player, connection) -> this.locks.clear());
        ChunkEvent.UNLOAD.register((level, chunk) -> {
            if (level.isClientSide() && level instanceof Level) {
                Level levelInstance = (Level) level;
                this.getLockMap(levelInstance.dimension()).remove(chunk.getPos());
            }
        });
    }

    public Map<ChunkPos, Set<LockData>> getLockMap(ResourceKey<Level> level) {
        return locks.computeIfAbsent(level, levelResourceKey -> new HashMap<>());
    }

    @Nullable
    public LockData getLock(ResourceKey<Level> level, BlockPos pos) {
        for (Set<LockData> lock : this.getLockMap(level).values()) {
            for (LockData data : lock) {
                if (data.getPos().equals(pos))
                    return data;
            }
        }
        return null;
    }
}
