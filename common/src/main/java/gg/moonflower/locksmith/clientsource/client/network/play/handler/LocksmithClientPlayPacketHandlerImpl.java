package gg.moonflower.locksmith.clientsource.client.network.play.handler;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.api.lock.LockManager;
import gg.moonflower.locksmith.api.lock.position.BlockLockPosition;
import gg.moonflower.locksmith.api.lock.position.EntityLockPosition;
import gg.moonflower.locksmith.api.lock.position.LockPosition;
import gg.moonflower.locksmith.clientsource.client.lock.ClientLockManager;
import gg.moonflower.locksmith.clientsource.client.screen.LockPickingScreen;
import gg.moonflower.locksmith.common.lockpicking.LockPickingContext;
import gg.moonflower.locksmith.common.network.play.ClientboundAddLocksPacket;
import gg.moonflower.locksmith.common.network.play.ClientboundDeleteLocksPacket;
import gg.moonflower.locksmith.common.network.play.ClientboundLockPickingPacket;
import gg.moonflower.locksmith.common.network.play.handler.LocksmithClientPlayPacketHandler;
import gg.moonflower.pollen.api.network.v1.packet.PollinatedPacketContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;

import java.util.UUID;

public class LocksmithClientPlayPacketHandlerImpl implements LocksmithClientPlayPacketHandler {

    private Entity getLockEntity(ClientLevel level, EntityLockPosition pos) {
        UUID id = pos.getEntityId();
        for (Entity e : level.entitiesForRendering()) {
            if (e.getUUID().equals(id)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public void handleAddLocks(ClientboundAddLocksPacket msg, PollinatedPacketContext ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null)
            return;

        LockManager manager = LockManager.get(level);
        ctx.enqueueWork(() -> {
            if (msg.isReplace())
                msg.getLocks().stream().filter(lock -> lock.getPos() instanceof BlockLockPosition).map(lock -> new ChunkPos(lock.getPos().blockPosition())).distinct().forEach(chunk -> ((ClientLockManager) manager).clearLocks(chunk));
            for (AbstractLock lock : msg.getLocks()) {
                if (lock.getPos() instanceof EntityLockPosition) {
                    EntityLockPosition entityLockPos = (EntityLockPosition) lock.getPos();
                    entityLockPos.setEntity(() -> this.getLockEntity(level, entityLockPos));
                }
                manager.addLock(lock);
            }
        });
    }

    @Override
    public void handleDeleteLock(ClientboundDeleteLocksPacket msg, PollinatedPacketContext ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null)
            return;

        for (LockPosition pos : msg.getPositions()) {
            if (pos instanceof BlockLockPosition) {
                ctx.enqueueWork(() -> LockManager.get(level).removeLock(pos.blockPosition()));
            } else if (pos instanceof EntityLockPosition) {
                Entity e = this.getLockEntity(level, (EntityLockPosition) pos);
                if (e != null)
                    ctx.enqueueWork(() -> LockManager.get(level).removeLock(e));
            }
        }
    }

    @Override
    public void handleLockPicking(ClientboundLockPickingPacket msg, PollinatedPacketContext ctx) {
        Screen screen = Minecraft.getInstance().screen;
        if (!(screen instanceof LockPickingScreen))
            return;

        LockPickingContext context = ((LockPickingScreen) screen).getMenu().getContext();
        ctx.enqueueWork(() -> {
            switch (msg.getType()) {
                case SET:
                    context.setPinState(msg.getPin(), msg.isSet());
                    ((LockPickingScreen) screen).lowerPick();
                    break;
                case RESET:
                    context.reset();
                    ((LockPickingScreen) screen).lowerPick();
                    break;
                case SUCCESS:
                    context.stop(true);
                    break;
                case FAIL:
                    context.stop(false);
                    break;
            }
        });
    }
}
