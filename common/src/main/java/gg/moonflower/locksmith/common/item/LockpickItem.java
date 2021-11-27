package gg.moonflower.locksmith.common.item;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.common.lock.LockManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class LockpickItem extends Item {
    public LockpickItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        LockManager manager = LockManager.get(context.getLevel());
        AbstractLock lock = manager.getLock(pos);
        if (player == null || lock == null)
            return InteractionResult.PASS;

        Level level = context.getLevel();
        if (level.isClientSide() && !player.isCreative()) {
            lock.onLockpick(context);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
