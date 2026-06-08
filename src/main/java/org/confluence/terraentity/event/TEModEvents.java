package org.confluence.terraentity.event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.confluence.lib.api.event.NameFixRegisterEvent;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.network.c2s.*;
import org.confluence.terraentity.network.s2c.*;
import org.confluence.terraentity.utils.DriveAwaySystem.DriveAwayExecutor;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.entity.PortEntityAttributeCreationEvent;
import org.mesdag.portlib.network.PortNetworkHandler;

public class TEModEvents {
    public static void init() {
        registerPayloadHandlers();
        PortEventHandler.addListener(TEModEvents::registerEntityAttributes);
    }

    private static void registerPayloadHandlers() {
        PortNetworkHandler handler = TerraEntity.NETWORK_HANDLER;
        handler.registerInGameS2C(SyncCameraShakePacket.TYPE, SyncCameraShakePacket.STREAM_CODEC);
        handler.registerInGameS2C(SyncSummonPacket.TYPE, SyncSummonPacket.STREAM_CODEC);
        handler.registerInGameS2C(SyncBossEventHealthPacket.TYPE, SyncBossEventHealthPacket.STREAM_CODEC);
        handler.registerInGameS2C(SyncNPCTradesPacketS2C.TYPE, SyncNPCTradesPacketS2C.STREAM_CODEC);
        handler.registerInGameS2C(SyncDataS2C.TYPE, SyncDataS2C.STREAM_CODEC);
        handler.registerInGameS2C(UpdateNPCTradePacket.TYPE, UpdateNPCTradePacket.STREAM_CODEC);
        handler.registerInGameS2C(ChesterAttachmentPacketS2C.TYPE, ChesterAttachmentPacketS2C.STREAM_CODEC);
        handler.registerInGameS2C(EventPacketS2C.TYPE, EventPacketS2C.STREAM_CODEC);
        handler.registerInGameS2C(SummonBossPacket.TYPE, SummonBossPacket.STREAM_CODEC);
        handler.registerInGameS2C(SyncLevelNamePacketS2C.TYPE, SyncLevelNamePacketS2C.STREAM_CODEC);
        handler.registerInGameS2C(SetAnglerDialogPacketS2C.TYPE, SetAnglerDialogPacketS2C.STREAM_CODEC);
        handler.registerInGameS2C(SyncWallOfFleshTargetPacket.TYPE, SyncWallOfFleshTargetPacket.STREAM_CODEC);
        handler.registerInGameS2C(SyncWallOfFleshPositionsPacket.TYPE, SyncWallOfFleshPositionsPacket.STREAM_CODEC);
        handler.registerInGameS2C(UpdateBlackboardPacket.TYPE, UpdateBlackboardPacket.STREAM_CODEC);

        handler.registerInGameC2S(ServerBoundVehicleExtensionPacket.TYPE, ServerBoundVehicleExtensionPacket.STREAM_CODEC);
        handler.registerInGameC2S(ServerBoundHousePacket.TYPE, ServerBoundHousePacket.STREAM_CODEC);
        handler.registerInGameC2S(NPCShopPacket.TYPE, NPCShopPacket.STREAM_CODEC);
        handler.registerInGameC2S(EventPacketC2S.TYPE, EventPacketC2S.STREAM_CODEC);
        handler.registerInGameC2S(SetDebugModePacket.TYPE, SetDebugModePacket.STREAM_CODEC);
    }

    private static void registerEntityAttributes(PortEntityAttributeCreationEvent event) {
        TEEntities.registerEntityAttributes(event);
    }

    // 注册生成位置
    @SubscribeEvent
    public static void spawnPlacementRegister(RegisterSpawnPlacementsEvent event) {
        TEEntities.spawnPlacementRegister(event);
    }

    // 修改物品组件
    @SubscribeEvent
    public static void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        ItemComponentModify.modifyDefaultComponents(event);
    }

    @SubscribeEvent
    public static void itemNameFixRegister(NameFixRegisterEvent.Item event) {
        // 1.1.2 -> 1.1.3
        event.register("terra_entity:emerald_whip", "terra_entity:jade_whip");
    }

    // 这个事件在注册能力之前调用
    @SubscribeEvent
    public static void registerCapabilitiesBefore(RegisterCauldronFluidContentEvent event) {
        if (ModChecker.curios.isLoaded()) {
            CuriosHelper.registerCurios();
        }
    }

    // 服务器tick事件：更新被驱离实体的位置
    @SubscribeEvent
    public static void serverTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        if (server == null) {
            return;
        }

        // 遍历所有维度（ServerLevel）并更新被驱离的实体
        for (ServerLevel level : server.getAllLevels()) {
            DriveAwayExecutor.tickAllDriveAwayEntities(level);
        }
    }

}
