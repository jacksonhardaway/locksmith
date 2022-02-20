package gg.moonflower.locksmith.common.lock;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.api.lock.LockManager;
import gg.moonflower.locksmith.api.lock.position.BlockLockPosition;
import gg.moonflower.locksmith.api.lock.position.EntityLockPosition;
import gg.moonflower.locksmith.api.lock.position.LockPosition;
import gg.moonflower.locksmith.common.network.LocksmithMessages;
import gg.moonflower.locksmith.common.network.play.ClientboundAddLocksPacket;
import gg.moonflower.locksmith.common.network.play.ClientboundDeleteLocksPacket;
import gg.moonflower.pollen.api.event.events.entity.player.server.ServerPlayerTrackingEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.saveddata.SavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class ServerLockManager extends SavedData implements LockManager {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<ChunkPos, ChunkLockData> locks = new HashMap<>();
    private final ChunkLockData entityLocks = new ChunkLockData();
    private final ServerLevel level;

    private ServerLockManager(ServerLevel level) {
        super("locksmithLocks");
        this.level = level;
    }

    public static ServerLockManager getOrCreate(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(() -> new ServerLockManager(level), "locksmithLocks");
    }

    public static void init() {
        ServerPlayerTrackingEvents.START_TRACKING_CHUNK.register((player, chunk) -> {
            if (!(player.level instanceof ServerLevel) || !(player instanceof ServerPlayer))
                return;

            Collection<AbstractLock> locks = getOrCreate((ServerLevel) player.level).getLocks(chunk);
            if (locks.isEmpty())
                return;

            LocksmithMessages.PLAY.sendTo((ServerPlayer) player, new ClientboundAddLocksPacket(locks, true));
        });
        ServerPlayerTrackingEvents.STOP_TRACKING_CHUNK.register((player, chunk) -> {
            if (!(player.level instanceof ServerLevel) || !(player instanceof ServerPlayer))
                return;

            Collection<AbstractLock> locks = getOrCreate((ServerLevel) player.level).getLocks(chunk);
            if (locks.isEmpty())
                return;

            LocksmithMessages.PLAY.sendTo((ServerPlayer) player, new ClientboundDeleteLocksPacket(locks.stream().map(AbstractLock::getPos).collect(Collectors.toSet())));
        });

        ServerPlayerTrackingEvents.START_TRACKING_ENTITY.register((player, chunk) -> {
            if (!(player.level instanceof ServerLevel) || !(player instanceof ServerPlayer))
                return;

            Collection<AbstractLock> locks = getOrCreate((ServerLevel) player.level).entityLocks.getLocks();
            if (locks.isEmpty())
                return;

            LocksmithMessages.PLAY.sendTo((ServerPlayer) player, new ClientboundAddLocksPacket(locks, true));
        });
        ServerPlayerTrackingEvents.STOP_TRACKING_ENTITY.register((player, chunk) -> {
            if (!(player.level instanceof ServerLevel) || !(player instanceof ServerPlayer))
                return;

            Collection<AbstractLock> locks = getOrCreate((ServerLevel) player.level).entityLocks.getLocks();
            if (locks.isEmpty())
                return;

            LocksmithMessages.PLAY.sendTo((ServerPlayer) player, new ClientboundDeleteLocksPacket(locks.stream().map(AbstractLock::getPos).collect(Collectors.toSet())));
        });
    }

    private Collection<AbstractLock> getLocks(ChunkPos chunkPos) {
        ChunkLockData chunk = this.locks.get(chunkPos);
        if (chunk == null)
            return Collections.emptySet();
        return chunk.getLocks();
    }

    @Override
    @Nullable
    public AbstractLock getLock(LockPosition pos) {
        ChunkLockData chunk = this.get(pos, false);
        if (chunk == null)
            return null;

        AbstractLock lock = chunk.getLock(pos);
        if (lock != null)
            return lock;

        if (pos instanceof BlockLockPosition) {
            BlockPos offsetPos = LockManager.getLockPosition(this.level, pos.blockPosition());
            if (!pos.blockPosition().equals(offsetPos))
                return chunk.getLock(LockPosition.of(offsetPos));
        }

        return null;
    }

    @Override
    public void addLock(AbstractLock data) {
        ChunkLockData lockData = this.get(data.getPos(), true);
        LockPosition pos = data.getPos();

        if (pos instanceof BlockLockPosition) {
            LocksmithMessages.PLAY.sendToTracking(this.level, new ChunkPos(pos.blockPosition()), new ClientboundAddLocksPacket(Collections.singleton(data), false));
        } else {
            LocksmithMessages.PLAY.sendToTracking(((EntityLockPosition) pos).getEntity(), new ClientboundAddLocksPacket(Collections.singleton(data), false));
        }

        lockData.addLock(data);
        this.setDirty();
    }

    private ChunkLockData get(LockPosition pos, boolean create) {
        ChunkLockData chunkData;

        if (pos instanceof BlockLockPosition) {
            ChunkPos chunk = new ChunkPos(pos.blockPosition());
            chunkData = this.locks.get(chunk);
            if (chunkData != null || !create)
                return chunkData;
            chunkData = new ChunkLockData();
            this.locks.put(chunk, chunkData);
            return chunkData;
        } else {
            return this.entityLocks;
        }
    }

    private void removeLock(LockPosition pos, BlockPos clickPos, boolean drop) {
        ChunkLockData chunkData = this.get(pos, false);
        if (chunkData == null)
            return;

        AbstractLock lock = chunkData.removeLock(pos);
        if (lock == null)
            return;

        if (pos instanceof BlockLockPosition) {
            lock.onRemove(this.level, pos.blockPosition(), clickPos);
            if (drop) {
                ItemStack lockStack = lock.getStack();
                if (!lockStack.isEmpty())
                    Block.popResource(this.level, clickPos, lockStack);
            }
            LocksmithMessages.PLAY.sendToTracking(this.level, new ChunkPos(pos.blockPosition()), new ClientboundDeleteLocksPacket(Collections.singleton(pos)));
        } else {
            Entity entity = ((EntityLockPosition) pos).getEntity();
            lock.onRemove(entity);
            if (drop) {
                ItemStack lockStack = lock.getStack();
                if (!lockStack.isEmpty())
                    Block.popResource(this.level, clickPos, lockStack);
            }
            LocksmithMessages.PLAY.sendToTracking(entity, new ClientboundDeleteLocksPacket(Collections.singleton(pos)));
        }

        this.setDirty();
    }

    @Override
    public void removeLock(BlockPos pos, BlockPos clickPos, boolean drop) {
        this.removeLock(LockPosition.of(pos), clickPos, drop);
    }

    @Override
    public void removeLock(Entity entity, boolean drop) {
        this.removeLock(LockPosition.of(entity), entity.blockPosition(), drop);
    }

    @Override
    public void load(CompoundTag nbt) {
        ListTag tag = nbt.getList("Chunks", 10);
        for (int i = 0; i < tag.size(); i++) {
            CompoundTag lock = tag.getCompound(i);
            ChunkLockData data = new ChunkLockData();
            data.load(lock);

            this.locks.put(new ChunkPos(lock.getInt("X"), lock.getInt("Z")), data);
        }

        this.entityLocks.load(nbt.getCompound("Entities"));
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag locksNbt = new ListTag();
        for (ChunkPos pos : this.locks.keySet()) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("X", pos.x);
            tag.putInt("Z", pos.z);

            locksNbt.add(this.locks.get(pos).save(tag));
        }

        nbt.put("Chunks", locksNbt);
        nbt.put("Entities", this.entityLocks.save(new CompoundTag()));
        return nbt;
    }

    private static class ChunkLockData {

        private final Map<LockPosition, AbstractLock> locks = new HashMap<>();

        public void load(CompoundTag compoundTag) {
            ListTag tag = compoundTag.getList("Locks", 10);
            for (int i = 0; i < tag.size(); i++) {
                AbstractLock lock = AbstractLock.CODEC.parse(NbtOps.INSTANCE, tag.getCompound(i)).getOrThrow(false, LOGGER::error);
                this.locks.put(lock.getPos(), lock);
            }
        }

        public CompoundTag save(CompoundTag compoundTag) {
            ListTag list = new ListTag();
            for (AbstractLock lock : this.locks.values()) {
                list.add(AbstractLock.CODEC.encodeStart(NbtOps.INSTANCE, lock).getOrThrow(false, LOGGER::error));
            }

            compoundTag.put("Locks", list);
            return compoundTag;
        }

        public Collection<AbstractLock> getLocks() {
            return locks.values();
        }

        @Nullable
        public AbstractLock getLock(LockPosition pos) {
            return this.locks.get(pos);
        }

        public void addLock(AbstractLock lock) {
            this.locks.put(lock.getPos(), lock);
        }

        @Nullable
        public AbstractLock removeLock(LockPosition pos) {
            return this.locks.remove(pos);
        }
    }
}
