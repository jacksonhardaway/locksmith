package gg.moonflower.locksmith.common.network.play.handler;

import gg.moonflower.locksmith.client.lock.ClientLockManager;
import gg.moonflower.locksmith.common.network.play.ClientboundLockSyncPacket;
import gg.moonflower.locksmith.api.lock.LockData;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;
import net.minecraft.world.level.ChunkPos;

import java.util.*;

public class LocksmithClientPlayPacketHandlerImpl implements LocksmithClientPlayPacketHandler {

    @Override
    public void handleLockSync(ClientboundLockSyncPacket msg, PollinatedPacketContext ctx) {
        Set<LockData> data = ClientLockManager.INSTANCE.getLockMap().computeIfAbsent(msg.getChunk(), chunkPos -> new HashSet<>());
        switch (msg.getAction()) {
            case REPLACE:
                data.clear();
                data.addAll(msg.getLocks());
                break;
            case APPEND:
                data.addAll(msg.getLocks());
                break;
            case REMOVE:
                data.removeAll(msg.getLocks());
                break;
        }
    }
}
