package dev.hardaway.locksmith.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record OriginalKey(int copies) {

    public static final Codec<OriginalKey> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("copies").forGetter(OriginalKey::copies)
    ).apply(instance, OriginalKey::new));
}
