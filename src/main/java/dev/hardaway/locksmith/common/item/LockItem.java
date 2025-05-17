package dev.hardaway.locksmith.common.item;

import dev.hardaway.locksmith.api.lock.Lock;
import dev.hardaway.locksmith.api.lock.Lockable;
import dev.hardaway.locksmith.common.component.KeyData;
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
        KeyData keyData = stack.get(LocksmithComponents.KEY_DATA);
        if (keyData == null)
            return InteractionResult.PASS;

        Lockable lockable = target.getCapability(LocksmithCapabilities.LOCKABLE_ENTITY);
        if (lockable == null || lockable.getLock().isPresent())
            return InteractionResult.PASS;

        if (lockable.lock(new Lock(keyData.id(), stack.split(1)))) {
            return InteractionResult.sidedSuccess(player.level().isClientSide());
        }

        return InteractionResult.FAIL;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        KeyData keyData = stack.get(LocksmithComponents.KEY_DATA);
        if (keyData == null)
            return InteractionResult.PASS;

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);

        Lockable lockable = level.getCapability(LocksmithCapabilities.LOCKABLE_BLOCK, pos, state, be);
        if (lockable == null || lockable.getLock().isPresent())
            return InteractionResult.PASS;

        if (lockable.lock(new Lock(keyData.id(), stack.split(1)))) {
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        KeyData keyData = stack.get(LocksmithComponents.KEY_DATA);
        if (keyData == null)
            return;

        tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".requires_key", keyData.label()).withStyle(ChatFormatting.GRAY));
    }

    private void onEntityInteraction(PlayerInteractEvent.EntityInteractSpecific event) {
        ItemStack stack = event.getItemStack();
        if (!stack.is(this))
            return;

        Level level = event.getLevel();
        Entity target = event.getTarget();

        KeyData keyData = stack.get(LocksmithComponents.KEY_DATA);
        if (keyData == null)
            return;

        Lockable lockable = target.getCapability(LocksmithCapabilities.LOCKABLE_ENTITY);
        if (lockable == null || lockable.getLock().isPresent())
            return;

        if (lockable.lock(new Lock(keyData.id(), stack.split(1)))) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
        }
    }
}
