package gg.moonflower.locksmith.api.lock;

import com.mojang.serialization.Codec;
import gg.moonflower.locksmith.api.lock.position.LockPosition;
import gg.moonflower.locksmith.core.registry.LocksmithLocks;
import gg.moonflower.locksmith.core.registry.LocksmithParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.UUID;

/**
 * @author Jackson
 * @since 1.0.0
 */
public abstract class AbstractLock {

    public static final Codec<AbstractLock> CODEC = LocksmithLocks.LOCKS.dispatch(AbstractLock::getType, LockType::codec);

    private final LockType type;
    private final UUID id;
    private final LockPosition pos;
    private final ItemStack stack;

    public AbstractLock(LockType type, UUID id, LockPosition pos, ItemStack stack) {
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
    public LockPosition getPos() {
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

    /**
     * Only called on the server when a lock is removed from the world.
     *
     * @param level    The level the lock was removed from
     * @param clickPos The position to spawn the lock at
     * @param pos      The position of the lock in the world
     */
    public void onRemove(Level level, BlockPos pos, BlockPos clickPos) {
        level.getBlockState(clickPos).getVisualShape(level, pos, CollisionContext.empty()).forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            double d1 = Math.min(1.0, maxX - minX);
            double d2 = Math.min(1.0, maxY - minY);
            double d3 = Math.min(1.0, maxZ - minZ);
            int i = Math.max(2, Mth.ceil(d1 / 0.4));
            int j = Math.max(2, Mth.ceil(d2 / 0.4));
            int k = Math.max(2, Mth.ceil(d3 / 0.4));

            ((ServerLevel) level).sendParticles(LocksmithParticles.LOCK_BREAK.get(), pos.getX() + minX + maxX / 2.0, pos.getY() + minY + maxY / 2.0, pos.getZ() + minZ + maxZ / 2.0, i * j * k, (maxX - minX) / 4.0 + 0.0625, (maxY - minY) / 4.0 + 0.0625, (maxZ - minZ) / 4.0 + 0.0625, 0.0);
        });
    }

    /**
     * Only called on the server when a lock is removed from the world.
     *
     * @param entity The entity the lock is removed from
     */
    public void onRemove(Entity entity) {
        double d1 = Math.min(1.0, entity.getBbWidth());
        double d2 = Math.min(1.0, entity.getBbHeight());
        double d3 = Math.min(1.0, entity.getBbWidth());
        int i = Math.max(2, Mth.ceil(d1 / 0.4));
        int j = Math.max(2, Mth.ceil(d2 / 0.4));
        int k = Math.max(2, Mth.ceil(d3 / 0.4));

        ((ServerLevel) entity.level).sendParticles(LocksmithParticles.LOCK_BREAK.get(), entity.getX(), entity.getY(0.5), entity.getZ(), i * j * k, entity.getBbWidth() / 4.0 + 0.0625, entity.getBbHeight() / 4.0 + 0.0625, entity.getBbWidth() / 4.0 + 0.0625, 0.0);
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
     * Checks to see if this lock can actually be removed using a lock pick.
     *
     * @param player    The player removing the lock.
     * @param level     The level of the lock.
     * @param pos       The position the player clicked
     * @param pickStack The lock pick stack
     * @param hand      The hand the lock pick is in
     * @return Whether the lock can be removed by picking
     */
    public abstract boolean pick(Player player, Level level, LockPosition pos, ItemStack pickStack, InteractionHand hand);

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
     * Fires when a player right-clicks on a locked entity.
     *
     * @return Whether the right-click action should succeed.
     */
    public abstract boolean onRightClick(Player player, Level level, ItemStack stack, Entity hitResult);

    /**
     * Fires when a player left-clicks on a locked entity.
     *
     * @return Whether the left-click action should succeed.
     */
    public abstract boolean onLeftClick(Player player, Level level, InteractionHand hand, Entity entity);
}
