package gg.moonflower.locksmith.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import gg.moonflower.locksmith.common.recipe.KeyringRecipe;
import gg.moonflower.locksmith.common.recipe.LocksmithingRecipe;
import gg.moonflower.locksmith.core.Locksmith;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;

public class LocksmithRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(Locksmith.MOD_ID, Registry.RECIPE_SERIALIZER_REGISTRY);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Locksmith.MOD_ID, Registry.RECIPE_TYPE_REGISTRY);

    public static final RegistrySupplier<SimpleRecipeSerializer<KeyringRecipe>> KEYRING = RECIPES.register("keyring", () -> new SimpleRecipeSerializer<>(KeyringRecipe::new));
    public static final RegistrySupplier<RecipeSerializer<LocksmithingRecipe>> LOCKSMITHING = RECIPES.register("locksmithing", LocksmithingRecipe.Serializer::new);
    public static final RegistrySupplier<RecipeType<LocksmithingRecipe>> LOCKSMITHING_TYPE = RECIPE_TYPES.register("locksmithing", () -> new RecipeType<LocksmithingRecipe>() {
        @Override
        public String toString() {
            return Locksmith.MOD_ID + ":locksmithing";
        }
    });

}
