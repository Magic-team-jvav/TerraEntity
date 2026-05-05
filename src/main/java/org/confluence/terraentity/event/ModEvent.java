package org.confluence.terraentity.event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.fluids.RegisterCauldronFluidContentEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.confluence.lib.api.event.NameFixRegisterEvent;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.integration.ItemComponentModify;
import org.confluence.terraentity.integration.ModChecker;
import org.confluence.terraentity.integration.curios.CuriosHelper;
import org.confluence.terraentity.network.NetworkHandler;
import org.confluence.terraentity.utils.DriveAwaySystem.DriveAwayExecutor;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = TerraEntity.MODID)
public class ModEvent {
    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        NetworkHandler.register(event);
    }

    // 注册怪物属性
    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
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
