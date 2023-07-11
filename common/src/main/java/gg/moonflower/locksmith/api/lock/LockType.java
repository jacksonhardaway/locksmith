package gg.moonflower.locksmith.api.lock;

import com.mojang.serialization.Codec;

/**
 * A type of lock that can be serialized and deserialized.
 *
 * @author Ocelot
 * @since 1.0.0
 */
public record LockType(Codec<? extends AbstractLock> codec) {
}