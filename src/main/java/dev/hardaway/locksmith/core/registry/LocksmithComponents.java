package dev.hardaway.locksmith.core.registry;

import dev.hardaway.locksmith.common.component.CopiedKey;
import dev.hardaway.locksmith.common.component.KeyData;
import dev.hardaway.locksmith.common.component.Keychain;
import dev.hardaway.locksmith.common.component.OriginalKey;
import dev.hardaway.locksmith.core.Locksmith;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class LocksmithComponents {

    public static final DeferredRegister.DataComponents REGISTRY = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Locksmith.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<KeyData>> KEY_DATA = REGISTRY.registerComponentType("key_data", builder -> builder.persistent(KeyData.CODEC).networkSynchronized(KeyData.STREAM_CODEC).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<OriginalKey>> ORIGINAL_KEY = REGISTRY.registerComponentType("original_key", builder -> builder.persistent(OriginalKey.CODEC).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CopiedKey>> COPIED_KEY = REGISTRY.registerComponentType("copied_key", builder -> builder.persistent(CopiedKey.CODEC).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Keychain>> KEYCHAIN = REGISTRY.registerComponentType("keychain", builder -> builder.persistent(Keychain.CODEC).networkSynchronized(Keychain.STREAM_CODEC).cacheEncoding());
}
