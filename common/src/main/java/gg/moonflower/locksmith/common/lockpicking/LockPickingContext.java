package gg.moonflower.locksmith.common.lockpicking;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ocelot
 */
public abstract class LockPickingContext {

    private static final int PIN_SIZE = Mth.smallestEncompassingPowerOfTwo(PinState.values().length);

    protected final Set<LockPickingListener> listeners;
    private int pinStates;

    protected LockPickingContext() {
        this.listeners = new HashSet<>();
        this.pinStates = 0;
    }

    public void addListener(LockPickingListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(LockPickingListener listener) {
        this.listeners.add(listener);
    }

    public abstract void pick(int pin);

    public PinState getPinState(int pin) {
        if (pin < 0 || pin >= 5)
            return PinState.SET;
        return PinState.values()[(this.pinStates >> (pin * PIN_SIZE)) & (PIN_SIZE - 1)];
    }

    public void setPinState(int pin, PinState state) {
        if (pin < 0 || pin >= 5)
            return;
        PinState oldState = this.getPinState(pin);
        this.pinStates &= ~((PIN_SIZE - 1) << (pin * PIN_SIZE));
        this.pinStates |= state.ordinal() << (pin * PIN_SIZE);
        if (oldState != state)
            this.listeners.forEach(listener -> listener.onStateChange(state, pin));
    }

    public abstract boolean stillValid(Player player);

    public static LockPickingContext dummy() {
        return new ClientPickingContext();
    }

    public static LockPickingContext of(ServerPlayer player, ItemStack pickStack) {
        return new ServerPickingContext(player, pickStack);
    }
}
