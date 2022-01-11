package gg.moonflower.locksmith.common.lockpicking;

/**
 * @author Ocelot
 */
public interface LockPickingListener {

    void onPick(int pin);

    void onStateChange(PinState state, int pin);
}
