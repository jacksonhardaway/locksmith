package dev.hardaway.locksmith.core.data.loot;

import dev.hardaway.locksmith.core.registry.LocksmithBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class LocksmithBlockLootProvider extends BlockLootSubProvider {
    public LocksmithBlockLootProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return LocksmithBlocks.REGISTRY.getEntries()
                .stream()
                .map(e -> (Block) e.value())
                .toList();
    }

    @Override
    protected void generate() {
        this.dropSelf(LocksmithBlocks.LOCKSMITHING_TABLE.get());
    }
}
