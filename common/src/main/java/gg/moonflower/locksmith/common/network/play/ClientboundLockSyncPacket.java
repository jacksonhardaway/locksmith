package gg.moonflower.locksmith.common.network.play;

import gg.moonflower.locksmith.api.lock.LockData;
import gg.moonflower.locksmith.common.network.play.handler.LocksmithClientPlayPacketHandler;
import gg.moonflower.pollen.api.network.packet.PollinatedPacket;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class ClientboundLockSyncPacket implements PollinatedPacket<LocksmithClientPlayPacketHandler> {

    private final Action action;
    private final ChunkPos chunk;
    private final Collection<LockData> locks;

    public ClientboundLockSyncPacket(Action action, ChunkPos chunk, Collection<LockData> locks) {
        this.action = action;
        this.chunk = chunk;
        this.locks = locks;
    }

    public ClientboundLockSyncPacket(FriendlyByteBuf buf) {
        this.action = buf.readEnum(Action.class);
        this.chunk = new ChunkPos(buf.readLong());

        int size = buf.readVarInt();
        this.locks = new HashSet<>();
        try {
            for (int i = 0; i < size; i++) {
                this.locks.add(buf.readWithCodec(LockData.CODEC));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read lock", e);
        }
    }

    @Override
    public void writePacketData(FriendlyByteBuf buf) {
        buf.writeEnum(this.action);
        buf.writeLong(this.chunk.toLong());
        buf.writeVarInt(this.locks.size());
        try {
            for (LockData lock : this.locks) {
                buf.writeWithCodec(LockData.CODEC, lock);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write lock", e);
        }
    }

    @Override
    public void processPacket(LocksmithClientPlayPacketHandler handler, PollinatedPacketContext ctx) {
        handler.handleLockSync(this, ctx);
    }

    public Action getAction() {
        return action;
    }

    public ChunkPos getChunk() {
        return chunk;
    }

    public Collection<LockData> getLocks() {
        return locks;
    }

    public enum Action {
        REPLACE, APPEND, REMOVE
    }
}
