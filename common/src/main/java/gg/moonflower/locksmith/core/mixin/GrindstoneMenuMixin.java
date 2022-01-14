package gg.moonflower.locksmith.core.mixin;

import gg.moonflower.locksmith.core.registry.LocksmithBlocks;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMenuMixin {

    @Shadow
    @Final
    private Container repairSlots;

    @Shadow
    @Final
    private Container resultSlots;

    @Unique
    @Nullable
    private static ItemStack getResult(ItemStack stack) {
        if (stack.getItem() == LocksmithItems.LOCK.get()) {
            return new ItemStack(LocksmithItems.BLANK_LOCK.get());
        } else if (stack.getItem() == LocksmithItems.KEY.get()) {
            return new ItemStack(LocksmithItems.BLANK_KEY.get());
        } else if (stack.getItem() == LocksmithBlocks.LOCK_BUTTON.get().asItem()) {
            return new ItemStack(LocksmithItems.BLANK_LOCK_BUTTON.get());
        }
        return null;
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    public void modifyAllowed(CallbackInfo ci) {
        ItemStack slotOne = this.repairSlots.getItem(0);
        ItemStack slotTwo = this.repairSlots.getItem(1);
        boolean allowed = slotOne.isEmpty() ^ slotTwo.isEmpty();
        if (!allowed)
            return;

        ItemStack modifiedStack = getResult(!slotOne.isEmpty() ? slotOne : slotTwo);
        if (modifiedStack != null) {
            this.resultSlots.setItem(0, modifiedStack);
            ci.cancel();
        }
    }
}
