package gg.moonflower.locksmith.core.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.api.lock.LockManager;
import gg.moonflower.locksmith.api.lock.position.LockPosition;
import gg.moonflower.locksmith.core.Locksmith;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WalkNodeEvaluator.class)
public abstract class WalkNodeEvaluatorMixin extends NodeEvaluator {

    @ModifyVariable(method = "evaluateBlockPathType", index = 5, at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/pathfinder/BlockPathTypes;WALKABLE_DOOR:Lnet/minecraft/world/level/pathfinder/BlockPathTypes;", shift = At.Shift.BY, by = 2), argsOnly = true)
    private BlockPathTypes locksmith$blockLockedDoors(BlockPathTypes original, @Local(index = 4) BlockPos pos) {
        if (Locksmith.CONFIG.allowNpcsToOpenDoors.get())
            return original;

        LockPosition lockPosition = LockPosition.of(pos);
        AbstractLock lock = LockManager.get(this.mob.getLevel()).getLock(lockPosition);
        if (lock == null)
            return original;

        return BlockPathTypes.BLOCKED;
    }
}
