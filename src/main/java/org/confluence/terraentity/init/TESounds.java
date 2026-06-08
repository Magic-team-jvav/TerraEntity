package org.confluence.terraentity.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;


public final class TESounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, TerraEntity.MODID);

    public static final RegistryObject<SoundEvent> ROUTINE_HURT = register("routine_hurt"); // 常规受伤音效
    public static final RegistryObject<SoundEvent> ROUTINE_DEATH = register("routine_death"); // 常规死亡音效
    public static final RegistryObject<SoundEvent> DRIPPLER_HURT = register("drippler_hurt"); // 滴滴怪受伤音效
    public static final RegistryObject<SoundEvent> DRIPPLER_DEATH = register("drippler_death"); // 滴滴怪死亡音效
    public static final RegistryObject<SoundEvent> METAL_HURT = register("metal_hurt"); // 金属受伤音效
    public static final RegistryObject<SoundEvent> METAL_DEATH = register("metal_death"); // 金属死亡（爆炸）音效
    public static final RegistryObject<SoundEvent> ROAR = register("roar"); // boss吼叫
    public static final RegistryObject<SoundEvent> HURRIED_ROARING = register("hurried_roaring"); //疯狗冲刺
    public static final RegistryObject<SoundEvent> DIG_SOUND = register("dig_sound"); //蠕虫挖掘
    public static final RegistryObject<SoundEvent> USE_MOUNTS = register("use_mounts"); // 召唤坐骑
    public static final RegistryObject<SoundEvent> WHIP_ATTACK = register("whip_attack"); // 鞭打
    // 饿鬼
    public static final RegistryObject<SoundEvent> THE_HUNGRY_DEATH = register("the_hungry_death");
    public static final RegistryObject<SoundEvent> THE_HUNGRY_HURT = register("the_hungry_hurt");
    // 血肉墙
    public static final RegistryObject<SoundEvent> WALL_OF_FLESH_HURT = register("wall_of_flesh_hurt");
    public static final RegistryObject<SoundEvent> WALL_OF_FLESH_ROAR = register("wall_of_flesh_roar");
    public static final RegistryObject<SoundEvent> WALL_OF_FLESH_SUMMON = register("wall_of_flesh_summon");
    // 血爬虫
    public static final RegistryObject<SoundEvent> BLOOD_CRAWLER_DEATH = register("blood_crawler_death");
    public static final RegistryObject<SoundEvent> BLOOD_CRAWLER_FREE = register("blood_crawler_free");
    public static final RegistryObject<SoundEvent> BLOOD_CRAWLER_HURT = register("blood_crawler_hurt");
    // 巨型卷壳怪
    public static final RegistryObject<SoundEvent> GIANT_SHELLY_DEATH = register("giant_shelly_death");
    public static final RegistryObject<SoundEvent> GIANT_SHELLY_FREE_0 = register("giant_shelly_free_0");
    public static final RegistryObject<SoundEvent> GIANT_SHELLY_FREE_1 = register("giant_shelly_free_1");
    public static final RegistryObject<SoundEvent> GIANT_SHELLY_HURT = register("giant_shelly_hurt");
    // 飞眼怪
    public static final RegistryObject<SoundEvent> VISUAL_NEURON_DEATH = register("visual_neuron_death");
    public static final RegistryObject<SoundEvent> VISUAL_NEURON_HURT = register("visual_neuron_hurt");
    // 脸怪
    public static final RegistryObject<SoundEvent> FACE_HOOT = register("face_hoot");
    // 僵尸或骷髅
    public static final RegistryObject<SoundEvent> TR_ZOMBIE_DEATH = register("tr_zombie_death");
    public static final RegistryObject<SoundEvent> TR_ZOMBIE_FREE = register("tr_zombie_free");
    public static final RegistryObject<SoundEvent> TR_SKELETON_HURT = register("tr_skeleton_hurt");
    // 蚁狮
    public static final RegistryObject<SoundEvent> ANTLION_DEATH = register("antlion_death");
    public static final RegistryObject<SoundEvent> ANTLION_HURT = register("antlion_hurt");
    public static final RegistryObject<SoundEvent> ANTLION_FREE = register("antlion_free");
    // 蚁狮蜂
    public static final RegistryObject<SoundEvent> ANTLION_SWARMER_DEATH = register("antlion_swarmer_death");
    public static final RegistryObject<SoundEvent> ANTLION_SWARMER_FREE = register("antlion_swarmer_free");
    // 蚁狮射沙音效
    public static final RegistryObject<SoundEvent> SAND_SHOOT = register("sand_shoot");
    // 蝙蝠
    public static final RegistryObject<SoundEvent> BAT_DEATH = register("bat_death");
    // 胭脂虫
    public static final RegistryObject<SoundEvent> BEETLE_DEATH = register("beetle_death");
    // 血水母
    public static final RegistryObject<SoundEvent> BLOOD_JELLY_DEATH = register("blood_jelly_death");
    public static final RegistryObject<SoundEvent> BLOOD_JELLY_FREE = register("blood_jelly_free");
    // 骨蛇
    public static final RegistryObject<SoundEvent> BONE_SERPENT_DEATH = register("bone_serpent_death");
    // 恶魔
    public static final RegistryObject<SoundEvent> DEMON_DEATH = register("demon_death");
    public static final RegistryObject<SoundEvent> DEMON_FREE = register("demon_free");
    public static final RegistryObject<SoundEvent> DEMON_HURT = register("demon_hurt");
    // 地牢幽灵
    public static final RegistryObject<SoundEvent> DUNGEON_SPIRIT_DEATH = register("dungeon_spirit_death");
    public static final RegistryObject<SoundEvent> DUNGEON_SPIRIT_FREE = register("dungeon_spirit_free");
    public static final RegistryObject<SoundEvent> DUNGEON_SPIRIT_HURT = register("dungeon_spirit_hurt");
    // 花岗岩巨人
    public static final RegistryObject<SoundEvent> GRANITE_GOLEM_DEATH = register("granite_golem_death");
    public static final RegistryObject<SoundEvent> GRANITE_GOLEM_HURT = register("granite_golem_hurt");
    public static final RegistryObject<SoundEvent> GRANITE_GOLEM_FREE = register("granite_golem_free");
    // 水母
    public static final RegistryObject<SoundEvent> JELLYFISH_DEATH = register("jellyfish_death");
    public static final RegistryObject<SoundEvent> JELLYFISH_FREE = register("jellyfish_free");
    public static final RegistryObject<SoundEvent> JELLYFISH_HURT = register("jellyfish_hurt");
    // 妖精
    public static final RegistryObject<SoundEvent> PIXIE_DEATH = register("pixie_death");
    public static final RegistryObject<SoundEvent> PIXIE_FREE = register("pixie_free");
    public static final RegistryObject<SoundEvent> PIXIE_HURT = register("pixie_hurt");
    // 灵体相关音效
    public static final RegistryObject<SoundEvent> SOUL_DEATH = register("soul_death");
    // 独角兽相关音效
    public static final RegistryObject<SoundEvent> UNICORN_DEATH = register("unicorn_death");
    public static final RegistryObject<SoundEvent> UNICORN_HURT = register("unicorn_hurt");
    // 飞龙相关音效
    public static final RegistryObject<SoundEvent> WYVERN_DEATH = register("wyvern_death");
    public static final RegistryObject<SoundEvent> WYVERN_HURT = register("wyvern_hurt");
    // 血腥芽孢
    public static final RegistryObject<SoundEvent> BLOODY_SPORE_DEATH = register("bloody_spore_death");
    public static final RegistryObject<SoundEvent> BLOODY_SPORE_FUSE = register("bloody_spore_fuse");
    public static final RegistryObject<SoundEvent> BLOODY_SPORE_HIT = register("bloody_spore_hit");
    // 腐骴
    public static final RegistryObject<SoundEvent> DECAYEDER_AMBIENT = register("decayeder_ambient");
    public static final RegistryObject<SoundEvent> DECAYEDER_DEATH = register("decayeder_death");
    public static final RegistryObject<SoundEvent> DECAYEDER_HURT = register("decayeder_hurt");
    public static final RegistryObject<SoundEvent> DECAYEDER_STEP = register("decayeder_step");
    // 泰拉挥动
    public static final RegistryObject<SoundEvent> WAVING = register("waving");
    // 召唤
    public static final RegistryObject<SoundEvent> ROUTINE_SUMMON = register("routine_summon"); // 大多数召唤杖
    public static final RegistryObject<SoundEvent> SUMMON_HORNET = register("summon_hornet"); //黄蜂
    public static final RegistryObject<SoundEvent> SUMMON_EYE = register("summon_eye"); // 魔眼
    public static final RegistryObject<SoundEvent> SUMMON_IMP = register("summon_imp"); // 小鬼
    public static final RegistryObject<SoundEvent> SUMMON_MONEY_TROUGH = register("summon_money_trough"); // 存钱罐


    private static RegistryObject<SoundEvent> register(String id) {
        return SOUNDS.register(id, () -> SoundEvent.createVariableRangeEvent(TerraEntity.space(id)));
    }
}
