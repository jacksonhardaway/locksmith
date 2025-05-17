package dev.hardaway.locksmith.core.data;

import dev.hardaway.locksmith.core.Locksmith;
import dev.hardaway.locksmith.core.registry.LocksmithSounds;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class LocksmithSoundDefinitionsProvider extends SoundDefinitionsProvider {

    public LocksmithSoundDefinitionsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, Locksmith.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        this.add(LocksmithSounds.ITEM_LOCK_PLACE, SoundDefinition.definition()
                .subtitle("subtitles." + Locksmith.MOD_ID + ".item.lock.place")
                .with(SoundDefinition.Sound.sound(Locksmith.path("item/lock/place"), SoundDefinition.SoundType.SOUND)));
        this.add(LocksmithSounds.ITEM_LOCK_LOCKED, SoundDefinition.definition()
                .subtitle("subtitles." + Locksmith.MOD_ID + ".item.lock.locked")
                .with(SoundDefinition.Sound.sound(SoundEvents.CHEST_LOCKED.getLocation(), SoundDefinition.SoundType.EVENT)));
        this.add(LocksmithSounds.UI_LOCKSMITHING_TABLE_TAKE_RESULT, SoundDefinition.definition()
                .with(SoundDefinition.Sound.sound(Locksmith.path("ui/locksmithing_table/take_result"), SoundDefinition.SoundType.SOUND)));
    }
}
