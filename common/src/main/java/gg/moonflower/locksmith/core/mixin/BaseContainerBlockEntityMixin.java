package gg.moonflower.locksmith.core.mixin;

import gg.moonflower.locksmith.api.lock.LockManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseContainerBlockEntity.class)
public abstract class BaseContainerBlockEntityMixin extends BlockEntity {

    @Shadow private Component name;

    public BaseContainerBlockEntityMixin(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
    }

    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    public void getDisplayName(CallbackInfoReturnable<Component> cir) {
        if (this.name != null || this.getLevel() == null)
            return;

        if (LockManager.get(this.getLevel()).getLock(this.getBlockPos()) == null)
            return;

        cir.setReturnValue(new TranslatableComponent("container.locksmith.locked", cir.getReturnValue()));
    }
}
