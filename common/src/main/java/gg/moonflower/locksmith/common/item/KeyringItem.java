package gg.moonflower.locksmith.common.item;

import gg.moonflower.locksmith.core.registry.LocksmithItems;
import gg.moonflower.pollen.api.util.NbtConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class KeyringItem extends Item {

    public static final int MAX_KEYS = 4;

    public KeyringItem(Properties properties) {
        super(properties);
    }

    public static List<ItemStack> getKeys(ItemStack stack) {
        if (stack.getItem() != LocksmithItems.KEYRING.get())
            return Collections.emptyList();

        CompoundTag nbt = stack.getTag();
        if (nbt == null || !nbt.contains("Keys", NbtConstants.LIST))
            return Collections.emptyList();

        ListTag keysNbt = nbt.getList("Keys", NbtConstants.COMPOUND);
        if (keysNbt.isEmpty())
            return Collections.emptyList();

        List<ItemStack> list = new ArrayList<>(keysNbt.size());
        for (int i = 0; i < Math.min(MAX_KEYS, keysNbt.size()); i++) {
            ItemStack key = ItemStack.of(keysNbt.getCompound(i));
            if (!key.isEmpty())
                list.add(key);
        }

        return list;
    }

    public static void setKeys(ItemStack stack, Collection<ItemStack> keys) {
        if (stack.getItem() != LocksmithItems.KEYRING.get() || keys.isEmpty())
            return;

        CompoundTag nbt = stack.getOrCreateTag();
        ListTag keysNbt = new ListTag();
        int i = 0;
        for (ItemStack key : keys) {
            if (i >= MAX_KEYS)
                break;
            keysNbt.add(key.save(new CompoundTag()));
            i++;
        }
        nbt.put("Keys", keysNbt);
    }
}
