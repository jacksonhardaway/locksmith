package dev.hardaway.locksmith.core.registry;

import dev.hardaway.locksmith.api.lock.Lock;
import dev.hardaway.locksmith.api.storage.LockContainer;
import dev.hardaway.locksmith.core.Locksmith;
import net.minecraft.Util;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class LocksmithAttachments {

    public static final DeferredRegister<AttachmentType<?>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Locksmith.MOD_ID);

    public static final Supplier<AttachmentType<Lock>> LOCK = REGISTRY.register("lock", () -> AttachmentType.builder(() -> Lock.EMPTY)
            .serialize(Lock.CODEC, lock -> !Util.NIL_UUID.equals(lock.getId()))
            .sync(Lock.STREAM_CODEC)
            .build());

    public static final Supplier<AttachmentType<LockContainer>> LOCK_CONTAINER = REGISTRY.register("lock_container", () -> AttachmentType.builder(LockContainer::new)
            .serialize(LockContainer.CODEC)
            .sync(LockContainer.STREAM_CODEC)
            .build());
}
