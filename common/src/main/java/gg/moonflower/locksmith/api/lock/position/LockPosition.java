package gg.moonflower.locksmith.api.lock.position;

import com.mojang.serialization.Codec;
import gg.moonflower.locksmith.common.lock.LockPositionCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;

import java.util.UUID;

/**
 * A position a lock can be at.
 *
 * @author Ocelot
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface LockPosition {

    Codec<LockPosition> CODEC = LockPositionCodec.create();

    /**
     * @return The grid-aligned block pos
     */
    BlockPos blockPosition();

    /**
     * @return The raw position
     */
    Vec3 position();

    /**
     * Creates a lock position for a block.
     * @param pos The block position
     */
    static LockPosition of(BlockPos pos) {
        return new BlockLockPosition(pos);
    }

    /**
     * Creates a lock position for an entity.
     * @param entity The entity
     */
    static LockPosition of(Entity entity) {
        return new EntityLockPosition(entity.getUUID(), () -> entity);
    }

    /**
     * Creates a lock position for a server entity.
     * @param entityId The id of the entity
     */
    static LockPosition of(ServerLevel level, UUID entityId) {
        return new EntityLockPosition(entityId, () -> level.getEntity(entityId));
    }

    static LockPosition of(Level level, int entityId) {
        return new EntityLockPosition(entityId, () -> level.getEntity(entityId));
    }
}
