package gg.moonflower.locksmith.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import gg.moonflower.locksmith.common.lockpicking.LockPickingContext;
import gg.moonflower.locksmith.common.lockpicking.LockPickingListener;
import gg.moonflower.locksmith.common.lockpicking.PinState;
import gg.moonflower.locksmith.common.menu.LockpickingMenu;
import gg.moonflower.locksmith.core.Locksmith;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;

public class LockPickingScreen extends AbstractContainerScreen<LockpickingMenu> implements LockPickingListener {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Locksmith.MOD_ID, "textures/gui/lockpicking.png");
    private static final float MOVE_SPEED = 0.8F;
    private static final float RAISE_SPEED = 0.8F;

    private final LockPickingContext context;
    private float lastPickIndex;
    private int pickIndex;
    private boolean raised;
    private float lastPickProgress;
    private float pickProgress;
    private float lastRaiseProgress;
    private float raiseProgress;

    public LockPickingScreen(LockpickingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.context = menu.getContext();
        this.context.addListener(this);
        this.imageWidth = 104;
        this.imageHeight = 34;
    }

    @Override
    public void onPick(int pin) {
        this.raised = false;
    }

    @Override
    public void onStateChange(PinState state, int pin) {
        switch (state) {
            case SET:
                System.out.println("Pin " + pin + " Set");
                break;
            case OVERSET:
                System.out.println("Pin " + pin + " Overset");
                break;
            case DROPPED:
                System.out.println("Pin " + pin + " Dropped");
                break;
        }
    }

    @Override
    public void tick() {
        this.lastPickProgress = this.pickProgress;
        this.lastRaiseProgress = this.raiseProgress;
        if (this.pickProgress < 1.0F) {
            this.pickProgress += MOVE_SPEED;
            if (this.pickProgress > 1.0F)
                this.pickProgress = 1.0F;
        }
        if (this.pickProgress == 1.0F) {
            if (this.raised) {
                if (this.raiseProgress < 1.0F) {
                    this.raiseProgress += RAISE_SPEED;
                    if (this.raiseProgress > 1.0F)
                        this.raiseProgress = 1.0F;
                    if (this.raiseProgress >= 1.0F)
                        this.context.pick(this.pickIndex);
                }
            } else if (this.raiseProgress > 0.0F) {
                this.raiseProgress -= RAISE_SPEED;
                if (this.raiseProgress < 0.0F)
                    this.raiseProgress = 0.0F;
            }
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        partialTicks = this.minecraft.getFrameTime();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight); // Behind pick

        float x = (this.getRenderPickX(partialTicks) * 18) - 27;
        float y = -this.getRenderPickY(partialTicks) * 13;
        this.blit(matrixStack, this.leftPos + (int) x, this.topPos + this.imageHeight + (int) y, 0, 68, 46, 18);

        this.blit(matrixStack, this.leftPos, this.topPos, 0, this.imageHeight, this.imageWidth, this.imageHeight); // In front of pick
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        this.font.draw(poseStack, this.title, this.titleLabelX, this.titleLabelY, 4210752);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.raised) {
            if (keyCode == GLFW.GLFW_KEY_A || keyCode == GLFW.GLFW_KEY_LEFT) {
                if (this.pickIndex < 1) {
                    // TODO play move too far sound
                    return true;
                }

                this.lastPickIndex = this.getRenderPickX(1.0F);
                this.pickProgress = this.lastPickProgress = 0.0F;
                this.pickIndex--;
                return true;
            }

            if (keyCode == GLFW.GLFW_KEY_D || keyCode == GLFW.GLFW_KEY_RIGHT) {
                if (this.pickIndex >= 4) {
                    // TODO play move too far sound
                    return true;
                }

                this.lastPickIndex = this.getRenderPickX(1.0F);
                this.pickProgress = this.lastPickProgress = 0.0F;
                this.pickIndex++;
                return true;
            }

            if (this.raiseProgress < 1.0F && keyCode == GLFW.GLFW_KEY_SPACE) {
                this.raised = true;
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private float getRenderPickX(float partialTicks) {
        return Mth.lerp(Mth.lerp(partialTicks, this.lastPickProgress, this.pickProgress), this.lastPickIndex, this.pickIndex);
    }

    private float getRenderPickY(float partialTicks) {
        return Mth.lerp(Mth.lerp(partialTicks, this.lastRaiseProgress, this.raiseProgress), 0.0F, 1.0F);
    }
}
