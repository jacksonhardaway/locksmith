package gg.moonflower.locksmith.common.network.play.handler;

import gg.moonflower.locksmith.common.network.play.ClientboundAddLocksPacket;
import gg.moonflower.locksmith.common.network.play.ClientboundDeleteLocksPacket;
import gg.moonflower.locksmith.common.network.play.ClientboundLockPickingPacket;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;

public interface LocksmithClientPlayPacketHandler {

    void handleAddLocks(ClientboundAddLocksPacket msg, PollinatedPacketContext ctx);

    void handleDeleteLock(ClientboundDeleteLocksPacket msg, PollinatedPacketContext ctx);
    
    void handleLockPicking(ClientboundLockPickingPacket msg, PollinatedPacketContext ctx);
}
