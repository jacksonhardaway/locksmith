package gg.moonflower.locksmith.common.menu;

import com.mojang.datafixers.util.Pair;
import gg.moonflower.locksmith.common.item.KeyItem;
import gg.moonflower.locksmith.common.recipe.LocksmithingRecipe;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.locksmith.core.registry.LocksmithBlocks;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import gg.moonflower.locksmith.core.registry.LocksmithMenus;
import gg.moonflower.locksmith.core.registry.LocksmithRecipes;
import gg.moonflower.locksmith.core.registry.LocksmithSounds;
import gg.moonflower.pollen.api.util.QuickMoveHelper;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class LocksmithingTableMenu extends AbstractContainerMenu {

    public static final ResourceLocation EMPTY_SLOT_KEY = new ResourceLocation(Locksmith.MOD_ID, "item/empty_locksmithing_table_slot_key");
    private static final QuickMoveHelper MOVE_HELPER = new QuickMoveHelper().
            add(0, 4, 4, 36, true). // to Inventory
                    add(4, 36, 0, 2, false); // from Inventory

    private final ContainerLevelAccess access;
    private final Player player;
    private final CraftingContainer craftSlots;
    private final LocksmithResultContainer resultSlots = new LocksmithResultContainer(2);
    private final Slot keyInputSlot;
    private final Slot inputSlot;
    private long lastSoundTime;
    private boolean partialTake;
    private boolean pendingTake;

    public LocksmithingTableMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    public LocksmithingTableMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(LocksmithMenus.LOCKSMITHING_TABLE_MENU.get(), containerId);
        this.player = inventory.player;
        this.access = access;

        this.craftSlots = new CraftingContainer(this, 1, 2);

        this.keyInputSlot = this.addSlot(new Slot(this.craftSlots, 0, 21, 17) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() == LocksmithItems.KEY.get() || stack.getItem() == LocksmithItems.BLANK_KEY.get();
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_SLOT_KEY);
            }
        });
        this.inputSlot = this.addSlot(new Slot(this.craftSlots, 1, 21, 54));

        this.addSlot(new LocksmithingResultSlot(0, 101, 35));
        this.addSlot(new LocksmithingResultSlot(1, 131, 35));

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
        this.access.execute((level, blockPos) -> this.clearContainer(player, this.craftSlots));
        if (this.pendingTake)
            this.access.execute((level, blockPos) -> this.clearContainer(player, this.resultSlots));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return MOVE_HELPER.quickMoveStack(this, player, slot);
    }

    @Override
    public void slotsChanged(Container inventory) {
        super.slotsChanged(inventory);

        LocksmithingTableMenu.this.partialTake = LocksmithingTableMenu.this.resultSlots.getItem(0).isEmpty() ^ LocksmithingTableMenu.this.resultSlots.getItem(1).isEmpty();
//        if (inventory == this.craftSlots)
        this.createResult();
    }

    protected static void slotChangedCraftingGrid(int containerId, Level level, Player player, CraftingContainer container, LocksmithResultContainer resultContainer) {
        if (!level.isClientSide()) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            ItemStack leftResult = ItemStack.EMPTY;
            ItemStack rightResult = ItemStack.EMPTY;
            Optional<LocksmithingRecipe> optional = level.getServer().getRecipeManager().getRecipeFor(LocksmithRecipes.LOCKSMITHING_TYPE.get(), container, level);
            if (optional.isPresent()) {
                LocksmithingRecipe recipe = optional.get();
                if (resultContainer.setRecipeUsed(level, serverPlayer, recipe)) {
                    leftResult = recipe.assemble(container);
                    rightResult = recipe.getSecondAssembledResult();
                }
            }

            resultContainer.setItem(0, leftResult);
            resultContainer.setItem(1, rightResult);
            serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(containerId, 0, 2, leftResult));
            serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(containerId, 0, 3, rightResult));
        }
    }

    private void createResult() {
        if (this.partialTake)
            return;

        this.resultSlots.setItem(0, ItemStack.EMPTY);
        this.resultSlots.setItem(1, ItemStack.EMPTY);
//        if (!this.isValid())
//            return;

        this.pendingTake = false;
        this.access.execute((level, blockPos) -> slotChangedCraftingGrid(this.containerId, level, this.player, this.craftSlots, this.resultSlots));


//        ItemStack keyStack = this.keyInputSlot.getItem();
//        ItemStack inputStack = this.inputSlot.getItem();
//        boolean blank = keyStack.getItem() == LocksmithItems.BLANK_KEY.get();
//
//        ItemStack result;
//        if (inputStack.getItem() == LocksmithItems.BLANK_LOCK.get()) {
//            result = new ItemStack(LocksmithItems.LOCK.get());
//        } else if (inputStack.getItem() == LocksmithItems.BLANK_KEY.get()) {
//            result = new ItemStack(LocksmithItems.KEY.get());
//        } else if (inputStack.getItem() == LocksmithItems.BLANK_LOCK_BUTTON.get()) {
//            result = new ItemStack(LocksmithBlocks.LOCK_BUTTON.get());
//        } else return;
//
//        if (inputStack.hasCustomHoverName())
//            result.setHoverName(keyStack.getHoverName());
//
//        if (blank && (inputStack.getItem() == LocksmithItems.BLANK_LOCK.get() || inputStack.getItem() == LocksmithItems.BLANK_LOCK_BUTTON.get())) {
//            UUID id = UUID.randomUUID();
//
//            ItemStack newKey = new ItemStack(LocksmithItems.KEY.get());
//            if (keyStack.hasCustomHoverName())
//                newKey.setHoverName(keyStack.getHoverName());
//            KeyItem.setOriginal(newKey, true);
//            KeyItem.setLockId(newKey, id);
//            KeyItem.setLockId(result, id);
//
//            this.resultSlots.setItem(0, newKey);
//            this.resultSlots.setItem(1, result);
//        } else if (!blank && this.isValidInputItem(inputStack)) {
//            UUID id = KeyItem.getLockId(keyStack);
//            if (id == null)
//                return;
//
//            KeyItem.setLockId(result, id);
//            this.resultSlots.setItem(0, keyStack.copy());
//            this.resultSlots.setItem(1, result);
//        }
    }

    private boolean isValid() {
        if (!this.keyInputSlot.hasItem() && !this.inputSlot.hasItem())
            return false;

        Item key = this.keyInputSlot.getItem().getItem();
        if (key != LocksmithItems.KEY.get() && key != LocksmithItems.BLANK_KEY.get())
            return false;
        if (!this.isValidInputItem(this.inputSlot.getItem()))
            return false;

        return key != LocksmithItems.KEY.get() || (KeyItem.isOriginal(this.keyInputSlot.getItem()) && KeyItem.getLockId(this.keyInputSlot.getItem()) != null);
    }

    private boolean isValidInputItem(ItemStack stack) {
        return stack.getItem() == LocksmithItems.BLANK_LOCK.get() || stack.getItem() == LocksmithItems.BLANK_KEY.get() || stack.getItem() == LocksmithItems.BLANK_LOCK_BUTTON.get();
    }

    class LocksmithingResultSlot extends ResultSlot {

        public LocksmithingResultSlot(int index, int x, int y) {
            super(LocksmithingTableMenu.this.player, LocksmithingTableMenu.this.craftSlots, LocksmithingTableMenu.this.resultSlots, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            if (LocksmithingTableMenu.this.pendingTake) {
                super.onTake(player, stack);
                return;
            }

            if (!LocksmithingTableMenu.this.keyInputSlot.getItem().isEmpty() && !LocksmithingTableMenu.this.inputSlot.getItem().isEmpty())
                LocksmithingTableMenu.this.pendingTake = true;
            LocksmithingTableMenu.this.keyInputSlot.remove(1);
            LocksmithingTableMenu.this.inputSlot.remove(1);

            LocksmithingTableMenu.this.access.execute((level, pos) -> {
                long l = level.getGameTime();
                if (LocksmithingTableMenu.this.lastSoundTime != l) {
                    level.playSound(null, pos, LocksmithSounds.UI_LOCKSMITHING_TABLE_TAKE_RESULT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    LocksmithingTableMenu.this.lastSoundTime = l;
                }
            });

            super.onTake(player, stack);
        }
    }
}
