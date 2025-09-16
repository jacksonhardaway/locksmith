package dev.hardaway.locksmith.api.lock;

import dev.hardaway.locksmith.core.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BasicEntityLockable extends KeyAndLockLockable {

    private final Entity entity;

    public BasicEntityLockable(Entity entity, Object context) {
        this.entity = entity;
    }

    @Override
    public Optional<Lock> getLock() {
        return this.entity.getExistingData(LocksmithAttachments.LOCK);
    }

    @Override
    public boolean lock(Lock lock) {
        if (this.getLock().isPresent())
            return false;

        if (!this.entity.level().isClientSide()) {
            BlockPos pos = this.entity.blockPosition();
            EntityDimensions dimensions = this.entity.getDimensions(this.entity.getPose());
            AABB bb = new AABB(0, 0, 0, dimensions.width(), dimensions.height(), dimensions.width());
            double d1 = Math.min(1.0, bb.maxX - bb.minX);
            double d2 = Math.min(1.0, bb.maxY - bb.minY);
            double d3 = Math.min(1.0, bb.maxZ - bb.minZ);
            int i = Math.max(2, Mth.ceil(d1 / 0.4));
            int j = Math.max(2, Mth.ceil(d2 / 0.4));
            int k = Math.max(2, Mth.ceil(d3 / 0.4));

            ServerLevel level = (ServerLevel) this.entity.level();
            level.sendParticles(LocksmithParticles.LOCK_SPARK.get(), pos.getX() + bb.minX + bb.maxX / 2.0, pos.getY() + bb.minY + bb.maxY / 2.0, pos.getZ() + bb.minZ + bb.maxZ / 2.0, i * j * k, (bb.maxX - bb.minX) / 4.0 + 0.0625, (bb.maxY - bb.minY) / 4.0 + 0.0625, (bb.maxZ - bb.minZ) / 4.0 + 0.0625, 0.0);
            level.playSound(null, pos, LocksmithSounds.ITEM_LOCK_PLACE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        this.entity.setData(LocksmithAttachments.LOCK, lock);
        return true;
    }

    @Override
    public boolean canLock(ItemStack stack) {
        return super.canLock(stack) && this.entity.getType().is(LocksmithTags.Entities.LOCKABLES);
    }

    @Override
    public @Nullable Lock unlock() {
        Lock lock = this.entity.removeData(LocksmithAttachments.LOCK);
        if (lock != null) {
            Block.popResource(this.entity.level(), this.entity.blockPosition(), lock.getLockStack().copy());
            this.entity.removeData(LocksmithAttachments.LOCK);
            return lock;
        }

        return null;
    }
}
