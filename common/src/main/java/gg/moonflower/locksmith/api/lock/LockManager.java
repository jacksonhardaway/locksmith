package gg.moonflower.locksmith.api.lock;

import gg.moonflower.locksmith.api.lock.position.LockPosition;
import gg.moonflower.locksmith.client.lock.ClientLockManager;
import gg.moonflower.locksmith.common.lock.EmptyLockManager;
import gg.moonflower.locksmith.common.lock.ServerLockManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public interface LockManager {

    /**
     * Gets a lock manager for a level.
     *
     * @param level The level storing the locks.
     * @return The lock manager.
     */
    static LockManager get(Level level) {
        if (level instanceof ServerLevel) {
            return ServerLockManager.getOrCreate((ServerLevel) level);
        } else if (level.isClientSide()) { // Ensure we are on the client before using client classes
            if (level instanceof ClientLevel)
                return ClientLockManager.getOrCreate((ClientLevel) level);
        }

        return new EmptyLockManager();
    }

    @ApiStatus.Internal
    static BlockPos getLockPosition(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        if (state.hasProperty(ChestBlock.TYPE) && state.getValue(ChestBlock.TYPE) != ChestType.SINGLE)
            return pos.relative(ChestBlock.getConnectedDirection(state));

        if (state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
            boolean top = state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER;
            if (top) {
                return pos.relative(Direction.DOWN);
            } else {
                return pos.relative(Direction.UP);
            }
        }

        return pos;
    }

    void addLock(AbstractLock data);

    default void removeLock(BlockPos pos) {
        this.removeLock(pos, pos, false);
    }

    void removeLock(BlockPos pos, BlockPos clickPos, boolean drop);

    default void removeLock(Entity entity) {
        this.removeLock(entity, false);
    }

    void removeLock(Entity entity, boolean drop);

    /**
     * Retrieves a lock at a position.
     *
     * @param pos The position of the lock
     * @return The lock at the specified position or <code>null</code> if there is no lock
     */
    @Nullable
    AbstractLock getLock(LockPosition pos);
}
