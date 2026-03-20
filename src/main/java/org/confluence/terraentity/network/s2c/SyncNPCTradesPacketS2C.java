package org.confluence.terraentity.network.s2c;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.npc.trade.NPCTradeManager;
import org.confluence.terraentity.network.CustomPacketPayload;
import org.confluence.terraentity.utils.AdapterUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record SyncNPCTradesPacketS2C(Map<ResourceLocation, Tag> tradesMap) implements CustomPacketPayload {
    public static final Type<SyncNPCTradesPacketS2C> TYPE = new Type<>(TerraEntity.space("npc_trades_packet_s2c"));


    public static SyncNPCTradesPacketS2C decode(FriendlyByteBuf buffer) {
        Map<ResourceLocation, Tag> map = new HashMap<>();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            map.put(buffer.readResourceLocation(), buffer.readNbt(NbtAccounter.UNLIMITED));
        }
        return new SyncNPCTradesPacketS2C(map);
    }


    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(tradesMap.size());
        for (Map.Entry<ResourceLocation, Tag> entry : tradesMap.entrySet()) {
            buffer.writeResourceLocation(entry.getKey());
            buffer.writeNbt((CompoundTag) entry.getValue());
        }
    };

    @Override
    public @NotNull Type<SyncNPCTradesPacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            NPCTradeManager.Loader.getInstance().syncFromServer(context.player().level().registryAccess(), tradesMap);
        });
    }

    public static void sync(ServerPlayer player) {
        AdapterUtils.sendToPlayer(player, new SyncNPCTradesPacketS2C(NPCTradeManager.Loader.getInstance().getTagMap()));
    }
}
