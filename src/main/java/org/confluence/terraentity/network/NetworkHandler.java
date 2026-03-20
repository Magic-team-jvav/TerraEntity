package org.confluence.terraentity.network;


import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.network.c2s.*;
import org.confluence.terraentity.network.s2c.*;

public final class NetworkHandler {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(TerraEntity.space("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void register() {
        int packetId = 0;
        CHANNEL.registerMessage(packetId++, SyncCameraShakePacket.class,  SyncCameraShakePacket::encode,  SyncCameraShakePacket::decode,  SyncCameraShakePacket::handle);
        CHANNEL.registerMessage(packetId++, SyncSummonPacket.class,  SyncSummonPacket::encode,  SyncSummonPacket::decode,  SyncSummonPacket::handle);
        CHANNEL.registerMessage(packetId++, SyncBossEventHealthPacket.class,  SyncBossEventHealthPacket::encode,  SyncBossEventHealthPacket::decode,  SyncBossEventHealthPacket::handle);
        CHANNEL.registerMessage(packetId++, SyncNPCTradesPacketS2C.class,  SyncNPCTradesPacketS2C::encode,  SyncNPCTradesPacketS2C::decode,  SyncNPCTradesPacketS2C::handle);
        CHANNEL.registerMessage(packetId++, SyncDataS2C.class,  SyncDataS2C::encode,  SyncDataS2C::decode,  SyncDataS2C::handle);
        CHANNEL.registerMessage(packetId++, UpdateNPCTradePacket.class,  UpdateNPCTradePacket::encode,  UpdateNPCTradePacket::decode,  UpdateNPCTradePacket::handle);
        CHANNEL.registerMessage(packetId++, ChesterAttachmentPacketS2C.class,  ChesterAttachmentPacketS2C::encode,  ChesterAttachmentPacketS2C::decode,  ChesterAttachmentPacketS2C::handle);
        CHANNEL.registerMessage(packetId++, EventPacketS2C.class,  EventPacketS2C::encode,  EventPacketS2C::decode,  EventPacketS2C::handle);
        CHANNEL.registerMessage(packetId++, SummonBossPacket.class,  SummonBossPacket::encode,  SummonBossPacket::new,  SummonBossPacket::handle);
        CHANNEL.registerMessage(packetId++, SyncWallOfFleshTargetPacket.class, SyncWallOfFleshTargetPacket::encode,  SyncWallOfFleshTargetPacket::new,  SyncWallOfFleshTargetPacket::handle);
        CHANNEL.registerMessage(packetId++, SyncWallOfFleshPositionsPacket.class, SyncWallOfFleshPositionsPacket::encode,  SyncWallOfFleshPositionsPacket::new,  SyncWallOfFleshPositionsPacket::handle);
        CHANNEL.registerMessage(packetId++, UpdateBlackboardPacket.class,  UpdateBlackboardPacket::encode,  UpdateBlackboardPacket::new,  UpdateBlackboardPacket::handle);


        CHANNEL.registerMessage(packetId++, ServerBoundVehicleExtensionPacket.class,  ServerBoundVehicleExtensionPacket::encode,  ServerBoundVehicleExtensionPacket::decode,  ServerBoundVehicleExtensionPacket::handle);
        CHANNEL.registerMessage(packetId++, ServerBoundHousePacket.class,  ServerBoundHousePacket::encode,  ServerBoundHousePacket::new,  ServerBoundHousePacket::handle);
        CHANNEL.registerMessage(packetId++, NPCShopPacket.class,  NPCShopPacket::encode,  NPCShopPacket::decode,  NPCShopPacket::handle);
        CHANNEL.registerMessage(packetId++, EventPacketC2S.class,  EventPacketC2S::encode,  EventPacketC2S::decode,  EventPacketC2S::handle);
        CHANNEL.registerMessage(packetId++, ServerBoundEventPacket.class,  ServerBoundEventPacket::encode,  ServerBoundEventPacket::decode,  ServerBoundEventPacket::handle);



    }
}
