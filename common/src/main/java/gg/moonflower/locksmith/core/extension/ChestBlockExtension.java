package gg.moonflower.locksmith.core.extension;

import net.minecraft.world.level.block.entity.ChestBlockEntity;

public interface ChestBlockExtension {
    ThreadLocal<ChestBlockEntity> chestBlockEntity1 = new ThreadLocal<>();
    ThreadLocal<ChestBlockEntity> chestBlockEntity2 = new ThreadLocal<>();
}
