package gg.moonflower.locksmith.common.lock.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import gg.moonflower.locksmith.api.key.Key;
import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.api.lock.LockType;
import gg.moonflower.locksmith.api.lock.position.LockPosition;
import gg.moonflower.locksmith.common.lockpicking.LockPickingContext;
import gg.moonflower.locksmith.common.menu.LockpickingMenu;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import gg.moonflower.locksmith.core.registry.LocksmithLocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class KeyLock extends AbstractLock {

    public static final Codec<KeyLock> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.UUID.fieldOf("id").forGetter(KeyLock::getId),
            LockPosition.CODEC.fieldOf("pos").forGetter(KeyLock::getPos),
            ItemStack.CODEC.fieldOf("stack").forGetter(KeyLock::getStack)
    ).apply(instance, KeyLock::new));

    private static final Component LOCK_PICKING = Component.translatable("container." + Locksmith.MOD_ID + ".lock_picking");

    public KeyLock(LockType type, UUID id, LockPosition pos, ItemStack stack) {
        super(type, id, pos, stack);
    }

    public KeyLock(UUID id, LockPosition pos, ItemStack stack) {
        this(LocksmithLocks.KEY, id, pos, stack);
    }

    @Override
    public boolean canRemove(Player player, Level level, ItemStack stack) {
        return player.isSecondaryUseActive() && ((stack.getItem() == LocksmithItems.KEY.get() || stack.getItem() == LocksmithItems.KEYRING.get()) && ((Key) stack.getItem()).matchesLock(this.getId(), stack));
    }

    @Override
    public boolean canUnlock(Player player, Level level, ItemStack stack) {
        return player.isCreative() || ((stack.getItem() == LocksmithItems.KEY.get() || stack.getItem() == LocksmithItems.KEYRING.get()) && ((Key) stack.getItem()).matchesLock(this.getId(), stack));
    }

    @Override
    public boolean pick(Player player, Level level, LockPosition pos, ItemStack pickStack, InteractionHand hand) {
        if (pickStack.getItem() != LocksmithItems.LOCKPICK.get())
            return false;

        if (!level.isClientSide()) {
            player.openMenu(new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return LOCK_PICKING;
                }

                @Override
                public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
                    return new LockpickingMenu(containerId, LockPickingContext.server(KeyLock.this.getPos(), pos.blockPosition(), (ServerPlayer) player, pickStack, hand));
                }
            });
        }

        return true;
    }

    @Override
    public boolean onRightClick(Player player, Level level, ItemStack stack, InteractionHand hand, BlockPos pos, Direction direction) {
        if (this.canUnlock(player, level, stack)) {
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            return true;
        }
        return false;
    }

    @Override
    public boolean onLeftClick(Player player, Level level, InteractionHand hand, BlockPos pos, Direction direction) {
        return Locksmith.CONFIG.allowLocksToBeBroken.get();
    }

    @Override
    public boolean onRightClick(Player player, Level level, ItemStack stack, InteractionHand hand, Entity entity) {
        if (this.canUnlock(player, level, stack)) {
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            return true;
        }
        return false;
    }

    @Override
    public boolean onLeftClick(Player player, Level level, InteractionHand hand, Entity entity) {
        return false;
    }
}
