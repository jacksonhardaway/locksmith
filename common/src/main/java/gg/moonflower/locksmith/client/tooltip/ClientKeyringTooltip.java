package gg.moonflower.locksmith.client.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import gg.moonflower.locksmith.common.item.KeyItem;
import gg.moonflower.locksmith.common.item.KeyringItem;
import gg.moonflower.locksmith.common.tooltip.KeyringTooltip;
import gg.moonflower.locksmith.core.Locksmith;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.IntStream;

public class ClientKeyringTooltip implements ClientTooltipComponent {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(Locksmith.MOD_ID, "textures/gui/container/keyring_tooltip.png");

    private final KeyringTooltip tooltip;

    public ClientKeyringTooltip(KeyringTooltip tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public int getHeight() {
        return 83;
    }

    @Override
    public int getWidth(Font font) {
        return 22 + this.tooltip.getItems().stream().mapToInt(stack -> Math.max(font.width(stack.getHoverName()), font.width(KeyItem.isOriginal(stack) ? KeyItem.ORIGINAL : KeyItem.COPY))).max().orElse(0);
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix4f, MultiBufferSource.BufferSource bufferSource) {
        List<ItemStack> stacks = this.tooltip.getItems();
        for (int i = 0; i < Math.min(KeyringItem.MAX_KEYS, stacks.size()); i++) {
            ItemStack stack = stacks.get(i);
            font.drawInBatch(stack.getHoverName(), x + 22, y + i * 20 + 2, -1, true, matrix4f, bufferSource, false, 0, 15728880);
            font.drawInBatch(KeyItem.isOriginal(stack) ? KeyItem.ORIGINAL : KeyItem.COPY, x + 22, y + i * 20 + 11, -1, true, matrix4f, bufferSource, false, 0, 15728880);
        }
    }

    @Override
    public void renderImage(Font font, int x, int y, PoseStack poseStack, ItemRenderer itemRenderer, int z) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
        GuiComponent.blit(poseStack, x, y, z, 0, 0, 20, 81, 128, 128);

        List<ItemStack> stacks = this.tooltip.getItems();
        for (int i = 0; i < Math.min(KeyringItem.MAX_KEYS, stacks.size()); i++) {
            ItemStack stack = stacks.get(i);
            itemRenderer.renderAndDecorateItem(stack, x + 2, y + i * 20 + 2, z);
            itemRenderer.renderGuiItemDecorations(font, stack, x + 2, y + i * 20 + 2);

            if (i == 0)
                AbstractContainerScreen.renderSlotHighlight(poseStack, x + 2, y + 2, z);
        }
    }
}
