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
    public static final Supplier<SoundEvent> ITEM_LOCK_PLACE = registerSound("item.lock.place");

    private static Supplier<SoundEvent> registerSound(String id) {
        return SOUNDS.register(id, () -> new SoundEvent(new ResourceLocation(Locksmith.MOD_ID, id)));
    }
}
