package dev.hardaway.locksmith.api.lock;

import dev.hardaway.locksmith.core.registry.LocksmithAttachments;
import dev.hardaway.locksmith.core.registry.LocksmithParticles;
import dev.hardaway.locksmith.core.registry.LocksmithSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DoubleBlockLockable extends BasicBlockLockable {

    public DoubleBlockLockable(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity, Object context) {
        super(level, pos, state, blockEntity, context);
    }

    @Override
    public Optional<Lock> getLock() {
        if (this.blockEntity != null) {
            Optional<Lock> lock = this.blockEntity.getExistingData(LocksmithAttachments.LOCK);
            if (lock.isPresent())
                return lock;
        }

        ChunkAccess chunk = this.level.getChunk(pos);
        return chunk.getData(LocksmithAttachments.LOCK_CONTAINER).getLockAt(this.pos);
    }

    @Override
    public boolean lock(Lock lock) {
        if (this.getLock().isPresent())
            return false;

        if (this.blockEntity != null) {
            this.blockEntity.setData(LocksmithAttachments.LOCK, lock);
        } else {
            ChunkAccess chunk = this.level.getChunk(this.pos);
            chunk.getData(LocksmithAttachments.LOCK_CONTAINER).putLockAt(this.pos, lock);
            chunk.setUnsaved(true);
        }

        if (!level.isClientSide()) {
            this.level.getBlockState(this.pos).getVisualShape(this.level, this.pos, CollisionContext.empty()).forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                double d1 = Math.min(1.0, maxX - minX);
                double d2 = Math.min(1.0, maxY - minY);
                double d3 = Math.min(1.0, maxZ - minZ);
                int i = Math.max(2, Mth.ceil(d1 / 0.4));
                int j = Math.max(2, Mth.ceil(d2 / 0.4));
                int k = Math.max(2, Mth.ceil(d3 / 0.4));

                ((ServerLevel) this.level).sendParticles(LocksmithParticles.LOCK_SPARK.get(), this.pos.getX() + minX + maxX / 2.0, this.pos.getY() + minY + maxY / 2.0, this.pos.getZ() + minZ + maxZ / 2.0, i * j * k, (maxX - minX) / 4.0 + 0.0625, (maxY - minY) / 4.0 + 0.0625, (maxZ - minZ) / 4.0 + 0.0625, 0.0);
            });
            this.level.playSound(null, this.pos, LocksmithSounds.ITEM_LOCK_PLACE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        return true;
    }

    @Override
    public @Nullable Lock unlock() {
        Lock lock;
        if (blockEntity != null) {
            lock = blockEntity.removeData(LocksmithAttachments.LOCK);
        } else {
            ChunkAccess chunk = level.getChunk(pos);
            lock = chunk.getData(LocksmithAttachments.LOCK_CONTAINER).removeLockAt(pos);
            chunk.setUnsaved(true);
        }

        if (lock != null) {
            Block.popResource(level, pos, lock.getLockStack().copy());
            return lock;
        }

        return null;
    }

}