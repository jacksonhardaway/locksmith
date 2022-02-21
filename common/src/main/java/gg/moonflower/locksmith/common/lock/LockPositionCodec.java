package gg.moonflower.locksmith.common.lock;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import gg.moonflower.locksmith.api.lock.position.BlockLockPosition;
import gg.moonflower.locksmith.api.lock.position.EntityLockPosition;
import gg.moonflower.locksmith.api.lock.position.LockPosition;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@ApiStatus.Internal
public class LockPositionCodec implements Codec<LockPositionCodec.Type> {

    private static final LockPositionCodec CODEC = new LockPositionCodec();

    private LockPositionCodec() {
    }

    public static Codec<LockPosition> create() {
        return CODEC.dispatch(Type::getType, Type::codec);
    }

    @Override
    public <U> DataResult<Pair<Type, U>> decode(DynamicOps<U> ops, U input) {
        return ops.compressMaps() ? ops.getNumberValue(input).flatMap(number -> {
            int id = number.intValue();
            Type object = (id < 0 || id >= Type.values().length) ? null : Type.values()[id];
            return object == null ? DataResult.error("Unknown lock position id: " + number) : DataResult.success(object, Lifecycle.stable());
        }).map(pos -> Pair.of(pos, ops.empty())) : Codec.STRING.decode(ops, input).flatMap((pair) -> {
            Type object = Type.byName(pair.getFirst());
            return object == null ? DataResult.error("Unknown lock position key: " + pair.getFirst()) : DataResult.success(Pair.of(object, pair.getSecond()), Lifecycle.stable());
        });
    }

    @Override
    public <U> DataResult<U> encode(Type input, DynamicOps<U> ops, U prefix) {
        return ops.compressMaps() ? ops.mergeToPrimitive(prefix, ops.createInt(input.ordinal())).setLifecycle(Lifecycle.stable()) : ops.mergeToPrimitive(prefix, ops.createString(input.name().toLowerCase(Locale.ROOT))).setLifecycle(Lifecycle.stable());
    }

    public enum Type {
        BLOCK(BlockLockPosition.CODEC), ENTITY(EntityLockPosition.CODEC);

        private final Codec<? extends LockPosition> codec;

        Type(Codec<? extends LockPosition> codec) {
            this.codec = codec;
        }

        public Codec<? extends LockPosition> codec() {
            return codec;
        }

        @Nullable
        public static Type getType(LockPosition lockPosition) {
            if (lockPosition instanceof EntityLockPosition)
                return Type.ENTITY;
            if (lockPosition instanceof BlockLockPosition)
                return Type.BLOCK;
            return null;
        }

        @Nullable
        public static Type byName(String name) {
            for (Type type : values())
                if (type.name().toLowerCase(Locale.ROOT).equals(name))
                    return type;
            return null;
        }
    }
}
