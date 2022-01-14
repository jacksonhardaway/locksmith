package gg.moonflower.locksmith.common.recipe;

import dev.architectury.injectables.annotations.ExpectPlatform;
import gg.moonflower.locksmith.common.item.KeyItem;
import gg.moonflower.locksmith.core.registry.LocksmithBlocks;
import gg.moonflower.locksmith.core.registry.LocksmithRecipes;
import gg.moonflower.pollen.api.platform.Platform;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
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
        if (KeyItem.isKey(topInput) && !KeyItem.isOriginal(topInput))
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
        if (bottomInput.hasCustomHoverName())
            rightResult.setHoverName(bottomInput.getHoverName());

        if (KeyItem.isKey(topInput) || KeyItem.isBlankKey(topInput)) {
            UUID lockId = KeyItem.hasLockId(topInput) ? KeyItem.getLockId(topInput) : UUID.randomUUID();
            if (KeyItem.canHaveLock(leftResult))
                KeyItem.setLockId(leftResult, lockId);
            if (KeyItem.canHaveLock(rightResult))
                KeyItem.setLockId(rightResult, lockId);
            KeyItem.setOriginal(leftResult, true);
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

    @ExpectPlatform
    public static RecipeSerializer<LocksmithingRecipe> createSerializer() {
        return Platform.error();
    }
}
