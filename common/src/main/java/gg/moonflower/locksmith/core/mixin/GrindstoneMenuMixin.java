package gg.moonflower.locksmith.core.mixin;

import gg.moonflower.locksmith.core.registry.LocksmithItems;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMenuMixin {

    @Shadow @Final private Container repairSlots;

    @Shadow @Final private Container resultSlots;

    @Shadow protected abstract ItemStack removeNonCurses(ItemStack stack, int damage, int count);

    @ModifyVariable(method = "removeNonCurses", index = 4, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;removeTagKey(Ljava/lang/String;)V", shift = At.Shift.BEFORE, ordinal = 0))
    public ItemStack removeNonCurses(ItemStack original) {
        if (original.getItem() == LocksmithItems.LOCK.get()) {
            return new ItemStack(LocksmithItems.BLANK_LOCK.get());
        } else if (original.getItem() == LocksmithItems.KEY.get()) {
            return new ItemStack(LocksmithItems.BLANK_KEY.get());
        }
        return original;
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    public void modifyAllowed(CallbackInfo ci) {
        ItemStack slotOne = this.repairSlots.getItem(0);
        ItemStack slotTwo = this.repairSlots.getItem(1);
        boolean allowed = slotOne.isEmpty() ^ slotTwo.isEmpty();
        if (!allowed)
            return;

        ItemStack modifiedStack = !slotOne.isEmpty() ? slotOne : slotTwo;
        if ((slotOne.getItem() == LocksmithItems.KEY.get() || slotTwo.getItem() == LocksmithItems.KEY.get()) || (slotOne.getItem() == LocksmithItems.LOCK.get() || slotTwo.getItem() == LocksmithItems.LOCK.get())) {
            this.resultSlots.setItem(0, removeNonCurses(modifiedStack, modifiedStack.getDamageValue(), modifiedStack.getCount()));
            ci.cancel();
        }
    }
}
