package dev.hardaway.locksmith.core.data;

import dev.hardaway.locksmith.core.Locksmith;
import dev.hardaway.locksmith.core.registry.LocksmithBlocks;
import dev.hardaway.locksmith.core.registry.LocksmithTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class LocksmithEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public LocksmithEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Locksmith.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(LocksmithTags.Entities.LOCKABLES)
                .addTag(Tags.EntityTypes.MINECARTS)
                .addTag(Tags.EntityTypes.BOATS)
        ;
    }
}
