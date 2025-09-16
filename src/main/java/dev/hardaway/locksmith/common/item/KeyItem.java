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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.List;

/*

LOCKS
lock component stores multiple locks
multiple keys stacked on top of eachother creates a keyring
a keyring has a lock component with all of the keys
you can select which key you want to take out of the keyring using the scroll wheel
keyring will show all the keys in the tooltip

key component stores the name of the key and the uuid of the key
the name of the key shows in the lock description
applying the key component to a key will put the key name and uuid in it
copying keys will copy that key component
only the original key will have the original flag and keys can only be made from the original


entities get an EntityLock applied to them, this is a neoforge Capability
blocks get a BlockLock applied to them, this is a neoforge capability with data stored in the locks.dat file
blocks which need special support can implement the capability differently
e.g doors have a DoorLock to prevent entities from opening doors and ChestLock to lock double wide chests
blocks with entities have their data stored in the blockentity itself, allows support for things like carryon

lockables are based on a tag #lockables

kube js support for lock implementations??

VANITY
vanity can only be applied to ORIGINAL keys
changing the vanity of an original key does not change other keys
copying an original key that has a vanity will copy the vanity, the vanity will not change

CURIO
keys can go on the key slot
they appear on the side of the player (?)

 */
public class KeyItem extends Item {
    public KeyItem(Properties properties) {
        super(properties);
        NeoForge.EVENT_BUS.addListener(this::onEntityInteraction);
    }

    @Override
    public Component getName(ItemStack stack) {
        Component stackName = super.getName(stack);
        KeyData keyData = stack.get(LocksmithComponents.KEY_DATA);
        if (keyData == null)
            return stackName;

        if (keyData.copyId() <= 0)
            return stackName;

        return Component.translatable(this.getDescriptionId() + ".copied_key", keyData.label(), keyData.copyId());
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand usedHand) {
        if (!player.isSecondaryUseActive())
            return InteractionResult.PASS;

        Lockable lockable = target.getCapability(LocksmithCapabilities.ENTITY_LOCKABLE);
        if (lockable == null)
            return InteractionResult.PASS;

        if (lockable.canUnlock(stack)) {
            lockable.unlock();
            return InteractionResult.sidedSuccess(player.level().isClientSide());
        }

        return InteractionResult.FAIL;
    }

    private void onEntityInteraction(PlayerInteractEvent.EntityInteractSpecific event) {
        Player player = event.getEntity();
        if (!player.isSecondaryUseActive())
            return;

        Entity target = event.getTarget();
        Lockable lockable = target.getCapability(LocksmithCapabilities.ENTITY_LOCKABLE);
        if (lockable == null)
            return;

        ItemStack stack = event.getItemStack();
        if (lockable.canUnlock(stack)) {
            lockable.unlock();
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide()));
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

        if (lockable.canUnlock(stack)) {
            lockable.unlock();
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        KeyData keyData = stack.get(LocksmithComponents.KEY_DATA);
        if (keyData != null && keyData.copyId() <= 0)
            tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".original_key").withStyle(ChatFormatting.GRAY));
    }
}
