package org.confluence.terraentity.data.gen.loot;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.item.TESummonItems;

import java.util.function.BiConsumer;

public record TESubLoot() implements LootTableSubProvider {


    public static final ResourceLocation SPAWN_WOODEN_SWORD_STAFF = getLootTableKey("chest/spawn_wooden_sword_staff");
    public static final ResourceLocation SPAWN_STONE_SWORD_STAFF = getLootTableKey("chest/spawn_stone_sword_staff");
    public static final ResourceLocation SPAWN_IRON_SWORD_STAFF = getLootTableKey("chest/spawn_iron_sword_staff");
    public static final ResourceLocation SPAWN_GOLDEN_SWORD_STAFF = getLootTableKey("gameplay/spawn_golden_sword_staff");
    public static final ResourceLocation SPAWN_DIAMOND_SWORD_STAFF = getLootTableKey("entities/spawn_diamond_sword_staff");
    public static final ResourceLocation SPAWN_NETHERITE_SWORD_STAFF = getLootTableKey("chest/spawn_netherite_sword_staff");

    public static final ResourceLocation SPAWN_SCULK_WISP_STAFF = getLootTableKey("chest/spawn_sculk_wisp_staff");

    // 泰拉饰品联动
    public static final ResourceLocation SPAWN_royal_gel = getLootTableKey("entities/spawn_royal_gel");
    public static final ResourceLocation SPAWN_shield_of_cthulhu = getLootTableKey("entities/spawn_shield_of_cthulhu");
    public static final ResourceLocation SPAWN_hive_pack = getLootTableKey("entities/spawn_hive_pack");
    public static final ResourceLocation SPAWN_brain_of_confusion = getLootTableKey("entities/spawn_brain_of_confusion");
    public static final ResourceLocation SPAWN_bone_glove = getLootTableKey("entities/spawn_bone_glove");
    public static final ResourceLocation SPAWN_worm_scarf = getLootTableKey("entities/spawn_worm_scarf");


    public static final ResourceLocation SPAWN_metal_detector = getLootTableKey("entities/spawn_metal_detector");
    public static final ResourceLocation SPAWN_tally_counter = getLootTableKey("entities/spawn_tally_counter");
    public static final ResourceLocation SPAWN_magma_stone = getLootTableKey("entities/spawn_magma_stone");
    public static final ResourceLocation SPAWN_obsidian_rose = getLootTableKey("entities/spawn_obsidian_rose");
    public static final ResourceLocation SPAWN_giant_shelly = getLootTableKey("entities/spawn_giant_shelly");
    public static final ResourceLocation SPAWN_compass = getLootTableKey("entities/spawn_compass");
    public static final ResourceLocation SPAWN_fast_clock = getLootTableKey("entities/spawn_fast_clock");
    public static final ResourceLocation SPAWN_depth_meter = getLootTableKey("entities/spawn_depth_meter");
    public static final ResourceLocation SPAWN_bezoar = getLootTableKey("entities/spawn_bezoar");




    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {

        consumer.accept(SPAWN_WOODEN_SWORD_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SUMMON_WOODEN_SWORD_STAFF.get(), 0.15f))
        );
        //
        consumer.accept(SPAWN_STONE_SWORD_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SUMMON_STONE_SWORD_STAFF.get(), 0.15f))
        );
        consumer.accept(SPAWN_IRON_SWORD_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SUMMON_IRON_SWORD_STAFF.get(), 0.15f))
        );
        consumer.accept(SPAWN_GOLDEN_SWORD_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SUMMON_GOLDEN_SWORD_STAFF.get(), 0.05f))
        );
        consumer.accept(SPAWN_DIAMOND_SWORD_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SUMMON_DIAMOND_SWORD_STAFF.get(), 0.05f))
        );
//        consumer.accept(SPAWN_NETHERITE_SWORD_STAFF, LootTable.lootTable()
//                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SUMMON_NETHERITE_SWORD_STAFF.get(), 0.15f))
//        );
        consumer.accept(SPAWN_SCULK_WISP_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SCULK_WISP_STAFF.get(), 0.3f))
        );

        // 泰拉饰品联动
//        consumer.accept(SPAWN_royal_gel, LootTable.lootTable()
//                .withPool(TEEntityLootProvider.singleItemPool(TCItems.ROYAL_GEL))
//        );
//        consumer.accept(SPAWN_shield_of_cthulhu, LootTable.lootTable()
//                .withPool(TEEntityLootProvider.singleItemPool(TCItems.SHIELD_OF_CTHULHU))
//        );
//        consumer.accept(SPAWN_hive_pack, LootTable.lootTable()
//                .withPool(TEEntityLootProvider.singleItemPool(TCItems.HIVE_PACK))
//        );
//        consumer.accept(SPAWN_brain_of_confusion, LootTable.lootTable()
//                .withPool(TEEntityLootProvider.singleItemPool(TCItems.BRAIN_OF_CONFUSION))
//        );
//        consumer.accept(SPAWN_bone_glove, LootTable.lootTable()
//                .withPool(TEEntityLootProvider.singleItemPool(TCItems.BONE_GLOVE))
//        );
//        consumer.accept(SPAWN_worm_scarf, LootTable.lootTable()
//                .withPool(TEEntityLootProvider.singleItemPool(TCItems.WORM_SCARF))
//        );
//
//
//        consumer.accept(SPAWN_metal_detector, LootTable.lootTable()
//                .withPool(TEEntityLootProvider.singleItemPool(TCItems.METAL_DETECTOR, 1f))
//        );
//        consumer.accept(SPAWN_tally_counter, LootTable.lootTable()
//                .withPool(TEEntityLootProvider.singleItemPool(TCItems.TALLY_COUNTER, 0.01f))
//        );
//        consumer.accept(SPAWN_magma_stone, LootTable.lootTable()
//                .withPool(TEEntityLootProvider.singleItemPool(TCItems.MAGMA_STONE, 0.007f))
//        );
//        consumer.accept(SPAWN_obsidian_rose, LootTable.lootTable()
//                .withPool(TEEntityLootProvider.singleItemPool(TCItems.OBSIDIAN_ROSE, 0.02f))
//        );
//        consumer.accept(SPAWN_giant_shelly, LootTable.lootTable()
//                .withPool(TEEntityLootProvider.singleItemPool(TCItems.DEPTH_METER, 0.013f))
//                .withPool(TEEntityLootProvider.singleItemPool(TCItems.COMPASS, 0.012f))
//        );
//        consumer.accept(SPAWN_compass, LootTable.lootTable()
//                .withPool(TEEntityLootProvider.singleItemPool(TCItems.COMPASS, 0.012f))
//        );
//        consumer.accept(SPAWN_fast_clock, LootTable.lootTable()
//                .withPool(TEEntityLootProvider.singleItemPool(TCItems.FAST_CLOCK, 0.02f))
//        );
//        consumer.accept(SPAWN_depth_meter, LootTable.lootTable()
//                .withPool(TEEntityLootProvider.singleItemPool(TCItems.DEPTH_METER, 0.01f))
//        );
//        consumer.accept(SPAWN_bezoar, LootTable.lootTable()
//                .withPool(TEEntityLootProvider.singleItemPool(TCItems.BEZOAR, 0.01f))
//        );




//
    }

    static ResourceLocation getLootTableKey(String name) {
        return TerraEntity.space(name);
    }
}