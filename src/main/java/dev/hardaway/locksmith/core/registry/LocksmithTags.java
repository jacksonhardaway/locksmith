package dev.hardaway.locksmith.core.registry;

import dev.hardaway.locksmith.core.Locksmith;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public class LocksmithTags {

    public static final class Blocks {
        public static final TagKey<Block> LOCKABLES = tag("lockables");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(Locksmith.path(name));
        }
    }

    public static final class Entities {
        public static final TagKey<EntityType<?>> LOCKABLES = tag("lockables");

        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, Locksmith.path(name));
        }
    }
}
