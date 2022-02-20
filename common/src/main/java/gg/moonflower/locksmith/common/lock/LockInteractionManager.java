package gg.moonflower.locksmith.common.lock;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.api.lock.LockManager;
import gg.moonflower.locksmith.api.lock.position.LockPosition;
import gg.moonflower.locksmith.common.item.KeyItem;
import gg.moonflower.locksmith.common.item.LockItem;
import gg.moonflower.locksmith.common.lock.types.KeyLock;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.locksmith.core.registry.LocksmithParticles;
import gg.moonflower.locksmith.core.registry.LocksmithSounds;
import gg.moonflower.locksmith.core.registry.LocksmithTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class LockInteractionManager {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Component LOCKED = new TranslatableComponent("lock." + Locksmith.MOD_ID + ".locked");

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        LockPosition lockPosition = LockPosition.of(pos);
        AbstractLock lock = LockManager.get(level).getLock(lockPosition);
        if (lock == null || player.isCreative())
            return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);
        if (Locksmith.CONFIG.enableLockpicking.get() && lock.pick(player, level, lockPosition, stack, hand)) {
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        if (!lock.onRightClick(player, level, stack, hitResult) && (player.getOffhandItem().isEmpty() || hand == InteractionHand.OFF_HAND)) {
            player.displayClientMessage(LOCKED, true);
            if (level.isClientSide()) {
                player.playNotifySound(LocksmithSounds.ITEM_LOCK_LOCKED.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                player.swing(hand); // so awful
            }
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }

    public static InteractionResult onLeftClickBlock(Player player, Level level, InteractionHand hand, BlockPos pos, Direction direction) {
        AbstractLock lock = LockManager.get(level).getLock(LockPosition.of(pos));
        if (lock == null || player.isCreative())
            return InteractionResult.PASS;

        if (lock.onLeftClick(player, level, hand, pos, direction))
            return InteractionResult.PASS;
        else {
            player.displayClientMessage(LOCKED, true);
            return InteractionResult.FAIL;
        }
    }

    public static void onBreakBlock(Level level, BlockPos pos, BlockState state) {
        AbstractLock lock = LockManager.get(level).getLock(LockPosition.of(pos));
        if (lock == null)
            return;

        if (state.hasProperty(ChestBlock.TYPE) && state.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
            Tag tag = AbstractLock.CODEC.encodeStart(NbtOps.INSTANCE, lock).getOrThrow(false, LOGGER::error);
            if (!(tag instanceof CompoundTag))
                return;

            CompoundTag lockTag = (CompoundTag) tag;
            lockTag.put("pos", BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, lock.getPos().blockPosition().relative(ChestBlock.getConnectedDirection(state))).getOrThrow(false, LOGGER::error));
            AbstractLock newLock = AbstractLock.CODEC.parse(NbtOps.INSTANCE, lockTag).getOrThrow(false, LOGGER::error);
            LockManager.get(level).addLock(newLock);
            return;
        }

        LockManager.get(level).removeLock(lock.getPos().blockPosition(), pos, false);
    }
}
