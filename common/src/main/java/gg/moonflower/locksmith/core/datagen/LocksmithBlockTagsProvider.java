//package gg.moonflower.locksmith.core.datagen;
//
//import gg.moonflower.locksmith.core.registry.LocksmithTags;
//import gg.moonflower.pollen.api.datagen.provider.tags.PollinatedBlockTagsProvider;
//import gg.moonflower.pollen.api.util.PollinatedModContainer;
//import net.minecraft.data.DataGenerator;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.tags.BlockTags;
//
//public class LocksmithBlockTagsProvider extends PollinatedBlockTagsProvider {
//
//    public LocksmithBlockTagsProvider(DataGenerator dataGenerator, PollinatedModContainer container) {
//        super(dataGenerator, container);
//    }
//
//    @Override
//    protected void addTags() {
//        this.tag(LocksmithTags.LOCKABLE_BLOCKS).
//                addTag(BlockTags.WOODEN_DOORS, BlockTags.WOODEN_TRAPDOORS, BlockTags.FENCE_GATES).
//                addOptionalTag(new ResourceLocation("c", "chest")).
//                addOptionalTag(new ResourceLocation("forge", "chests")).
//                addOptionalTag(new ResourceLocation("c", "barrel")).
//                addOptionalTag(new ResourceLocation("forge", "barrels"));
//    }
//}
