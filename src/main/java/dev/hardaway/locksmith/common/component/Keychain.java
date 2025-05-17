package dev.hardaway.locksmith.common.component;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Keychain implements TooltipComponent {
    public static final Keychain EMPTY = new Keychain(List.of());
    public static final Codec<Keychain> CODEC = ItemStack.CODEC.listOf().xmap(Keychain::new, keychain -> keychain.keys);
    public static final StreamCodec<RegistryFriendlyByteBuf, Keychain> STREAM_CODEC = ItemStack.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(Keychain::new, keychain -> keychain.keys);
    private static final int NO_STACK_INDEX = -1;
    final List<ItemStack> keys;

    Keychain(List<ItemStack> keys) {
        this.keys = keys;
    }

    public ItemStack getItemUnsafe(int index) {
        return this.keys.get(index);
    }

    public Stream<ItemStack> itemCopyStream() {
        return this.keys.stream().map(ItemStack::copy);
    }

    public Iterable<ItemStack> items() {
        return this.keys;
    }

    public Iterable<ItemStack> itemsCopy() {
        return Lists.transform(this.keys, ItemStack::copy);
    }

    public int size() {
        return this.keys.size();
    }

    public boolean isEmpty() {
        return this.keys.isEmpty();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return other instanceof Keychain keychain && ItemStack.listMatches(this.keys, keychain.keys);
        }
    }

    @Override
    public int hashCode() {
        return ItemStack.hashStackList(this.keys);
    }

    @Override
    public String toString() {
        return "Keychain" + this.keys;
    }

    public static class Mutable {
        private final List<ItemStack> keys;

        public Mutable(Keychain keychain) {
            this.keys = new ArrayList<>(keychain.keys);
        }

        public Keychain.Mutable clearKeys() {
            this.keys.clear();
            return this;
        }

        private int findStackIndex(ItemStack stack) {
            for (int i = 0; i < this.keys.size(); i++) {
                if (ItemStack.isSameItemSameComponents(this.keys.get(i), stack)) {
                    return i;
                }
            }

            return NO_STACK_INDEX;
        }

        public int tryInsert(ItemStack stack) {
            int j = this.findStackIndex(stack);
            if (j == NO_STACK_INDEX) {
                this.keys.addFirst(stack.split(1));
                return 1;
            }

            return 0;
        }

        @Nullable
        public ItemStack removeOne() {
            if (this.keys.isEmpty()) {
                return null;
            } else {
                return this.keys.removeFirst().copy();
            }
        }

        public Keychain toImmutable() {
            return new Keychain(List.copyOf(this.keys));
        }
    }
}
