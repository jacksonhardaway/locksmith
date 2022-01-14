package gg.moonflower.locksmith.core.registry;

import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.pollen.api.registry.PollinatedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class LocksmithSounds {
    public static final PollinatedRegistry<SoundEvent> SOUNDS = PollinatedRegistry.create(Registry.SOUND_EVENT, Locksmith.MOD_ID);

    public static final Supplier<SoundEvent> UI_LOCKSMITHING_TABLE_TAKE_RESULT = registerSound("ui.locksmithing_table.take_result");
    public static final Supplier<SoundEvent> LOCK_PICKING_SET = registerSound("lock_picking.set");
    public static final Supplier<SoundEvent> LOCK_PICKING_SUCCESS = registerSound("lock_picking.success");
    public static final Supplier<SoundEvent> LOCK_PICKING_TOO_FAR = registerSound("lock_picking.too_far");
    public static final Supplier<SoundEvent> LOCK_PICKING_TOO_NEAR = registerSound("lock_picking.too_near");
    public static final Supplier<SoundEvent> LOCK_PICKING_FAIL = registerSound("lock_picking.fail");
    public static final Supplier<SoundEvent> LOCK_PICKING_OVERSET = registerSound("lock_picking.overset");
    public static final Supplier<SoundEvent> LOCK_PICKING_PINS_DROP = registerSound("lock_picking.pins_drop");
    public static final Supplier<SoundEvent> ITEM_LOCK_PLACE = registerSound("item.lock.place");
    public static final Supplier<SoundEvent> ITEM_LOCK_LOCKED = registerSound("item.lock.locked");

    private static Supplier<SoundEvent> registerSound(String id) {
        return SOUNDS.register(id, () -> new SoundEvent(new ResourceLocation(Locksmith.MOD_ID, id)));
    }
}
