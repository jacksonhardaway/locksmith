package dev.hardaway.locksmith.api.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.hardaway.locksmith.api.lock.Lock;
import net.minecraft.core.BlockPos;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LockContainer {

    public static final Codec<LockContainer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(BlockPos.CODEC, Lock.CODEC).fieldOf("locks").forGetter(container -> container.locks)
    ).apply(instance, LockContainer::new));

    private final Map<BlockPos, Lock> locks;

    LockContainer(Map<BlockPos, Lock> locks) {
        this.locks = new HashMap<>(locks);
    }

    public LockContainer() {
        this(Collections.emptyMap());
    }

    public boolean hasLockAt(BlockPos pos) {
        return locks.containsKey(pos);
    }

    public Optional<Lock> getLockAt(BlockPos pos) {
        return Optional.ofNullable(locks.get(pos));
    }

    public boolean putLockAt(BlockPos pos, Lock lock) {
        return locks.put(pos, lock) == null;
    }

    public Lock removeLockAt(BlockPos pos) {
        return locks.remove(pos);
    }
}
