package gg.moonflower.locksmith.common.item;

import gg.moonflower.locksmith.api.lock.LockData;
import gg.moonflower.locksmith.client.lock.ClientLockManager;
import gg.moonflower.locksmith.common.world.lock.LockManager;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class KeyItem extends Item {
    public KeyItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide())
            return InteractionResult.CONSUME;

        ServerLevel level = (ServerLevel) context.getLevel();
        BlockPos pos = context.getClickedPos();
        LockManager manager = LockManager.getOrCreate(level);
        LockData lock = manager.getLock(pos);

        if (KeyItem.matchesLock(level, pos, context.getItemInHand())) {
            manager.removeLock(pos);
            return InteractionResult.SUCCESS;
        } else if (lock == null) {
            LockData newLock = new LockData(UUID.randomUUID(), context.getClickedPos(), true);
            manager.addLock(new ChunkPos(context.getClickedPos()), newLock);
            KeyItem.bind(newLock, context.getItemInHand());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    public static boolean matchesLock(ServerLevel level, BlockPos pos, ItemStack stack) {
        if (stack.getItem() != LocksmithItems.KEY.get())
            return false;

        LockManager manager = LockManager.getOrCreate(level);
        LockData lock = manager.getLock(pos);
        if (lock == null)
            return false;

        return lock.getId().equals(KeyItem.getLockId(stack));
    }

    public static void bind(LockData data, ItemStack stack) {
        if (stack.getItem() != LocksmithItems.KEY.get())
            return;

        if (stack.getOrCreateTag().contains("BoundLock"))
            return;

        stack.getOrCreateTag().putUUID("BoundLock", data.getId());
    }

    @Nullable
    public static UUID getLockId(ItemStack stack) {
        if (stack.getItem() != LocksmithItems.KEY.get())
            return null;

        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("BoundLock"))
            return null;

        return tag.getUUID("BoundLock");
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (isAdvanced.isAdvanced()) {
            UUID id = KeyItem.getLockId(stack);
            if (id == null)
                return;

            tooltipComponents.add(new TextComponent(id.toString()).withStyle(ChatFormatting.GRAY));
        }
    }
}
