package gg.moonflower.locksmith.common.lockpicking;

import gg.moonflower.locksmith.common.network.LocksmithMessages;
import gg.moonflower.locksmith.common.network.play.ServerboundLockPickingPacket;
import net.minecraft.world.entity.player.Player;

public class ClientPickingContext extends LockPickingContext {

    @Override
    public void pick(int index) {
        LocksmithMessages.PLAY.sendToServer(new ServerboundLockPickingPacket(index));
    }

    @Override
    public void setPinState(int pin, PinState state) {
        if (pin < 0 || pin >= 5)
            return;
        super.setPinState(pin, state);
        this.listeners.forEach(listener -> listener.onPick(pin));
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
