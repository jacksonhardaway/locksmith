package gg.moonflower.locksmith.common.network;

import gg.moonflower.locksmith.common.network.play.ClientboundAddLocksPacket;
import gg.moonflower.locksmith.common.network.play.ClientboundDeleteLockPacket;
import gg.moonflower.locksmith.common.network.play.ClientboundLockPickingPacket;
import gg.moonflower.locksmith.common.network.play.handler.LocksmithClientPlayPacketHandlerImpl;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.pollen.api.network.PollinatedPlayNetworkChannel;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketDirection;
import gg.moonflower.pollen.api.registry.NetworkRegistry;
import net.minecraft.resources.ResourceLocation;

public class LocksmithMessages {

    public static final PollinatedPlayNetworkChannel PLAY = NetworkRegistry.createPlay(new ResourceLocation(Locksmith.MOD_ID, "play"), "1", () -> LocksmithClientPlayPacketHandlerImpl::new, () -> Object::new);

    public static void init() {
        PLAY.register(ClientboundAddLocksPacket.class, ClientboundAddLocksPacket::new, PollinatedPacketDirection.PLAY_CLIENTBOUND);
        PLAY.register(ClientboundDeleteLockPacket.class, ClientboundDeleteLockPacket::new, PollinatedPacketDirection.PLAY_CLIENTBOUND);
        PLAY.register(ClientboundLockPickingPacket.class, ClientboundLockPickingPacket::new, PollinatedPacketDirection.PLAY_CLIENTBOUND);
    }
}
