package gg.moonflower.locksmith.common.item;

import gg.moonflower.locksmith.api.lock.LockManager;
import gg.moonflower.locksmith.api.lock.position.LockPosition;
import gg.moonflower.locksmith.common.lock.types.LockButtonLock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class LockButtonItem extends BlockItem {

    public LockButtonItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        return super.canPlace(context, state) && KeyItem.getLockId(context.getItemInHand()) != null;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        LockItem.appendHoverText(stack, tooltipComponents, isAdvanced);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
        UUID id = KeyItem.getLockId(stack);
        if (id != null) {
            ItemStack newStack = stack.copy();
            newStack.setCount(1);
            LockManager.get(level).addLock(new LockButtonLock(id, LockPosition.of(pos), newStack));
        }
        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }
}
