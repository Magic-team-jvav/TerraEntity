package org.confluence.terraentity.network.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.terraentity.api.npc.trade.ITradeHolder;
import org.confluence.terraentity.api.npc.trade.ITrade;
import org.confluence.terraentity.network.NetworkHandler;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * 更新npc交易的单个列表，以节省网络流量
 */
public class UpdateNPCTradePacket {

    private final int index;
    private final UUID npcId;
    private final ITrade trade;

    private UpdateNPCTradePacket(int index, UUID npcId, ITrade trade) {
        this.index = index;
        this.npcId = npcId;
        this.trade = trade;
    }

    public UpdateNPCTradePacket(FriendlyByteBuf buffer) {
       this.index = buffer.readInt();
       this.npcId = buffer.readUUID();
       this.trade = buffer.readJsonWithCodec(ITrade.TYPED_CODEC);
    }


    public static UpdateNPCTradePacket decode(FriendlyByteBuf buffer) {
        return new UpdateNPCTradePacket(buffer);
    }

    public static void encode(UpdateNPCTradePacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.index);
        buf.writeUUID(packet.npcId);
        buf.writeJsonWithCodec(ITrade.TYPED_CODEC, packet.trade);
        
    }

    public static void handle(UpdateNPCTradePacket packet, Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        var player = context.getSender();
        UUID npcId = packet.npcId;
        int index = packet.index;
        ITrade trade = packet.trade;

        context.enqueueWork(() -> {
            if(player.level().getEntities().get(npcId) instanceof ITradeHolder npc){
                var trades = npc.getTradeManager().trades();
                var availableTrades = npc.getTradeManager().availableTrades();
                var availableTrade = availableTrades.get(index);

                // 由于客户端传来的index是availableTrades的索引，所以需要将availableTrades的索引转换为trades的索引
                int oriIndex = trades.indexOf(availableTrade);
                npc.getTradeManager().trades().set(oriIndex, trade);
                availableTrades.set(index, trade);
            }

        }).exceptionally(e -> null);
        context.setPacketHandled(true);
    }



    public static <T extends Entity & ITradeHolder> void syncNpcTrade(int index, T npc){
        NetworkHandler.sendToAllPlayers(new UpdateNPCTradePacket(index, npc.getUUID(), npc.getTradeManager().trades().get(index)));
    }

    public static <T extends ITradeHolder> void syncNpcTrade(int index, UUID npcId, T npc){
        NetworkHandler.sendToAllPlayers(new UpdateNPCTradePacket(index, npcId, npc.getTradeManager().trades().get(index)));
    }
}
