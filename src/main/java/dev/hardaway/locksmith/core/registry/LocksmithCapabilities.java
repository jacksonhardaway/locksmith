package dev.hardaway.locksmith.core.registry;

import dev.hardaway.locksmith.api.lock.Lock;
import dev.hardaway.locksmith.api.lock.Lockable;
import dev.hardaway.locksmith.core.Locksmith;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LocksmithCapabilities {

    public static final BlockCapability<Lockable, Void> LOCKABLE_BLOCK =
            BlockCapability.createVoid(Locksmith.path("lockable"), Lockable.class);

    public static final EntityCapability<Lockable, Void> LOCKABLE_ENTITY =
            EntityCapability.createVoid(Locksmith.path("lockable"), Lockable.class);


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlock(LocksmithCapabilities.LOCKABLE_BLOCK, (level, pos, state, blockEntity, context) -> new Lockable() {

            @Override
            public Optional<Lock> getLock() {
                if (blockEntity != null) {
                    Optional<Lock> lock = blockEntity.getExistingData(LocksmithAttachments.LOCK);
                    if (lock.isPresent())
                        return lock;
                }

                ChunkAccess chunk = level.getChunk(pos);
                return chunk.getData(LocksmithAttachments.LOCK_CONTAINER).getLockAt(pos);
            }

            @Override
            public boolean lock(Lock lock) {
                if (this.getLock().isPresent())
                    return false;

                if (blockEntity != null) {
                    blockEntity.setData(LocksmithAttachments.LOCK, lock);
                } else {
                    ChunkAccess chunk = level.getChunk(pos);
                    chunk.getData(LocksmithAttachments.LOCK_CONTAINER).putLockAt(pos, lock);
                }

                if (!level.isClientSide()) {
                    level.getBlockState(pos).getVisualShape(level, pos, CollisionContext.empty()).forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                        double d1 = Math.min(1.0, maxX - minX);
                        double d2 = Math.min(1.0, maxY - minY);
                        double d3 = Math.min(1.0, maxZ - minZ);
                        int i = Math.max(2, Mth.ceil(d1 / 0.4));
                        int j = Math.max(2, Mth.ceil(d2 / 0.4));
                        int k = Math.max(2, Mth.ceil(d3 / 0.4));

                        ((ServerLevel) level).sendParticles(LocksmithParticles.LOCK_SPARK.get(), pos.getX() + minX + maxX / 2.0, pos.getY() + minY + maxY / 2.0, pos.getZ() + minZ + maxZ / 2.0, i * j * k, (maxX - minX) / 4.0 + 0.0625, (maxY - minY) / 4.0 + 0.0625, (maxZ - minZ) / 4.0 + 0.0625, 0.0);
                    });
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
                }

                if (lock != null) {
                    Block.popResource(level, pos, lock.getLockStack().copy());
                    return lock;
                }

                return null;
            }
        }, BuiltInRegistries.BLOCK.stream().toArray(Block[]::new));

        for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
            event.registerEntity(LocksmithCapabilities.LOCKABLE_ENTITY, entityType, (entity, __) -> new Lockable() {
                @Override
                public Optional<Lock> getLock() {
                    return entity.getExistingData(LocksmithAttachments.LOCK);
                }

                @Override
                public boolean lock(Lock lock) {
                    if (this.getLock().isPresent())
                        return false;

                    if (!entity.level().isClientSide()) {
                        BlockPos pos = entity.blockPosition();
                        EntityDimensions dimensions = entity.getDimensions(entity.getPose());
                        AABB bb = new AABB(0, 0, 0, dimensions.width(), dimensions.height(), dimensions.width());
                        double d1 = Math.min(1.0, bb.maxX - bb.minX);
                        double d2 = Math.min(1.0, bb.maxY - bb.minY);
                        double d3 = Math.min(1.0, bb.maxZ - bb.minZ);
                        int i = Math.max(2, Mth.ceil(d1 / 0.4));
                        int j = Math.max(2, Mth.ceil(d2 / 0.4));
                        int k = Math.max(2, Mth.ceil(d3 / 0.4));

                        ServerLevel level = (ServerLevel) entity.level();
                        level.sendParticles(LocksmithParticles.LOCK_SPARK.get(), pos.getX() + bb.minX + bb.maxX / 2.0, pos.getY() + bb.minY + bb.maxY / 2.0, pos.getZ() + bb.minZ + bb.maxZ / 2.0, i * j * k, (bb.maxX - bb.minX) / 4.0 + 0.0625, (bb.maxY - bb.minY) / 4.0 + 0.0625, (bb.maxZ - bb.minZ) / 4.0 + 0.0625, 0.0);
                        level.playSound(null, pos, LocksmithSounds.ITEM_LOCK_PLACE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    }

                   entity.setData(LocksmithAttachments.LOCK, lock);
                    return true;
                }

                @Override
                public @Nullable Lock unlock() {
                    Lock lock = entity.removeData(LocksmithAttachments.LOCK);
                    if (lock != null) {
                        Block.popResource(entity.level(), entity.blockPosition(), lock.getLockStack().copy());
                        entity.removeData(LocksmithAttachments.LOCK);
                        return lock;
                    }

                    return null;
                }
            });
        }
    }

}
