package org.confluence.terraentity.data.gen.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.data.util.AdditionLootModifier;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.integration.ModChecker;

import java.util.stream.Stream;

public class LinkageTCLootModifyProvider extends GlobalLootModifierProvider {

    public LinkageTCLootModifyProvider(PackOutput output) {
        super(output, ModChecker.terraCurio.getId());
    }

    @Override
    protected void start() {
        this.addLootModifier(TEBossEntities.KING_SLIME, TESubLoot.SPAWN_royal_gel);
        this.addLootModifier(TEBossEntities.EYE_OF_CTHULHU, TESubLoot.SPAWN_shield_of_cthulhu);
        this.addLootModifier(TEBossEntities.QUEEN_BEE, TESubLoot.SPAWN_hive_pack);
        this.addLootModifier(TEBossEntities.BRAIN_OF_CTHULHU, TESubLoot.SPAWN_brain_of_confusion);
        this.addLootModifier(TEBossEntities.EATER_OF_WORLDS, TESubLoot.SPAWN_worm_scarf);
        this.addLootModifier(TEBossEntities.SKELETRON, TESubLoot.SPAWN_bone_glove);


        this.addLootModifier(TEMonsterEntities.NYMPH, TESubLoot.SPAWN_metal_detector);
        this.addLootModifier(TEMonsterEntities.CURSED_SKULL, TESubLoot.SPAWN_tally_counter);
        this.addLootModifier(TEMonsterEntities.HELL_BAT, TESubLoot.SPAWN_magma_stone);
        this.addLootModifier(TEMonsterEntities.FIRE_IMP, TESubLoot.SPAWN_obsidian_rose);
        this.addLootModifier(TEMonsterEntities.DARK_CASTER, TESubLoot.SPAWN_tally_counter);
        this.addLootModifier(TEMonsterEntities.GIANT_SHELLY, TESubLoot.SPAWN_giant_shelly);
        this.addLootModifier(TEMonsterEntities.PIRANHA, TESubLoot.SPAWN_compass);
        this.addLootModifier(TEMonsterEntities.PIXIE, TESubLoot.SPAWN_fast_clock);
        this.addLootModifier(TEMonsterEntities.SNOW_FLINX, TESubLoot.SPAWN_compass);
        Stream.of(TEMonsterEntities.CAVE_BAT,TEMonsterEntities.ICE_BAT, TEMonsterEntities.JUNGLE_BAT, TEMonsterEntities.SPORE_BAT).forEach(e->
                this.addLootModifier(e, TESubLoot.SPAWN_depth_meter)
        );
        this.addLootModifier(TEMonsterEntities.HORNET, TESubLoot.SPAWN_bezoar);


    }

    private void addLootModifier(RegistryObject<?> entityType, ResourceLocation lootTableAdd) {
        LootItemCondition condition;
        ResourceLocation lootTableId = ((EntityType<?>) entityType.get()).getDefaultLootTable();
        condition = LootTableIdCondition.builder(lootTableId).build();

        this.add("entities/add_" + entityType.getId().getPath(), new AdditionLootModifier(new LootItemCondition[]{condition}, lootTableAdd));
    }

}