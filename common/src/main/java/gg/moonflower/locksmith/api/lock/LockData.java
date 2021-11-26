package gg.moonflower.locksmith.api.lock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SerializableUUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class LockData {
    public static final Codec<LockData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SerializableUUID.CODEC.fieldOf("id").forGetter(LockData::getId),
            BlockPos.CODEC.fieldOf("pos").forGetter(LockData::getPos),
            ItemStack.CODEC.fieldOf("item").forGetter(LockData::getStack),
            Codec.BOOL.fieldOf("locked").forGetter(LockData::isLocked)
    ).apply(instance, LockData::new));

    private final UUID id;
    private final BlockPos pos;
    private final ItemStack stack;
    private boolean locked;

    public LockData(UUID id, BlockPos pos, ItemStack stack, boolean locked) {
        this.id = id;
        this.pos = pos;
        this.stack = stack;
        this.locked = locked;
    }

    public UUID getId() {
        return id;
    }

    public BlockPos getPos() {
        return pos;
    }

    public ItemStack getStack() {
        return stack;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
