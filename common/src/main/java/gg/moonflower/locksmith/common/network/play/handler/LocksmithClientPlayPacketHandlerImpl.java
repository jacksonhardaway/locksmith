package gg.moonflower.locksmith.common.network.play.handler;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.client.lock.ClientLockManager;
import gg.moonflower.locksmith.common.lock.LockManager;
import gg.moonflower.locksmith.common.network.play.ClientboundLockSyncPacket;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

public class LocksmithClientPlayPacketHandlerImpl implements LocksmithClientPlayPacketHandler {

    @Override
    public void handleLockSync(ClientboundLockSyncPacket msg, PollinatedPacketContext ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null)
            return;

        LockManager manager = LockManager.get(level);
        switch (msg.getAction()) {
            case REPLACE:
                if (msg.getLocks() == null)
                    break;
                ((ClientLockManager) manager).clearLocks(msg.getChunk());
                for (AbstractLock lock : msg.getLocks()) {
                    manager.addLock(lock);
                }
                break;
            case APPEND:
                if (msg.getLocks() == null)
                    break;
                for (AbstractLock lock : msg.getLocks()) {
                    manager.addLock(lock);
                }
                break;
            case REMOVE:
                if (msg.getPos() == null)
                    break;
                manager.removeLock(msg.getPos());
                break;
        }
    }
}
