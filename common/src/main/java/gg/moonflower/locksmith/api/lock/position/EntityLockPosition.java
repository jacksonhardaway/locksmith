package gg.moonflower.locksmith.api.lock.position;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;

import java.util.UUID;
import java.util.function.Supplier;

@ApiStatus.NonExtendable
public class EntityLockPosition implements LockPosition {

    public static final Codec<EntityLockPosition> CODEC = ExtraCodecs.UUID.xmap(EntityLockPosition::new, pos -> pos.entity.get().getUUID());
    private final UUID entityId;
    private final int hashCode;
    private Supplier<Entity> entity;

    private EntityLockPosition(UUID entityId) {
        this.entity = null;
        this.entityId = entityId;
        this.hashCode = entityId.hashCode();
    }

    EntityLockPosition(UUID entityId, Supplier<Entity> entity) {
        this.entity = entity;
        this.entityId = entityId;
        this.hashCode = entityId.hashCode();
    }

    EntityLockPosition(int entityId, Supplier<Entity> entity) {
        this.entity = entity;
        this.entityId = null;
        this.hashCode = entityId;
    }

    @Override
    public BlockPos blockPosition() {
        return this.entity.get().blockPosition();
    }

    @Override
    public Vec3 position() {
        return this.entity.get().position();
    }

    public Entity getEntity() {
        return this.entity.get();
    }

    @ApiStatus.Internal
    public void setEntity(Supplier<Entity> entity) {
        Validate.isTrue(this.entity == null);
        this.entity = entity;
    }

    public UUID getEntityId() {
        return entityId;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityLockPosition that = (EntityLockPosition) o;
        Entity thisEntity = this.getEntity();
        Entity thatEntity = that.getEntity();
        return thisEntity != null && thatEntity != null ? thisEntity.equals(thatEntity) : this.entityId.equals(that.entityId);
    }
}
