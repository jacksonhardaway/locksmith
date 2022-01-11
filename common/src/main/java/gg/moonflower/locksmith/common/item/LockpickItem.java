package gg.moonflower.locksmith.common.item;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.api.lock.types.KeyLock;
import gg.moonflower.locksmith.common.lock.LockManager;
import gg.moonflower.locksmith.core.registry.LocksmithLocks;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class LockpickItem extends Item {
    public LockpickItem(Properties properties) {
        super(properties);
    }

//    @Override
//    public InteractionResult useOn(UseOnContext context) {
//        BlockPos pos = context.getClickedPos();
//        Player player = context.getPlayer();
//        Level level = context.getLevel();
//        AbstractLock abstractLock = LockManager.getLock(level, pos);
//        if (player == null || abstractLock == null || abstractLock.getType() != LocksmithLocks.KEY.get() || !player.isCreative())
//            return InteractionResult.PASS;
//
//        if (level.isClientSide())
//            return InteractionResult.SUCCESS;
//
//        KeyLock lock = (KeyLock) abstractLock;
//        LockManager.get(level).removeLock(lock.getPos());
//        player.awardStat(Stats.ITEM_USED.get(this));
//        return InteractionResult.CONSUME;
//    }
}
