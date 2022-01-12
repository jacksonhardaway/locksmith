package gg.moonflower.locksmith.core.forge.compat.jei;

import gg.moonflower.locksmith.client.screen.LocksmithingTableScreen;
import gg.moonflower.locksmith.common.menu.LocksmithingTableMenu;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.locksmith.core.registry.LocksmithBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

@JeiPlugin
@ApiStatus.Internal
public class LocksmithJeiPlugin implements IModPlugin {

    public static final ResourceLocation PLUGIN_ID = new ResourceLocation(Locksmith.MOD_ID, Locksmith.MOD_ID);
    public static final ResourceLocation LOCKSMITHING_TABLE_CATEGORY_ID = new ResourceLocation(Locksmith.MOD_ID, Locksmith.MOD_ID);

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(new LocksmithingTableCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(LocksmithingTableRecipeMaker.getLocksmithingTableRecipes(), LOCKSMITHING_TABLE_CATEGORY_ID);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(LocksmithingTableScreen.class, 54, 36, 22, 15, LOCKSMITHING_TABLE_CATEGORY_ID);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(LocksmithingTableMenu.class, LOCKSMITHING_TABLE_CATEGORY_ID, 0, 2, 4, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(LocksmithBlocks.LOCKSMITHING_TABLE.get()), LOCKSMITHING_TABLE_CATEGORY_ID);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }
}
