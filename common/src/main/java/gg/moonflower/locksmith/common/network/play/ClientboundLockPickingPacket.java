package gg.moonflower.locksmith.common.network.play;

import gg.moonflower.locksmith.common.network.play.handler.LocksmithClientPlayPacketHandler;
import gg.moonflower.pollen.api.network.packet.PollinatedPacket;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.commons.lang3.Validate;

import java.io.IOException;

public class ClientboundLockPickingPacket implements PollinatedPacket<LocksmithClientPlayPacketHandler> {

    private final Type type;
    private final int pin;
    private final boolean set;

    public ClientboundLockPickingPacket(int pin, boolean set) {
        this.type = Type.SET;
        this.pin = pin;
        this.set = set;
    }

    public ClientboundLockPickingPacket(Type type) {
        Validate.isTrue(type != Type.SET, "Invalid type: " + type + ". Use other constructor");
        this.type = type;
        this.pin = -1;
        this.set = false;
    }

    public ClientboundLockPickingPacket(FriendlyByteBuf buf) throws IOException {
        this.type = buf.readEnum(Type.class);
        this.pin = this.type == Type.SET ? buf.readVarInt() : -1;
        this.set = this.type == Type.SET && buf.readBoolean();
    }

    @Override
    public void writePacketData(FriendlyByteBuf buf) throws IOException {
        buf.writeEnum(this.type);
        if (this.type == Type.SET) {
            buf.writeVarInt(this.pin);
            buf.writeBoolean(this.set);
        }
    }

    @Override
    public void processPacket(LocksmithClientPlayPacketHandler handler, PollinatedPacketContext ctx) {
        handler.handleLockPicking(this, ctx);
    }

    public Type getType() {
        return type;
    }

    public int getPin() {
        return pin;
    }

    public boolean isSet() {
        return set;
    }

    public enum Type {
        SET, RESET, SUCCESS, FAIL
    }
}
