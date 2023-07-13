package gg.moonflower.locksmith.common.lock;

import com.mojang.serialization.DataResult;
import dev.architectury.event.EventResult;
import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.api.lock.LockManager;
import gg.moonflower.locksmith.api.lock.position.LockPosition;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.locksmith.core.registry.LocksmithSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LockInteractionManager {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Component LOCKED = Component.translatable("lock." + Locksmith.MOD_ID + ".locked");

    public static EventResult onRightClickBlock(Player player, InteractionHand hand, BlockPos pos, Direction face) {
        Level level = player.getLevel();
        LockPosition lockPosition = LockPosition.of(pos);
        AbstractLock lock = LockManager.get(level).getLock(lockPosition);
        if (lock == null || player.isCreative())
            return EventResult.pass();

        ItemStack stack = player.getItemInHand(hand);
        if (Locksmith.CONFIG.enableLockpicking.get() && lock.pick(player, level, lockPosition, stack, hand)) {
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            return EventResult.interrupt(level.isClientSide());
        }

        if (!lock.onRightClick(player, level, stack, hand, pos, face) && (hand == InteractionHand.OFF_HAND || !lock.onRightClick(player, level, player.getOffhandItem(), hand, pos, face))) {
            player.displayClientMessage(LOCKED, true);
            if (level.isClientSide()) {
                player.playNotifySound(LocksmithSounds.ITEM_LOCK_LOCKED.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                player.swing(hand); // so awful
            }
            return EventResult.interrupt(level.isClientSide());
        }
        return EventResult.pass();
    }

    public static EventResult onLeftClickBlock(Player player, InteractionHand hand, BlockPos pos, Direction face) {
        Level level = player.getLevel();
        AbstractLock lock = LockManager.get(level).getLock(LockPosition.of(pos));
        if (lock == null || player.isCreative())
            return EventResult.pass();

        if (lock.onLeftClick(player, level, hand, pos, face))
            return EventResult.pass();
        else {
            player.displayClientMessage(LOCKED, true);
            return EventResult.interruptFalse();
        }
    }

    public static void onBreakBlock(Level level, BlockPos pos, BlockState state) {
        AbstractLock lock = LockManager.get(level).getLock(LockPosition.of(pos));
        if (lock == null)
            return;

        LockManager.get(level).removeLock(lock.getPos().blockPosition(), pos, false);

        // Hack to move lock to other chest if a double chest
        if (state.hasProperty(ChestBlock.TYPE) && state.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
            Tag tag = AbstractLock.CODEC.encodeStart(NbtOps.INSTANCE, lock).getOrThrow(false, LOGGER::error);
            if (!(tag instanceof CompoundTag lockTag))
                return;

            DataResult<Tag> newPos = BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, lock.getPos().blockPosition().relative(ChestBlock.getConnectedDirection(state)));
            if (newPos.error().isPresent()) {
                LOGGER.warn("Failed to encode lock data {}", newPos.error().get());
                return;
            }

            lockTag.put("pos", newPos.result().get());
            DataResult<AbstractLock> newLock = AbstractLock.CODEC.parse(NbtOps.INSTANCE, lockTag);
            if (newLock.result().isPresent()) {
                LOGGER.warn("Failed to create lock {}", newLock.error().get());
                return;
            }

            LockManager.get(level).addLock(newLock.result().get());
        }
    }
}
