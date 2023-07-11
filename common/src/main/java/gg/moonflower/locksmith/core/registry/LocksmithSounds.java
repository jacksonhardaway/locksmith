package gg.moonflower.locksmith.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import gg.moonflower.locksmith.core.Locksmith;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class LocksmithSounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Locksmith.MOD_ID, Registry.SOUND_EVENT_REGISTRY);

    public static final RegistrySupplier<SoundEvent> UI_LOCKSMITHING_TABLE_TAKE_RESULT = registerSound("ui.locksmithing_table.take_result");
    public static final RegistrySupplier<SoundEvent> LOCK_PICKING_SET = registerSound("lock_picking.set");
    public static final RegistrySupplier<SoundEvent> LOCK_PICKING_SUCCESS = registerSound("lock_picking.success");
    public static final RegistrySupplier<SoundEvent> LOCK_PICKING_TOO_FAR = registerSound("lock_picking.too_far");
    public static final RegistrySupplier<SoundEvent> LOCK_PICKING_TOO_NEAR = registerSound("lock_picking.too_near");
    public static final RegistrySupplier<SoundEvent> LOCK_PICKING_FAIL = registerSound("lock_picking.fail");
    public static final RegistrySupplier<SoundEvent> LOCK_PICKING_OVERSET = registerSound("lock_picking.overset");
    public static final RegistrySupplier<SoundEvent> LOCK_PICKING_PINS_DROP = registerSound("lock_picking.pins_drop");
    public static final RegistrySupplier<SoundEvent> ITEM_LOCK_PLACE = registerSound("item.lock.place");
    public static final RegistrySupplier<SoundEvent> ITEM_LOCK_LOCKED = registerSound("item.lock.locked");

    private static RegistrySupplier<SoundEvent> registerSound(String id) {
        return REGISTRY.register(id, () -> new SoundEvent(new ResourceLocation(Locksmith.MOD_ID, id)));
    }
}
