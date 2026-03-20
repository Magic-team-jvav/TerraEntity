package org.confluence.terraentity.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    public static ForgeConfigSpec.ConfigValue<Boolean> BOSS_CLEAR_WHEN_NO_TARGET;
    public static ForgeConfigSpec.ConfigValue<Double> BOSS_ATTRIBUTES_MULTIPLIER_HEALTH;
    public static ForgeConfigSpec.ConfigValue<Double> BOSS_ATTRIBUTES_MULTIPLIER_DAMAGE;
    public static ForgeConfigSpec.ConfigValue<Boolean> BOSS_NO_PHYSICS;
    public static ForgeConfigSpec.ConfigValue<Boolean> BOSS_LEAVE_ON_DAY;
    public static ForgeConfigSpec.ConfigValue<Boolean> BOSS_KEEP_WANDERING;


    public static ForgeConfigSpec.ConfigValue<Boolean> ENHANCE_ALL_MONSTER;
    public static ForgeConfigSpec.ConfigValue<Double> MONSTER_ATTRIBUTES_MULTIPLIER_HEALTH;
    public static ForgeConfigSpec.ConfigValue<Double> MONSTER_ATTRIBUTES_MULTIPLIER_DAMAGE ;
    public static ForgeConfigSpec.ConfigValue<Boolean> DISABLE_BUILTIN_MODIFIER;

    public static ForgeConfigSpec.ConfigValue<Boolean> SPAWN_WITHOUT_LIGHT;

    public static ForgeConfigSpec.ConfigValue<Double> CHANCE_TO_SPAWN_SLIME_ON_ZOMBIE_HEAD;
    public static ForgeConfigSpec.ConfigValue<Double> ENEMY_SPAWN_CHANCE;
    public static ForgeConfigSpec.ConfigValue<Boolean> ENEMY_SPAWN_CHANCE_APPLY_ALL;

    public static ForgeConfigSpec.ConfigValue<Integer> BEHAVIOR_TREE_WEB_VIEWER_SERVER_PORT;

    public static ForgeConfigSpec.Builder init(ForgeConfigSpec.Builder BUILDER){
        BUILDER.push("server");

        BOSS_CLEAR_WHEN_NO_TARGET = BUILDER
                .comment("When a boss has no target, should it be cleared?")
                .define("boss_clear_when_no_target", true);
        BOSS_ATTRIBUTES_MULTIPLIER_HEALTH = BUILDER
                .comment("Multiplier for boss attributes health.")
                .defineInRange("boss_attributes_multiplier_health", 0.5F, 0.0625f, 10f);
        BOSS_ATTRIBUTES_MULTIPLIER_DAMAGE = BUILDER
                .comment("Multiplier for boss attributes damage.")
                .defineInRange("boss_attributes_multiplier_damage", 0.5F, 0.0625f, 10f);

        BOSS_NO_PHYSICS = BUILDER
                .comment("Should the boss have no physics? Only for some bosses.")
                .define("boss_no_physics", true);

        BOSS_LEAVE_ON_DAY = BUILDER
                .comment("Should the boss leave on day? Only for some bosses.")
                .define("boss_leave_on_day", false);

        BOSS_KEEP_WANDERING = BUILDER
                .comment("Should bosses random stroll when have no target? ")
                .define("boss_keep_wandering", true);

//        RESPAWN_PROTECT = BUILDER
//                .comment("Should players be protected from respawning?.If true, nearby bosses will be discard")
//                .define("respawn_protect", true);

        ENHANCE_ALL_MONSTER = BUILDER
                .comment("Should all monsters be enhanced?\nIf false, only specific monsters in this mod.")
                .define("enhance_all_monster", false);

        MONSTER_ATTRIBUTES_MULTIPLIER_HEALTH = BUILDER
                .comment("Multiplier for monster attributes health.")
                .defineInRange("monster_attributes_multiplier_health", 0.5F, 0.0625f, 100f);
        MONSTER_ATTRIBUTES_MULTIPLIER_DAMAGE = BUILDER
                .comment("Multiplier for monster attributes damage.")
                .defineInRange("monster_attributes_multiplier_damage", 0.5F, 0.0625f, 100f);
        DISABLE_BUILTIN_MODIFIER = BUILDER
                .comment("Should the built-in attributes setting be disabled for monsters?")
                .comment("For kjs modify")
                .define("disable_builtin_health_attack_modifier", false);
        SPAWN_WITHOUT_LIGHT = BUILDER
                .comment("Should monsters spawn without light?")
                .define("spawn_without_light", false);

        CHANCE_TO_SPAWN_SLIME_ON_ZOMBIE_HEAD = BUILDER
                .comment("Chance to spawn slime on zombie head.")
                .defineInRange("chance_to_spawn_slime_on_zombie_head", 0.05, 0, 1);
        ENEMY_SPAWN_CHANCE = BUILDER
                .comment("Chance to spawn a monster.")
                .defineInRange("enemy_spawn_chance", 1D, 0, 1);
        ENEMY_SPAWN_CHANCE_APPLY_ALL = BUILDER
                .comment("Should the chance to spawn a monster apply to all monsters?")
                .define("enemy_spawn_chance_apply_all", false);

        BEHAVIOR_TREE_WEB_VIEWER_SERVER_PORT = BUILDER
                .comment("Port for behavior web viewer.")
                .defineInRange("behavior_tree_web_viewer_server_port", 59160, 1024, 65535);

        BUILDER.pop();
        return BUILDER;
    }

    public static boolean bossNoPhysics() {
        return BOSS_NO_PHYSICS.get();
    }
}
