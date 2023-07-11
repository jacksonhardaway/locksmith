package gg.moonflower.locksmith.common.lockpicking;

import dev.architectury.injectables.annotations.ExpectPlatform;
import gg.moonflower.locksmith.api.lock.position.EntityLockPosition;
import gg.moonflower.locksmith.api.lock.position.LockPosition;
import gg.moonflower.pollen.core.Pollen;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

/**
 * @author Ocelot
 */
public abstract class LockPickingContext {

    private static final byte ALL_PINS = 0b11111;

    private byte pinStates;
    private byte pickDamage;

    protected LockPickingContext() {
        this.pinStates = 0;
        this.pickDamage = 0;
    }

    public abstract void pick(int pin);

    public void reset() {
        this.pinStates = 0;
    }

    public abstract void stop(boolean success);

    public abstract GameState getState();

    protected boolean areAllPinsSet() {
        return this.pinStates == ALL_PINS;
    }

    public boolean getPinState(int pin) {
        if (pin < 0 || pin >= 5)
            return false;
        return ((this.pinStates >> pin) & 1) > 0;
    }

    public int getPickDamage() {
        return pickDamage;
    }

    public void setPinState(int pin, boolean set) {
        if (pin < 0 || pin >= 5)
            return;
        this.pinStates &= ~(1 << pin);
        if (set)
            this.pinStates |= 1 << pin;
    }

    public void setPickDamage(int pickDamage) {
        this.pickDamage = (byte) pickDamage;
    }

    public abstract boolean stillValid(Player player);

    @ExpectPlatform
    public static LockPickingContext client(int containerId) {
        return Pollen.expect();
    }

    public static LockPickingContext server(LockPosition pos, BlockPos clickPos, ServerPlayer player, ItemStack pickStack, InteractionHand pickHand) {
        return pos instanceof EntityLockPosition ? new ServerEntityPickingContext(((EntityLockPosition) pos).getEntity(), player, pickStack, pickHand) : new ServerBlockPickingContext(ContainerLevelAccess.create(player.level, pos.blockPosition()), clickPos, player, pickStack, pickHand);
    }

    public enum GameState {
        RUNNING, SUCCESS, FAIL
    }
}
