package org.confluence.terraentity.init;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.lib.ConfluenceMagicLib;
import org.confluence.lib.common.item.GroupItem;
import org.confluence.lib.util.WipNotDisplayOutput;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.item.*;
import org.confluence.terraentity.item.DebugItem;
import org.confluence.terraentity.item.HouseDetectItem;
import org.confluence.terraentity.runtime.TERuntime;
import org.confluence.terraentity.utils.TEUtils;

import java.util.function.Consumer;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TEItems {
    public static final DeferredRegister.Items TOOLS = DeferredRegister.createItems(MODID);

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredItem<Item> HOUSE_DETECTOR = TOOLS.register("house_detector", () -> new HouseDetectItem(new Item.Properties().stacksTo(1)));


    // Sentry Items
//    public static final DeferredItem<Item> SENTRY_STAFF = SENTRY_ITEMS.register("sentry_staff", () -> new SentryItem<>(new Item.Properties(), TEEntities.SUMMON_HORNET, 1, 5));
    public static final DeferredItem<Item> DEBUG_ITEM = TOOLS.register("debug_item", () -> new DebugItem(new Item.Properties().stacksTo(1)));


    public static final DeferredHolder<CreativeModeTab,CreativeModeTab> NEO_TERRA =
            TABS.register(MODID + "_tab", ()-> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.terraentity.title"))
                    .icon(()-> TESpawnEggItems.KING_SLIME_SPAWN_EGG.asItem().getDefaultInstance())
                    .displayItems((itemDisplayParameters, output) -> {
                        WipNotDisplayOutput wrappedOutput = new WipNotDisplayOutput(output);
                        Consumer<DeferredHolder<Item, ? extends Item>> action = item -> wrappedOutput.accept(item.get());
                        // todo 在泰拉生物单独运行时，启用 TESpawnEggItems.ITEMS.getEntries().forEach(action);
                        // 猩红生物
                        CreativeModeTab.Output crimson = GroupItem.belongsTo("crimson_entity", output);
                        crimson.accept(TESpawnEggItems.CRIMSLIME_SPAWN_EGG.get());
                        crimson.accept(TESpawnEggItems.BLOOD_CRAWLER_SPAWN_EGG.get());
                        crimson.accept(TESpawnEggItems.BLOODY_SPORE_SPAWN_EGG.get());
                        crimson.accept(TESpawnEggItems.CRIMERA_SPAWN_EGG.get());
                        crimson.accept(TESpawnEggItems.HERPLING_SPAWN_EGG.get());
                        crimson.accept(TESpawnEggItems.FACE_MONSTER_SPAWN_EGG.get());

// 腐化生物
                        CreativeModeTab.Output corruption = GroupItem.belongsTo("corruption_entity", output);
                        corruption.accept(TESpawnEggItems.CORRUPT_SLIME_SPAWN_EGG.get());
                        corruption.accept(TESpawnEggItems.EATER_OF_SOULS_SPAWN_EGG.get());
                        corruption.accept(TESpawnEggItems.DEVOURER_SPAWN_EGG.get());
                        corruption.accept(TESpawnEggItems.DECAYEDER_SPAWN_EGG.get());

// 神圣生物
                        CreativeModeTab.Output hallow = GroupItem.belongsTo("hallow_entity", output);
                        hallow.accept(TESpawnEggItems.LUMINOUS_SLIME_SPAWN_EGG.get());
                        hallow.accept(TESpawnEggItems.PIXIE_SPAWN_EGG.get());

// 沙漠生物
                        CreativeModeTab.Output desert = GroupItem.belongsTo("desert_entity", output);
                        desert.accept(TESpawnEggItems.DESERT_SLIME_SPAWN_EGG.get());
                        desert.accept(TESpawnEggItems.ANTLION_SWARMER_SPAWN_EGG.get());
                        desert.accept(TESpawnEggItems.GIANT_ANTLION_SWARMER_SPAWN_EGG.get());
                        desert.accept(TESpawnEggItems.MUMMY_SPAWN_EGG.get());
                        desert.accept(TESpawnEggItems.DARK_MUMMY_SPAWN_EGG.get());
                        desert.accept(TESpawnEggItems.BLOOD_MUMMY_SPAWN_EGG.get());
                        desert.accept(TESpawnEggItems.LIGHT_MUMMY_SPAWN_EGG.get());
                        desert.accept(TESpawnEggItems.SAND_POACHER_SPAWN_EGG.get());
                        desert.accept(TESpawnEggItems.DARK_LAMIA_SPAWN_EGG.get());
                        desert.accept(TESpawnEggItems.LIGHT_LAMIA_SPAWN_EGG.get());
                        desert.accept(TESpawnEggItems.GHOUL_SPAWN_EGG.get());
                        desert.accept(TESpawnEggItems.TAINTED_GHOUL_SPAWN_EGG.get());
                        desert.accept(TESpawnEggItems.VILE_GHOUL_SPAWN_EGG.get());
                        desert.accept(TESpawnEggItems.DREAMER_GHOUL_SPAWN_EGG.get());
                        desert.accept(TESpawnEggItems.TOMB_CRAWLER_SPAWN_EGG.get());

// 丛林生物
                        CreativeModeTab.Output jungle = GroupItem.belongsTo("jungle_entity", output);
                        jungle.accept(TESpawnEggItems.JUNGLE_SLIME_SPAWN_EGG.get());
                        jungle.accept(TESpawnEggItems.SPIKED_JUNGLE_SLIME_SPAWN_EGG.get());
                        jungle.accept(TESpawnEggItems.JUNGLE_BAT_SPAWN_EGG.get());
                        jungle.accept(TESpawnEggItems.HORNET_SPAWN_EGG.get());
                        jungle.accept(TESpawnEggItems.MAN_EATER_SPAWN_EGG.get());
                        jungle.accept(TESpawnEggItems.TROPIC_SLIME_SPAWN_EGG.get());
                        jungle.accept(TESpawnEggItems.DERPLING_SPAWN_EGG.get());
                        jungle.accept(TESpawnEggItems.HONEY_SLIME_SPAWN_EGG.get());
                        jungle.accept(TESpawnEggItems.SNATCHER_SPAWN_EGG.get());

// 冰雪生物
                        CreativeModeTab.Output ice = GroupItem.belongsTo("ice_entity", output);
                        ice.accept(TESpawnEggItems.ICE_SLIME_SPAWN_EGG.get());
                        ice.accept(TESpawnEggItems.SPIKED_ICE_SLIME_SPAWN_EGG.get());
                        ice.accept(TESpawnEggItems.ICE_BAT_SPAWN_EGG.get());
                        ice.accept(TESpawnEggItems.SNOW_FLINX_SPAWN_EGG.get());
                        ice.accept(TESpawnEggItems.UNDEAD_VIKING_SPAWN_EGG.get());
// 森林生物
                        CreativeModeTab.Output forest = GroupItem.belongsTo("forest_entity", output);
                        forest.accept(TESpawnEggItems.PURPLE_SLIME_SPAWN_EGG.get());
                        forest.accept(TESpawnEggItems.BLUE_SLIME_SPAWN_EGG.get());
                        forest.accept(TESpawnEggItems.GREEN_SLIME_SPAWN_EGG.get());
                        forest.accept(TESpawnEggItems.PINK_SLIME_SPAWN_EGG.get());
                        forest.accept(TESpawnEggItems.GOLDEN_SLIME_SPAWN_EGG.get());
                        forest.accept(TESpawnEggItems.DEMON_EYE_SPAWN_EGG.get());
                        forest.accept(TESpawnEggItems.POSSESS_ARMOR_SPAWN_EGG.get());
                        forest.accept(TESpawnEggItems.WRAITH_SPAWN_EGG.get());
                        forest.accept(TESpawnEggItems.SWAMP_SLIME_SPAWN_EGG.get());
                        forest.accept(TESpawnEggItems.SPIKED_SLIME_SPAWN_EGG.get());
                        forest.accept(TESpawnEggItems.GREEN_DUMPLING_SLIME_SPAWN_EGG.get());
// 动物
                        forest.accept(TESpawnEggItems.SQUIRREL_SPAWN_EGG.get());
                        forest.accept(TESpawnEggItems.JEWEL_SQUIRREL_SPAWN_EGG.get());
                        forest.accept(TESpawnEggItems.BUNNY_SPAWN_EGG.get());
                        forest.accept(TESpawnEggItems.JEWEL_BUNNY_SPAWN_EGG.get());
                        forest.accept(TESpawnEggItems.EXPLOSIVE_BUNNY_SPAWN_EGG.get());
                        forest.accept(TESpawnEggItems.DUCK_SPAWN_EGG.get());
                        forest.accept(TESpawnEggItems.BIRD_SPAWN_EGG.get());
                        forest.accept(TESpawnEggItems.BLUE_JAY_SPAWN_EGG.get());
                        forest.accept(TESpawnEggItems.CARDINAL_SPAWN_EGG.get());
// 地下生物
                        CreativeModeTab.Output underground = GroupItem.belongsTo("underground_entity", output);
                        underground.accept(TESpawnEggItems.BLACK_SLIME_SPAWN_EGG.get());
                        underground.accept(TESpawnEggItems.RED_SLIME_SPAWN_EGG.get());
                        underground.accept(TESpawnEggItems.YELLOW_SLIME_SPAWN_EGG.get());
                        underground.accept(TESpawnEggItems.CAVE_BAT_SPAWN_EGG.get());
                        underground.accept(TESpawnEggItems.GIANT_SHELLY_SPAWN_EGG.get());
                        underground.accept(TESpawnEggItems.CRAWDAD_SPAWN_EGG.get());
                        underground.accept(TESpawnEggItems.GIANT_WORM_SPAWN_EGG.get());
                        underground.accept(TESpawnEggItems.NYMPH_SPAWN_EGG.get());

// 发光蘑菇地
                        CreativeModeTab.Output mushroom = GroupItem.belongsTo("mushroom_entity", output);
                        mushroom.accept(TESpawnEggItems.SPORE_BAT_SPAWN_EGG.get());
                        mushroom.accept(TESpawnEggItems.SPORE_SKELETON_SPAWN_EGG.get());
                        mushroom.accept(TESpawnEggItems.SPORE_ZOMBIE_SPAWN_EGG.get());
                        mushroom.accept(TESpawnEggItems.HAT_SPORE_ZOMBIE_SPAWN_EGG.get());
// 地牢
                        CreativeModeTab.Output dungeon = GroupItem.belongsTo("dungeon_entity", output);
                        dungeon.accept(TESpawnEggItems.ANGER_BONES_SPAWN_EGG.get());
                        dungeon.accept(TESpawnEggItems.SHORT_BONES_SPAWN_EGG.get());
                        dungeon.accept(TESpawnEggItems.BIG_BONES_SPAWN_EGG.get());
                        dungeon.accept(TESpawnEggItems.BIG_ANGER_BONES_SPAWN_EGG.get());
                        dungeon.accept(TESpawnEggItems.BIG_MUSCLE_ANGER_BONES_SPAWN_EGG.get());
                        dungeon.accept(TESpawnEggItems.BIG_HELMET_ANGER_BONES_SPAWN_EGG.get());
                        dungeon.accept(TESpawnEggItems.CURSED_SKULL_SPAWN_EGG.get());
                        dungeon.accept(TESpawnEggItems.DARK_CASTER_SPAWN_EGG.get());
                        dungeon.accept(TESpawnEggItems.DUNGEON_GUARDIAN_SPAWN_EGG.get());
                        dungeon.accept(TESpawnEggItems.DUNGEON_SLIME_SPAWN_EGG.get());

// 地狱
                        CreativeModeTab.Output nether = GroupItem.belongsTo("nether_entity", output);
                        nether.accept(TESpawnEggItems.FIRE_IMG_SPAWN_EGG.get());
                        nether.accept(TESpawnEggItems.DEMON_SPAWN_EGG.get());
                        nether.accept(TESpawnEggItems.VOODOO_DEMON_SPAWN_EGG.get());
                        nether.accept(TESpawnEggItems.LAVA_SLIME_SPAWN_EGG.get());
                        nether.accept(TESpawnEggItems.HELL_BAT_SPAWN_EGG.get());
                        nether.accept(TESpawnEggItems.BONE_SERPENT_SPAWN_EGG.get());
                        nether.accept(TESpawnEggItems.WITHER_BONE_SERPENT_SPAWN_EGG.get());
// 天空生物
                        CreativeModeTab.Output sky = GroupItem.belongsTo("sky_entity", output);
                        sky.accept(TESpawnEggItems.HARPY_SPAWN_EGG.get());
                        sky.accept(TESpawnEggItems.WYVERN_SPAWN_EGG.get());

// 宝箱怪 (独立分类，可自由移动)
                        CreativeModeTab.Output mimic = GroupItem.belongsTo("mimic_entity", output);
                        mimic.accept(TESpawnEggItems.WOODEN_MIMIC_SPAWN_EGG.get());
                        mimic.accept(TESpawnEggItems.GOLDEN_MIMIC_SPAWN_EGG.get());
                        mimic.accept(TESpawnEggItems.SHADOW_MIMIC_SPAWN_EGG.get());
                        mimic.accept(TESpawnEggItems.ICE_MIMIC_SPAWN_EGG.get());
                        mimic.accept(TESpawnEggItems.CRIMSON_MIMIC_SPAWN_EGG.get());
                        mimic.accept(TESpawnEggItems.CORRUPT_MIMIC_SPAWN_EGG.get());
                        mimic.accept(TESpawnEggItems.HALLOWED_MIMIC_SPAWN_EGG.get());
                        mimic.accept(TESpawnEggItems.JUNGLE_MIMIC_SPAWN_EGG.get());

// 哥布林军队 (独立分类)
                        CreativeModeTab.Output goblin = GroupItem.belongsTo("goblin_entity", output);
                        goblin.accept(TESpawnEggItems.GOBLIN_SORCERER_SPAWN_EGG.get());
                        goblin.accept(TESpawnEggItems.GOBLIN_ARCHER_SPAWN_EGG.get());
                        goblin.accept(TESpawnEggItems.GOBLIN_PEON_SPAWN_EGG.get());
                        goblin.accept(TESpawnEggItems.GOBLIN_WARRIOR_SPAWN_EGG.get());
                        goblin.accept(TESpawnEggItems.GOBLIN_THIEF_SPAWN_EGG.get());
                        goblin.accept(TESpawnEggItems.GOBLIN_SCOUT_SPAWN_EGG.get());
                        goblin.accept(TESpawnEggItems.ANGER_GOBLIN_SPAWN_EGG.get());

// 水生生物 (独立分类)
                        CreativeModeTab.Output water = GroupItem.belongsTo("water_entity", output);
                        water.accept(TESpawnEggItems.PIRANHA_SPAWN_EGG.get());
                        water.accept(TESpawnEggItems.SHARK_SPAWN_EGG.get());
                        water.accept(TESpawnEggItems.ARAPAIMA_SPAWN_EGG.get());
                        water.accept(TESpawnEggItems.BLUE_JELLYFISH_SPAWN_EGG.get());
                        water.accept(TESpawnEggItems.PINK_JELLYFISH_SPAWN_EGG.get());
                        water.accept(TESpawnEggItems.GREEN_JELLYFISH_SPAWN_EGG.get());
                        water.accept(TESpawnEggItems.CRAB_SPAWN_EGG.get());
// 昆虫生物 (独立分类)
                        CreativeModeTab.Output insect = GroupItem.belongsTo("insect_entity", output);
                        insect.accept(TESpawnEggItems.GLOWING_SNAIL_SPAWN_EGG.get());
                        insect.accept(TESpawnEggItems.GRUBBY_SPAWN_EGG.get());
                        insect.accept(TESpawnEggItems.MAGGOT_SPAWN_EGG.get());
                        insect.accept(TESpawnEggItems.MAGMA_SNAIL_SPAWN_EGG.get());
                        insect.accept(TESpawnEggItems.SLUGGY_SPAWN_EGG.get());
                        insect.accept(TESpawnEggItems.SNAIL_SPAWN_EGG.get());
                        insect.accept(TESpawnEggItems.BUTTERFLY_SPAWN_EGG.get());
                        insect.accept(TESpawnEggItems.HELL_BUTTERFLY_SPAWN_EGG.get());
                        insect.accept(TESpawnEggItems.PRISMATIC_LACEWING_SPAWN_EGG.get());
                        insect.accept(TESpawnEggItems.DRAGONFLY_SPAWN_EGG.get());
                        insect.accept(TESpawnEggItems.FAIRY_SPAWN_EGG.get());
                        insect.accept(TESpawnEggItems.FEALING_SPAWN_EGG.get());
                        insect.accept(TESpawnEggItems.GRASSHOPPER_SPAWN_EGG.get());
                        insect.accept(TESpawnEggItems.LADYBUG_SPAWN_EGG.get());
                        insect.accept(TESpawnEggItems.SCORPION_SPAWN_EGG.get());
                        insect.accept(TESpawnEggItems.WORM_SPAWN_EGG.get());
// NPC (独立分类)
                        CreativeModeTab.Output npc = GroupItem.belongsTo("npc_entity", output);
                        npc.accept(TESpawnEggItems.GUIDE_SPAWN_EGG.get());
                        npc.accept(TESpawnEggItems.DEMOLITIONIST_SPAWN_EGG.get());
                        npc.accept(TESpawnEggItems.GOBLIN_TINKERER_SPAWN_EGG.get());
                        npc.accept(TESpawnEggItems.ARMS_DEALER_SPAWN_EGG.get());
                        npc.accept(TESpawnEggItems.NURSE_SPAWN_EGG.get());
                        npc.accept(TESpawnEggItems.MERCHANT_SPAWN_EGG.get());
                        npc.accept(TESpawnEggItems.PAINTER_SPAWN_EGG.get());
                        npc.accept(TESpawnEggItems.DRYAD_SPAWN_EGG.get());
                        npc.accept(TESpawnEggItems.DYE_TRADER_SPAWN_EGG.get());
                        npc.accept(TESpawnEggItems.ANGLER_SPAWN_EGG.get());
                        npc.accept(TESpawnEggItems.OLD_MAN_SPAWN_EGG.get());
                        npc.accept(TESpawnEggItems.MECHANIC_SPAWN_EGG.get());
                        npc.accept(TESpawnEggItems.TRAVELING_MERCHANT_SPAWN_EGG.get());
                        npc.accept(TESpawnEggItems.WITCH_DOCTOR_SPAWN_EGG.get());
                        npc.accept(TESpawnEggItems.PARTY_GIRL_SPAWN_EGG.get());
                        npc.accept(TESpawnEggItems.CLOTHIER_SPAWN_EGG.get());
                        npc.accept(TESpawnEggItems.ZOOLOGIST_SPAWN_EGG.get());
                        npc.accept(TESpawnEggItems.TRUFFLE_SPAWN_EGG.get());
                        npc.accept(TESpawnEggItems.WIZARD_SPAWN_EGG.get());
// BOSS 总集
                        CreativeModeTab.Output boss = GroupItem.belongsTo("boss_entity", output);
                        boss.accept(TESpawnEggItems.KING_SLIME_SPAWN_EGG.get());
                        boss.accept(TESpawnEggItems.EYE_OF_CTHULHU_SPAWN_EGG.get());
                        boss.accept(TESpawnEggItems.EATER_OF_WORLD_SPAWN_EGG.get());
                        boss.accept(TESpawnEggItems.BRAIN_OF_CTHULHU_SPAWN_EGG.get());
                        boss.accept(TESpawnEggItems.QUEEN_BEE_SPAWN_EGG.get());
                        boss.accept(TESpawnEggItems.SKELETRON_SPAWN_EGG.get());
                        boss.accept(TESpawnEggItems.DEERCLOPS_SPAWN_EGG.get());
                        boss.accept(TESpawnEggItems.WALL_OF_FLESH_SPAWN_EGG.get());
                        boss.accept(TESpawnEggItems.HILL_OF_FLESH_SPAWN_EGG.get());
                        boss.accept(TESpawnEggItems.PRIME_ENDER_DRAGON_SPAWN_EGG.get());
                        boss.accept(TESpawnEggItems.RETINAZER_SPAWN_EGG.get());
                        boss.accept(TESpawnEggItems.SPAZMATISM_SPAWN_EGG.get());
                        boss.accept(TESpawnEggItems.THE_TWINS_SPAWN_EGG.get());
                        boss.accept(TESpawnEggItems.SKELETRON_PRIME_SPAWN_EGG.get());
                        boss.accept(TESpawnEggItems.THE_DESTROYER_SPAWN_EGG.get());
                        boss.accept(TESpawnEggItems.PLANTERA_SPAWN_EGG.get());

// 其余
                        CreativeModeTab.Output misc = GroupItem.belongsTo("misc_entity", output);
                        misc.accept(TESpawnEggItems.DRIPPLER_SPAWN_EGG.get());
                        misc.accept(TESpawnEggItems.BLOOD_ZOMBIE_SPAWN_EGG.get());
                        misc.accept(TESpawnEggItems.GRANTITE_ELEMENTAL_SPAWN_EGG.get());
                        misc.accept(TESpawnEggItems.FLYING_FISH_SPAWN_EGG.get());
                        misc.accept(TESpawnEggItems.GHOST_SPAWN_EGG.get());
                        misc.accept(TESpawnEggItems.WANDERING_EYE_FISH_SPAWN_EGG.get());
                        misc.accept(TESpawnEggItems.METEOR_HEAD_SPAWN_EGG.get());
                        if(!ConfluenceMagicLib.IS_CONFLUENCE_LOAD || TERuntime.isDevMode()) {
                            TEBossSummonsItems.ITEMS.getEntries().forEach(action);
                        }
                        TERideableItems.ITEMS.getEntries().forEach(action);
                        TEPetItems.ITEMS.getEntries().forEach(action);
                        // todo 在和本体一起时不使用以下四条
                        TESummonItems.ITEMS.getEntries().forEach(action);
                        TEWhipItems.ITEMS.getEntries().forEach(action);
                        TEBoomerangItems.ITEMS.getEntries().forEach(action);
                        TEYoyosItems.ITEMS.getEntries().forEach(action);
                        // todo 在和本体一起时不使用以上四条
                        TEArmors.ITEMS.getEntries().forEach(action);
                        TEItems.TOOLS.getEntries().forEach(action);
                        TEBlocks.BLOCKITEMS.getEntries().forEach(action);
                        HolderLookup.RegistryLookup<Enchantment> registryLookup = itemDisplayParameters.holders().lookupOrThrow(Registries.ENCHANTMENT);
                        registryLookup.listElements().forEach(enchantment -> {
                            if(enchantment.getKey() != null && enchantment.getKey().location().getNamespace().equals(MODID)) {
                                wrappedOutput.accept(TEUtils.enchantedBook(enchantment.getDelegate(), enchantment.value().getMaxLevel()));
                            }
                        });
//                        output.accept(TEUtils.enchantedBook(registryLookup, TEEnchantments.MULTI_BOOMERANG, 3));
//                        output.accept(TEUtils.enchantedBook(registryLookup, TEEnchantments.WHIP_SWEEP, 1));
                    })
                    //.withTabsAfter(ResourceKey.create(Registries.CREATIVE_MODE_TAB, TerraEntity.fromSpaceAndPath("terra_moment", "tab")))
                    .withTabsAfter(ResourceKey.create(Registries.CREATIVE_MODE_TAB, TerraEntity.fromSpaceAndPath("enemybanner", "enemybanner_tab")))
                    .withTabsBefore(
                            ResourceKey.create(Registries.CREATIVE_MODE_TAB, TerraEntity.fromSpaceAndPath("confluence", "summoners")),
                            CreativeModeTabs.SPAWN_EGGS
                    ).build());

    public static void register(IEventBus bus) {
        TESpawnEggItems.ITEMS.register(bus);
        TEBossSummonsItems.ITEMS.register(bus);
        TEPetItems.ITEMS.register(bus);
        TESummonItems.ITEMS.register(bus);
        TEWhipItems.ITEMS.register(bus);
        TEBoomerangItems.ITEMS.register(bus);
        TERideableItems.ITEMS.register(bus);
        TEItems.TOOLS.register(bus);
        TEYoyosItems.ITEMS.register(bus);
        TEArmors.register(bus);
//        SENTRY_ITEMS.register(bus);
        TABS.register(bus);

    }
}
