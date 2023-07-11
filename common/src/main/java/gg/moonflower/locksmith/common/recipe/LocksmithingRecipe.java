package gg.moonflower.locksmith.common.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import gg.moonflower.locksmith.common.item.KeyItem;
import gg.moonflower.locksmith.common.item.LockItem;
import gg.moonflower.locksmith.core.registry.LocksmithBlocks;
import gg.moonflower.locksmith.core.registry.LocksmithRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import java.util.UUID;

/**
 * @author Ocelot
 */
public class LocksmithingRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    private final String group;
    private final Ingredient topInput;
    private final Ingredient bottomInput;
    private final ItemStack leftResult;
    private final ItemStack rightResult;

    private ItemStack rightAssembledResult;

    public LocksmithingRecipe(ResourceLocation id, String group, Ingredient topInput, Ingredient bottomInput, ItemStack leftResult, ItemStack rightResult) {
        this.id = id;
        this.group = group;
        this.topInput = topInput;
        this.bottomInput = bottomInput;
        this.leftResult = leftResult;
        this.rightResult = rightResult;
    }

    @Override
    public boolean matches(Container container, Level level) {
        ItemStack topInput = container.getItem(0);
        ItemStack bottomInput = container.getItem(1);

        if (!this.topInput.test(topInput) || !this.bottomInput.test(bottomInput))
            return false;
        if (KeyItem.isKey(topInput) && KeyItem.isKey(bottomInput) && !KeyItem.isOriginal(topInput)) // two keys cannot be used for crafting if the top is not original
            return false;

        return (!KeyItem.canHaveLock(topInput) || KeyItem.hasLockId(topInput)) && (!KeyItem.canHaveLock(bottomInput) || KeyItem.hasLockId(bottomInput));
    }

    @Override
    public ItemStack assemble(Container container) {
        ItemStack topInput = container.getItem(0);
        ItemStack bottomInput = container.getItem(1);

        ItemStack leftResult = this.leftResult.copy();
        ItemStack rightResult = this.rightResult.copy();

        if (topInput.hasCustomHoverName())
            leftResult.setHoverName(topInput.getHoverName());
        if (bottomInput.hasCustomHoverName()) {
            rightResult.setHoverName(bottomInput.getHoverName());
        } else if (topInput.hasCustomHoverName() && (KeyItem.isKey(topInput) || KeyItem.isBlankKey(topInput)) && (KeyItem.isKey(bottomInput) || KeyItem.isBlankKey(bottomInput))) {
            rightResult.setHoverName(topInput.getHoverName());
        }

        if (KeyItem.isKey(topInput) || KeyItem.isBlankKey(topInput)) {
            UUID lockId = KeyItem.hasLockId(topInput) ? KeyItem.getLockId(topInput) : UUID.randomUUID();
            if (KeyItem.canHaveLock(leftResult))
                KeyItem.setLockId(leftResult, lockId);
            if (KeyItem.canHaveLock(rightResult))
                KeyItem.setLockId(rightResult, lockId);

            if (KeyItem.isOriginal(topInput) || KeyItem.isBlankKey(topInput))
                KeyItem.setOriginal(leftResult, true);

            if (topInput.hasCustomHoverName()) {
                Component name = topInput.getHoverName();
                LockItem.setKeyName(leftResult, name);
                LockItem.setKeyName(rightResult, name);
            }
        }

        this.rightAssembledResult = rightResult;
        return leftResult;
    }

    public ItemStack getSecondAssembledResult() {
        if (this.rightAssembledResult == null)
            throw new IllegalStateException("LocksmithingRecipe#assemble() must be called before LocksmithingRecipe#getSecondAssembledResult()");
        ItemStack result = this.rightAssembledResult.copy();
        this.rightAssembledResult = null;
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 1 && height >= 2;
    }

    public Ingredient getTopInput() {
        return topInput;
    }

    public Ingredient getBottomInput() {
        return bottomInput;
    }

    @Override
    public ItemStack getResultItem() {
        return leftResult;
    }

    public ItemStack getSecondResultItem() {
        return rightResult;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, this.topInput, this.bottomInput);
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(LocksmithBlocks.LOCKSMITHING_TABLE.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return LocksmithRecipes.LOCKSMITHING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return LocksmithRecipes.LOCKSMITHING_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<LocksmithingRecipe> {

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
}
