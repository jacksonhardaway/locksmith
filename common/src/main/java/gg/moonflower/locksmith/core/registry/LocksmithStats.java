package gg.moonflower.locksmith.core.registry;

import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.pollen.api.registry.PollinatedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class LocksmithStats {
    public static final PollinatedRegistry<ResourceLocation> STATS = PollinatedRegistry.createVanilla(Registry.CUSTOM_STAT, Locksmith.MOD_ID);

    public static final ResourceLocation INTERACT_WITH_LOCKSMITHING_TABLE = registerStat("interact_with_locksmithing_table", StatFormatter.DEFAULT);
    public static final ResourceLocation PICK_LOCK = registerStat("pick_lock", StatFormatter.DEFAULT);

    private static ResourceLocation registerStat(String key, StatFormatter formatter) {
        ResourceLocation stat = new ResourceLocation(Locksmith.MOD_ID, key);
        STATS.register(key, () -> stat);
        Stats.CUSTOM.get(stat, formatter);
        return stat;
    }
}
