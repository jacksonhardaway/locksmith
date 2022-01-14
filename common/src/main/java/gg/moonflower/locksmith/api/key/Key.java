package gg.moonflower.locksmith.api.key;

import gg.moonflower.locksmith.api.lock.AbstractLock;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

/**
 * A key is an item that can be used to unlock {@link AbstractLock}.
 *
 * @author Ocelot
 * @since 1.0.0
 */
public interface Key {

    /**
     * Checks if this key can unlock the specified lock.
     *
     * @param id    The id of lock to check
     * @param stack The key stack
     * @return Whether that key can unlock that lock
     */
    boolean matchesLock(UUID id, ItemStack stack);
}
