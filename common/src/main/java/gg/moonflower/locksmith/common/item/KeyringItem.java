package gg.moonflower.locksmith.common.item;

import gg.moonflower.locksmith.api.key.Key;
import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.api.lock.LockManager;
import gg.moonflower.locksmith.common.menu.KeyringMenu;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import gg.moonflower.locksmith.core.registry.LocksmithLocks;
import gg.moonflower.pollen.api.util.NbtConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.*;

public class KeyringItem extends Item implements Key {

    public static final int MAX_KEYS = 4;

    public KeyringItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean matchesLock(UUID id, ItemStack stack) {
        for (ItemStack key : getKeys(stack))
            if (id.equals(KeyItem.getLockId(key)))
                return true;
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isSecondaryUseActive()) {
            int index = player.inventory.findSlotMatchingItem(stack);
            if (index == -1)
                return InteractionResultHolder.pass(stack);

            if (!level.isClientSide()) {
                player.awardStat(Stats.ITEM_USED.get(this));
                player.openMenu(new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return stack.getHoverName();
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
                        return new KeyringMenu(containerId, player.inventory, index);
                    }
                });
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        Level level = context.getLevel();
        AbstractLock lock = LockManager.getLock(level, pos);
        if (player == null || lock == null || lock.getType() != LocksmithLocks.LOCK_BUTTON.get())
            return InteractionResult.PASS;

        for (ItemStack key : KeyringItem.getKeys(context.getItemInHand())) {
            if (lock.canRemove(player, level, key)) {
                if (level.isClientSide())
                    return InteractionResult.SUCCESS;

                LockManager.get(level).removeLock(lock.getPos(), pos, true);
                player.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    public static List<ItemStack> getKeys(ItemStack stack) {
        if (stack.getItem() != LocksmithItems.KEYRING.get())
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
        if (stack.getItem() != LocksmithItems.KEYRING.get() || keys.isEmpty())
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
}
