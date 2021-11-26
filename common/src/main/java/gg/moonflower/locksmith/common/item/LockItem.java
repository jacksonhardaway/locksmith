package gg.moonflower.locksmith.common.item;

import gg.moonflower.locksmith.api.lock.LockData;
import gg.moonflower.locksmith.client.lock.ClientLockManager;
import gg.moonflower.locksmith.common.world.lock.LockManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
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

        if (context.getLevel().isClientSide())
            return ClientLockManager.INSTANCE.getLock(level.dimension(), pos) != null || lockId == null ? InteractionResult.PASS : InteractionResult.CONSUME;


        LockManager manager = LockManager.getOrCreate((ServerLevel) level);
        if (manager.getLock(pos) != null || lockId == null)
            return InteractionResult.PASS;

        manager.addLock(new LockData(lockId, pos, stack.copy(), true));
        if (context.getPlayer() != null && !context.getPlayer().isCreative())
            stack.shrink(1);
        return InteractionResult.SUCCESS;
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
