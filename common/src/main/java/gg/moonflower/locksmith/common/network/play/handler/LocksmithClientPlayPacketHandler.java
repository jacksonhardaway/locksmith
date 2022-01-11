package gg.moonflower.locksmith.common.network.play.handler;

import gg.moonflower.locksmith.common.network.play.ClientboundAddLocksPacket;
import gg.moonflower.locksmith.common.network.play.ClientboundDeleteLockPacket;
import gg.moonflower.locksmith.common.network.play.ClientboundLockPickingPacket;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;

public interface LocksmithClientPlayPacketHandler {

    void handleAddLocks(ClientboundAddLocksPacket msg, PollinatedPacketContext ctx);

    void handleDeleteLock(ClientboundDeleteLockPacket msg, PollinatedPacketContext ctx);

    void handleLockPicking(ClientboundLockPickingPacket msg, PollinatedPacketContext ctx);
}
