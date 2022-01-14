package gg.moonflower.locksmith.common.item;

import gg.moonflower.locksmith.api.key.Key;
import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.api.lock.LockManager;
import gg.moonflower.locksmith.core.registry.LocksmithTags;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
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

public class KeyItem extends Item implements Key {

    private static final Component ORIGINAL = new TranslatableComponent("item.locksmith.key.original").withStyle(ChatFormatting.GRAY);
    private static final Component COPY = new TranslatableComponent("item.locksmith.key.copy").withStyle(ChatFormatting.GRAY);

    public KeyItem(Properties properties) {
        super(properties);
    }

//    public static boolean matchesLock(UUID id, ItemStack stack) {
//        if (stack.getItem() == LocksmithItems.KEYRING.get()) {
//            for (ItemStack key : KeyringItem.getKeys(stack))
//                if (id.equals(KeyItem.getLockId(key)))
//                    return true;
//        } else if (stack.getItem() == LocksmithItems.KEY.get()) {
//            return id.equals(KeyItem.getLockId(stack));
//        }
//        return false;
//    }

    @Override
    public boolean matchesLock(UUID id, ItemStack stack) {
        return id.equals(getLockId(stack));
    }

    public static boolean canHaveLock(ItemStack stack) {
        return stack.getItem().is(LocksmithTags.LOCKING);
//        return stack.getItem() == LocksmithItems.KEY.get() || stack.getItem() == LocksmithItems.LOCK.get() || stack.getItem() == LocksmithBlocks.LOCK_BUTTON.get().asItem();
    }

    public static boolean isKey(ItemStack stack) {
        return !stack.getItem().is(LocksmithTags.BLANK_KEY) && stack.getItem() instanceof Key;
//        return stack.getItem() == LocksmithItems.KEY.get();
    }

    public static boolean isBlankKey(ItemStack stack) {
        return stack.getItem().is(LocksmithTags.BLANK_KEY);
//        return stack.getItem() == LocksmithItems.BLANK_KEY.get();
    }

    public static boolean hasLockId(ItemStack stack) {
        if (stack.getTag() == null || !canHaveLock(stack))
            return false;
        CompoundTag tag = stack.getTag();
        return tag.hasUUID("Lock");
    }

    @Nullable
    public static UUID getLockId(ItemStack stack) {
        if (stack.getTag() == null || !canHaveLock(stack))
            return null;
        CompoundTag tag = stack.getTag();
        return tag.hasUUID("Lock") ? tag.getUUID("Lock") : null;
    }

    public static void setLockId(ItemStack stack, @Nullable UUID id) {
        if (canHaveLock(stack)) {
            if (stack.getTag() == null && id == null)
                return;
            if (id == null) {
                stack.getTag().remove("Lock");
                if (stack.getTag().isEmpty())
                    stack.setTag(null);
                return;
            }
            stack.getOrCreateTag().putUUID("Lock", id);
        }
    }

    public static boolean isOriginal(ItemStack stack) {
        return isKey(stack) && hasLockId(stack) && stack.getOrCreateTag().getBoolean("Original");
    }

    public static void setOriginal(ItemStack stack, boolean original) {
        if (isKey(stack) && hasLockId(stack))
            stack.getOrCreateTag().putBoolean("Original", original);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        Level level = context.getLevel();
        AbstractLock abstractLock = LockManager.getLock(level, pos);
        if (player == null || abstractLock == null)
            return InteractionResult.PASS;

        if (abstractLock.canRemove(player, level, context.getItemInHand())) {
            if (level.isClientSide())
                return InteractionResult.SUCCESS;

            LockManager.get(level).removeLock(abstractLock.getPos(), pos, true);
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
            if (id == null || Util.NIL_UUID.equals(id))
                return;

            tooltipComponents.add(new TextComponent("Lock Id: " + id).withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
