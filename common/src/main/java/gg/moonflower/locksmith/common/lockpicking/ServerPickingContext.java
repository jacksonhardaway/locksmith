package gg.moonflower.locksmith.common.lockpicking;

import gg.moonflower.locksmith.common.lock.LockManager;
import gg.moonflower.locksmith.common.network.LocksmithMessages;
import gg.moonflower.locksmith.common.network.play.ClientboundLockPickingPacket;
import gg.moonflower.locksmith.core.registry.LocksmithSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * @author Ocelot
 */
public class ServerPickingContext extends LockPickingContext {

    /**
     * The amount of time to wait before ending the minigame after failing.
     */
    private static final int FAIL_WAIT_TIME = 10;
    /**
     * The amount of time there must be between setting a pin or else the game fails.
     */
    private static final int FAIL_THRESHOLD = 10;

    private final ContainerLevelAccess access;
    private final ServerPlayer player;
    private final ItemStack pickStack;
    private final InteractionHand pickHand;
    private final Game game;
    private int lastSetTime;
    private GameState state;
    private int stop;

    ServerPickingContext(ContainerLevelAccess access, ServerPlayer player, ItemStack pickStack, InteractionHand pickHand) {
        this.access = access;
        this.player = player;
        this.pickStack = pickStack;
        this.pickHand = pickHand;
        Random random = player.level.getRandom();
        this.game = new Game(2 + random.nextInt(2), random); // 2-3 pins will drop
        this.state = GameState.RUNNING;
        this.stop = 0;
        player.server.execute(() -> this.setPickDamage(this.pickStack.getDamageValue()));
    }

    @Override
    public void pick(int pin) {
        if (this.getPinState(pin)) {
            LocksmithMessages.PLAY.sendTo(this.player, new ClientboundLockPickingPacket(pin, true)); // Client must be behind, so reply with the current state
            return;
        }

        if (this.game.shouldDrop(pin)) {
            this.access.execute((level, pos) -> level.playSound(this.player, pos, LocksmithSounds.ITEM_LOCK_PLACE.get(), SoundSource.PLAYERS, 1.0F, 1.0F));
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
        this.access.execute((level, pos) -> level.playSound(this.player, pos, SoundEvents.CHEST_LOCKED, SoundSource.PLAYERS, 1.0F, 1.0F));
        super.reset();
        LocksmithMessages.PLAY.sendTo(this.player, new ClientboundLockPickingPacket(ClientboundLockPickingPacket.Type.RESET));
    }

    @Override
    public void stop(boolean success) {
        this.state = success ? GameState.SUCCESS : GameState.FAIL;
        this.stop = this.player.tickCount;

        if (success) {
            LocksmithMessages.PLAY.sendTo(this.player, new ClientboundLockPickingPacket(ClientboundLockPickingPacket.Type.SUCCESS));
            this.access.execute((level, pos) -> LockManager.get(level).removeLock(pos));
            return;
        }

        this.access.execute((level, pos) -> level.playSound(this.player, pos, SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F));
        this.pickStack.hurtAndBreak(1, this.player, contextPlayer -> contextPlayer.broadcastBreakEvent(this.pickHand));
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

    private static class Game {

        private final int[] droppingPins;
        private int droppingIndex;

        private Game(int complexity, Random random) {
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
