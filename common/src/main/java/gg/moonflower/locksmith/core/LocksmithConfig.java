package gg.moonflower.locksmith.core;

import gg.moonflower.pollen.api.config.PollinatedConfigBuilder;
import gg.moonflower.pollen.api.config.PollinatedConfigBuilder.ConfigValue;

public class LocksmithConfig {

    public final ConfigValue<Boolean> allowLocksToBeBroken;
    public final ConfigValue<Double> lockBreakingMultiplier;
    public final ConfigValue<Boolean> enableLockpicking;

    protected LocksmithConfig(PollinatedConfigBuilder builder) {
        builder.push("Locks");
        {
            this.allowLocksToBeBroken = builder.comment("Allows locked blocks to be broken by players with a hardness modifier.").define("Allow locks to be broken", true);
            this.lockBreakingMultiplier = builder.comment("Multiplies the base hardness of a block when locked.").define("Lock hardness multiplier", 3D);
            this.enableLockpicking = builder.comment("Enables the recipe for the Lockpick.").define("Enable lockpick", true);
        }
        builder.pop();
    }
}
