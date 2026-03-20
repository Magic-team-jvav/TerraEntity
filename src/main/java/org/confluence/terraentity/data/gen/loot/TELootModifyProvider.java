package org.confluence.terraentity.data.gen.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.util.AdditionLootModifier;

import javax.annotation.Nullable;

public class TELootModifyProvider extends GlobalLootModifierProvider {

    public TELootModifyProvider(PackOutput output, String modId) {
        super(output,  modId);
    }

    @Override
    public void start() {

        // 丛林府邸箱子
        this.addChestLootModifier("chest/spawn_wooden_sword_staff", BuiltInLootTables.WOODLAND_MANSION, TESubLoot.SPAWN_WOODEN_SWORD_STAFF);
        // 金字塔箱子
        this.addChestLootModifier("chest/spawn_stone_sword_staff", BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY, TESubLoot.SPAWN_STONE_SWORD_STAFF);
        // 铁匠村民箱子
        this.addChestLootModifier("chest/spawn_iron_sword_staff", BuiltInLootTables.VILLAGE_WEAPONSMITH, TESubLoot.SPAWN_IRON_SWORD_STAFF);
        // 猪灵交易
        this.addChestLootModifier("gameplay/spawn_golden_sword_staff", BuiltInLootTables.PIGLIN_BARTERING, TESubLoot.SPAWN_GOLDEN_SWORD_STAFF);
        // 远古守卫者
        this.addChestLootModifier("entities/spawn_diamond_sword_staff", EntityType.ELDER_GUARDIAN.getDefaultLootTable(), TESubLoot.SPAWN_DIAMOND_SWORD_STAFF);
        // 堡垒遗迹珍宝
        this.addChestLootModifier("chest/spawn_netherite_sword_staff", BuiltInLootTables.BASTION_TREASURE, TESubLoot.SPAWN_NETHERITE_SWORD_STAFF);
        // 远古城市
        this.addChestLootModifier("chest/spawn_sculk_wisp_staff", BuiltInLootTables.ANCIENT_CITY, TESubLoot.SPAWN_SCULK_WISP_STAFF);

    }

    private void addChestLootModifier(String name, @Nullable ResourceLocation lootTableId, ResourceLocation lootTableAdd) {
        LootItemCondition condition;
        if (lootTableId != null) {
            condition = LootTableIdCondition.builder(lootTableId).build();
            this.add(name, new AdditionLootModifier(new LootItemCondition[]{condition}, lootTableAdd));
        }else{
            TerraEntity.LOGGER.warn("Loot table id is null for " + name);
        }
    }

}