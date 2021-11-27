package gg.moonflower.locksmith.api.lock;

import com.mojang.serialization.Codec;

public class LockType {
    private final Codec<? extends AbstractLock> codec;

    public LockType(Codec<? extends AbstractLock> codec)
    {
        this.codec = codec;
    }

    /**
     * @return The codec to use for serializing and deserializing locks.
     */
    public Codec<? extends AbstractLock> getCodec()
    {
        return codec;
    }
}
