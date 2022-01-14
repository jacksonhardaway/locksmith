package gg.moonflower.locksmith.common.item;

import gg.moonflower.locksmith.api.key.Key;
import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.common.lock.LockManager;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import gg.moonflower.locksmith.core.registry.LocksmithTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class KeyItem extends Item implements Key {

    private static final Component ORIGINAL = new TranslatableComponent("item." + Locksmith.MOD_ID + ".key.original").withStyle(ChatFormatting.GRAY);
    private static final Component COPY = new TranslatableComponent("item." + Locksmith.MOD_ID + ".key.copy").withStyle(ChatFormatting.GRAY);

    public KeyItem(Properties properties) {
        super(properties);
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
    public boolean overrideStackedOnOther(ItemStack key, Slot slot, ClickAction clickAction, Player player) {
        if (Locksmith.CONFIG.useKeyringMenu.get())
            return false;
        if (clickAction != ClickAction.SECONDARY)
            return false;
        return addKeyring(key, slot.getItem(), slot, player);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack key, ItemStack clickItem, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (Locksmith.CONFIG.useKeyringMenu.get())
            return false;
        if (clickAction == ClickAction.SECONDARY && slot.allowModification(player))
            return addKeyring(key, clickItem, slot, player);
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add(isOriginal(stack) ? ORIGINAL : COPY);
        if (advanced.isAdvanced()) {
            UUID id = getLockId(stack);
            if (id == null)
                return;

            tooltip.add(new TextComponent("Lock Id: " + id).withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    private static boolean addKeyring(ItemStack key, ItemStack clickItem, Slot slot, Player player) {
        if (clickItem.isEmpty())
            return false;

        if (!clickItem.is(LocksmithItems.KEY.get()))
            return false;

        player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + player.getLevel().getRandom().nextFloat() * 0.4F);

        ItemStack keyring = new ItemStack(LocksmithItems.KEYRING.get());
        for (int i = 0; i < player.getInventory().items.size(); ++i) {
            if (!player.getInventory().items.get(i).isEmpty() && slot.getItem() == player.getInventory().items.get(i)) {
                KeyringItem.setKeys(keyring, Arrays.asList(key.split(1), slot.safeTake(clickItem.getCount(), 1, player)));
                player.getInventory().setItem(i, keyring);
                return true;
            }
        }

        KeyringItem.setKeys(keyring, Arrays.asList(key.split(1), slot.safeTake(clickItem.getCount(), 1, player)));
        player.getInventory().placeItemBackInInventory(keyring);

        return true;
    }

    @Override
    public boolean matchesLock(UUID id, ItemStack stack) {
        return id.equals(getLockId(stack));
    }

    public static boolean canHaveLock(ItemStack stack) {
        return stack.is(LocksmithTags.LOCKING);
//        return stack.getItem() == LocksmithItems.KEY.get() || stack.getItem() == LocksmithItems.LOCK.get() || stack.getItem() == LocksmithBlocks.LOCK_BUTTON.get().asItem();
    }

    public static boolean isKey(ItemStack stack) {
        return !stack.is(LocksmithTags.BLANK_KEY) && stack.getItem() instanceof Key;
//        return stack.getItem() == LocksmithItems.KEY.get();
    }

    public static boolean isBlankKey(ItemStack stack) {
        return stack.is(LocksmithTags.BLANK_KEY);
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
}
