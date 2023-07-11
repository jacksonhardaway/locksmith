package gg.moonflower.locksmith.common.network;

import gg.moonflower.locksmith.common.network.play.ClientboundAddLocksPacket;
import gg.moonflower.locksmith.common.network.play.ClientboundDeleteLocksPacket;
import gg.moonflower.locksmith.common.network.play.ClientboundLockPickingPacket;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.pollen.api.network.v1.PollinatedPlayNetworkChannel;
import gg.moonflower.pollen.api.network.v1.packet.PollinatedPacketDirection;
import gg.moonflower.pollen.api.registry.network.v1.PollinatedNetworkRegistry;
import net.minecraft.resources.ResourceLocation;

public class LocksmithMessages {

    public static final PollinatedPlayNetworkChannel PLAY = PollinatedNetworkRegistry.createPlay(new ResourceLocation(Locksmith.MOD_ID, "play"), "1");

    public static void init() {
        PLAY.register(ClientboundAddLocksPacket.class, ClientboundAddLocksPacket::new, PollinatedPacketDirection.PLAY_CLIENTBOUND);
        PLAY.register(ClientboundDeleteLocksPacket.class, ClientboundDeleteLocksPacket::new, PollinatedPacketDirection.PLAY_CLIENTBOUND);
        PLAY.register(ClientboundLockPickingPacket.class, ClientboundLockPickingPacket::new, PollinatedPacketDirection.PLAY_CLIENTBOUND);
    }
}
