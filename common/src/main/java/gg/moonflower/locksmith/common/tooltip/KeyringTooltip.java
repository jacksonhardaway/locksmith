package gg.moonflower.locksmith.common.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class KeyringTooltip implements TooltipComponent {

    private final List<ItemStack> items;

    public KeyringTooltip(List<ItemStack> nonNullList) {
        this.items = nonNullList;
    }

    public List<ItemStack> getItems() {
        return this.items;
    }
}
