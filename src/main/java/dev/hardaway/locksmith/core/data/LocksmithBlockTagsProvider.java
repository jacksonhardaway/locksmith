package dev.hardaway.locksmith.core.data;

import dev.hardaway.locksmith.core.Locksmith;
import dev.hardaway.locksmith.core.registry.LocksmithBlocks;
import dev.hardaway.locksmith.core.registry.LocksmithTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class LocksmithBlockTagsProvider extends BlockTagsProvider {
    public LocksmithBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Locksmith.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(LocksmithBlocks.LOCKSMITHING_TABLE.get());

        this.tag(LocksmithTags.Blocks.LOCKABLES)
                .addTag(Tags.Blocks.CHESTS)
                .addTag(Tags.Blocks.BARRELS)
                .addTag(Tags.Blocks.PLAYER_WORKSTATIONS_FURNACES)
                .addTag(BlockTags.DOORS)
                .addTag(BlockTags.TRAPDOORS)
                .addTag(BlockTags.FENCE_GATES)
                .add(Blocks.BREWING_STAND)
                .add(Blocks.DISPENSER)
                .add(Blocks.DROPPER)
                .add(Blocks.CRAFTER)
        ;
    }
}
