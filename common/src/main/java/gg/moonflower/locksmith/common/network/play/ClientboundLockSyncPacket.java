package gg.moonflower.locksmith.common.network.play;

import gg.moonflower.locksmith.api.lock.LockData;
import gg.moonflower.locksmith.common.network.play.handler.LocksmithClientPlayPacketHandler;
import gg.moonflower.pollen.api.network.packet.PollinatedPacket;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class ClientboundLockSyncPacket implements PollinatedPacket<LocksmithClientPlayPacketHandler> {

    private final Action action;
    private final ChunkPos chunk;
    private BlockPos pos;
    private Collection<LockData> locks;

    public ClientboundLockSyncPacket(ChunkPos chunk, BlockPos pos) {
        this.action = Action.REMOVE;
        this.chunk = chunk;
        this.pos = pos;
    }

    public ClientboundLockSyncPacket(ChunkPos chunk, Collection<LockData> locks, boolean replace) {
        this.action = replace ? Action.REPLACE : Action.APPEND;
        this.chunk = chunk;
        this.locks = locks;
    }

    public ClientboundLockSyncPacket(FriendlyByteBuf buf) throws IOException {
        this.action = buf.readEnum(Action.class);
        this.chunk = new ChunkPos(buf.readLong());
        switch (this.action) {
            case REMOVE:
                this.pos = buf.readBlockPos();
                break;
            case REPLACE:
            case APPEND:
                int size = buf.readVarInt();
                this.locks = new HashSet<>();
                for (int i = 0; i < size; i++)
                    this.locks.add(buf.readWithCodec(LockData.CODEC));
                break;
        }
    }

    @Override
    public void writePacketData(FriendlyByteBuf buf) throws IOException {
        buf.writeEnum(this.action);
        buf.writeLong(this.chunk.toLong());
        switch (this.action) {
            case REMOVE:
                buf.writeBlockPos(this.pos);
                break;
            case REPLACE:
            case APPEND:
                buf.writeVarInt(this.locks.size());
                for (LockData lock : this.locks) {
                    buf.writeWithCodec(LockData.CODEC, lock);
                }
                break;
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

    @Nullable
    public BlockPos getPos() {
        return pos;
    }

    @Nullable
    public Collection<LockData> getLocks() {
        return locks;
    }

    public enum Action {
        REPLACE, APPEND, REMOVE
    }
}
