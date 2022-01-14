package gg.moonflower.locksmith.core.registry;

import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.pollen.api.registry.resource.TagRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class LocksmithTags {

    public static final Tag.Named<Block> LOCKABLE = TagRegistry.bindBlock(new ResourceLocation(Locksmith.MOD_ID, "lockable"));
    public static final Tag.Named<Item> BLANK_KEY = TagRegistry.bindItem(new ResourceLocation(Locksmith.MOD_ID, "blank_key"));
    public static final Tag.Named<Item> LOCKING = TagRegistry.bindItem(new ResourceLocation(Locksmith.MOD_ID, "locking"));

}
