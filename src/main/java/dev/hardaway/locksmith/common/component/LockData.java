package dev.hardaway.locksmith.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.hardaway.locksmith.api.lock.Lock;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public record LockData(UUID id, Component label) {
    public static final Codec<LockData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("id").forGetter(LockData::id),
            ComponentSerialization.CODEC.fieldOf("label").forGetter(LockData::label)
    ).apply(instance, LockData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LockData> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            LockData::id,
            ComponentSerialization.STREAM_CODEC,
            LockData::label,
            LockData::new
    );

    public Lock createLock() {
        return new Lock(this.id);
    }

    public Lock createLock(ItemStack stack) {
        return new Lock(this.id, stack);
    }
}
