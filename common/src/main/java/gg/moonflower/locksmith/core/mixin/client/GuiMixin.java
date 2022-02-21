package gg.moonflower.locksmith.core.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.api.lock.LockManager;
import gg.moonflower.locksmith.api.lock.position.LockPosition;
import gg.moonflower.locksmith.core.Locksmith;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin extends GuiComponent {
    private static final ResourceLocation LOCK_ICONS = new ResourceLocation(Locksmith.MOD_ID, "textures/gui/crosshair_icons.png");

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private int screenWidth;

    @Shadow
    private int screenHeight;

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIII)V", shift = At.Shift.BEFORE, ordinal = 0))
    public void renderLock(PoseStack matrixStack, CallbackInfo ci) {
        Level level = this.minecraft.level;
        Player player = this.minecraft.player;
        HitResult hit = this.minecraft.hitResult;
        if (level == null || player == null || hit == null || hit.getType() != HitResult.Type.BLOCK)
            return;

        BlockHitResult blockHit = (BlockHitResult) hit;
        AbstractLock lock = LockManager.get(level).getLock(LockPosition.of(blockHit.getBlockPos()));
        if (lock == null)
            return;

        boolean moveDown = false;
        if (this.minecraft.options.attackIndicator == AttackIndicatorStatus.CROSSHAIR) {
            float strengthScale = player.getAttackStrengthScale(0.0F);
            moveDown = strengthScale < 1.0F;

            if (!moveDown && this.minecraft.crosshairPickEntity != null && this.minecraft.crosshairPickEntity instanceof LivingEntity && strengthScale >= 1.0F) {
                moveDown = player.getCurrentItemAttackStrengthDelay() > 5.0F;
                moveDown &= this.minecraft.crosshairPickEntity.isAlive();
            }
        }

        int y = this.screenHeight / 2 + 9 + (moveDown ? 9 : 0);
        int x = this.screenWidth / 2 - 5;
        RenderSystem.setShaderTexture(0, LOCK_ICONS);
        blit(matrixStack, x, y, lock.canUnlock(player, level, player.getMainHandItem()) ? 9 : 0, 0, 9, 9, 32, 32);

        RenderSystem.setShaderTexture(0, GUI_ICONS_LOCATION);
    }
}
