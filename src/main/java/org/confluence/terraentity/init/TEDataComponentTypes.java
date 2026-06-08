package org.confluence.terraentity.init;

import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.component.EffectStrategyComponent;
import org.confluence.terraentity.data.component.ResourceLocationComponent;
import org.confluence.terraentity.data.component.SingleBooleanComponent;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.registries.PortDataComponentRegistration;
import org.mesdag.portlib.registries.PortRegisterHandler;
import org.mesdag.portlib.registries.PortRegistryEntry;

public final class TEDataComponentTypes {
    public static final PortDataComponentRegistration TYPES = PortRegisterHandler.dataComponent(TerraEntity.MODID);

    public static final PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<EffectStrategyComponent>> EFFECT_STRATEGY = TYPES.builder(
            "effect_strategy", builder -> builder.persistent(EffectStrategyComponent.CODEC).networkSynchronized(EffectStrategyComponent.STREAM_CODEC)
    );

    public static final PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<EffectStrategyComponent>> EFFECT_STRATEGY_BENEFICIAL = TYPES.builder(
            "effect_strategy_beneficial", builder -> builder.persistent(EffectStrategyComponent.CODEC).networkSynchronized(EffectStrategyComponent.STREAM_CODEC)
    );

    public static final PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<EffectStrategyComponent>> BOW_FULL_CHARGE_EFFECT_STRATEGY = TYPES.builder(
            "bow_full_charge_effect_strategy", builder -> builder.persistent(EffectStrategyComponent.CODEC).networkSynchronized(EffectStrategyComponent.STREAM_CODEC)
    );

    public static final PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<SingleBooleanComponent>> BOOMERANG_READY = TYPES.builder(
            "boomerang_ready", builder -> builder.persistent(SingleBooleanComponent.CODEC).networkSynchronized(SingleBooleanComponent.STREAM_CODEC)
    );

    public static final PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<ResourceLocationComponent>> WHIP_PATH = TYPES.builder(
            "whip_path", builder -> builder.persistent(ResourceLocationComponent.CODEC).networkSynchronized(ResourceLocationComponent.STREAM_CODEC)
    );
}
