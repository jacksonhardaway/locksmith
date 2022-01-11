package gg.moonflower.locksmith.common.network.play;

import gg.moonflower.locksmith.common.network.play.handler.LocksmithServerPlayPacketHandler;
import gg.moonflower.pollen.api.network.packet.PollinatedPacket;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;
import net.minecraft.network.FriendlyByteBuf;

import java.io.IOException;

public class ServerboundLockPickingPacket implements PollinatedPacket<LocksmithServerPlayPacketHandler> {

    private final EventType event;
    private int selectedIndex;

    public ServerboundLockPickingPacket(int selectedIndex) {
        this.event = EventType.PICK;
        this.selectedIndex = selectedIndex;
    }

    public ServerboundLockPickingPacket(FriendlyByteBuf buf) throws IOException {
        this.event = buf.readEnum(EventType.class);
        this.selectedIndex = this.event == EventType.PICK ? buf.readVarInt() : 0;
    }

    @Override
    public void writePacketData(FriendlyByteBuf buf) throws IOException {
        buf.writeEnum(this.event);
        switch (this.event) {
            case PICK:
                buf.writeVarInt(this.selectedIndex);
                break;
        }
    }

    @Override
    public void processPacket(LocksmithServerPlayPacketHandler handler, PollinatedPacketContext ctx) {
        handler.handleLockPicking(this, ctx);
    }

    public EventType getEvent() {
        return event;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public enum EventType {
        PICK
    }
}
