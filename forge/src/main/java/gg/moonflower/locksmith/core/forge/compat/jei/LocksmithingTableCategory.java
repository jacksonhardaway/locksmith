//package gg.moonflower.locksmith.core.forge.compat.jei;
//
//import gg.moonflower.locksmith.client.screen.LocksmithingTableScreen;
//import gg.moonflower.locksmith.core.registry.LocksmithBlocks;
//import mezz.jei.api.constants.VanillaTypes;
//import mezz.jei.api.gui.IRecipeLayout;
//import mezz.jei.api.gui.drawable.IDrawable;
//import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
//import mezz.jei.api.helpers.IGuiHelper;
//import mezz.jei.api.ingredients.IIngredients;
//import mezz.jei.api.recipe.category.IRecipeCategory;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.ItemStack;
//import org.jetbrains.annotations.ApiStatus;
//
//@ApiStatus.Internal
//public class LocksmithingTableCategory implements IRecipeCategory<LocksmithingJeiRecipe> {
//
//    private final IDrawable background;
//    private final IDrawable icon;
//
//    public LocksmithingTableCategory(IGuiHelper guiHelper) {
//        this.background = guiHelper.drawableBuilder(LocksmithingTableScreen.LOCKSMITHING_LOCATION, 20, 16, 136, 55).build();
//        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(LocksmithBlocks.LOCKSMITHING_TABLE.get()));
//    }
//
//    @Override
//    public ResourceLocation getUid() {
//        return LocksmithJeiPlugin.LOCKSMITHING_TABLE_CATEGORY_ID;
//    }
//
//    @Override
//    public Class<? extends LocksmithingJeiRecipe> getRecipeClass() {
//        return LocksmithingJeiRecipe.class;
//    }
//
//    @Override
//    public Component getTitle() {
//        return LocksmithBlocks.LOCKSMITHING_TABLE.get().getName();
//    }
//
//    @Override
//    public IDrawable getBackground() {
//        return background;
//    }
//
//    @Override
//    public IDrawable getIcon() {
//        return icon;
//    }
//
//    @Override
//    public void setIngredients(LocksmithingJeiRecipe recipe, IIngredients ingredients) {
//        ingredients.setInputLists(VanillaTypes.ITEM, recipe.getInputs());
//        ingredients.setOutputLists(VanillaTypes.ITEM, recipe.getOutputs());
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout recipeLayout, LocksmithingJeiRecipe recipe, IIngredients ingredients) {
//        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
//
//        guiItemStacks.init(0, true, 0, 0);
//        guiItemStacks.init(1, true, 0, 37);
//        guiItemStacks.init(2, false, 80, 18);
//        guiItemStacks.init(3, false, 110, 18);
//
//        guiItemStacks.set(ingredients);
//    }
//}
