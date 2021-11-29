package gg.moonflower.locksmith.api.lock.forge;

import com.mojang.serialization.Codec;
import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.api.lock.LockType;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class LockTypeImpl extends ForgeRegistryEntry<LockTypeImpl> implements LockType {

    private final Codec<? extends AbstractLock> codec;

    public LockTypeImpl(Codec<? extends AbstractLock> codec) {
        this.codec = codec;
    }

    public static LockType of(Codec<? extends AbstractLock> codec) {
        return new LockTypeImpl(codec);
    }

    @Override
    public Codec<? extends AbstractLock> codec() {
        return codec;
    }
}
