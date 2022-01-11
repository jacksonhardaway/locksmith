package gg.moonflower.locksmith.common.menu;

import gg.moonflower.locksmith.common.lockpicking.LockPickingContext;
import gg.moonflower.locksmith.common.network.play.ServerboundLockPickingPacket;
import gg.moonflower.locksmith.core.registry.LocksmithMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * @author Ocelot
 */
public class LockpickingMenu extends AbstractContainerMenu {

    private final LockPickingContext context;

    public LockpickingMenu(int containerId, Inventory inventory) {
        this(containerId, LockPickingContext.dummy());
    }

    public LockpickingMenu(int containerId, LockPickingContext context) {
        super(LocksmithMenus.LOCK_PICKING_MENU.get(), containerId);
        this.context = context;
    }

    public void handle(ServerboundLockPickingPacket pkt) {
        switch (pkt.getEvent()) {
            case PICK:
                this.context.pick(pkt.getSelectedIndex());
                break;
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return !player.isSpectator() && this.context.stillValid(player);
    }

    public LockPickingContext getContext() {
        return context;
    }
}
