package gg.moonflower.locksmith.core.datagen;

import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.locksmith.core.registry.LocksmithBlocks;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import gg.moonflower.pollen.api.config.PollinatedConfigType;
import gg.moonflower.pollen.api.datagen.GrindstoneRecipeBuilder;
import gg.moonflower.pollen.api.datagen.provider.PollinatedRecipeProvider;
import gg.moonflower.pollen.api.platform.Platform;
import gg.moonflower.pollen.api.registry.resource.TagRegistry;
import gg.moonflower.pollen.api.resource.condition.PollinatedResourceCondition;
import gg.moonflower.pollen.api.resource.condition.PollinatedResourceConditionProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;

public class LocksmithRecipeProvider extends PollinatedRecipeProvider {

    public LocksmithRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(LocksmithItems.BLANK_KEY.get())
                .define('i', Items.IRON_NUGGET)
                .define('I', Items.IRON_INGOT)
                .pattern(" I")
                .pattern("iI")
                .pattern("iI")
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .save(consumer);
        ShapedRecipeBuilder.shaped(LocksmithItems.BLANK_LOCK.get())
                .define('i', Items.IRON_NUGGET)
                .define('I', Items.IRON_INGOT)
                .pattern("III")
                .pattern("i i")
                .pattern("iii")
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .save(consumer);
        ShapedRecipeBuilder.shaped(LocksmithItems.BLANK_LOCK_BUTTON.get())
                .define('L', LocksmithItems.BLANK_LOCK.get())
                .define('I', Items.IRON_INGOT)
                .pattern(" I ")
                .pattern("ILI")
                .pattern(" I ")
                .unlockedBy("has_blank_lock", has(LocksmithItems.BLANK_LOCK.get()))
                .save(consumer);

        GrindstoneRecipeBuilder.grindstone(LocksmithItems.BLANK_KEY.get())
                .requires(LocksmithItems.KEY.get())
                .unlockedBy("has_key", has(LocksmithItems.KEY.get()))
                .save(consumer, new ResourceLocation(Locksmith.MOD_ID, "grindstone/blank_key"));
        GrindstoneRecipeBuilder.grindstone(LocksmithItems.BLANK_LOCK.get())
                .requires(LocksmithItems.LOCK.get())
                .unlockedBy("has_lock", has(LocksmithItems.LOCK.get()))
                .save(consumer, new ResourceLocation(Locksmith.MOD_ID, "grindstone/blank_lock"));
        GrindstoneRecipeBuilder.grindstone(LocksmithItems.BLANK_LOCK_BUTTON.get())
                .requires(LocksmithBlocks.LOCK_BUTTON.get())
                .unlockedBy("has_lock_button", has(LocksmithBlocks.LOCK_BUTTON.get()))
                .save(consumer, new ResourceLocation(Locksmith.MOD_ID, "grindstone/blank_lock_button"));

        ShapedRecipeBuilder.shaped(LocksmithBlocks.LOCKSMITHING_TABLE.get())
                .define('O', ItemTags.PLANKS)
                .define('I', Items.IRON_NUGGET)
                .pattern("II")
                .pattern("OO")
                .pattern("OO")
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .save(consumer);

        addConditions(new ResourceLocation(Locksmith.MOD_ID, "lockpick"), PollinatedResourceCondition.config(Locksmith.MOD_ID, PollinatedConfigType.SERVER, "Locks.Enable lockpick", true));
        ShapedRecipeBuilder.shaped(LocksmithItems.LOCKPICK.get())
                .define('N', Items.NETHERITE_INGOT)
                .define('I', Items.IRON_INGOT)
                .pattern("NII")
                .pattern("I  ")
                .pattern("I  ")
                .unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(consumer);
    }
}
