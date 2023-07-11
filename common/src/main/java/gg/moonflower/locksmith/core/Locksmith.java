package gg.moonflower.locksmith.core;

import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.registry.registries.Registries;
import gg.moonflower.locksmith.common.lock.LockInteractionManager;
import gg.moonflower.locksmith.common.lock.ServerLockManager;
import gg.moonflower.locksmith.common.network.LocksmithMessages;
import gg.moonflower.locksmith.core.registry.LocksmithBlocks;
import gg.moonflower.locksmith.core.registry.LocksmithItems;
import gg.moonflower.locksmith.core.registry.LocksmithLocks;
import gg.moonflower.locksmith.core.registry.LocksmithMenus;
import gg.moonflower.locksmith.core.registry.LocksmithParticles;
import gg.moonflower.locksmith.core.registry.LocksmithRecipes;
import gg.moonflower.locksmith.core.registry.LocksmithSounds;
import gg.moonflower.locksmith.core.registry.LocksmithStats;
import gg.moonflower.pollen.api.config.v1.ConfigManager;
import gg.moonflower.pollen.api.config.v1.PollinatedConfigType;

public class Locksmith {

    public static final String MOD_ID = "locksmith";
    public static final LocksmithServerConfig CONFIG = ConfigManager.register(MOD_ID, PollinatedConfigType.SERVER, LocksmithServerConfig::new);

    public static void init() {
        LocksmithBlocks.REGISTRY.register();
        LocksmithItems.REGISTRY.register();
        LocksmithSounds.REGISTRY.register();
        LocksmithMenus.REGISTRY.register();
        LocksmithParticles.REGISTRY.register();
        LocksmithRecipes.RECIPES.register();
        LocksmithRecipes.RECIPE_TYPES.register();
//        LocksmithStats.REGISTRY.register();
        LocksmithLocks.register();
        LocksmithMessages.init();
        ServerLockManager.init();

        InteractionEvent.RIGHT_CLICK_BLOCK.register(LockInteractionManager::onRightClickBlock);
        InteractionEvent.LEFT_CLICK_BLOCK.register(LockInteractionManager::onLeftClickBlock);
    }

//    private static void onDataInit(Platform.DataSetupContext ctx) {
//        DataGenerator dataGenerator = ctx.getGenerator();
//        PollinatedModContainer container = ctx.getMod();
//        LocksmithBlockTagsProvider blockTagsProvider = new LocksmithBlockTagsProvider(dataGenerator, container);
//        dataGenerator.addProvider(blockTagsProvider);
//        dataGenerator.addProvider(new LocksmithItemTagsProvider(dataGenerator, container, blockTagsProvider));
//        dataGenerator.addProvider(new LocksmithLanguageProvider(dataGenerator, container));
//        dataGenerator.addProvider(new LocksmithSoundProvider(dataGenerator, container));
//        dataGenerator.addProvider(new LocksmithRecipeProvider(dataGenerator));
//    }
}
