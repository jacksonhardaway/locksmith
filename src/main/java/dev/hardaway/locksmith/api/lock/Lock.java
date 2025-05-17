package dev.hardaway.locksmith.api.lock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class Lock {

    public static final Lock EMPTY = new Lock(Util.NIL_UUID);
    public static final Codec<Lock> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("id").forGetter(Lock::getId),
            ItemStack.OPTIONAL_CODEC.optionalFieldOf("id", ItemStack.EMPTY).forGetter(Lock::getLockStack)
    ).apply(instance, Lock::new));

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
