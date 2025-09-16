package dev.hardaway.locksmith.core.registry;

import dev.hardaway.locksmith.api.lock.BasicBlockLockable;
import dev.hardaway.locksmith.api.lock.BasicEntityLockable;
import dev.hardaway.locksmith.api.lock.Lockable;
import dev.hardaway.locksmith.core.Locksmith;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class LocksmithCapabilities {

    public static final BlockCapability<Lockable, Void> BLOCK_LOCKABLE =
            BlockCapability.createVoid(Locksmith.path("block_lockable"), Lockable.class);

    public static final EntityCapability<Lockable, Void> ENTITY_LOCKABLE =
            EntityCapability.createVoid(Locksmith.path("entity_lockable"), Lockable.class);


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (Block block : BuiltInRegistries.BLOCK) {
//            if (block instanceof DoorBlock) {
//                event.registerBlock(LocksmithCapabilities.BLOCK_LOCKABLE, DoorLockable::new, block);
//            } else {
            event.registerBlock(LocksmithCapabilities.BLOCK_LOCKABLE, BasicBlockLockable::new, block);
//            }
        }

        for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
            event.registerEntity(LocksmithCapabilities.ENTITY_LOCKABLE, entityType, BasicEntityLockable::new);
        }
    }

}
