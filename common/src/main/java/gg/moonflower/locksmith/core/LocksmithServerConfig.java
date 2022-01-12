package gg.moonflower.locksmith.core;

import gg.moonflower.pollen.api.config.PollinatedConfigBuilder;
import gg.moonflower.pollen.api.config.PollinatedConfigBuilder.ConfigValue;

public class LocksmithServerConfig {

    public final ConfigValue<Boolean> allowLocksToBeBroken;
    public final ConfigValue<Double> lockBreakingMultiplier;
    public final ConfigValue<Boolean> enableLockpicking;
//    public final ConfigValue<Boolean> useKeyringMenu;

    protected LocksmithServerConfig(PollinatedConfigBuilder builder) {
        builder.push("Locks");
        {
            this.allowLocksToBeBroken = builder.comment("Allows locked blocks to be broken by players with a hardness modifier.").define("Allow locks to be broken", true);
            this.lockBreakingMultiplier = builder.comment("Multiplies the base hardness of a block when locked.").define("Lock hardness multiplier", 3D);
            this.enableLockpicking = builder.comment("Enables the recipe for the Lockpick.").define("Enable lockpick", true);
//            this.useKeyringMenu = builder.comment("Disables right clicking keys together to become key rings and allows the menu to be used by shift right-clicking").define("Use key ring menu", false);
        }
        builder.pop();
    }
}
