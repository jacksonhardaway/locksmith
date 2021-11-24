package gg.moonflower.locksmith.common.world.lock;

import gg.moonflower.locksmith.api.lock.LockData;
import gg.moonflower.locksmith.common.network.LocksmithMessages;
import gg.moonflower.locksmith.common.network.play.ClientboundLockSyncPacket;
import gg.moonflower.pollen.api.event.events.entity.player.server.PlayerTrackingEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class LockManager extends SavedData {
    public static final LockManager INSTANCE = new LockManager();
    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<ChunkPos, ChunkLockData> locks = new HashMap<>();

    private LockManager() {
        super("locksmith_locks");
    }

    public static LockManager getOrCreate(ServerLevel level) {
        return level.getDataStorage().get(() -> LockManager.INSTANCE, "locksmith_locks");
    }

    public void init() {
        PlayerTrackingEvent.START_TRACKING_CHUNK.register((player, chunk) -> {
            if (!(player.level instanceof ServerLevel) || !(player instanceof ServerPlayer))
                return;

            ServerLevel serverLevel = (ServerLevel) player.level;
            Set<LockData> locks = LockManager.getOrCreate(serverLevel).getLocks(chunk);
            if (locks.isEmpty())
                return;

            LocksmithMessages.PLAY.sendTo((ServerPlayer) player, new ClientboundLockSyncPacket(locks));
        });
    }

    @Override
    public void load(CompoundTag compoundTag) {
        ListTag tag = compoundTag.getList("Locks", 10);
        for (int i = 0; i < tag.size(); i++) {
            CompoundTag lock = tag.getCompound(i);
            ChunkLockData data = new ChunkLockData();
            data.save(lock);

            this.locks.put(new ChunkPos(lock.getInt("X"), lock.getInt("Z")), data);
        }
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        ListTag list = new ListTag();
        for (ChunkPos pos : this.locks.keySet()) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("X", pos.x);
            tag.putInt("Z", pos.z);

            list.add(this.locks.get(pos).save(tag));
        }

        compoundTag.put("Locks", list);
        return compoundTag;
    }

    public void addLock(ChunkPos chunk, LockData lock) {
        this.locks.compute(chunk, (chunkPos, chunkLockData) -> {
            ChunkLockData data = chunkLockData == null ? new ChunkLockData() : chunkLockData;
            data.addLock(lock);
            return data;
        });
        this.setDirty();
    }

    public void removeLock(ChunkPos chunk, UUID id) {
        this.locks.get(chunk).removeLock(id);
        this.setDirty();
    }

    @Nullable
    public LockData getLock(ChunkPos chunk, BlockPos pos) {
        return this.locks.get(chunk).getLock(pos);
    }

    public Set<LockData> getLocks(ChunkPos chunk) {
        if (!this.locks.containsKey(chunk))
            return Collections.emptySet();
        return new HashSet<>(this.locks.get(chunk).getLocks().values());
    }

    private static class ChunkLockData {
        private final Map<UUID, LockData> locks = new HashMap<>();

        public void load(CompoundTag compoundTag) {
            ListTag tag = compoundTag.getList("Locks", 10);
            for (int i = 0; i < tag.size(); i++) {
                LockData lock = LockData.CODEC.parse(NbtOps.INSTANCE, tag.getCompound(i)).getOrThrow(false, LOGGER::error);
                this.locks.put(lock.getId(), lock);
            }
        }

        public CompoundTag save(CompoundTag compoundTag) {
            ListTag list = new ListTag();
            for (LockData lock : this.locks.values()) {
                list.add(LockData.CODEC.encodeStart(NbtOps.INSTANCE, lock).getOrThrow(false, LOGGER::error));
            }

            compoundTag.put("Locks", list);
            return compoundTag;
        }

        public void addLock(LockData lock) {
            UUID id = UUID.randomUUID();
            this.locks.put(id, lock);
        }

        public void removeLock(UUID id) {
            this.locks.remove(id);
        }

        @Nullable
        public LockData getLock(BlockPos pos) {
            for (LockData lock : this.locks.values()) {
                if (lock.getPos().equals(pos))
                    return lock;
            }
            return null;
        }

        public Map<UUID, LockData> getLocks() {
            return locks;
        }
    }
}
