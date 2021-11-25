package gg.moonflower.locksmith.common.item;

import gg.moonflower.locksmith.core.registry.LocksmithItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class KeyringItem extends Item {

    public KeyringItem(Properties properties) {
        super(properties);
    }

    public static List<ItemStack> getKeys(ItemStack stack) {
        if (stack.getItem() != LocksmithItems.KEYRING)
            return Collections.emptyList();

        return Collections.singletonList(new ItemStack(LocksmithItems.BLANK_KEY.get()));

//        CompoundTag tag = stack.getOrCreateTag();
//        if (!tag.contains("Keys", 10))
//            return Collections.emptyList();
//
//        List<ItemStack> keys = new ArrayList<>();
//        ListTag list = tag.getList("Keys", 9);
//        for (int i = 0; i < list.size(); i++) {
//            keys.add(ItemStack.of(list.getCompound(i)));
//        }
//        return keys;
    }

}
