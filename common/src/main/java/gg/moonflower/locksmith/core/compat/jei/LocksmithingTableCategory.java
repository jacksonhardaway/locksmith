package gg.moonflower.locksmith.core.compat.jei;

import gg.moonflower.locksmith.clientsource.client.screen.LocksmithingTableScreen;
import gg.moonflower.locksmith.core.registry.LocksmithBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public class LocksmithingTableCategory implements IRecipeCategory<LocksmithingJeiRecipe> {

    private final IDrawable background;
    private final IDrawable icon;

    public LocksmithingTableCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(LocksmithingTableScreen.LOCKSMITHING_LOCATION, 20, 16, 136, 55).build();
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(LocksmithBlocks.LOCKSMITHING_TABLE.get()));
    }

    @Override
    public RecipeType<LocksmithingJeiRecipe> getRecipeType() {
        return LocksmithJeiPlugin.LOCKSMITHING;
    }

    @Override
    public Component getTitle() {
        return LocksmithBlocks.LOCKSMITHING_TABLE.get().getName();
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, LocksmithingJeiRecipe recipe, IFocusGroup focuses) {
        List<List<ItemStack>> inputs = recipe.getInputs();
        List<List<ItemStack>> outputs = recipe.getOutputs();
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStacks(inputs.get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 38).addItemStacks(inputs.get(1));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 81, 19).addItemStacks(outputs.get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 111, 19).addItemStacks(outputs.get(1));
    }
}
