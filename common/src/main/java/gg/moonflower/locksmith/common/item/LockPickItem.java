package gg.moonflower.locksmith.common.item;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.api.lock.LockManager;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class LockPickItem extends Item {

    public LockPickItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        AbstractLock abstractLock = LockManager.getLock(level, pos);
        if (player == null || !player.isCreative() || abstractLock == null)
            return InteractionResult.PASS;
        if (level.isClientSide())
            return InteractionResult.SUCCESS;

        LockManager.get(level).removeLock(abstractLock.getPos(), pos, true);
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResult.CONSUME;
    }
}
