package org.confluence.terraentity.event;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.*;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.util.AmountIngredient;
import org.confluence.terraentity.init.TEAttributes;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.network.NetworkHandler;

import java.util.List;
@SuppressWarnings("all")
@Mod.EventBusSubscriber(modid = TerraEntity.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvent {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NetworkHandler.register();
            CraftingHelper.register(AmountIngredient.TYPE(), AmountIngredient.Serializer.INSTANCE);
        });
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
//        ServerConfig.init();
//        ClientConfig.load();

    }

    // 注册新属性
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeModificationEvent event) {
        // 召唤师属性
        List.of(TEAttributes.MINION_CAPACITY, TEAttributes.SENTRY_CAPACITY, TEAttributes.SUMMON_DAMAGE, TEAttributes.SUMMON_KNOCKBACK, TEAttributes.WHIP_RANGE, TEAttributes.MARK_DAMAGE)
                .forEach(att-> event.add(EntityType.PLAYER, att.get()));

    }

    // 注册怪物属性
    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        TEEntities.registerEntityAttributes(event);
    }

    // 注册生成位置
    @SubscribeEvent
    public static void spawnPlacementRegister(SpawnPlacementRegisterEvent event) {
        TEEntities.spawnPlacementRegister(event);
    }




    // 这个事件在注册能力之前调用
    @SubscribeEvent
    public static void registerCapabilitiesBefore(NewRegistryEvent event) {

    }


}
