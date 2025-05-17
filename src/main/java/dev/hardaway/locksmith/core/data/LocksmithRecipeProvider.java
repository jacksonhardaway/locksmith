package dev.hardaway.locksmith.core.data;

import dev.hardaway.locksmith.core.registry.LocksmithBlocks;
import dev.hardaway.locksmith.core.registry.LocksmithItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class LocksmithRecipeProvider extends RecipeProvider {
    public LocksmithRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, LocksmithItems.BLANK_KEY.get())
                .define('i', Items.IRON_NUGGET)
                .define('I', Items.IRON_INGOT)
                .pattern(" I")
                .pattern("iI")
                .pattern("iI")
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, LocksmithItems.BLANK_LOCK.get())
                .define('i', Items.IRON_NUGGET)
                .define('I', Items.IRON_INGOT)
                .pattern("III")
                .pattern("i i")
                .pattern("iii")
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, LocksmithBlocks.LOCKSMITHING_TABLE.get())
                .define('O', ItemTags.PLANKS)
                .define('i', Items.IRON_NUGGET)
                .pattern("ii")
                .pattern("OO")
                .pattern("OO")
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .save(output);
    }
}
