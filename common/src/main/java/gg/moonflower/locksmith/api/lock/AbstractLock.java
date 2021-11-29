package gg.moonflower.locksmith.api.lock;

import com.mojang.serialization.Codec;
import gg.moonflower.locksmith.core.registry.LocksmithLocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.UUID;

/**
 * @author Jackson
 * @since 1.0.0
 */
public abstract class AbstractLock {
    public static final Codec<AbstractLock> CODEC = LocksmithLocks.LOCKS.dispatch(AbstractLock::getType, LockType::codec);

    private final LockType type;
    private final UUID id;
    private final BlockPos pos;
    private final ItemStack stack;

    public AbstractLock(LockType type, UUID id, BlockPos pos, ItemStack stack) {
        this.type = type;
        this.id = id;
        this.pos = pos;
        this.stack = stack;
    }

    /**
     * @return The id of the lock.
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return The position of the lock.
     */
    public BlockPos getPos() {
        return pos;
    }

    /**
     * @return The item used to create this lock.
     */
    public ItemStack getStack() {
        return stack;
    }

    public LockType getType() {
        return type;
    }

    public void onRemove(Level level, BlockPos pos) {
        ItemStack lockStack = this.stack;
        if (!lockStack.isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), lockStack);
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }
    }

    /**
     * Called when attempting to remove the lock.
     *
     * @param player The player removing the lock.
     * @param level  The level of the lock.
     * @param stack  The stack being used to remove the lock.
     * @return Whether the lock can be removed.
     */
    public abstract boolean canRemove(Player player, Level level, ItemStack stack);

    /**
     * Checks if this lock can be unlocked by the player.
     *
     * @param player The player attempting to unlock the lock.
     * @param level  The level of the lock.
     * @param stack  The stack being used to unlock the lock.
     * @return Whether the lock can be unlocked.
     */
    public abstract boolean canUnlock(Player player, Level level, ItemStack stack);

    /**
     * Fires when a player right-clicks on a locked block.
     *
     * @return Whether the right-click action should succeed.
     */
    public abstract boolean onRightClick(Player player, Level level, ItemStack stack, BlockHitResult hitResult);

    /**
     * Fires when a player left-clicks on a locked block.
     *
     * @return Whether the left-click action should succeed.
     */
    public abstract boolean onLeftClick(Player player, Level level, InteractionHand hand, BlockPos pos, Direction direction);

    /**
     * Fires when a player right-clicks a locked block with a lockpick on the client.
     * <p>Used to open the lockpick minigame.
     */
    public abstract void onLockpick(Player player, Level level);
}
