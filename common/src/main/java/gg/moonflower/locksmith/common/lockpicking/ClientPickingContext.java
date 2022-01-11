package gg.moonflower.locksmith.common.lockpicking;

import gg.moonflower.locksmith.core.registry.LocksmithSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

/**
 * @author Ocelot
 */
public class ClientPickingContext extends LockPickingContext {

    private final int containerId;
    private GameState state;

    ClientPickingContext(int containerId) {
        this.containerId = containerId;
        this.state = GameState.RUNNING;
    }

    @Override
    public void pick(int pin) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(LocksmithSounds.ITEM_LOCK_PLACE.get(), 1.0F, 1.0F)); // TODO sounds
        super.setPinState(pin, true);
        Minecraft.getInstance().gameMode.handleInventoryButtonClick(this.containerId, pin);
    }

    @Override
    public void reset() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.CHEST_LOCKED, 1.0F, 1.0F)); // TODO sounds
        super.reset();
    }

    @Override
    public void stop(boolean success) {
        if (!success)
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.ITEM_BREAK, 1.0F, 1.0F));
        this.state = success ? GameState.SUCCESS : GameState.FAIL;
    }

    @Override
    public GameState getState() {
        return state;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
