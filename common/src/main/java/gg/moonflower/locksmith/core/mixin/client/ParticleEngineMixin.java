package gg.moonflower.locksmith.core.mixin.client;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.common.lock.LockManager;
import gg.moonflower.locksmith.core.registry.LocksmithParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {

    @Shadow
    protected ClientLevel level;
    @Shadow
    public abstract void add(Particle effect);

    @Inject(method = "crack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;add(Lnet/minecraft/client/particle/Particle;)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void crack(BlockPos pos, Direction side, CallbackInfo ci, BlockState state, int x, int y, int z, float f, AABB shape, double randX, double randY, double randZ) {
        AbstractLock lock = LockManager.getLock(this.level, pos);
        if (lock == null)
            return;
        this.level.addParticle(LocksmithParticles.LOCK_BREAK.get(), randX, randY, randZ, 0, 0, 0);
    }
}
