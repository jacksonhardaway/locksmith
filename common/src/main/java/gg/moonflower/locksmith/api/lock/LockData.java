package gg.moonflower.locksmith.api.lock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SerializableUUID;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class LockData {
    public static final Codec<LockData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SerializableUUID.CODEC.fieldOf("id").forGetter(LockData::getId),
            BlockPos.CODEC.fieldOf("pos").forGetter(LockData::getPos),
            Codec.BOOL.fieldOf("locked").forGetter(LockData::isLocked)
    ).apply(instance, LockData::new));

    private final UUID id;
    private final BlockPos pos;
    private boolean locked;

    public LockData(UUID id, BlockPos pos, boolean locked) {
        this.id = id;
        this.pos = pos;
        this.locked = locked;
    }

    public LockData(UUID id, CompoundTag tag) {
        this.id = id;
        this.pos = new BlockPos(tag.getInt("X"), tag.getInt("Y"), tag.getInt("Z"));
        this.locked = tag.getBoolean("Locked");
    }

    public UUID getId() {
        return id;
    }

    public BlockPos getPos() {
        return pos;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
