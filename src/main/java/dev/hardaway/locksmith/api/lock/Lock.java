package dev.hardaway.locksmith.api.lock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class Lock {

    public static final Lock EMPTY = new Lock(Util.NIL_UUID);
    public static final Codec<Lock> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("id").forGetter(Lock::getId),
            ItemStack.OPTIONAL_CODEC.optionalFieldOf("item", ItemStack.EMPTY).forGetter(Lock::getLockStack)
    ).apply(instance, Lock::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Lock> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            Lock::getId,
            ItemStack.OPTIONAL_STREAM_CODEC,
            Lock::getLockStack,
            Lock::new
    );

    private final UUID id;
    private final ItemStack lockStack;

    public Lock(UUID id) {
        this(id, ItemStack.EMPTY);
    }

    public Lock(UUID id, ItemStack lockStack) {
        this.id = id;
        this.lockStack = lockStack;
    }

    public UUID getId() {
        return id;
    }

    public ItemStack getLockStack() {
        return lockStack;
    }
}
