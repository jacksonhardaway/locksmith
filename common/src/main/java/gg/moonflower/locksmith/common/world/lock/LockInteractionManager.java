package gg.moonflower.locksmith.common.world.lock;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.common.lock.LockManager;
import gg.moonflower.pollen.api.event.events.entity.player.PlayerInteractEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class LockInteractionManager {
    private static final Component LOCKED = new TranslatableComponent("lock.locksmith.locked");

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        LockManager manager = LockManager.get(level);
        BlockPos pos = hitResult.getBlockPos();
        AbstractLock lock = manager.getLock(pos);
        if (lock == null)
            return InteractionResult.PASS;

        if (lock.onRightClick(player, level, hand, hitResult))
            return InteractionResult.PASS;
        else {
            player.displayClientMessage(LOCKED, true);
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        }
    }

    public static InteractionResult onLeftClickBlock(Player player, Level level, InteractionHand hand, BlockPos pos, Direction direction) {
        LockManager manager = LockManager.get(level);
        AbstractLock lock = manager.getLock(pos);
        if (lock == null)
            return InteractionResult.PASS;

        if (lock.onLeftClick(player, level, hand, pos, direction))
            return InteractionResult.PASS;
        else {
            player.displayClientMessage(LOCKED, true);
            return InteractionResult.FAIL;
        }
    }
}
