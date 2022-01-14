package gg.moonflower.locksmith.core.forge.compat.jei;

import gg.moonflower.locksmith.common.item.KeyItem;
import gg.moonflower.locksmith.core.registry.LocksmithBlocks;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApiStatus.Internal
public class LocksmithingTableRecipeMaker {

    public static List<LocksmithingTableRecipe> getLocksmithingTableRecipes() {
        List<LocksmithingTableRecipe> recipes = new ArrayList<>();

        ItemStack originalKey = new ItemStack(LocksmithItems.KEY.get());
        KeyItem.setOriginal(originalKey, true);
        recipes.add(new LocksmithingTableRecipe(
                Collections.singletonList(new ItemStack(LocksmithItems.BLANK_KEY.get())),
                Collections.singletonList(new ItemStack(LocksmithItems.BLANK_LOCK.get())),
                Collections.singletonList(originalKey.copy()),
                Collections.singletonList(new ItemStack(LocksmithItems.LOCK.get()))
        ));
        recipes.add(new LocksmithingTableRecipe(
                Collections.singletonList(new ItemStack(LocksmithItems.BLANK_KEY.get())),
                Collections.singletonList(new ItemStack(LocksmithItems.BLANK_LOCK_BUTTON.get())),
                Collections.singletonList(originalKey.copy()),
                Collections.singletonList(new ItemStack(LocksmithBlocks.LOCK_BUTTON.get()))
        ));
        recipes.add(new LocksmithingTableRecipe(
                Collections.singletonList(originalKey.copy()),
                Collections.singletonList(new ItemStack(LocksmithItems.BLANK_KEY.get())),
                Collections.singletonList(new ItemStack(LocksmithItems.KEY.get())),
                Collections.singletonList(new ItemStack(LocksmithItems.KEY.get()))
        ));

        return recipes;
    }
}
