package gg.moonflower.locksmith.common.item;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.api.lock.types.KeyLock;
import gg.moonflower.locksmith.common.lock.LockManager;
import gg.moonflower.locksmith.common.tooltip.KeyringTooltip;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import gg.moonflower.locksmith.core.registry.LocksmithLocks;
import gg.moonflower.pollen.api.util.NbtConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.*;

public class KeyringItem extends Item {

    public static final int MAX_KEYS = 4;

    public KeyringItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isSecondaryUseActive())
            return InteractionResultHolder.pass(stack);

        if (dropContents(stack, player)) {
            this.playDropContentsSound(player);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(ItemStack.EMPTY, level.isClientSide());
        }

        return InteractionResultHolder.fail(stack);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack keyRing, Slot slot, ClickAction clickAction, Player player) {
        if (clickAction != ClickAction.SECONDARY)
            return false;

        ItemStack clickItem = slot.getItem();
        if (clickItem.isEmpty()) {
            this.playRemoveOneSound(player);
            removeOne(player, keyRing).ifPresent(key -> add(keyRing, slot.safeInsert(key)));
        } else if (canAdd(keyRing, clickItem)) {
            this.playInsertSound(player);
            add(keyRing, slot.safeTake(clickItem.getCount(), 1, player));
        }

        return true;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack keyRing, ItemStack clickItem, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction == ClickAction.SECONDARY && slot.allowModification(player)) {
            if (clickItem.isEmpty()) {
                removeOne(player, keyRing).ifPresent(removedKey -> {
                    this.playRemoveOneSound(player);
                    slotAccess.set(removedKey);
                });
            } else if (canAdd(keyRing, clickItem)) {
                this.playInsertSound(player);
                add(keyRing, clickItem);
            }

            return true;
        }

        return false;
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
        for (ItemStack key : KeyringItem.getKeys(context.getItemInHand())) {
            if (lock.canRemove(player, level, key)) {
                if (level.isClientSide())
                    return InteractionResult.SUCCESS;

                lock.onRemove(level, pos);
                LockManager.get(level).removeLock(lock.getPos());
                player.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        return Optional.of(new KeyringTooltip(getKeys(itemStack)));
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        ItemUtils.onContainerDestroyed(itemEntity, getKeys(itemEntity.getItem()).stream());
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getLevel().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.getLevel().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.getLevel().getRandom().nextFloat() * 0.4F);
    }

    public static List<ItemStack> getKeys(ItemStack stack) {
        if (!stack.is(LocksmithItems.KEYRING.get()))
            return Collections.emptyList();

        CompoundTag nbt = stack.getTag();
        if (nbt == null || !nbt.contains("Keys", NbtConstants.LIST))
            return Collections.emptyList();

        ListTag keysNbt = nbt.getList("Keys", NbtConstants.COMPOUND);
        if (keysNbt.isEmpty())
            return Collections.emptyList();

        List<ItemStack> list = new ArrayList<>(keysNbt.size());
        for (int i = 0; i < Math.min(MAX_KEYS, keysNbt.size()); i++) {
            ItemStack key = ItemStack.of(keysNbt.getCompound(i));
            if (!key.isEmpty())
                list.add(key);
        }

        return list;
    }

    public static void setKeys(ItemStack stack, Collection<ItemStack> keys) {
        if (!stack.is(LocksmithItems.KEYRING.get()) || keys.isEmpty())
            return;

        CompoundTag nbt = stack.getOrCreateTag();
        ListTag keysNbt = new ListTag();
        int i = 0;
        for (ItemStack key : keys) {
            if (key.isEmpty())
                continue;
            if (i >= MAX_KEYS)
                break;
            keysNbt.add(key.save(new CompoundTag()));
            i++;
        }
        nbt.put("Keys", keysNbt);
    }

    private static Optional<ItemStack> removeOne(Player player, ItemStack keyRing) {
        CompoundTag tag = keyRing.getOrCreateTag();
        if (!tag.contains("Keys", NbtConstants.LIST))
            return Optional.empty();

        ListTag keysNbt = tag.getList("Keys", NbtConstants.COMPOUND);
        if (keysNbt.isEmpty())
            return Optional.empty();

        CompoundTag keyNbt = keysNbt.getCompound(0);
        ItemStack keyStack = ItemStack.of(keyNbt);
        keysNbt.remove(0);

        if (keysNbt.size() == 1) {
            ItemStack last = ItemStack.of(keysNbt.getCompound(0));
            for (int i = 0; i < player.getInventory().items.size(); ++i) {
                if (!player.getInventory().items.get(i).isEmpty() && keyRing == player.getInventory().items.get(i)) {
                    keyRing.setCount(0);
                    player.getInventory().setItem(i, last);
                    return Optional.of(keyStack);
                }
            }

            player.getInventory().placeItemBackInInventory(last);
        }

        if (keysNbt.isEmpty())
            keyRing.setCount(0);

        return Optional.of(keyStack);
    }

    private static boolean dropContents(ItemStack itemStack, Player player) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (!tag.contains("Keys"))
            return false;

        if (player instanceof ServerPlayer) {
            ListTag listTag = tag.getList("Keys", NbtConstants.COMPOUND);

            for (int i = 0; i < listTag.size(); i++)
                player.getInventory().placeItemBackInInventory(ItemStack.of(listTag.getCompound(i)));
        }

        itemStack.removeTagKey("Keys");
        return true;
    }

    private static void add(ItemStack stack, ItemStack key) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("Keys"))
            tag.put("Keys", new ListTag());

        ListTag keysNbt = tag.getList("Keys", NbtConstants.COMPOUND);

        ItemStack singleKey = key.split(1);
        CompoundTag keyTag = new CompoundTag();
        singleKey.save(keyTag);
        keysNbt.add(0, keyTag);
    }

    private static boolean canAdd(ItemStack keyRing, ItemStack key) {
        if (!keyRing.is(LocksmithItems.KEYRING.get()) || !key.is(LocksmithItems.KEY.get()))
            return false;
        return keyRing.getTag() == null || (keyRing.getTag().contains("Keys", NbtConstants.LIST) && keyRing.getTag().getList("Keys", NbtConstants.COMPOUND).size() < MAX_KEYS);
    }
}
