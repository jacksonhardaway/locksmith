package dev.hardaway.locksmith.common.handler;

import dev.hardaway.locksmith.api.lock.Lockable;
import dev.hardaway.locksmith.core.Locksmith;
import dev.hardaway.locksmith.core.registry.LocksmithCapabilities;
import dev.hardaway.locksmith.core.registry.LocksmithComponents;
import dev.hardaway.locksmith.core.registry.LocksmithSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = Locksmith.MOD_ID)
public class LockHandler {

    @SubscribeEvent
    public static void onEvent(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);

        Lockable lockable = level.getCapability(LocksmithCapabilities.LOCKABLE_BLOCK, pos, state, be);
        if (lockable == null)
            return;

        lockable.getLock().ifPresent(lock -> {
            Player player = event.getEntity();
            ItemStack mainStack = player.getMainHandItem();
            ItemStack offStack = player.getOffhandItem();
            if ((mainStack.has(LocksmithComponents.KEY_DATA) && lock.getId().equals(mainStack.get(LocksmithComponents.KEY_DATA).id())) ||
                    (offStack.has(LocksmithComponents.KEY_DATA) && lock.getId().equals(offStack.get(LocksmithComponents.KEY_DATA).id()))
            )
                return;


            level.playSound(player, pos, LocksmithSounds.ITEM_LOCK_LOCKED.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
        });
    }

    @SubscribeEvent
    public static void onEvent(PlayerInteractEvent.EntityInteractSpecific event) {
        Level level = event.getLevel();
        Entity entity = event.getTarget();

        Lockable lockable = entity.getCapability(LocksmithCapabilities.LOCKABLE_ENTITY);
        if (lockable == null)
            return;

        lockable.getLock().ifPresent(lock -> {
            Player player = event.getEntity();
            ItemStack mainStack = player.getMainHandItem();
            ItemStack offStack = player.getOffhandItem();
            if ((mainStack.has(LocksmithComponents.KEY_DATA) && lock.getId().equals(mainStack.get(LocksmithComponents.KEY_DATA).id())) ||
                    (offStack.has(LocksmithComponents.KEY_DATA) && lock.getId().equals(offStack.get(LocksmithComponents.KEY_DATA).id()))
            )
                return;

            level.playSound(player, entity.blockPosition(), LocksmithSounds.ITEM_LOCK_LOCKED.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
        });
    }
}
