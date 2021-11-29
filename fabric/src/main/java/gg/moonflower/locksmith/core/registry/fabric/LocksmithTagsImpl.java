package gg.moonflower.locksmith.core.registry.fabric;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;

public class LocksmithTagsImpl {
    public static Tag<Block> bindBlock(ResourceLocation tag) {
        return TagRegistry.block(tag);
    }
}
