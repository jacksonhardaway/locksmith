package gg.moonflower.locksmith.api.lock;

import com.mojang.serialization.Codec;
import dev.architectury.injectables.annotations.ExpectPlatform;
import gg.moonflower.pollen.api.platform.Platform;

/**
 * A type of body that can be serialized and deserialized.
 *
 * @author Ocelot
 */
public interface LockType {

    /**
     * Constructs a new type for each platform.
     *
     * @param codec The codec of the type
     * @return A new type with the codec
     */
    @ExpectPlatform
    static LockType of(Codec<? extends AbstractLock> codec) {
        return Platform.error();
    }

    /**
     * @return The codec for the lock type
     */
    Codec<? extends AbstractLock> codec();
}