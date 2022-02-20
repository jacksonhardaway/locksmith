package gg.moonflower.locksmith.common.lockpicking;

import gg.moonflower.locksmith.api.lock.LockManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

/**
 * @author Ocelot
 */
public class ServerBlockPickingContext extends ServerPickingContext {

    private final ContainerLevelAccess access;
    private final BlockPos clickPos;

    ServerBlockPickingContext(ContainerLevelAccess access, BlockPos clickPos, ServerPlayer player, ItemStack pickStack, InteractionHand pickHand) {
        super(player, pickStack, pickHand);
        this.access = access;
        this.clickPos = clickPos;
    }

    @Override
    protected void playSound(SoundEvent sound) {
        this.access.execute((level, pos) -> level.playSound(this.player, pos, sound, SoundSource.PLAYERS, 1.0F, 1.0F));
    }

    @Override
    protected void removeLock() {
        this.access.execute((level, pos) -> LockManager.get(level).removeLock(pos, this.clickPos, false));
    }
}
