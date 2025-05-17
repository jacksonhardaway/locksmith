package dev.hardaway.locksmith.core.registry;

import dev.hardaway.locksmith.core.Locksmith;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LocksmithSounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, Locksmith.MOD_ID);

    public static final Supplier<SoundEvent> UI_LOCKSMITHING_TABLE_TAKE_RESULT = registerSound("ui.locksmithing_table.take_result");
    public static final Supplier<SoundEvent> ITEM_LOCK_PLACE = registerSound("item.lock.place");
    public static final Supplier<SoundEvent> ITEM_LOCK_LOCKED = registerSound("item.lock.locked");

    private static Supplier<SoundEvent> registerSound(String id) {
        return REGISTRY.register(id, () -> SoundEvent.createVariableRangeEvent(Locksmith.path(id)));
    }
}