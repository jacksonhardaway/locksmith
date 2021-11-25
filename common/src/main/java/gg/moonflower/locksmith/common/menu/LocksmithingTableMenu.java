package gg.moonflower.locksmith.common.menu;

import com.mojang.datafixers.util.Pair;
import gg.moonflower.locksmith.common.item.KeyItem;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.locksmith.core.registry.LocksmithBlocks;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import gg.moonflower.locksmith.core.registry.LocksmithMenus;
import gg.moonflower.locksmith.core.registry.LocksmithSounds;
import gg.moonflower.pollen.api.util.QuickMoveHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class LocksmithingTableMenu extends AbstractContainerMenu {
    public static final ResourceLocation EMPTY_SLOT_KEY = new ResourceLocation(Locksmith.MOD_ID, "item/empty_locksmithing_table_slot_key");
    private static final QuickMoveHelper MOVE_HELPER = new QuickMoveHelper().
            add(0, 4, 4, 36, false). // to Inventory
                    add(4, 36, 0, 2, false); // from Inventory

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

    private final Slot keyInputSlot;
    private final Slot inputSlot;
    private long lastSoundTime;

    public LocksmithingTableMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    public LocksmithingTableMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(LocksmithMenus.LOCKSMITHING_TABLE_MENU.get(), containerId);
        this.access = access;

        this.keyInputSlot = this.addSlot(new Slot(this.inputSlots, 0, 21, 17) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() == LocksmithItems.KEY.get() || stack.getItem() == LocksmithItems.BLANK_KEY.get();
            }

            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_SLOT_KEY);
            }
        });
        this.inputSlot = this.addSlot(new Slot(this.inputSlots, 1, 21, 54) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() == LocksmithItems.BLANK_KEY.get() || stack.getItem() == LocksmithItems.BLANK_LOCK.get();
            }
        });

        this.addSlot(new ResultSlot(0, 101, 35));
        this.addSlot(new ResultSlot(1, 131, 35));

        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 9; ++y) {
                this.addSlot(new Slot(inventory, y + x * 9 + 9, 8 + y * 18, 84 + x * 18));
            }
        }

        for (int hotbarIndex = 0; hotbarIndex < 9; ++hotbarIndex) {
            this.addSlot(new Slot(inventory, hotbarIndex, 8 + hotbarIndex * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, LocksmithBlocks.LOCKSMITHING_TABLE.get());
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> this.clearContainer(player, level, this.inputSlots));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return MOVE_HELPER.performAction(this, slot);
    }

    @Override
    public void slotsChanged(Container inventory) {
        super.slotsChanged(inventory);
        if (inventory == this.inputSlots) {
            this.createResult();
        }
    }

    private void createResult() {
        this.resultSlots.setItem(0, ItemStack.EMPTY);
        this.resultSlots.setItem(1, ItemStack.EMPTY);
        if ((!KeyItem.isOriginal(this.keyInputSlot.getItem()) && this.keyInputSlot.getItem().getItem() != LocksmithItems.BLANK_KEY.get()) || !this.inputSlot.hasItem())
            return;

        System.out.println("Valid!");
    }

    class ResultSlot extends Slot {
        public ResultSlot(int index, int x, int y) {
            super(LocksmithingTableMenu.this.resultSlots, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack onTake(Player player, ItemStack stack) {
            LocksmithingTableMenu.this.keyInputSlot.remove(1);
            LocksmithingTableMenu.this.inputSlot.remove(1);

            LocksmithingTableMenu.this.createResult();
            LocksmithingTableMenu.this.broadcastChanges();

            LocksmithingTableMenu.this.access.execute((level, pos) -> {
                long l = level.getGameTime();
                if (LocksmithingTableMenu.this.lastSoundTime != l) {
                    level.playSound(null, pos, LocksmithSounds.UI_LOCKSMITHING_TABLE_TAKE_RESULT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    LocksmithingTableMenu.this.lastSoundTime = l;
                }

            });
            return super.onTake(player, stack);
        }
    }
}
