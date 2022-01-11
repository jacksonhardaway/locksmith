package gg.moonflower.locksmith.common.network.play.handler;

import gg.moonflower.locksmith.common.menu.LockpickingMenu;
import gg.moonflower.locksmith.common.network.play.ServerboundLockPickingPacket;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;
import net.minecraft.world.entity.player.Player;

public class LocksmithServerPlayPacketHandlerImpl implements LocksmithServerPlayPacketHandler {

    @Override
    public void handleLockPicking(ServerboundLockPickingPacket msg, PollinatedPacketContext ctx) {
        Player player = ctx.getSender();
        if (player == null || !(player.containerMenu instanceof LockpickingMenu))
            return;

        ctx.enqueueWork(() -> ((LockpickingMenu) player.containerMenu).handle(msg));
    }
}
