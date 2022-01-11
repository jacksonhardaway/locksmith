package gg.moonflower.locksmith.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import gg.moonflower.locksmith.common.lockpicking.LockPickingContext;
import gg.moonflower.locksmith.common.menu.LockpickingMenu;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.pollen.api.client.render.ShapeRenderer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;

public class LockPickingScreen extends AbstractContainerScreen<LockpickingMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Locksmith.MOD_ID, "textures/gui/lockpicking.png");
    private static final float MOVE_SPEED = 0.8F;

    private final LockPickingContext context;
    private float lastPickIndex;
    private int pickIndex;
    private boolean raised;
    private boolean deferRaised;
    private float lastPickProgress;
    private float pickProgress;

    public LockPickingScreen(LockpickingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.context = menu.getContext();
        this.imageWidth = 104;
        this.imageHeight = 34;
    }

    public void lowerPick() {
        this.raised = false;
    }

    @Override
    public void tick() {
        this.lastPickProgress = this.pickProgress;

        if (this.context.getState() != LockPickingContext.GameState.RUNNING)
            return;

        if (this.pickProgress < 1.0F) {
            this.pickProgress += MOVE_SPEED;
            if (this.pickProgress > 1.0F)
                this.pickProgress = 1.0F;
            if (this.pickProgress >= 1.0F && this.deferRaised) {
                this.deferRaised = false;
                this.context.pick(this.pickIndex);
            }
        }
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.context.getState() != LockPickingContext.GameState.FAIL)
            this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        if (this.context.getState() == LockPickingContext.GameState.FAIL)
            this.renderBackground(matrixStack);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        partialTicks = this.minecraft.getFrameTime();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);

        VertexConsumer builder = ShapeRenderer.begin();
        ShapeRenderer.drawRectWithTexture(builder, matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight, 128, 128);

        for (int i = 0; i < 5; i++) {
            ShapeRenderer.drawRectWithTexture(builder, matrixStack, this.leftPos + i * 18 + 10, this.topPos + this.imageHeight - 3, 104 + (this.context.getPinState(i) ? 12 : 0), 0, 12, 10, 12, 10, 128, 128);
        }

        float x = (this.getRenderPickIndex(partialTicks) * 18) - 27;
        float y = (this.getPickProgress(partialTicks) == 1.0F || this.context.getPinState((int) this.lastPickIndex) == this.context.getPinState(this.pickIndex)) && (this.raised || this.context.getPinState(this.pickIndex)) ? -2 : 0;
        ShapeRenderer.drawRectWithTexture(builder, matrixStack, this.leftPos + (int) x, this.topPos + this.imageHeight + (int) y + 7, 0, 34 + this.context.getPickDamage() * 18, 46, 18, 46, 18, 128, 128);

        ShapeRenderer.end();
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        this.font.draw(poseStack, this.title, this.titleLabelX, this.titleLabelY, 4210752);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && this.context.getState() == LockPickingContext.GameState.RUNNING && !this.raised) {
            int selectedIndex = (int) ((mouseX - this.leftPos - 11) / 18);
            if (selectedIndex >= 0 && selectedIndex < 5 && this.isHovering(12 + selectedIndex * 18, 32, 8, 10, mouseX, mouseY)) {
                this.lastPickIndex = this.getRenderPickIndex(1.0F);
                this.pickProgress = this.lastPickProgress = 0.0F;
                this.pickIndex = selectedIndex;

                if (!this.context.getPinState(this.pickIndex)) {
                    this.raised = true;
                    if (this.pickProgress == 1.0F) {
                        this.context.pick(this.pickIndex);
                    } else {
                        this.deferRaised = true;
                    }
                }

                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.context.getState() == LockPickingContext.GameState.RUNNING && !this.raised) {
            if (keyCode == GLFW.GLFW_KEY_A || keyCode == GLFW.GLFW_KEY_LEFT) {
                if (this.pickIndex < 1) {
                    // TODO play move too far sound
                    return true;
                }

                this.lastPickIndex = this.getRenderPickIndex(1.0F);
                this.pickProgress = this.lastPickProgress = 0.0F;
                this.pickIndex--;
                return true;
            }

            if (keyCode == GLFW.GLFW_KEY_D || keyCode == GLFW.GLFW_KEY_RIGHT) {
                if (this.pickIndex >= 4) {
                    // TODO play move too far sound
                    return true;
                }

                this.lastPickIndex = this.getRenderPickIndex(1.0F);
                this.pickProgress = this.lastPickProgress = 0.0F;
                this.pickIndex++;
                return true;
            }

            if (keyCode == GLFW.GLFW_KEY_SPACE && !this.context.getPinState(this.pickIndex)) {
                this.raised = true;
                if (this.pickProgress == 1.0F) {
                    this.context.pick(this.pickIndex);
                } else {
                    this.deferRaised = true;
                }

                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private float getPickProgress(float partialTicks) {
        return Mth.lerp(partialTicks, this.lastPickProgress, this.pickProgress);
    }

    private float getRenderPickIndex(float partialTicks) {
        return Mth.lerp(this.getPickProgress(partialTicks), this.lastPickIndex, this.pickIndex);
    }
}
