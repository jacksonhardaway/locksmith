package dev.hardaway.locksmith.core.data;

import dev.hardaway.locksmith.core.Locksmith;
import dev.hardaway.locksmith.core.registry.LocksmithBlocks;
import dev.hardaway.locksmith.core.registry.LocksmithItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class LocksmithLanguageProvider extends LanguageProvider {
    public LocksmithLanguageProvider(PackOutput output) {
        super(output, Locksmith.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.addItem(LocksmithItems.BLANK_KEY, "Blank Key");
        this.addItem(LocksmithItems.BLANK_LOCK, "Blank Lock");
        this.addItem(LocksmithItems.KEY, "Key");
        this.addItem(LocksmithItems.LOCK, "Lock");
        this.addBlock(LocksmithBlocks.LOCKSMITHING_TABLE, "Locksmithing Table");

        this.add("item.locksmith.lock.requires_key", "Requires %s");
        this.add("item.locksmith.key.original_key", "Original");
        this.add("item.locksmith.key.copied_key", "%s #%s");

    }
}
