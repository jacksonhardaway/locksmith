package gg.moonflower.locksmith.common.lock;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.BlockHitResult;

public class LockInteractionManager {
    private static final Component LOCKED = new TranslatableComponent("lock.locksmith.locked");

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        AbstractLock lock = LockManager.getLock(level, pos);
        if (lock == null)
            return InteractionResult.PASS;

        if (player.getItemInHand(hand).getItem() == LocksmithItems.LOCKPICK.get()) {
            lock.onLockpick(player, level);
            player.awardStat(Stats.ITEM_USED.get(LocksmithItems.LOCKPICK.get()));
            return InteractionResult.SUCCESS;
        }

        if (lock.onRightClick(player, level, hand, hitResult))
            return InteractionResult.PASS;
        else {
            player.displayClientMessage(LOCKED, true);
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        }
    }

    public static InteractionResult onLeftClickBlock(Player player, Level level, InteractionHand hand, BlockPos pos, Direction direction) {
        AbstractLock lock = LockManager.getLock(level, pos);
        if (lock == null)
            return InteractionResult.PASS;

        if (lock.onLeftClick(player, level, hand, pos, direction))
            return InteractionResult.PASS;
        else {
            player.displayClientMessage(LOCKED, true);
            return InteractionResult.FAIL;
        }
    }

    public static void onBreakBlock(Level level, BlockPos pos, BlockState state) {
        AbstractLock lock = LockManager.getLock(level, pos);
        if (lock == null)
            return;

        // FIXME: make this better
        if (state.hasProperty(ChestBlock.TYPE) && state.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
            Tag tag = AbstractLock.CODEC.encodeStart(NbtOps.INSTANCE, lock).getOrThrow(false, e -> {
            });
            if (!(tag instanceof CompoundTag))
                return;

            CompoundTag lockTag = (CompoundTag) tag;
            lockTag.put("pos", BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, lock.getPos().relative(ChestBlock.getConnectedDirection(state))).getOrThrow(false, e -> {
            }));
            AbstractLock newLock = AbstractLock.CODEC.parse(NbtOps.INSTANCE, lockTag).getOrThrow(false, e -> {
            });
            LockManager.get(level).addLock(newLock);
            return;
        }

        LockManager.get(level).removeLock(lock.getPos());
    }
}
