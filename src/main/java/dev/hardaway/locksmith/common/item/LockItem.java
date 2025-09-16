package dev.hardaway.locksmith.common.item;

import dev.hardaway.locksmith.api.lock.Lock;
import dev.hardaway.locksmith.api.lock.Lockable;
import dev.hardaway.locksmith.common.component.LockData;
import dev.hardaway.locksmith.core.registry.LocksmithCapabilities;
import dev.hardaway.locksmith.core.registry.LocksmithComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.List;

public class LockItem extends Item {
    public LockItem(Properties properties) {
        super(properties);
        NeoForge.EVENT_BUS.addListener(this::onEntityInteraction);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand usedHand) {
        Lockable lockable = target.getCapability(LocksmithCapabilities.ENTITY_LOCKABLE);
        if (lockable == null)
            return InteractionResult.PASS;

        if (lockable.canLock(stack)) {
            Lock lock = stack.get(LocksmithComponents.LOCK_DATA).createLock(stack.copyWithCount(1));
            if (lockable.lock(lock)) {
                stack.consume(1, player);
                return InteractionResult.sidedSuccess(player.level().isClientSide());
            }
        }

        return InteractionResult.FAIL;
    }

    private void onEntityInteraction(PlayerInteractEvent.EntityInteractSpecific event) {
        ItemStack stack = event.getItemStack();
        Level level = event.getLevel();
        Entity target = event.getTarget();

        Lockable lockable = target.getCapability(LocksmithCapabilities.ENTITY_LOCKABLE);
        if (lockable == null)
            return;

        if (lockable.canLock(stack)) {
            Lock lock = stack.get(LocksmithComponents.LOCK_DATA).createLock(stack.copyWithCount(1));
            if (lockable.lock(lock)) {
                stack.consume(1, event.getEntity());
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
            }
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.isSecondaryUseActive())
            return InteractionResult.PASS;

        ItemStack stack = context.getItemInHand();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);

        Lockable lockable = level.getCapability(LocksmithCapabilities.BLOCK_LOCKABLE, pos, state, be);
        if (lockable == null)
            return InteractionResult.PASS;

        if (lockable.canLock(stack)) {
            Lock lock = stack.get(LocksmithComponents.LOCK_DATA).createLock(stack.copyWithCount(1));
            if (lockable.lock(lock)) {
                stack.consume(1, context.getPlayer());
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        LockData lock = stack.get(LocksmithComponents.LOCK_DATA);
        if (lock == null)
            return;

        tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".requires_key", lock.label()).withStyle(ChatFormatting.GRAY));
    }
}
