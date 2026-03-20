package org.confluence.terraentity.network.s2c;

import io.netty.buffer.ByteBuf;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.network.CustomPacketPayload;
import org.confluence.terraentity.utils.AdapterUtils;

public record SyncLevelNamePacketS2C(String name) implements CustomPacketPayload {
    public static final Type<SyncLevelNamePacketS2C> TYPE = new Type<>(TerraEntity.space("sync_level_name"));


    public static String levelName = "Otherworld";

    @Override
    public Type<SyncLevelNamePacketS2C> type() {
        return TYPE;
    }

    public static SyncLevelNamePacketS2C decode(FriendlyByteBuf buf) {
        int len = buf.readInt();
        return new SyncLevelNamePacketS2C(buf.readBytes(len).toString());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        byte[] bytes = name.getBytes();
        int len = bytes.length;
        buf.writeInt(len);
        buf.writeBytes(bytes);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> levelName = name);
    }

    public static void sendToClient(ServerPlayer player) {
        AdapterUtils.sendToPlayer(player, new SyncLevelNamePacketS2C(player.server.getWorldData().getLevelName()));
    }
}
