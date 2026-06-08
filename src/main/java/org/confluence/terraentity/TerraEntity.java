package org.confluence.terraentity;


import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.confluence.terraentity.config.ServerConfig;
import org.confluence.terraentity.data.biome.TEBiomes;
import org.confluence.terraentity.data.enchantment.TEEnchantments;
import org.confluence.terraentity.event.TEModEvents;
import org.confluence.terraentity.init.*;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.runtime.TERuntime;
import org.mesdag.portlib.network.PortNetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TerraEntity.MODID)
public class TerraEntity {
    public static final String MODID = "terra_entity";
    public static final Logger LOGGER = LoggerFactory.getLogger("TerraEntity");
    public static final PortNetworkHandler NETWORK_HANDLER = new PortNetworkHandler(MODID, "1");

    public static ResourceLocation space(String path) {return ResourceLocation.fromNamespaceAndPath(MODID, path);}

    public static ResourceLocation parse(String path) {return ResourceLocation.parse(path);}

    public static ResourceLocation fromSpaceAndPath(String space, String path) {return ResourceLocation.fromNamespaceAndPath(space, path);}

    public static ResourceLocation defaultPath(String path) {return ResourceLocation.withDefaultNamespace(path);}

    public static String toLang(ResourceLocation location) {return location.toLanguageKey().replace("/", ".");}

    public TerraEntity(FMLJavaModLoadingContext context) {
        IEventBus eventBus = context.getModEventBus();

        TEEntities.register(eventBus);
        TERegistries.register(eventBus);

        TESounds.SOUNDS.register(eventBus);
        TEParticles.PARTICLES.register(eventBus);
        TEItems.register(eventBus);
        TEEffects.EFFECTS.register(eventBus);
        TEAttachments.TYPES.register(eventBus);
        TEDataComponentTypes.TYPES.register(eventBus);
        TEEntityDataSerializers.SERIALIZERS.register(eventBus);
        TEBlocks.register(eventBus);
        TEAi.register(eventBus);
        TEMenus.TYPES.register(eventBus);
        TEBiomes.register(eventBus);
        TELoots.register(eventBus);
        TEEnchantments.register(eventBus);
        eventBus.addListener(TEDataMaps::registerDataMapTypes);

        context.registerConfig(ModConfig.Type.SERVER, ServerConfig.init());

        ModChecker.registerEvents(eventBus);
        TERuntime.getInstance().start();
//        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        TEModEvents.init();
    }
}
