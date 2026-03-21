package org.confluence.terraentity.network.c2s;

import com.github.edg_thexu.cafelib.network.CustomPacketPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import net.minecraft.world.entity.player.Player;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.attachment.UnSyncableAttachment;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.item.DebugItem;
import org.confluence.terraentity.network.NetworkHandler;
import org.jetbrains.annotations.NotNull;

public record SetDebugModePacket(DebugItem.DebugMode mode) implements CustomPacketPayload {

    public static final Type<SetDebugModePacket> TYPE = new Type<>(TerraEntity.fromSpaceAndPath(TerraEntity.MODID, "set_debug_mode_packet"));


    public SetDebugModePacket(FriendlyByteBuf buf) {
        this(buf.readEnum(DebugItem.DebugMode.class));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(mode);
    }
    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            player.getCapability(TEAttachments.UNSYNC).orElse(new UnSyncableAttachment()).setDebugMode(mode);
            player.sendSystemMessage(Component.literal("Debug mode set to: " + mode.name()));
        });
    }


    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }



    public static void send(DebugItem.DebugMode mode, Player localPlayer) {
        NetworkHandler.sendToServer(new SetDebugModePacket(mode));
        localPlayer.getCapability(TEAttachments.UNSYNC).orElse(new UnSyncableAttachment()).setDebugMode(mode);
    }


}
