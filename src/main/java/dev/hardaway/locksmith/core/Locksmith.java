package dev.hardaway.locksmith.core;

import dev.hardaway.locksmith.core.data.*;
import dev.hardaway.locksmith.core.data.loot.LocksmithBlockLootProvider;
import dev.hardaway.locksmith.core.data.loot.LocksmithLootTableProvider;
import dev.hardaway.locksmith.core.registry.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod(Locksmith.MOD_ID)
public class Locksmith {

    public static final String MOD_ID = "locksmith";

    public Locksmith(IEventBus bus) {
        bus.addListener(this::setup);
        bus.addListener(this::registerCreativeTabs);
        bus.addListener(this::registerPayloadHandlers);
        bus.addListener(this::gatherData);
        bus.register(LocksmithCapabilities.class);

        LocksmithAttachments.REGISTRY.register(bus);
        LocksmithSounds.REGISTRY.register(bus);
        LocksmithBlocks.REGISTRY.register(bus);
        LocksmithItems.REGISTRY.register(bus);
        LocksmithMenus.REGISTRY.register(bus);
        LocksmithComponents.REGISTRY.register(bus);
        LocksmithParticles.REGISTRY.register(bus);
    }

    public static ResourceLocation path(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    private void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }

    private void registerCreativeTabs(BuildCreativeModeTabContentsEvent event) {
    }


    private void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("l2");
    }

    private void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        PackOutput packOutput = generator.getPackOutput();
        generator.addProvider(event.includeClient(), new LocksmithLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new LocksmithSoundDefinitionsProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new LocksmithParticleDescriptionProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new LocksmithBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new LocksmithItemModelProvider(packOutput, existingFileHelper));

        generator.addProvider(event.includeServer(), new LocksmithBlockTagsProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new LocksmithEntityTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new LocksmithRecipeProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new LocksmithLootTableProvider(packOutput, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(LocksmithBlockLootProvider::new, LootContextParamSets.BLOCK)
        ), lookupProvider));
    }
}
