package gg.moonflower.locksmith.core.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.pollen.api.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;

public class LocksmithTags {

    public static final Tag<Block> LOCKABLE = bindBlock(new ResourceLocation(Locksmith.MOD_ID, "lockable"));

    // TODO: Move into pollen
    @ExpectPlatform
    public static Tag<Block> bindBlock(ResourceLocation tag) {
        return Platform.error();
    }
}
