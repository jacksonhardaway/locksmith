//package gg.moonflower.locksmith.core.datagen;
//
//import gg.moonflower.locksmith.core.registry.LocksmithBlocks;
//import gg.moonflower.locksmith.core.registry.LocksmithItems;
//import gg.moonflower.locksmith.core.registry.LocksmithTags;
//import gg.moonflower.pollen.api.datagen.provider.tags.PollinatedItemTagsProvider;
//import gg.moonflower.pollen.api.util.PollinatedModContainer;
//import net.minecraft.data.DataGenerator;
//import net.minecraft.data.tags.BlockTagsProvider;
//
//public class LocksmithItemTagsProvider extends PollinatedItemTagsProvider {
//
//    public LocksmithItemTagsProvider(DataGenerator dataGenerator, PollinatedModContainer container, BlockTagsProvider blockTagsProvider) {
//        super(dataGenerator, container, blockTagsProvider);
//    }
//
//    @Override
//    protected void addTags() {
//        this.tag(LocksmithTags.BLANK_KEY).add(LocksmithItems.BLANK_KEY.get());
//        this.tag(LocksmithTags.LOCKING).pollenAdd(LocksmithItems.KEY.get(), LocksmithItems.LOCK.get(), LocksmithBlocks.LOCK_BUTTON.get().asItem());
//    }
//}
