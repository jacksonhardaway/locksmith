package dev.hardaway.locksmith.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CopiedKey(int copy) {

    public static final Codec<CopiedKey>  CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("copy").forGetter(CopiedKey::copy)
    ).apply(instance, CopiedKey::new));
}
