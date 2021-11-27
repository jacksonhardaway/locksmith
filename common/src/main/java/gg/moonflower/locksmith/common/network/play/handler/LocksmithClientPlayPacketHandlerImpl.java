package gg.moonflower.locksmith.common.network.play.handler;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.client.lock.ClientLockManager;
import gg.moonflower.locksmith.common.lock.LockManager;
import gg.moonflower.locksmith.common.network.play.ClientboundAddLocksPacket;
import gg.moonflower.locksmith.common.network.play.ClientboundDeleteLockPacket;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

public class LocksmithClientPlayPacketHandlerImpl implements LocksmithClientPlayPacketHandler {

    @Override
    public void handleAddLocks(ClientboundAddLocksPacket msg, PollinatedPacketContext ctx) {
        Level level = Minecraft.getInstance().level;
        if (level == null)
            return;

        LockManager manager = LockManager.get(level);
        if (msg.isReplace())
            ((ClientLockManager) manager).clearLocks(msg.getChunk());
        for (AbstractLock lock : msg.getLocks())
            manager.addLock(lock);
    }

    @Override
    public void handleDeleteLock(ClientboundDeleteLockPacket msg, PollinatedPacketContext ctx) {
        Level level = Minecraft.getInstance().level;
        if (level == null)
            return;

        LockManager.get(level).removeLock(msg.getPos());
    }
}
