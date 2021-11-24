package gg.moonflower.locksmith.common.network.play;

import gg.moonflower.locksmith.common.network.play.handler.LocksmithClientPlayPacketHandler;
import gg.moonflower.locksmith.api.lock.LockData;
import gg.moonflower.pollen.api.network.packet.PollinatedPacket;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;
import net.minecraft.network.FriendlyByteBuf;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ClientboundLockSyncPacket implements PollinatedPacket<LocksmithClientPlayPacketHandler> {

    private final Set<LockData> locks;

    public ClientboundLockSyncPacket(Set<LockData> locks) {
        this.locks = locks;
    }

    public ClientboundLockSyncPacket(FriendlyByteBuf buf) {
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

    public Set<LockData> getLocks() {
        return locks;
    }
}
