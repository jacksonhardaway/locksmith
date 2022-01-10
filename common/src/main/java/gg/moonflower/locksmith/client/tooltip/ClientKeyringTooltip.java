package gg.moonflower.locksmith.client.tooltip;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import gg.moonflower.locksmith.common.tooltip.KeyringTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;

public class ClientKeyringTooltip implements ClientTooltipComponent {

    private static final int PADDING = 2;

    private final KeyringTooltip tooltip;
    private final List<Component> tooltipList;

    public ClientKeyringTooltip(KeyringTooltip tooltip) {
        this.tooltip = tooltip;
        this.tooltipList = new ArrayList<>();
    }

    @Override
    public int getHeight() {
        List<ItemStack> stacks = this.tooltip.getItems();
        int y = 0;
        for (ItemStack stack : stacks) {
            this.updateTooltip(stack);
            y += Math.max(this.tooltipList.size() * (PADDING + 9), 18);
        }
        return y + Math.max(0, stacks.size() - 1) * PADDING;
    }

    @Override
    public int getWidth(Font font) {
        return 18 + PADDING + this.tooltip.getItems().stream().flatMapToInt(stack -> {
            this.updateTooltip(stack);
            return this.tooltipList.stream().mapToInt(font::width);
        }).max().orElse(0);
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix4f, MultiBufferSource.BufferSource bufferSource) {
        List<ItemStack> stacks = this.tooltip.getItems();
        for (ItemStack stack : stacks) {
            this.updateTooltip(stack);

            for (int i = 0; i < this.tooltipList.size(); i++)
                font.drawInBatch(this.tooltipList.get(i), x + 18 + PADDING, y + i * (PADDING + 9), -1, true, matrix4f, bufferSource, false, 0, 15728880);

            y += Math.max(this.tooltipList.size() * (PADDING + 9) + PADDING, 18 + PADDING);
        }
    }

    @Override
    public void renderImage(Font font, int x, int y, PoseStack poseStack, ItemRenderer itemRenderer, int z) {
        List<ItemStack> stacks = this.tooltip.getItems();
        for (ItemStack stack : stacks) {
            this.updateTooltip(stack);

            itemRenderer.renderAndDecorateItem(stack, x, y, z);
            itemRenderer.renderGuiItemDecorations(font, stack, x, y);

            y += Math.max(this.tooltipList.size() * (PADDING + 9) + PADDING, 18 + PADDING);
        }
    }

    private void updateTooltip(ItemStack stack) {
        this.tooltipList.clear();

        MutableComponent name = new TextComponent("").append(stack.getHoverName()).withStyle(stack.getRarity().color);
        if (stack.hasCustomHoverName())
            name.withStyle(ChatFormatting.ITALIC);
        this.tooltipList.add(name);
        stack.getItem().appendHoverText(stack, Minecraft.getInstance().level, this.tooltipList, Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
    }
}
