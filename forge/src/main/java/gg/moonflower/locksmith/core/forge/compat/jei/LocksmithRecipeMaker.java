//package gg.moonflower.locksmith.core.forge.compat.jei;
//
//import gg.moonflower.pollen.core.mixin.forge.client.RecipeManagerAccessor;
//import mezz.jei.api.recipe.category.IRecipeCategory;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.multiplayer.ClientPacketListener;
//import net.minecraft.world.Container;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.item.crafting.RecipeManager;
//import net.minecraft.world.item.crafting.RecipeType;
//import org.jetbrains.annotations.ApiStatus;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//@ApiStatus.Internal
//public final class LocksmithRecipeMaker {
//
//    private LocksmithRecipeMaker() {
//    }
//
////    @SuppressWarnings("unchecked")
////    public static <C extends Container, T extends Recipe<C>> List<T> getRecipes(IRecipeCategory<T> category, RecipeType<T> type) {
////        ClientPacketListener connection = Minecraft.getInstance().getConnection();
////        if (connection == null)
////            return Collections.emptyList();
////        return (List<T>) getRecipes(connection.getRecipeManager(), type)
////                .stream()
////                .filter(recipe -> category.isHandled(recipe) && !recipe.isSpecial());
////    }
//
//    public static <C extends Container, T, R extends Recipe<C>> List<T> getRecipes(IRecipeCategory<T> category, RecipeType<R> type, Function<R, T> converter) {
//        ClientPacketListener connection = Minecraft.getInstance().getConnection();
//        if (connection == null)
//            return Collections.emptyList();
//        return getRecipes(connection.getRecipeManager(), type)
//                .stream()
//                .filter(recipe -> !recipe.isSpecial())
//                .map(converter)
//                .filter(category::isHandled).
//                collect(Collectors.toList());
//    }
//
//    @SuppressWarnings("unchecked")
//    private static <C extends Container, T extends Recipe<C>> Collection<T> getRecipes(RecipeManager recipeManager, RecipeType<T> recipeType) {
//        return (Collection<T>) ((RecipeManagerAccessor) recipeManager).invokeByType(recipeType).values();
//    }
//}
