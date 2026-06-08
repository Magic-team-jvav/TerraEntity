package org.confluence.terraentity.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.effect.harmful.*;

public class TEEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, TerraEntity.MODID);

    public static final RegistryObject<DemonicThoughtsEffect> DEMONIC_THOUGHTS = EFFECTS.register("demonic_thoughts", DemonicThoughtsEffect::new);
    public static final RegistryObject<SummonFocusEffect> SUMMON_FOCUS = EFFECTS.register("summon_mark", SummonFocusEffect::new);
    public static final RegistryObject<MobEffect> FROST_BURN = EFFECTS.register("frost_burn", FrostburnEffect::new);
    public static final RegistryObject<MobEffect> HELLFIRE = EFFECTS.register("hellfire", HellFireEffect::new);
    public static final RegistryObject<HorrifiedEffect> HORRIFIED = EFFECTS.register("horrified", HorrifiedEffect::new);
    public static final RegistryObject<TheTongueEffect> THE_TONGUE = EFFECTS.register("the_tongue", TheTongueEffect::new);
    public static final RegistryObject<CrimsonStorm> CRIMSON_STORM = EFFECTS.register("crimson_storm", CrimsonStorm::new);

    //惊吓/威慑效果（SCARED）- 对飞行单位造成驱离
    public static final RegistryObject<DriveAwayEffect> SCARED = EFFECTS.register("scared",
            () -> new DriveAwayEffect(0.3, 200.0, 0.8, 1.2, 1.5, 0.0));
}
