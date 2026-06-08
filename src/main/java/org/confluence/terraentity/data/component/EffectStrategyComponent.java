package org.confluence.terraentity.data.component;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.registries.hit_effect.EffectStrategy;
import org.confluence.terraentity.registries.hit_effect.IEffectStrategy;
import org.confluence.terraentity.registries.hit_effect.variant.PrefabEffect;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

import java.util.List;

/// 命中效果的数据生成器组件
///
/// @param effects 命中效果
public record EffectStrategyComponent(List<IEffectStrategy> effects) {
    public static final Codec<EffectStrategyComponent> CODEC = IEffectStrategy.TYPED_CODEC.listOf().xmap(EffectStrategyComponent::new, EffectStrategyComponent::effects);
    public static final PortStreamCodec<PortRegistryFriendlyByteBuf, EffectStrategyComponent> STREAM_CODEC = IEffectStrategy.STREAM_CODEC
            .apply(PortByteBufCodecs.list()).map(EffectStrategyComponent::new, EffectStrategyComponent::effects);

    public void applyAll(LivingEntity owner, LivingEntity target) {
        for (IEffectStrategy effect : effects) {
            effect.getEffect().accept(owner, target);
        }
    }

    public static EffectStrategyComponent of(IEffectStrategy effect) {
        return new EffectStrategyComponent(List.of(effect));
    }

    public static EffectStrategyComponent ofPrefab(String name, RegistryObject<EffectStrategy> effect) {
        return of(PrefabEffect.of(name, effect));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        return obj instanceof EffectStrategyComponent c && c.effects == effects;
    }
}
