package gg.moonflower.locksmith.common.network.play;

import gg.moonflower.locksmith.common.lockpicking.PinState;
import gg.moonflower.locksmith.common.network.play.handler.LocksmithClientPlayPacketHandler;
import gg.moonflower.pollen.api.network.packet.PollinatedPacket;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;
import net.minecraft.network.FriendlyByteBuf;

import java.io.IOException;

public class ClientboundLockPickingPacket implements PollinatedPacket<LocksmithClientPlayPacketHandler> {

    private final PinState state;
    private final int pin;

    public ClientboundLockPickingPacket(PinState state, int pin) {
        this.state = state;
        this.pin = pin;
    }

    public ClientboundLockPickingPacket(FriendlyByteBuf buf) throws IOException {
        this.state = buf.readEnum(PinState.class);
        this.pin = buf.readVarInt();
    }

    @Override
    public void writePacketData(FriendlyByteBuf buf) throws IOException {
        buf.writeEnum(this.state);
        buf.writeVarInt(this.pin);
    }

    @Override
    public void processPacket(LocksmithClientPlayPacketHandler handler, PollinatedPacketContext ctx) {
        handler.handleLockPicking(this, ctx);
    }

    public PinState getState() {
        return state;
    }

    public int getPin() {
        return pin;
    }
}
