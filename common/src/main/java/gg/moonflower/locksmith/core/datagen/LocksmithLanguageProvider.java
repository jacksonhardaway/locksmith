package gg.moonflower.locksmith.core.datagen;

import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.locksmith.core.registry.LocksmithBlocks;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import gg.moonflower.pollen.api.datagen.provider.PollinatedLanguageProvider;
import gg.moonflower.pollen.api.util.PollinatedModContainer;
import net.minecraft.data.DataGenerator;

public class LocksmithLanguageProvider extends PollinatedLanguageProvider {

    public LocksmithLanguageProvider(DataGenerator generator, PollinatedModContainer container) {
        super(generator, container, "en_us");
    }

    @Override
    protected void registerTranslations() {
        this.addItem(LocksmithItems.BLANK_LOCK, "Blank Lock");
        this.addItem(LocksmithItems.BLANK_KEY, "Blank Key");
        this.addItem(LocksmithItems.BLANK_LOCK_BUTTON, "Blank Lock Button");
        this.addItem(LocksmithItems.LOCK, "Lock");
        this.add(LocksmithItems.LOCK.get().getDescriptionId() + ".key", "%s");
        this.addItem(LocksmithItems.KEY, "Key");
        this.add(LocksmithItems.KEY.get().getDescriptionId() + ".original", "Original");
        this.add(LocksmithItems.KEY.get().getDescriptionId() + ".copy", "Copy");
        this.addItem(LocksmithItems.KEYRING, "Keyring");
        this.addItem(LocksmithItems.LOCKPICK, "Lockpick");
        this.addBlock(LocksmithBlocks.LOCKSMITHING_TABLE, "Locksmithing Table");
        this.addBlock(LocksmithBlocks.LOCK_BUTTON, "Lock Button");
        this.add("stat." + Locksmith.MOD_ID + ".interact_with_locksmithing_table", "Interactions with Locksmithing Table");
        this.add("stat." + Locksmith.MOD_ID + ".pick_lock", "Locks picked");
        this.add("container." + Locksmith.MOD_ID + ".locksmithing_table", "Locksmithing Table");
        this.add("container." + Locksmith.MOD_ID + ".lock_picking", "Lock Picking");
        this.add("container." + Locksmith.MOD_ID + ".locked", "Locked %s");
        this.add("screen." + Locksmith.MOD_ID + ".locksmithing_table.missing_key", "Key Missing");
        this.add("screen." + Locksmith.MOD_ID + ".locksmithing_table.missing_key.tooltip", "You must provide a key in the top slot.");
        this.add("screen." + Locksmith.MOD_ID + ".locksmithing_table.missing_input", "Blank Item Missing");
        this.add("screen." + Locksmith.MOD_ID + ".locksmithing_table.missing_input.tooltip", "You must provide a blank key or lock in the bottom slot to create or copy the key onto.");
        this.add("screen." + Locksmith.MOD_ID + ".locksmithing_table.unoriginal_key", "Copied Key");
        this.add("screen." + Locksmith.MOD_ID + ".locksmithing_table.unoriginal_key.tooltip", "Only the original key can create copies.");
        this.add("screen." + Locksmith.MOD_ID + ".locksmithing_table.invalid_key", "Invalid Key");
        this.add("screen." + Locksmith.MOD_ID + ".locksmithing_table.invalid_key.tooltip", "The key you provided is missing a lock id. This usually happens when a key is spawned in and not from a blank key in the Locksmithing Table.");
        this.add("lock." + Locksmith.MOD_ID + ".locked", "It's locked.");
        this.add("subtitles." + Locksmith.MOD_ID + ".item.lock.place", "Lock Placed");
        this.add("subtitles." + Locksmith.MOD_ID + ".item.lock.locked", "Lock Locked");
    }
}
