package gg.moonflower.locksmith.common.network.play.handler;

import gg.moonflower.locksmith.client.lock.ClientLockManager;
import gg.moonflower.locksmith.common.network.play.ClientboundLockSyncPacket;
import gg.moonflower.locksmith.api.lock.LockData;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;
import net.minecraft.world.level.ChunkPos;

public class LocksmithClientPlayPacketHandlerImpl implements LocksmithClientPlayPacketHandler {

    @Override
    public void handleLockSync(ClientboundLockSyncPacket msg, PollinatedPacketContext ctx) {
        for (LockData lock : msg.getLocks()) {
            ClientLockManager.INSTANCE.getLocks().put(new ChunkPos(lock.getPos()), lock);
        }
    }
}
