package gg.moonflower.locksmith.core.datagen;

import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.locksmith.core.registry.LocksmithSounds;
import gg.moonflower.pollen.api.datagen.SoundDefinitionBuilder;
import gg.moonflower.pollen.api.datagen.provider.PollinatedSoundProvider;
import gg.moonflower.pollen.api.util.PollinatedModContainer;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

import java.util.function.Consumer;

public class LocksmithSoundProvider extends PollinatedSoundProvider {

    public LocksmithSoundProvider(DataGenerator generator, PollinatedModContainer container) {
        super(generator, container);
    }

    @Override
    protected void registerSounds(Consumer<SoundDefinitionBuilder> registry) {
        registry.accept(SoundDefinitionBuilder.forSound(LocksmithSounds.UI_LOCKSMITHING_TABLE_TAKE_RESULT));
        registry.accept(SoundDefinitionBuilder.forSound(LocksmithSounds.LOCK_PICKING_SET));
        registry.accept(SoundDefinitionBuilder.forSound(LocksmithSounds.LOCK_PICKING_SUCCESS));
        registry.accept(SoundDefinitionBuilder.forSound(LocksmithSounds.LOCK_PICKING_TOO_FAR));
        registry.accept(SoundDefinitionBuilder.forSound(LocksmithSounds.LOCK_PICKING_TOO_NEAR));
        registry.accept(SoundDefinitionBuilder.forSound(LocksmithSounds.LOCK_PICKING_FAIL));
        registry.accept(SoundDefinitionBuilder.forSound(LocksmithSounds.LOCK_PICKING_OVERSET));
        registry.accept(SoundDefinitionBuilder.forSound(LocksmithSounds.LOCK_PICKING_PINS_DROP));
        registry.accept(SoundDefinitionBuilder.forSound(LocksmithSounds.ITEM_LOCK_PLACE).subtitle("subtitles." + Locksmith.MOD_ID + ".item.lock.place"));
        registry.accept(SoundDefinitionBuilder.definition(LocksmithSounds.ITEM_LOCK_LOCKED).subtitle("subtitles." + Locksmith.MOD_ID + ".item.lock.locked").addSound(SoundDefinitionBuilder.sound(SoundEvents.CHEST_LOCKED.getLocation()).type(SoundDefinitionBuilder.SoundType.EVENT)));
    }
}
