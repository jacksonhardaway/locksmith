package gg.moonflower.locksmith.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import gg.moonflower.locksmith.common.item.KeyItem;
import gg.moonflower.locksmith.common.menu.LocksmithingTableMenu;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class LocksmithingTableScreen extends AbstractContainerScreen<LocksmithingTableMenu> implements ContainerListener {

    public static final ResourceLocation LOCKSMITHING_LOCATION = new ResourceLocation(Locksmith.MOD_ID, "textures/gui/container/locksmithing_table.png");

    private static final Component MISSING_KEY = new TextComponent("")
            .append(new TranslatableComponent("screen." + Locksmith.MOD_ID + ".locksmithing_table.missing_key"))
            .append("\n")
            .append(new TranslatableComponent("screen." + Locksmith.MOD_ID + ".locksmithing_table.missing_key.tooltip").withStyle(ChatFormatting.GRAY));
    private static final Component MISSING_INPUT = new TextComponent("")
            .append(new TranslatableComponent("screen." + Locksmith.MOD_ID + ".locksmithing_table.missing_input"))
            .append("\n")
            .append(new TranslatableComponent("screen." + Locksmith.MOD_ID + ".locksmithing_table.missing_input.tooltip").withStyle(ChatFormatting.GRAY));
    private static final Component UNORIGINAL_KEY = new TextComponent("")
            .append(new TranslatableComponent("screen." + Locksmith.MOD_ID + ".locksmithing_table.unoriginal_key"))
            .append("\n")
            .append(new TranslatableComponent("screen." + Locksmith.MOD_ID + ".locksmithing_table.unoriginal_key.tooltip").withStyle(ChatFormatting.GRAY));
    private static final Component INVALID_KEY = new TextComponent("")
            .append(new TranslatableComponent("screen." + Locksmith.MOD_ID + ".locksmithing_table.invalid_key"))
            .append("\n")
            .append(new TranslatableComponent("screen." + Locksmith.MOD_ID + ".locksmithing_table.invalid_key.tooltip").withStyle(ChatFormatting.GRAY));

    private ItemStack keyStack = ItemStack.EMPTY;
    private ItemStack inputStack = ItemStack.EMPTY;

    public LocksmithingTableScreen(LocksmithingTableMenu menu, Inventory inventory, Component name) {
        super(menu, inventory, name);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.menu.addSlotListener(this);
    }

    @Override
    public void refreshContainer(AbstractContainerMenu container, NonNullList<ItemStack> inventory) {
        this.slotChanged(container, 0, container.getSlot(0).getItem());
        this.slotChanged(container, 1, container.getSlot(1).getItem());
    }

    @Override
    public void slotChanged(AbstractContainerMenu container, int slot, ItemStack stack) {
        switch (slot) {
            case 0:
                this.keyStack = stack;
                break;
            case 1:
                this.inputStack = stack;
                break;
        }
    }

    @Override
    public void setContainerData(AbstractContainerMenu container, int varToUpdate, int newValue) {
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(LOCKSMITHING_LOCATION);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);

        if (!this.getFailureTooltip().isEmpty())
            this.blit(poseStack, i + 51, j + 33, 176, 0, 27, 21);
    }

    @Override
    protected void renderTooltip(PoseStack poseStack, int x, int y) {
        super.renderTooltip(poseStack, x, y);
        List<? extends FormattedCharSequence> failureTooltip = this.getFailureTooltip();
        if (this.isHovering(55, 37, 20, 13, x, y) && !failureTooltip.isEmpty())
            this.renderTooltip(poseStack, failureTooltip, x, y);
    }

    public List<? extends FormattedCharSequence> getFailureTooltip() {
        if (this.keyStack.isEmpty() && this.inputStack.isEmpty())
            return Collections.emptyList();

        Item key = this.keyStack.getItem();
        Item input = this.inputStack.getItem();
        if (key != LocksmithItems.KEY.get() && key != LocksmithItems.BLANK_KEY.get())
            return this.font.split(MISSING_KEY, 200);

        if (input != LocksmithItems.BLANK_KEY.get() && input != LocksmithItems.BLANK_LOCK.get())
            return this.font.split(MISSING_INPUT, 200);

        if (key == LocksmithItems.KEY.get()) {
            if (!KeyItem.isOriginal(this.keyStack)) {
                return this.font.split(UNORIGINAL_KEY, 200);
            } else if (KeyItem.getLockId(this.keyStack) == null) {
                return this.font.split(INVALID_KEY, 200);
            }
        }

        return Collections.emptyList();
    }
}