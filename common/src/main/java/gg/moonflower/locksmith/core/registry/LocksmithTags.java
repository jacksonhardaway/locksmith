package gg.moonflower.locksmith.core.registry;

import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.pollen.api.registry.resource.v1.TagRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class LocksmithTags {

    public static final TagKey<Block> LOCKABLE_BLOCKS = TagRegistry.bindBlock(new ResourceLocation(Locksmith.MOD_ID, "lockable"));
    public static final TagKey<EntityType<?>> LOCKABLE_ENTITIES = TagRegistry.bindEntityType(new ResourceLocation(Locksmith.MOD_ID, "lockable"));

    public static final TagKey<Item> BLANK_KEY = TagRegistry.bindItem(new ResourceLocation(Locksmith.MOD_ID, "blank_key"));
    public static final TagKey<Item> LOCKING = TagRegistry.bindItem(new ResourceLocation(Locksmith.MOD_ID, "locking"));

}
