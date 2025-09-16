package dev.hardaway.locksmith.core.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.hardaway.locksmith.api.lock.Lockable;
import dev.hardaway.locksmith.core.Locksmith;
import dev.hardaway.locksmith.core.registry.LocksmithCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Gui.class, priority = 800)
public class GuiMixin {
    @Unique
    private static final ResourceLocation CROSSHAIR_LOCKED = Locksmith.path("crosshair_locked");
    @Unique
    private static final ResourceLocation CROSSHAIR_UNLOCKED = Locksmith.path("crosshair_unlocked");

    @Shadow
    @Final
    private Minecraft minecraft;


    @WrapOperation(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 0))
    private void renderLocked(
            GuiGraphics instance, ResourceLocation sprite, int x, int y, int width, int height, Operation<Void> original
    ) {
        Level level = this.minecraft.level;
        Player player = this.minecraft.player;
        HitResult hit = this.minecraft.hitResult;
        if (level != null && player != null && hit != null) {
            Lockable lockable = null;
            if (hit instanceof BlockHitResult blockHit) {
                BlockPos pos = blockHit.getBlockPos();
                BlockState state = level.getBlockState(pos);
                BlockEntity be = level.getBlockEntity(pos);
                lockable = level.getCapability(LocksmithCapabilities.BLOCK_LOCKABLE, pos, state, be);
            } else if (hit instanceof EntityHitResult entityHit) {
                lockable = entityHit.getEntity().getCapability(LocksmithCapabilities.ENTITY_LOCKABLE);
            }

            ItemStack mainStack = player.getMainHandItem();
            ItemStack offStack = player.getOffhandItem();
            if (lockable != null) {
                if (!lockable.canUnlock(mainStack) && !lockable.canUnlock(offStack)) {
                    sprite = CROSSHAIR_LOCKED;
                } else if (lockable.canLock(mainStack) || lockable.canLock(offStack)) {
                    sprite = CROSSHAIR_UNLOCKED;
                }
            }

        }

        original.call(instance, sprite, x, y, width, height);
    }
}