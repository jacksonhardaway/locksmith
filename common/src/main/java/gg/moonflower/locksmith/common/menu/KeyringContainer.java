package gg.moonflower.locksmith.common.menu;

import gg.moonflower.locksmith.common.item.KeyringItem;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * @author Ocelot
 */
public class KeyringContainer implements Container {

    private final Inventory inventory;
    private final int index;
    private final ItemStack keyring;
    private final NonNullList<ItemStack> keys;

    public KeyringContainer(Inventory inventory, int index) {
        this.inventory = inventory;
        this.index = index;
        this.keyring = inventory.getItem(index);
        this.keys = NonNullList.withSize(4, ItemStack.EMPTY);

        List<ItemStack> keys = KeyringItem.getKeys(this.keyring);
        for (int i = 0; i < keys.size(); i++)
            this.keys.set(i, keys.get(i));
    }

    private void update() {
        KeyringItem.setKeys(this.keyring, this.keys);
    }

    @Override
    public int getContainerSize() {
        return KeyringItem.MAX_KEYS;
    }

    @Override
    public boolean isEmpty() {
        return this.keys.isEmpty() || this.keys.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int index) {
        if (index < 0 || index >= this.keys.size())
            return ItemStack.EMPTY;
        return this.keys.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack result = ContainerHelper.removeItem(this.keys, index, count);
        this.update();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack result = ContainerHelper.takeItem(this.keys, index);
        this.update();
        return result;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index < 0 || index >= KeyringItem.MAX_KEYS)
            return;
        this.keys.set(index, stack);
        this.update();
    }

    @Override
    public void setChanged() {
        this.update();
    }

    @Override
    public boolean stillValid(Player player) {
        return ItemStack.matches(this.inventory.getItem(this.index), this.keyring);
    }

    @Override
    public void clearContent() {
        this.keys.clear();
        this.update();
    }
}
