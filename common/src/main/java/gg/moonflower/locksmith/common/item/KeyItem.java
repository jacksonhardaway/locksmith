package gg.moonflower.locksmith.common.item;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.api.lock.types.KeyLock;
import gg.moonflower.locksmith.common.lock.LockManager;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import gg.moonflower.locksmith.core.registry.LocksmithLocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class KeyItem extends Item {
    private static final Component ORIGINAL = new TranslatableComponent("item.locksmith.key.original").withStyle(ChatFormatting.GRAY);
    private static final Component COPY = new TranslatableComponent("item.locksmith.key.copy").withStyle(ChatFormatting.GRAY);

    public KeyItem(Properties properties) {
        super(properties);
    }

    public static boolean matchesLock(UUID id, ItemStack stack) {
        if (stack.getItem() == LocksmithItems.KEYRING.get()) {
            for (ItemStack key : KeyringItem.getKeys(stack)) {
                if (id.equals(KeyItem.getLockId(key)))
                    return true;
            }
        } else if (stack.getItem() == LocksmithItems.KEY.get()) {
            return id.equals(KeyItem.getLockId(stack));
        }
        return false;
    }

    @Nullable
    public static UUID getLockId(ItemStack stack) {
        if (stack.getItem() != LocksmithItems.KEY.get() && stack.getItem() != LocksmithItems.LOCK.get())
            return null;

        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("Lock"))
            return null;

        return tag.getUUID("Lock");
    }

    public static void setLockId(ItemStack stack, UUID id) {
        if (stack.getItem() != LocksmithItems.KEY.get() && stack.getItem() != LocksmithItems.LOCK.get())
            return;

        stack.getOrCreateTag().putUUID("Lock", id);
    }

    public static boolean isOriginal(ItemStack stack) {
        return stack.getItem() == LocksmithItems.KEY.get() && stack.getOrCreateTag().getBoolean("Original");
    }

    public static void setOriginal(ItemStack stack, boolean original) {
        if (stack.getItem() != LocksmithItems.KEY.get())
            return;

        stack.getOrCreateTag().putBoolean("Original", original);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        Level level = context.getLevel();
        AbstractLock abstractLock = LockManager.getLock(level, pos);
        if (player == null || abstractLock == null || abstractLock.getType() != LocksmithLocks.KEY.get())
            return InteractionResult.PASS;

        KeyLock lock = (KeyLock) abstractLock;
        if (lock.canRemove(player, level, context.getItemInHand())) {
            if (level.isClientSide())
                return InteractionResult.SUCCESS;

            LockManager.get(level).removeLock(lock.getPos(), pos, true);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (KeyItem.isOriginal(stack))
            tooltipComponents.add(ORIGINAL);
        else
            tooltipComponents.add(COPY);
        if (isAdvanced.isAdvanced()) {
            UUID id = KeyItem.getLockId(stack);
            if (id == null)
                return;

            tooltipComponents.add(new TextComponent("Lock Id: " + id).withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
