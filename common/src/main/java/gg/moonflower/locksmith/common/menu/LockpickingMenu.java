package gg.moonflower.locksmith.common.menu;

import gg.moonflower.locksmith.common.lockpicking.LockPickingContext;
import gg.moonflower.locksmith.core.registry.LocksmithMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;

/**
 * @author Ocelot
 */
public class LockpickingMenu extends AbstractContainerMenu {

    private final LockPickingContext context;

    public LockpickingMenu(int containerId, Inventory inventory) {
        this(containerId, LockPickingContext.client(containerId));
    }

    public LockpickingMenu(int containerId, LockPickingContext context) {
        super(LocksmithMenus.LOCK_PICKING_MENU.get(), containerId);
        this.context = context;
        this.addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return context.getPickDamage();
            }

            @Override
            public void set(int value) {
                context.setPickDamage(value);
            }
        });
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (this.context.getState() == LockPickingContext.GameState.RUNNING && id >= 0 && id < 5) {
            this.context.pick(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean stillValid(Player player) {
        return !player.isSpectator() && this.context.stillValid(player);
    }

    public LockPickingContext getContext() {
        return context;
    }
}
