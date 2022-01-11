package gg.moonflower.locksmith.common.network.play;

import gg.moonflower.locksmith.common.network.play.handler.LocksmithClientPlayPacketHandler;
import gg.moonflower.pollen.api.network.packet.PollinatedPacket;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class ClientboundDeleteLockPacket  implements PollinatedPacket<LocksmithClientPlayPacketHandler> {

    private final BlockPos pos;

    public ClientboundDeleteLockPacket(BlockPos pos) {
        this.pos = pos;
    }

    public ClientboundDeleteLockPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    @Override
    public void writePacketData(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public void processPacket(LocksmithClientPlayPacketHandler handler, PollinatedPacketContext ctx) {
        handler.handleDeleteLock(this, ctx);
    }

    public BlockPos getPos() {
        return pos;
    }
}
