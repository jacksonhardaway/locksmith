package gg.moonflower.locksmith.common.network.play;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.api.lock.position.LockPosition;
import gg.moonflower.locksmith.common.network.play.handler.LocksmithClientPlayPacketHandler;
import gg.moonflower.pollen.api.network.packet.PollinatedPacket;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;
import net.minecraft.network.FriendlyByteBuf;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class ClientboundDeleteLocksPacket implements PollinatedPacket<LocksmithClientPlayPacketHandler> {

    private final Collection<LockPosition> positions;

    public ClientboundDeleteLocksPacket(Collection<LockPosition> positions) {
        this.positions = positions;
    }

    public ClientboundDeleteLocksPacket(FriendlyByteBuf buf) throws IOException {
        int size = buf.readVarInt();
        this.positions = new HashSet<>();
        for (int i = 0; i < size; i++)
            this.positions.add(buf.readWithCodec(LockPosition.CODEC));
    }

    @Override
    public void writePacketData(FriendlyByteBuf buf) throws IOException {
        buf.writeVarInt(this.positions.size());
        for (LockPosition pos : this.positions)
            buf.writeWithCodec(LockPosition.CODEC, pos);
    }

    @Override
    public void processPacket(LocksmithClientPlayPacketHandler handler, PollinatedPacketContext ctx) {
        handler.handleDeleteLock(this, ctx);
    }

    public Collection<LockPosition> getPositions() {
        return positions;
    }
}
