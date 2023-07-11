package gg.moonflower.locksmith.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import gg.moonflower.locksmith.core.Locksmith;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class LocksmithStats {
    public static final DeferredRegister<ResourceLocation> REGISTRY = DeferredRegister.create(Locksmith.MOD_ID, Registry.CUSTOM_STAT_REGISTRY);

    public static final ResourceLocation INTERACT_WITH_LOCKSMITHING_TABLE = registerStat("interact_with_locksmithing_table", StatFormatter.DEFAULT);
    public static final ResourceLocation PICK_LOCK = registerStat("pick_lock", StatFormatter.DEFAULT);

    private static ResourceLocation registerStat(String key, StatFormatter formatter) {
        ResourceLocation stat = new ResourceLocation(Locksmith.MOD_ID, key);
        REGISTRY.register(key, () -> stat);
        Stats.CUSTOM.get(stat, formatter);
        return stat;
    }
}
