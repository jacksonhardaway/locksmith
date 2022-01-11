package gg.moonflower.locksmith.common.network.play.handler;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.client.lock.ClientLockManager;
import gg.moonflower.locksmith.client.screen.LockPickingScreen;
import gg.moonflower.locksmith.common.lock.LockManager;
import gg.moonflower.locksmith.common.lockpicking.LockPickingContext;
import gg.moonflower.locksmith.common.network.play.ClientboundAddLocksPacket;
import gg.moonflower.locksmith.common.network.play.ClientboundDeleteLockPacket;
import gg.moonflower.locksmith.common.network.play.ClientboundLockPickingPacket;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.Level;

public class LocksmithClientPlayPacketHandlerImpl implements LocksmithClientPlayPacketHandler {

    @Override
    public void handleAddLocks(ClientboundAddLocksPacket msg, PollinatedPacketContext ctx) {
        Level level = Minecraft.getInstance().level;
        if (level == null)
            return;

        LockManager manager = LockManager.get(level);
        ctx.enqueueWork(() -> {
            if (msg.isReplace())
                ((ClientLockManager) manager).clearLocks(msg.getChunk());
            for (AbstractLock lock : msg.getLocks())
                manager.addLock(lock);
        });
    }

    @Override
    public void handleDeleteLock(ClientboundDeleteLockPacket msg, PollinatedPacketContext ctx) {
        Level level = Minecraft.getInstance().level;
        if (level == null)
            return;

        ctx.enqueueWork(() -> LockManager.get(level).removeLock(msg.getPos()));
    }

    @Override
    public void handleLockPicking(ClientboundLockPickingPacket msg, PollinatedPacketContext ctx) {
        Screen screen = Minecraft.getInstance().screen;
        if (!(screen instanceof LockPickingScreen))
            return;

        LockPickingContext context = ((LockPickingScreen) screen).getMenu().getContext();
        ctx.enqueueWork(() -> context.setPinState(msg.getPin(), msg.getState()));
    }
}
