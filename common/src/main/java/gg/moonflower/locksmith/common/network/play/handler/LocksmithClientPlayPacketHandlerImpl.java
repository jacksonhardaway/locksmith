package gg.moonflower.locksmith.common.network.play.handler;

import gg.moonflower.locksmith.api.lock.LockData;
import gg.moonflower.locksmith.client.lock.ClientLockManager;
import gg.moonflower.locksmith.common.network.play.ClientboundLockSyncPacket;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;
import net.minecraft.client.Minecraft;

import java.util.HashSet;
import java.util.Set;

public class LocksmithClientPlayPacketHandlerImpl implements LocksmithClientPlayPacketHandler {

    @Override
    public void handleLockSync(ClientboundLockSyncPacket msg, PollinatedPacketContext ctx) {
        Set<LockData> data = ClientLockManager.INSTANCE.getLockMap(Minecraft.getInstance().level.dimension()).computeIfAbsent(msg.getChunk(), chunkPos -> new HashSet<>());
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
