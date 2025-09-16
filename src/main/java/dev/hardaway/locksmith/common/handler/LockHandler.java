package dev.hardaway.locksmith.common.handler;

import dev.hardaway.locksmith.api.lock.Lockable;
import dev.hardaway.locksmith.core.Locksmith;
import dev.hardaway.locksmith.core.registry.LocksmithCapabilities;
import dev.hardaway.locksmith.core.registry.LocksmithSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = Locksmith.MOD_ID)
public class LockHandler {

    @SubscribeEvent
    public static void onEvent(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);

        Lockable lockable = level.getCapability(LocksmithCapabilities.BLOCK_LOCKABLE, pos, state, be);
        if (lockable == null)
            return;

        LockHandler.lockInteraction(lockable, event.getEntity(), () -> {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide));
        });
    }

    @SubscribeEvent
    public static void onEvent(RenderHighlightEvent.Block event) {
        BlockHitResult hit = event.getTarget();

        Minecraft minecraft = Minecraft.getInstance();

        Level level = minecraft.level;
        Player player = minecraft.player;
        if (level == null || player == null)
            return;

        BlockPos pos = hit.getBlockPos();
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);

        Lockable lockable = level.getCapability(LocksmithCapabilities.BLOCK_LOCKABLE, pos, state, be);
        if (lockable == null)
            return;

        ItemStack mainStack = player.getMainHandItem();
        ItemStack offStack = player.getOffhandItem();
        if (lockable.canUnlock(mainStack) || lockable.canUnlock(offStack))
            return;

        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEvent(PlayerInteractEvent.EntityInteractSpecific event) {
        Lockable lockable = event.getTarget().getCapability(LocksmithCapabilities.ENTITY_LOCKABLE);
        if (lockable == null)
            return;

        LockHandler.lockInteraction(lockable, event.getEntity(), () -> {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide));
        });
    }

    @SubscribeEvent
    public static void onEvent(PlayerInteractEvent.EntityInteract event) {
        Lockable lockable = event.getTarget().getCapability(LocksmithCapabilities.ENTITY_LOCKABLE);
        if (lockable == null)
            return;

        LockHandler.lockInteraction(lockable, event.getEntity(), () -> {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide));
        });
    }


    private static void lockInteraction(Lockable lockable, Player player, Runnable cancel) {
        ItemStack mainStack = player.getMainHandItem();
        ItemStack offStack = player.getOffhandItem();
        if (lockable.canUnlock(mainStack) || lockable.canUnlock(offStack))
            return;

        if (player.level().isClientSide()) {
            player.displayClientMessage(Component.literal("It's locked."), true);
            player.playNotifySound(LocksmithSounds.ITEM_LOCK_LOCKED.get(), SoundSource.BLOCKS, 0.2F, 1.0F);
        }

        cancel.run();
    }
}
