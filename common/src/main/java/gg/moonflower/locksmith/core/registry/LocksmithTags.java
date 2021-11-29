package gg.moonflower.locksmith.core.registry;

import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.pollen.api.registry.TagRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;

public class LocksmithTags {

    public static final Tag<Block> LOCKABLE = TagRegistry.bindBlock(new ResourceLocation(Locksmith.MOD_ID, "lockable"));

}
