package gg.moonflower.locksmith.api.lock.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.common.item.KeyItem;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import gg.moonflower.locksmith.core.registry.LocksmithLocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SerializableUUID;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.UUID;

public class KeyLock extends AbstractLock {

    public static final Codec<KeyLock> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SerializableUUID.CODEC.fieldOf("id").forGetter(KeyLock::getId),
            BlockPos.CODEC.fieldOf("pos").forGetter(KeyLock::getPos),
            ItemStack.CODEC.fieldOf("stack").forGetter(KeyLock::getStack)
    ).apply(instance, KeyLock::new));

    public KeyLock(UUID id, BlockPos pos, ItemStack stack) {
        super(LocksmithLocks.KEY.get(), id, pos, stack);
    }

    @Override
    public boolean canRemove(Player player, Level level, ItemStack stack) {
        return player.isSecondaryUseActive() && stack.getItem() == LocksmithItems.KEY.get() && KeyItem.matchesLock(this.getId(), stack);
    }

    @Override
    public boolean canUnlock(Player player, Level level, ItemStack stack) {
        return KeyItem.matchesLock(this.getId(), stack);
    }

    @Override
    public boolean canPick(Player player, Level level) {
        return true;
    }

    @Override
    public boolean onRightClick(Player player, Level level, ItemStack stack, BlockHitResult hitResult) {
        if (KeyItem.matchesLock(this.getId(), stack)) {
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            return true;
        }
        return false;
    }

    @Override
    public boolean onLeftClick(Player player, Level level, InteractionHand hand, BlockPos pos, Direction direction) {
        return Locksmith.CONFIG.allowLocksToBeBroken.get();
    }
}
