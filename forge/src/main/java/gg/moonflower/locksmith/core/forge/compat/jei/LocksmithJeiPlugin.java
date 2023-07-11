//package gg.moonflower.locksmith.core.forge.compat.jei;
//
//import gg.moonflower.locksmith.client.screen.LockPickingScreen;
//import gg.moonflower.locksmith.client.screen.LocksmithingTableScreen;
//import gg.moonflower.locksmith.common.menu.LocksmithingTableMenu;
//import gg.moonflower.locksmith.core.Locksmith;
//import gg.moonflower.locksmith.core.registry.LocksmithBlocks;
//import gg.moonflower.locksmith.core.registry.LocksmithRecipes;
//import mezz.jei.api.IModPlugin;
//import mezz.jei.api.JeiPlugin;
//import mezz.jei.api.gui.handlers.IGuiClickableArea;
//import mezz.jei.api.gui.handlers.IGuiContainerHandler;
//import mezz.jei.api.helpers.IGuiHelper;
//import mezz.jei.api.helpers.IJeiHelpers;
//import mezz.jei.api.registration.*;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.ItemStack;
//import org.apache.commons.lang3.Validate;
//import org.jetbrains.annotations.ApiStatus;
//import org.jetbrains.annotations.NotNull;
//
//import javax.annotation.Nullable;
//import java.util.Collection;
//import java.util.Collections;
//
//@JeiPlugin
//@ApiStatus.Internal
//public class LocksmithJeiPlugin implements IModPlugin {
//
//    public static final ResourceLocation PLUGIN_ID = new ResourceLocation(Locksmith.MOD_ID, Locksmith.MOD_ID);
//    public static final ResourceLocation LOCKSMITHING_TABLE_CATEGORY_ID = new ResourceLocation(Locksmith.MOD_ID, Locksmith.MOD_ID);
//
//    @Nullable
//    private LocksmithingTableCategory locksmithingTableCategory;
//
//    @Override
//    public void registerCategories(IRecipeCategoryRegistration registration) {
//        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
//        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
//        registration.addRecipeCategories(this.locksmithingTableCategory = new LocksmithingTableCategory(guiHelper));
//    }
//
//    @Override
//    public void registerRecipes(IRecipeRegistration registration) {
//        Validate.notNull(this.locksmithingTableCategory, "locksmithingTableCategory");
//        registration.addRecipes(LocksmithRecipeMaker.getRecipes(this.locksmithingTableCategory, LocksmithRecipes.LOCKSMITHING_TYPE.get(), LocksmithingJeiRecipe::new), LOCKSMITHING_TABLE_CATEGORY_ID);
//    }
//
//    @Override
//    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
//        registration.addGuiContainerHandler(LocksmithingTableScreen.class, new IGuiContainerHandler<LocksmithingTableScreen>() {
//            @Override
//            public @NotNull Collection<IGuiClickableArea> getGuiClickableAreas(@NotNull LocksmithingTableScreen containerScreen, double mouseX, double mouseY) {
//                return !containerScreen.getFailureTooltip().isEmpty() ? Collections.emptySet() : Collections.singleton(IGuiClickableArea.createBasic(54, 36, 22, 15, LOCKSMITHING_TABLE_CATEGORY_ID));
//            }
//        });
//        registration.addGuiScreenHandler(LockPickingScreen.class, guiScreen -> null); // block drawing JEI on lock picking
//    }
//
//    @Override
//    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
//        registration.addRecipeTransferHandler(LocksmithingTableMenu.class, LOCKSMITHING_TABLE_CATEGORY_ID, 0, 2, 4, 36);
//    }
//
//    @Override
//    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
//        registration.addRecipeCatalyst(new ItemStack(LocksmithBlocks.LOCKSMITHING_TABLE.get()), LOCKSMITHING_TABLE_CATEGORY_ID);
//    }
//
//    @Override
//    public ResourceLocation getPluginUid() {
//        return PLUGIN_ID;
//    }
//}
