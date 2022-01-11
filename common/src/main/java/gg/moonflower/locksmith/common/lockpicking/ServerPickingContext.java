package gg.moonflower.locksmith.common.lockpicking;

import gg.moonflower.locksmith.common.network.LocksmithMessages;
import gg.moonflower.locksmith.common.network.play.ClientboundLockPickingPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ServerPickingContext extends LockPickingContext {

    private final ServerPlayer player;
    private final ItemStack pickStack;
    private boolean failed;

    public ServerPickingContext(ServerPlayer player, ItemStack pickStack) {
        this.player = player;
        this.pickStack = pickStack;
        this.failed = false;
    }

    @Override
    public void pick(int pin) {
        System.out.println("Player set pin " + pin);
        this.setPinState(pin, PinState.DROPPED);
        this.listeners.forEach(listener -> listener.onPick(pin));
    }

    @Override
    public void setPinState(int pin, PinState state) {
        if (pin < 0 || pin >= 5)
            return;
        super.setPinState(pin, state);
        LocksmithMessages.PLAY.sendTo(this.player, new ClientboundLockPickingPacket(state, pin));
    }

    @Override
    public boolean stillValid(Player player) {
        return this.player == player && !this.failed;
    }
}
