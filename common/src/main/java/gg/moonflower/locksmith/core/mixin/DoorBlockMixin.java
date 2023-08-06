package gg.moonflower.locksmith.core.mixin;

import dev.architectury.event.EventResult;
import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.api.lock.LockManager;
import gg.moonflower.locksmith.api.lock.position.LockPosition;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.locksmith.core.registry.LocksmithSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DoorBlock.class)
public class DoorBlockMixin {

    @Inject(method = "setOpen", at = @At("HEAD"), cancellable = true)
    private void locksmith$keepClosed(Entity entity, Level level, BlockState blockState, BlockPos blockPos, boolean bl, CallbackInfo ci) {
        if (Locksmith.CONFIG.allowNpcsToOpenDoors.get())
            return;

        LockPosition lockPosition = LockPosition.of(blockPos);
        AbstractLock lock = LockManager.get(level).getLock(lockPosition);
        if (lock == null || entity == null)
            return;

        ci.cancel();
        // TODO: allow entities with keys in their hand to open doors

//        if (!(entity instanceof LivingEntity living)) {
//            ci.cancel(); // The entity can't hold a key if it's not living.
//            return;
//        }

    }
}
