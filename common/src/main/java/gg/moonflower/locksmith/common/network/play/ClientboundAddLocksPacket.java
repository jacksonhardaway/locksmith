package gg.moonflower.locksmith.common.network.play;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.common.network.play.handler.LocksmithClientPlayPacketHandler;
import gg.moonflower.pollen.api.network.packet.PollinatedPacket;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class ClientboundAddLocksPacket implements PollinatedPacket<LocksmithClientPlayPacketHandler> {

    private final ChunkPos chunk;
    private final Collection<AbstractLock> locks;
    private final boolean replace;

    public ClientboundAddLocksPacket(ChunkPos chunk, Collection<AbstractLock> locks, boolean replace) {
        this.chunk = chunk;
        this.locks = locks;
        this.replace = replace;
    }

    public ClientboundAddLocksPacket(FriendlyByteBuf buf) throws IOException {
        this.chunk = new ChunkPos(buf.readLong());
        int size = buf.readVarInt();
        this.locks = new HashSet<>();
        for (int i = 0; i < size; i++)
            this.locks.add(buf.readWithCodec(AbstractLock.CODEC));
        this.replace = buf.readBoolean();
    }

    @Override
    public void writePacketData(FriendlyByteBuf buf) throws IOException {
        buf.writeLong(this.chunk.toLong());
        buf.writeVarInt(this.locks.size());
        for (AbstractLock lock : this.locks)
            buf.writeWithCodec(AbstractLock.CODEC, lock);
        buf.writeBoolean(this.replace);
    }

    @Override
    public void processPacket(LocksmithClientPlayPacketHandler handler, PollinatedPacketContext ctx) {
        handler.handleAddLocks(this, ctx);
    }

    public ChunkPos getChunk() {
        return chunk;
    }

    public Collection<AbstractLock> getLocks() {
        return locks;
    }

    public boolean isReplace() {
        return replace;
    }
}
