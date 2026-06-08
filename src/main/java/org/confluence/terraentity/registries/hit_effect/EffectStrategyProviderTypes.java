package org.confluence.terraentity.registries.hit_effect;

import com.mojang.serialization.MapCodec;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.hit_effect.variant.PrefabEffect;
import org.confluence.terraentity.registries.hit_effect.variant.RandomWeightEffect;
import org.confluence.terraentity.registries.hit_effect.variant.TimePossibilityAmplifierEffect;

/// 注册追踪编解码器的类型
public class EffectStrategyProviderTypes {
    public static final DeferredRegister<EffectStrategyProvider> TYPES = DeferredRegister.create(TERegistries.Keys.EFFECT_STRATEGY_PROVIDER, TerraEntity.MODID);

    public static final RegistryObject<EffectStrategyProvider> TIME_POSSIBILITY_AMPLIFIER_EFFECT_PROVIDER = register("time_possibility_amplifier_effect", TimePossibilityAmplifierEffect.CODEC);

    public static final RegistryObject<EffectStrategyProvider> PREFAB_EFFECT_PROVIDER = register("prefab_effect", PrefabEffect.CODEC);
    public static final RegistryObject<EffectStrategyProvider> RANDOM_EFFECT_PROVIDER = register("random_weight_effect", RandomWeightEffect.CODEC);


    private static RegistryObject<EffectStrategyProvider> register(String name, MapCodec<? extends IEffectStrategy> codec) {
        return TYPES.register(name, () -> new EffectStrategyProvider(codec));
    }
}
