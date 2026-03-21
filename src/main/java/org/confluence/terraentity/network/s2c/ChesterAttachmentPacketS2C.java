package org.confluence.terraentity.network.s2c;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.terraentity.attachment.SummonerAttachment;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.network.NetworkHandler;
import org.confluence.terraentity.registries.chester.ChesterConditionalType;

import java.util.Map;
import java.util.function.Supplier;

public class ChesterAttachmentPacketS2C {

    /**
     * chestType: 0-999, additionalType: 0-999
     */
    int code;

    Map<SummonerAttachment.Key, ChesterConditionalType> bandedBlocks;

    public ChesterAttachmentPacketS2C(int code, Map<SummonerAttachment.Key, ChesterConditionalType> bandedBlocks) {
        this.code = code;
        this.bandedBlocks = bandedBlocks;
    }

    private ChesterAttachmentPacketS2C(FriendlyByteBuf buf) {
        this.code = buf.readInt();
        this.bandedBlocks = buf.readJsonWithCodec(SummonerAttachment.bandedBlocksCodec);
    }


    public static ChesterAttachmentPacketS2C decode(FriendlyByteBuf buffer) {
        return new ChesterAttachmentPacketS2C(buffer);
    }

    public static void encode(ChesterAttachmentPacketS2C value, FriendlyByteBuf buf) {
        buf.writeInt(value.code);
        buf.writeJsonWithCodec(SummonerAttachment.bandedBlocksCodec, value.bandedBlocks);
    }


    public static void handle(ChesterAttachmentPacketS2C packet, Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> {
            Player player = context.getSender();
            int code = packet.code;
            int chestType = code % 1000;
            int additionalType = code / 1000;
            var data = Minecraft.getInstance().player.getCapability(TEAttachments.SUMMONER_STORAGE).orElseGet(() -> new SummonerAttachment());
            data.chestType = chestType;
            data.chestTypeAdditional = additionalType;
            data.boundBlocks = packet.bandedBlocks;
        });
    }


    public static void syncChesterOpenType(int chestType,int additionalType, ServerPlayer player){
        NetworkHandler.sendToPlayer(player,new ChesterAttachmentPacketS2C(chestType + additionalType * 1000,
                player.getCapability(TEAttachments.SUMMONER_STORAGE).orElseGet(SummonerAttachment::new).boundBlocks));
    }
}
