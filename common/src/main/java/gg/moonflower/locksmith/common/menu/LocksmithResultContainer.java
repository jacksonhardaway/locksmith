package gg.moonflower.locksmith.common.menu;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

public class LocksmithResultContainer implements Container, RecipeHolder {

    private final NonNullList<ItemStack> stacks;
    @Nullable
    private Recipe<?> recipeUsed;

    public LocksmithResultContainer(int slots) {
        this.stacks = NonNullList.withSize(slots, ItemStack.EMPTY);
    }

    @Override
    public int getContainerSize() {
        return this.stacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.stacks)
            if (!itemStack.isEmpty())
                return false;
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return index >= 0 && index < this.stacks.size() ? this.stacks.get(index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return index >= 0 && index < this.stacks.size() ? ContainerHelper.takeItem(this.stacks, index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return index >= 0 && index < this.stacks.size() ? ContainerHelper.takeItem(this.stacks, index) : ItemStack.EMPTY;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index >= 0 && index < this.stacks.size())
            this.stacks.set(index, stack);
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.stacks.clear();
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        this.recipeUsed = recipe;
    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return this.recipeUsed;
    }
}
