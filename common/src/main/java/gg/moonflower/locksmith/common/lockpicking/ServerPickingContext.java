package gg.moonflower.locksmith.common.lockpicking;

import gg.moonflower.locksmith.common.network.LocksmithMessages;
import gg.moonflower.locksmith.common.network.play.ClientboundLockPickingPacket;
import gg.moonflower.locksmith.core.registry.LocksmithSounds;
import gg.moonflower.locksmith.core.registry.LocksmithStats;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * @author Ocelot
 */
@ApiStatus.Internal
public abstract class ServerPickingContext extends LockPickingContext {

    /**
     * The amount of time to wait before ending the minigame after failing.
     */
    public static final int FAIL_WAIT_TIME = 10;
    /**
     * The amount of time there must be between setting a pin or else the game fails.
     */
    public static final int FAIL_THRESHOLD = 10;

    protected final ServerPlayer player;
    protected final ItemStack pickStack;
    protected final InteractionHand pickHand;
    protected final Game game;
    protected int lastSetTime;
    protected GameState state;
    protected int stop;

    protected ServerPickingContext(ServerPlayer player, ItemStack pickStack, InteractionHand pickHand) {
        this.player = player;
        this.pickStack = pickStack;
        this.pickHand = pickHand;
        RandomSource random = player.level.getRandom();
        this.game = new Game(2, random);
        this.state = GameState.RUNNING;
        this.stop = 0;
        this.setPickDamage(pickStack.getDamageValue());
    }

    protected abstract void playSound(SoundEvent sound);

    protected abstract void removeLock();

    @Override
    public void pick(int pin) {
        if (this.getPinState(pin)) {
            LocksmithMessages.PLAY.sendTo(this.player, new ClientboundLockPickingPacket(pin, true)); // Client must be behind, so reply with the current state
            return;
        }

        if (this.game.shouldDrop(pin)) {
            this.playSound(LocksmithSounds.LOCK_PICKING_SET.get());
            this.setPinState(pin, true);
            this.game.next(this);
            if (this.areAllPinsSet())
                this.stop(true);
        } else {
            this.reset();
        }
    }

    @Override
    public void reset() {
        this.game.reset();
        this.playSound(LocksmithSounds.LOCK_PICKING_PINS_DROP.get());
        this.pickStack.hurtAndBreak(1, this.player, contextPlayer -> {
            this.state = GameState.FAIL;
            this.stop = this.player.tickCount;
            this.playSound(LocksmithSounds.LOCK_PICKING_FAIL.get());
            LocksmithMessages.PLAY.sendTo(this.player, new ClientboundLockPickingPacket(ClientboundLockPickingPacket.Type.FAIL));
            contextPlayer.broadcastBreakEvent(this.pickHand);
        });
        this.setPickDamage(this.pickStack.isEmpty() ? 2 : this.pickStack.getDamageValue());
        this.player.inventoryMenu.broadcastChanges();
        super.reset();
        LocksmithMessages.PLAY.sendTo(this.player, new ClientboundLockPickingPacket(ClientboundLockPickingPacket.Type.RESET));
    }

    @Override
    public void stop(boolean success) {
        this.state = success ? GameState.SUCCESS : GameState.FAIL;
        this.stop = this.player.tickCount;
        this.playSound(success ? LocksmithSounds.LOCK_PICKING_SUCCESS.get() : LocksmithSounds.LOCK_PICKING_FAIL.get());

        if (success) {
            LocksmithMessages.PLAY.sendTo(this.player, new ClientboundLockPickingPacket(ClientboundLockPickingPacket.Type.SUCCESS));
            this.player.awardStat(LocksmithStats.PICK_LOCK.get());
            this.removeLock();
            return;
        }

        this.pickStack.hurtAndBreak(1, this.player, contextPlayer -> contextPlayer.broadcastBreakEvent(this.pickHand));
        this.setPickDamage(this.pickStack.isEmpty() ? 2 : this.pickStack.getDamageValue());
        LocksmithMessages.PLAY.sendTo(this.player, new ClientboundLockPickingPacket(ClientboundLockPickingPacket.Type.FAIL));
    }

    @Override
    public GameState getState() {
        return this.state;
    }

    @Override
    public void setPinState(int pin, boolean set) {
        if (pin < 0 || pin >= 5)
            return;
        super.setPinState(pin, set);

        if (this.player.tickCount - this.lastSetTime < FAIL_THRESHOLD) {
            this.playSound(LocksmithSounds.LOCK_PICKING_OVERSET.get());
            this.stop(false);
            return;
        }

        this.lastSetTime = this.player.tickCount;
        LocksmithMessages.PLAY.sendTo(this.player, new ClientboundLockPickingPacket(pin, set));
    }

    @Override
    public boolean stillValid(Player player) {
        return this.player == player && (this.state == GameState.RUNNING || this.player.tickCount - this.stop < FAIL_WAIT_TIME);
    }

    public static class Game {

        private final int[] droppingPins;
        private int droppingIndex;

        private Game(int complexity, RandomSource random) {
            int size = Mth.clamp(complexity, 0, 4);
            List<Integer> pins = new ArrayList<>(size);
            while (pins.size() < size)
                pins.add(random.nextInt(5));
            this.droppingPins = pins.stream().mapToInt(Integer::intValue).toArray();
            this.droppingIndex = -1;
        }

        private boolean isNonDropping(int pin) {
            return IntStream.of(this.droppingPins).noneMatch(dropping -> dropping == pin);
        }

        public void reset() {
            this.droppingIndex = -1;
        }

        public void next(LockPickingContext context) {
            if (this.droppingIndex >= 0) {
                this.droppingIndex++;
            } else {
                for (int i = 0; i < 5; i++) {
                    if (this.isNonDropping(i) && !context.getPinState(i))
                        return;
                }
                this.droppingIndex = 0;
            }
        }

        public boolean shouldDrop(int pin) {
            return this.droppingIndex == -1 ? this.isNonDropping(pin) : this.droppingPins[this.droppingIndex] == pin;
        }
    }
}
