package gg.moonflower.locksmith.core.fabric;

import gg.moonflower.locksmith.common.lock.LockInteractionManager;
import gg.moonflower.locksmith.core.Locksmith;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class LocksmithFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Locksmith.PLATFORM.setup();

        // TODO: move these back to common
        UseBlockCallback.EVENT.register(LockInteractionManager::onRightClickBlock);
        AttackBlockCallback.EVENT.register(LockInteractionManager::onLeftClickBlock);
    }
}
