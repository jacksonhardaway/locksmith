package dev.hardaway.locksmith.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;

import java.util.UUID;

public record KeyData(UUID id, Component label) {

    public static final Codec<KeyData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("id").forGetter(KeyData::id),
            ComponentSerialization.CODEC.fieldOf("label").forGetter(KeyData::label)
    ).apply(instance, KeyData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, KeyData> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            KeyData::id,
            ComponentSerialization.STREAM_CODEC,
            KeyData::label,
            KeyData::new
    );
}
