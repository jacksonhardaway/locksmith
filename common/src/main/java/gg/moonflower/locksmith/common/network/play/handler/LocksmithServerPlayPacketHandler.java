package gg.moonflower.locksmith.common.network.play.handler;

import gg.moonflower.locksmith.common.network.play.ServerboundLockPickingPacket;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;

public interface LocksmithServerPlayPacketHandler {

    void handleLockPicking(ServerboundLockPickingPacket msg, PollinatedPacketContext ctx);
}
