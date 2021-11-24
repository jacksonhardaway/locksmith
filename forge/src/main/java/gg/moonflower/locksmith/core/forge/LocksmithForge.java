package gg.moonflower.locksmith.core.forge;

import gg.moonflower.locksmith.core.Locksmith;
import net.minecraftforge.fml.common.Mod;

@Mod(Locksmith.MOD_ID)
public class LocksmithForge {
    public LocksmithForge() {
        Locksmith.PLATFORM.setup();
    }
}
