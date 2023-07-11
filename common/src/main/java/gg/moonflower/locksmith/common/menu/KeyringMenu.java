package gg.moonflower.locksmith.common.menu;

import com.mojang.datafixers.util.Pair;
import gg.moonflower.locksmith.common.item.KeyringItem;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import gg.moonflower.locksmith.core.registry.LocksmithMenus;
import gg.moonflower.pollen.api.container.util.v1.QuickMoveHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * @author Ocelot
 */
public class KeyringMenu extends AbstractContainerMenu {

    private static final QuickMoveHelper MOVE_HELPER = new QuickMoveHelper().
            add(0, 4, 4, 36, true). // to Inventory
                    add(4, 36, 0, 4, false); // from Inventory

    private final Inventory inventory;
    private final Container keyringInventory;
    private final int keyringIndex;

    public KeyringMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, -1);
    }

    public KeyringMenu(int containerId, Inventory inventory, int keyringIndex) {
        super(LocksmithMenus.KEYRING_MENU.get(), containerId);
        this.keyringInventory = keyringIndex == -1 ? new SimpleContainer(KeyringItem.MAX_KEYS) : new KeyringContainer(inventory, keyringIndex);
        this.keyringIndex = keyringIndex;
        this.inventory = inventory;

        for (int i = 0; i < 4; i++)
            this.addSlot(new Slot(this.keyringInventory, i, 53 + i * 18, 20) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() == LocksmithItems.KEY.get();
                }

                @Override
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, LocksmithingTableMenu.EMPTY_SLOT_KEY);
                }
            });

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, y * 18 + 51) {
                    @Override
                    public boolean mayPickup(Player player) {
                        return this.getItem().getItem() != LocksmithItems.KEYRING.get();
                    }
                });
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 109) {
                @Override
                public boolean mayPickup(Player player) {
                    return this.getItem().getItem() != LocksmithItems.KEYRING.get();
                }
            });
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.keyringInventory.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!this.stillValid(player) || this.keyringIndex == -1)
            return;

        int index = -1;
        for (int i = 0; i < this.keyringInventory.getContainerSize(); i++) {
            ItemStack stack = this.keyringInventory.getItem(i);
            if (!stack.isEmpty()) {
                if (index != -1)
                    return;
                index = i;
            }
        }

        this.inventory.setItem(this.keyringIndex, ItemStack.EMPTY);
        this.clearContainer(player, this.keyringInventory);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return MOVE_HELPER.quickMoveStack(this, player, slot);
    }
}
