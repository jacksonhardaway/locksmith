package dev.hardaway.locksmith.core.mixin.client;

import dev.hardaway.locksmith.api.lock.Lockable;
import dev.hardaway.locksmith.core.registry.LocksmithCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "blockActionRestricted", at = @At("HEAD"), cancellable = true)
    private void restrictLockedBlocks(Level level, BlockPos pos, GameType gameMode, CallbackInfoReturnable<Boolean> cir) {
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);

        Lockable lockable = level.getCapability(LocksmithCapabilities.BLOCK_LOCKABLE, pos, state, be);
        if (lockable == null)
            return;

        ItemStack mainStack = this.getMainHandItem();
        ItemStack offStack = this.getOffhandItem();
        if (lockable.canUnlock(mainStack) || lockable.canUnlock(offStack))
            return;

        cir.setReturnValue(true);
    }

}
