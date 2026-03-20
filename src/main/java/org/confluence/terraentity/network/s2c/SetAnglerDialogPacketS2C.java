package org.confluence.terraentity.network.s2c;

import net.minecraft.network.FriendlyByteBuf;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.gui.container.AnglerDialogScreen;
import org.confluence.terraentity.network.CustomPacketPayload;

public record SetAnglerDialogPacketS2C(byte data) implements CustomPacketPayload {
    public static final byte TASK_SUCCEED = 1;
    public static final byte WAKEUP = 2;
    public static final Type<SetAnglerDialogPacketS2C> TYPE = new Type<>(TerraEntity.space("set_angler_dialog"));


    @Override
    public Type<SetAnglerDialogPacketS2C> type() {
        return TYPE;
    }

    public static SetAnglerDialogPacketS2C decode(FriendlyByteBuf buf) {
        return new SetAnglerDialogPacketS2C(buf.readByte());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeByte(data);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> AnglerDialogScreen.Handler.handleTaskSucceed(data));
    }
}
