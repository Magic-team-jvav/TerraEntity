package org.confluence.terraentity.data.gen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.item.TESummonItems;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public record TESubLoot(HolderLookup.Provider registries) implements LootTableSubProvider {


    public static final ResourceKey<LootTable> SPAWN_WOODEN_SWORD_STAFF = getLootTableKey("chest/spawn_wooden_sword_staff");
    public static final ResourceKey<LootTable> SPAWN_STONE_SWORD_STAFF = getLootTableKey("chest/spawn_stone_sword_staff");
    public static final ResourceKey<LootTable> SPAWN_IRON_SWORD_STAFF = getLootTableKey("chest/spawn_iron_sword_staff");
    public static final ResourceKey<LootTable> SPAWN_GOLDEN_SWORD_STAFF = getLootTableKey("gameplay/spawn_golden_sword_staff");
    public static final ResourceKey<LootTable> SPAWN_DIAMOND_SWORD_STAFF = getLootTableKey("entities/spawn_diamond_sword_staff");
    public static final ResourceKey<LootTable> SPAWN_NETHERITE_SWORD_STAFF = getLootTableKey("chest/spawn_netherite_sword_staff");

    public static final ResourceKey<LootTable> SPAWN_SCULK_WISP_STAFF = getLootTableKey("chest/spawn_sculk_wisp_staff");

    // 泰拉饰品联动
    public static final ResourceKey<LootTable> SPAWN_royal_gel = getLootTableKey("entities/spawn_royal_gel");
    public static final ResourceKey<LootTable> SPAWN_shield_of_cthulhu = getLootTableKey("entities/spawn_shield_of_cthulhu");
    public static final ResourceKey<LootTable> SPAWN_hive_pack = getLootTableKey("entities/spawn_hive_pack");
    public static final ResourceKey<LootTable> SPAWN_brain_of_confusion = getLootTableKey("entities/spawn_brain_of_confusion");
    public static final ResourceKey<LootTable> SPAWN_bone_glove = getLootTableKey("entities/spawn_bone_glove");
    public static final ResourceKey<LootTable> SPAWN_worm_scarf = getLootTableKey("entities/spawn_worm_scarf");


    public static final ResourceKey<LootTable> SPAWN_metal_detector = getLootTableKey("entities/spawn_metal_detector");
    public static final ResourceKey<LootTable> SPAWN_tally_counter = getLootTableKey("entities/spawn_tally_counter");
    public static final ResourceKey<LootTable> SPAWN_magma_stone = getLootTableKey("entities/spawn_magma_stone");
    public static final ResourceKey<LootTable> SPAWN_obsidian_rose = getLootTableKey("entities/spawn_obsidian_rose");
    public static final ResourceKey<LootTable> SPAWN_giant_shelly = getLootTableKey("entities/spawn_giant_shelly");
    public static final ResourceKey<LootTable> SPAWN_compass = getLootTableKey("entities/spawn_compass");
    public static final ResourceKey<LootTable> SPAWN_fast_clock = getLootTableKey("entities/spawn_fast_clock");
    public static final ResourceKey<LootTable> SPAWN_depth_meter = getLootTableKey("entities/spawn_depth_meter");
    public static final ResourceKey<LootTable> SPAWN_bezoar = getLootTableKey("entities/spawn_bezoar");


    @Override
    public void generate(@NotNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {

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
    }

    static ResourceKey<LootTable> getLootTableKey(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, TerraEntity.space("modifier/" + name));
    }
}
