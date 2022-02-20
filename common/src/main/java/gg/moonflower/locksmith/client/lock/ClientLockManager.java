package gg.moonflower.locksmith.client.lock;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.api.lock.LockManager;
import gg.moonflower.locksmith.api.lock.position.BlockLockPosition;
import gg.moonflower.locksmith.api.lock.position.LockPosition;
import gg.moonflower.pollen.api.event.events.network.ClientNetworkEvents;
import gg.moonflower.pollen.api.event.events.world.ChunkEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class ClientLockManager implements LockManager {

    private static final Map<ResourceKey<Level>, ClientLockManager> INSTANCES = new HashMap<>();
    private final Map<LockPosition, AbstractLock> locks = new HashMap<>();

    static {
        ClientNetworkEvents.LOGOUT.register((controller, player, connection) -> INSTANCES.clear());
        ChunkEvents.UNLOAD.register((level, chunk) -> {
            if (level instanceof Level) {
                ClientLockManager manager = INSTANCES.get(((Level) level).dimension());
                if (manager == null)
                    return;
                manager.clearLocks(chunk.getPos());
            }
        });
    }

    private final ClientLevel level;

    private ClientLockManager(ClientLevel level) {
        this.level = level;
    }

    public static ClientLockManager getOrCreate(ClientLevel level) {
        return INSTANCES.computeIfAbsent(level.dimension(), levelResourceKey -> new ClientLockManager(level));
    }

    @Override
    @Nullable
    public AbstractLock getLock(LockPosition pos) {
        AbstractLock lock = this.locks.get(pos);
        if (lock != null)
            return lock;

        if (pos instanceof BlockLockPosition) {
            BlockPos offsetPos = LockManager.getLockPosition(this.level, pos.blockPosition());
            if (!pos.blockPosition().equals(offsetPos))
                return this.locks.get(LockPosition.of(offsetPos));
        }

        return null;
    }

    @Override
    public void addLock(AbstractLock data) {
        this.locks.put(data.getPos(), data);
    }

    private void removeLock(LockPosition pos) {
        this.locks.remove(pos);
    }

    @Override
    public void removeLock(BlockPos pos, BlockPos clickPos, boolean drop) {
        this.removeLock(LockPosition.of(pos));
    }

    @Override
    public void removeLock(Entity entity, boolean drop) {
        this.removeLock(LockPosition.of(entity));
    }

    public void clearLocks(ChunkPos chunk) {
        this.locks.values().removeIf(lock -> lock.getPos() instanceof BlockLockPosition && lock.getPos().blockPosition().getX() >> 4 == chunk.x && lock.getPos().blockPosition().getZ() >> 4 == chunk.z);
    }
}
