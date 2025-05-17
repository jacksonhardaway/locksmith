package dev.hardaway.locksmith.core.data;

import dev.hardaway.locksmith.core.Locksmith;
import dev.hardaway.locksmith.core.registry.LocksmithBlocks;
import dev.hardaway.locksmith.core.registry.LocksmithItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class LocksmithItemModelProvider extends ItemModelProvider {
    public LocksmithItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Locksmith.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.basicItem(LocksmithItems.BLANK_KEY.get());
        this.basicItem(LocksmithItems.BLANK_LOCK.get());
        this.basicItem(LocksmithItems.KEY.get());
        this.basicItem(LocksmithItems.LOCK.get());
        this.simpleBlockItem(LocksmithBlocks.LOCKSMITHING_TABLE.get());
    }
}
