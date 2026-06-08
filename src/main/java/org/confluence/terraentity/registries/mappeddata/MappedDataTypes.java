package org.confluence.terraentity.registries.mappeddata;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.mappeddata.BossSkillMapDatas;
import org.confluence.terraentity.data.mappeddata.MonsterMappedDatas;
import org.confluence.terraentity.data.mappeddata.NPCMappedDatas;
import org.confluence.terraentity.data.mappeddata.WeaponMappedDatas;
import org.confluence.terraentity.registries.TERegistries;

import java.util.function.Supplier;

public class MappedDataTypes {
    public static final DeferredRegister<MappedDataType<?,?>> TYPES = DeferredRegister.create(TERegistries.Keys.MAPPED_DATA, TerraEntity.MODID);

    public static final RegistryObject<BossSkillMapDatas.BossSkillType> BOSS_SKILL_MAP_DATAS = TYPES.register("boss_skill_params",
            BossSkillMapDatas::buildType);

    public static final RegistryObject<NPCMappedDatas.NPCMappedDataType> NPC_MAP_DATAS = TYPES.register("npc_params",
            NPCMappedDatas::buildType);

    public static final RegistryObject<MonsterMappedDatas.MonsterMappedDataType> MONSTER_MAP_DATAS = TYPES.register("monster_params",
            MonsterMappedDatas::buildType);

    public static final RegistryObject<WeaponMappedDatas.WeaponMappedDataType> WEAPON_MAP_DATAS = TYPES.register("weapon_params",
            WeaponMappedDatas::buildType);

    public static <Y extends MappedDataType<Y, T>, T extends MappedData<Y>, V> V getData(Supplier<Y> type, MappedKey<Y, V> key) {
        return type.get().getData(key);
    }
}
