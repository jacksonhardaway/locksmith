package gg.moonflower.locksmith.common.recipe.forge;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import gg.moonflower.locksmith.common.recipe.LocksmithingRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class LocksmithingRecipeImpl extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<LocksmithingRecipe> {

    public static RecipeSerializer<LocksmithingRecipe> createSerializer() {
        return new LocksmithingRecipeImpl();
    }

    @Override
    public LocksmithingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        String group = GsonHelper.getAsString(json, "group", "");
        Ingredient topInput = Ingredient.fromJson(json.get("topInput"));
        Ingredient bottomInput = Ingredient.fromJson(json.get("bottomInput"));

        boolean singleResult = json.has("result");
        if (singleResult && (json.has("leftResult") || json.has("rightResult")))
            throw new JsonSyntaxException("'result' is not compatible with either 'leftResult' or 'rightResult'");

        ItemStack leftResult = new ItemStack(ShapedRecipe.itemFromJson(GsonHelper.getAsJsonObject(json, singleResult ? "result" : "leftResult")));
        ItemStack rightResult = singleResult ? ItemStack.EMPTY : new ItemStack(ShapedRecipe.itemFromJson(GsonHelper.getAsJsonObject(json, "rightResult")));
        return new LocksmithingRecipe(recipeId, group, topInput, bottomInput, leftResult, rightResult);
    }

    @Override
    public LocksmithingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
        String group = buf.readUtf();
        Ingredient topInput = Ingredient.fromNetwork(buf);
        Ingredient bottomInput = Ingredient.fromNetwork(buf);
        ItemStack leftResult = buf.readItem();
        ItemStack rightResult = buf.readItem();
        return new LocksmithingRecipe(recipeId, group, topInput, bottomInput, leftResult, rightResult);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, LocksmithingRecipe recipe) {
        buf.writeUtf(recipe.getGroup());
        recipe.getTopInput().toNetwork(buf);
        recipe.getBottomInput().toNetwork(buf);
        buf.writeItem(recipe.getResultItem());
        buf.writeItem(recipe.getSecondResultItem());
    }
}
