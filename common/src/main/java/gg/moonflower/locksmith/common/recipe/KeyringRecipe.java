package gg.moonflower.locksmith.common.recipe;

import gg.moonflower.locksmith.common.item.KeyringItem;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import gg.moonflower.locksmith.core.registry.LocksmithRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ocelot
 */
public class KeyringRecipe extends CustomRecipe {

    public KeyringRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        int count = 0;

        boolean ring = false;
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack itemStack = inv.getItem(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.getItem() == LocksmithItems.KEYRING.get()) {
                    if (ring)
                        return false;
                    ring = true;
                } else if (itemStack.getItem() != LocksmithItems.KEY.get()) {
                    return false;
                }
                count++;
            }
        }

        return count >= 2 && count <= KeyringItem.MAX_KEYS;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        List<ItemStack> keys = new ArrayList<>(KeyringItem.MAX_KEYS);
        ItemStack ring = ItemStack.EMPTY;
        for (int j = 0; j < inv.getContainerSize(); ++j) {
            ItemStack itemStack2 = inv.getItem(j);
            if (!itemStack2.isEmpty()) {
                if (itemStack2.getItem() == LocksmithItems.KEY.get()) {
                    ItemStack copy = itemStack2.copy();
                    copy.setCount(1);
                    keys.add(copy);
                } else if (itemStack2.getItem() == LocksmithItems.KEYRING.get()) {
                    if (!ring.isEmpty())
                        return ItemStack.EMPTY;
                    ring = itemStack2;
                }
                if (keys.size() > KeyringItem.MAX_KEYS)
                    return ItemStack.EMPTY;
            }
        }

        if (!ring.isEmpty())
            keys.addAll(KeyringItem.getKeys(ring));

        if (keys.size() < 2 || keys.size() > KeyringItem.MAX_KEYS)
            return ItemStack.EMPTY;

        ItemStack stack = this.getResultItem();
        if (!ring.isEmpty() && ring.hasTag())
            stack.setTag(ring.getTag().copy());
        KeyringItem.setKeys(stack, keys);
        return stack;
    }

    @Override
    public ItemStack getResultItem() {
        return new ItemStack(LocksmithItems.KEYRING.get());
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return LocksmithRecipes.KEYRING.get();
    }
}
