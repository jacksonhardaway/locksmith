package gg.moonflower.locksmith.common.menu;

import gg.moonflower.locksmith.core.registry.LocksmithBlocks;
import gg.moonflower.locksmith.core.registry.LocksmithMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;

public class LocksmithingTableMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final Container inputSlots = new SimpleContainer(2) {
        @Override
        public void setChanged() {
            super.setChanged();
            LocksmithingTableMenu.this.slotsChanged(this);
        }

        // TODO: make result
    };
    private final Container resultSlots = new SimpleContainer(2) {
        @Override
        public void setChanged() {
            super.setChanged();
            LocksmithingTableMenu.this.slotsChanged(this);
        }
    };

    public LocksmithingTableMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    public LocksmithingTableMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(LocksmithMenus.LOCKSMITHING_TABLE_MENU.get(), containerId);
        this.access = access;

        this.addSlot(new Slot(this.inputSlots, 0, 21, 17));
        this.addSlot(new Slot(this.inputSlots, 1, 21, 54));

        this.addSlot(new Slot(this.resultSlots, 2, 101, 35));
        this.addSlot(new Slot(this.resultSlots, 3, 131, 35));

        for(int x = 0; x < 3; ++x) {
            for(int y = 0; y < 9; ++y) {
                this.addSlot(new Slot(inventory, y + x * 9 + 9, 8 + y * 18, 84 + x * 18));
            }
        }

        for(int hotbarIndex = 0; hotbarIndex < 9; ++hotbarIndex) {
            this.addSlot(new Slot(inventory, hotbarIndex, 8 + hotbarIndex * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, LocksmithBlocks.LOCKSMITHING_TABLE.get());
    }
}
