package dev.hardaway.locksmith.common.item;

import dev.hardaway.locksmith.common.component.Keychain;
import dev.hardaway.locksmith.core.registry.LocksmithComponents;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class KeyringItem extends Item {
    public KeyringItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean overrideOtherStackedOnMe(
            ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access
    ) {
        if (stack.getCount() != 1 || action != ClickAction.SECONDARY || !slot.allowModification(player))
            return false;

        Keychain keychain = stack.getOrDefault(LocksmithComponents.KEYCHAIN, Keychain.EMPTY);
        Keychain.Mutable mutable = new Keychain.Mutable(keychain);
        if (other.isEmpty()) {
            ItemStack itemstack = mutable.removeOne();
            if (itemstack != null) {
                // TODO: remove sound
                access.set(itemstack);
            }
        } else {
            if (mutable.tryInsert(other) > 0) {
                // TODO: insert sound
            }
        }

        stack.set(LocksmithComponents.KEYCHAIN, mutable.toImmutable());
        return true;
    }
}
