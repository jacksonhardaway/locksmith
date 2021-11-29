package gg.moonflower.locksmith.core.registry.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;

public class LocksmithTagsImpl {
    public static Tag<Block> bindBlock(ResourceLocation tag) {
        return BlockTags.bind(tag.toString());
    }
}
