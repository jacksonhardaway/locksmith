package gg.moonflower.locksmith.common.item;

import gg.moonflower.locksmith.api.lock.types.KeyLock;
import gg.moonflower.locksmith.common.lock.LockManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
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

public class LockItem extends Item {
    public LockItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        UUID lockId = KeyItem.getLockId(stack);
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        if (LockManager.getLock(level, pos) != null || lockId == null)
            return InteractionResult.PASS;

        if (level.isClientSide())
            return InteractionResult.SUCCESS;

        ItemStack lockStack = stack.copy();
        lockStack.setCount(1);
        LockManager.get(level).addLock(new KeyLock(lockId, pos, lockStack));
        Player player = context.getPlayer();
        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.isCreative())
                stack.shrink(1);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (isAdvanced.isAdvanced()) {
            UUID id = KeyItem.getLockId(stack);
            if (id == null)
                return;

            tooltipComponents.add(new TextComponent("Lock Id: " + id).withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
