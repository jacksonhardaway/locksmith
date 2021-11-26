package gg.moonflower.locksmith.common.world.lock;

import gg.moonflower.locksmith.api.lock.LockData;
import gg.moonflower.locksmith.common.item.KeyItem;
import gg.moonflower.locksmith.common.lock.LockManager;
import gg.moonflower.pollen.api.event.events.entity.player.PlayerInteractEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class LockInteractionManager {
    private static final Component LOCKED = new TranslatableComponent("lock.locksmith.locked");

    public static void init() {
        PlayerInteractEvent.RIGHT_CLICK_BLOCK.register(LockInteractionManager::onRightClickBlock);
        PlayerInteractEvent.LEFT_CLICK_BLOCK.register(LockInteractionManager::onLeftClickBlock);
    }

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        LockManager manager = LockManager.get(level);
        BlockPos pos = hitResult.getBlockPos();
        LockData lock = manager.getLock(pos);
        if (lock == null)
            return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);
        if (KeyItem.matchesLock(lock.getId(), stack))
            return InteractionResult.PASS;
        else {
            player.displayClientMessage(LOCKED, true);
            return InteractionResult.FAIL;
        }
    }

    public static InteractionResult onLeftClickBlock(Player player, Level level, InteractionHand hand, BlockPos pos, Direction direction) {
        return InteractionResult.PASS;
    }

}
