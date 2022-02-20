package gg.moonflower.locksmith.api.lock.position;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public class BlockLockPosition implements LockPosition {

    public static final Codec<BlockLockPosition> CODEC = BlockPos.CODEC.xmap(BlockLockPosition::new, BlockLockPosition::blockPosition);

    private final BlockPos blockPos;
    private final Vec3 pos;
    private final int hashCode;

    BlockLockPosition(BlockPos blockPos) {
        this.blockPos = blockPos;
        this.pos = new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
        this.hashCode = this.blockPos.hashCode();
    }

    @Override
    public BlockPos blockPosition() {
        return this.blockPos;
    }

    @Override
    public Vec3 position() {
        return this.pos;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockLockPosition that = (BlockLockPosition) o;
        return blockPos.equals(that.blockPos);
    }
}
