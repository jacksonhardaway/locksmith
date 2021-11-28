package gg.moonflower.locksmith.core.mixin;

import gg.moonflower.locksmith.common.lock.LockManager;
import gg.moonflower.locksmith.core.Locksmith;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class BlockStateBaseMixin {

    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    public void getDestroyProgress(BlockGetter level, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (!(level instanceof Level)|| !Locksmith.CONFIG.allowLocksToBeBroken.get() || LockManager.get((Level) level).getLock(pos) == null)
            return;
        cir.setReturnValue(cir.getReturnValue() * Locksmith.CONFIG.lockBreakingMultiplier.get().floatValue());
    }
}
