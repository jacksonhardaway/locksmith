package dev.hardaway.locksmith.core.registry;

import dev.hardaway.locksmith.core.Locksmith;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class LocksmithMenus {

    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, Locksmith.MOD_ID);
}
