package gg.moonflower.locksmith.core.compat.jei;

import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApiStatus.Internal
public final class LocksmithRecipeMaker {

    private LocksmithRecipeMaker() {
    }

    public static <C extends Container, T, R extends Recipe<C>> List<T> getRecipes(IRecipeCategory<T> category, RecipeType<R> type, Function<R, T> converter) {
        Minecraft minecraft = Objects.requireNonNull(Minecraft.getInstance(), "minecraft");
        ClientLevel level = Objects.requireNonNull(minecraft.level, "minecraft world");
        RecipeManager recipeManager = level.getRecipeManager();
        return recipeManager.getAllRecipesFor(type)
                .stream()
                .filter(recipe -> !recipe.isSpecial())
                .map(converter)
                .filter(category::isHandled)
                .toList();
    }
}
