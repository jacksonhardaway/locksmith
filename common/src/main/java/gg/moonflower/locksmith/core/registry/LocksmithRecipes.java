package gg.moonflower.locksmith.core.registry;

import gg.moonflower.locksmith.common.recipe.KeyringRecipe;
import gg.moonflower.locksmith.common.recipe.LocksmithingRecipe;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.pollen.api.registry.PollinatedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;

import java.util.function.Supplier;

public class LocksmithRecipes {

    public static final PollinatedRegistry<RecipeSerializer<?>> RECIPES = PollinatedRegistry.create(Registry.RECIPE_SERIALIZER, Locksmith.MOD_ID);
    public static final PollinatedRegistry<RecipeType<?>> RECIPE_TYPES = PollinatedRegistry.create(Registry.RECIPE_TYPE, Locksmith.MOD_ID);

    public static final Supplier<SimpleRecipeSerializer<KeyringRecipe>> KEYRING = RECIPES.register("keyring", () -> new SimpleRecipeSerializer<>(KeyringRecipe::new));
    public static final Supplier<RecipeSerializer<LocksmithingRecipe>> LOCKSMITHING = RECIPES.register("locksmithing", LocksmithingRecipe::createSerializer);
    public static final Supplier<RecipeType<LocksmithingRecipe>> LOCKSMITHING_TYPE = RECIPE_TYPES.register("locksmithing", () -> new RecipeType<LocksmithingRecipe>() {
        @Override
        public String toString() {
            return Locksmith.MOD_ID + ":locksmithing";
        }
    });

}
