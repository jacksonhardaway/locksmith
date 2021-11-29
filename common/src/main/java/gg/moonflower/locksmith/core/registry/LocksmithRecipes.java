package gg.moonflower.locksmith.core.registry;

import gg.moonflower.locksmith.common.recipe.KeyringRecipe;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.pollen.api.registry.PollinatedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;

import java.util.function.Supplier;

public class LocksmithRecipes {

    public static final PollinatedRegistry<RecipeSerializer<?>> RECIPES = PollinatedRegistry.create(Registry.RECIPE_SERIALIZER, Locksmith.MOD_ID);

    public static final Supplier<SimpleRecipeSerializer<KeyringRecipe>> KEYRING = RECIPES.register("keyring", () -> new SimpleRecipeSerializer<>(KeyringRecipe::new));

}
