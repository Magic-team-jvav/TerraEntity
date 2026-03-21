package org.confluence.terraentity.init;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.enchantment.TEEnchantments;
import org.confluence.terraentity.init.item.*;
import org.confluence.terraentity.integration.ModChecker;
import org.confluence.terraentity.item.DebugItem;
import org.confluence.terraentity.item.HouseDetectItem;
import org.confluence.terraentity.runtime.TERuntime;
import org.confluence.terraentity.utils.TEUtils;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TEItems {
    public static final DeferredRegister<Item> TOOLS = DeferredRegister.create(Registries.ITEM, MODID);

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final Supplier<Item> HOUSE_DETECTOR = TOOLS.register("house_detector", () -> new HouseDetectItem(new Item.Properties().stacksTo(1)));


    // Sentry Items
//    public static final DeferredItem<Item> SENTRY_STAFF = SENTRY_ITEMS.register("sentry_staff", () -> new SentryItem<>(new Item.Properties(), TEEntities.SUMMON_HORNET, 1, 5));
    public static final Supplier<Item> DEBUG_ITEM = TOOLS.register("debug_item", () -> new DebugItem(new Item.Properties().stacksTo(1)));


    public static final RegistryObject<CreativeModeTab> NEO_TERRA =
            TABS.register(MODID + "_tab", ()-> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.terraentity.title"))
                    .icon(()-> TESpawnEggItems.KING_SLIME_SPAWN_EGG.get().getDefaultInstance())
                    .displayItems((itemDisplayParameters, output) -> {
//                        WipNotDisplayOutput wrappedOutput = new WipNotDisplayOutput(output);
                        Consumer<RegistryObject<? extends Item>> action = item -> output.accept(item.get());
                        TESpawnEggItems.ITEMS.getEntries().forEach(action);
                        if(!ModChecker.confluence.isLoaded() || TERuntime.isDevMode()) {
                            TEBossSummonsItems.ITEMS.getEntries().forEach(action);
                        }
                        TERideableItems.ITEMS.getEntries().forEach(action);
                        TEPetItems.ITEMS.getEntries().forEach(action);
                        TESummonItems.ITEMS.getEntries().forEach(action);
                        TEWhipItems.ITEMS.getEntries().forEach(action);
                        TEBoomerangItems.ITEMS.getEntries().forEach(action);
                        TEYoyosItems.ITEMS.getEntries().forEach(action);

                        TEItems.TOOLS.getEntries().forEach(action);
                        TEBlocks.BLOCKITEMS.getEntries().forEach(action);
                        HolderLookup.RegistryLookup<Enchantment> registryLookup = itemDisplayParameters.holders().lookupOrThrow(Registries.ENCHANTMENT);
                        output.accept(TEUtils.enchantedBook(registryLookup, TEEnchantments.MULTI_BOOMERANG.getKey(), 3));
                        output.accept(TEUtils.enchantedBook(registryLookup, TEEnchantments.WHIP_SWEEP.getKey(), 1));
                    })
                    //.withTabsAfter(ResourceKey.create(Registries.CREATIVE_MODE_TAB, TerraEntity.fromSpaceAndPath("terra_moment", "tab")))
                    .withTabsAfter(ResourceKey.create(Registries.CREATIVE_MODE_TAB, TerraEntity.fromSpaceAndPath("enemybanner", "enemybanner_tab")))
                    .withTabsBefore(ResourceKey.create(Registries.CREATIVE_MODE_TAB, TerraEntity.fromSpaceAndPath("confluence", "summoners")))
                    .build());

    public static void register(IEventBus bus) {
        TESpawnEggItems.ITEMS.register(bus);
        TEBossSummonsItems.ITEMS.register(bus);
        TEPetItems.register(bus);
        TESummonItems.ITEMS.register(bus);
        TEWhipItems.ITEMS.register(bus);
        TEBoomerangItems.ITEMS.register(bus);
        TERideableItems.ITEMS.register(bus);
        TEItems.TOOLS.register(bus);
        TEYoyosItems.ITEMS.register(bus);
//        TEArmors.register(bus);
//        SENTRY_ITEMS.register(bus);
        TABS.register(bus);

    }
}
