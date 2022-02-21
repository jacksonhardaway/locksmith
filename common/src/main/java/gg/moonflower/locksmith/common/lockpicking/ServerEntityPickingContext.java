package gg.moonflower.locksmith.common.lockpicking;

import gg.moonflower.locksmith.api.lock.LockManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

/**
 * @author Ocelot
 */
public class ServerEntityPickingContext extends ServerPickingContext {

    private final Entity entity;

    ServerEntityPickingContext(Entity entity, ServerPlayer player, ItemStack pickStack, InteractionHand pickHand) {
        super(player, pickStack, pickHand);
        this.entity = entity;
    }

    @Override
    protected void playSound(SoundEvent sound) {
        this.entity.level.playSound(this.player, this.entity, sound, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    protected void removeLock() {
        LockManager.get(this.entity.level).removeLock(this.entity);
    }
}
