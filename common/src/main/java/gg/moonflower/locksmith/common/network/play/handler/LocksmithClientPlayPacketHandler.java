package gg.moonflower.locksmith.common.network.play.handler;

import gg.moonflower.locksmith.common.network.play.ClientboundLockSyncPacket;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;

public interface LocksmithClientPlayPacketHandler {

    void handleLockSync(ClientboundLockSyncPacket msg, PollinatedPacketContext ctx);
}
