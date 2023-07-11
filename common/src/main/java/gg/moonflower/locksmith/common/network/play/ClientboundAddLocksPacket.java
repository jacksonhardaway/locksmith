package gg.moonflower.locksmith.common.network.play;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.common.network.play.handler.LocksmithClientPlayPacketHandler;
import gg.moonflower.pollen.api.network.v1.packet.PollinatedPacket;
import gg.moonflower.pollen.api.network.v1.packet.PollinatedPacketContext;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Collection;
import java.util.HashSet;

public class ClientboundAddLocksPacket implements PollinatedPacket<LocksmithClientPlayPacketHandler> {

    private final Collection<AbstractLock> locks;
    private final boolean replace;

    public ClientboundAddLocksPacket(Collection<AbstractLock> locks, boolean replace) {
        this.locks = locks;
        this.replace = replace;
    }

    public ClientboundAddLocksPacket(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        this.locks = new HashSet<>();
        for (int i = 0; i < size; i++)
            this.locks.add(buf.readWithCodec(AbstractLock.CODEC));
        this.replace = buf.readBoolean();
    }

    @Override
    public void writePacketData(FriendlyByteBuf buf) {
        buf.writeVarInt(this.locks.size());
        for (AbstractLock lock : this.locks)
            buf.writeWithCodec(AbstractLock.CODEC, lock);
        buf.writeBoolean(this.replace);
    }

    @Override
    public void processPacket(LocksmithClientPlayPacketHandler handler, PollinatedPacketContext ctx) {
        handler.handleAddLocks(this, ctx);
    }

    public Collection<AbstractLock> getLocks() {
        return locks;
    }

    public boolean isReplace() {
        return replace;
    }
}
