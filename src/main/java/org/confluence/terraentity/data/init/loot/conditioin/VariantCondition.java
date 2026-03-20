package org.confluence.terraentity.data.init.loot.conditioin;

import com.google.common.base.Suppliers;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.serialization.Codec;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.confluence.terraentity.data.init.loot.TELootParams;
import org.confluence.terraentity.init.TELoots;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record VariantCondition(int variant) implements LootItemCondition {

    public static final Supplier<Codec<VariantCondition>> CODEC = Suppliers.memoize(()->
            Codec.INT.xmap(VariantCondition::new, VariantCondition::variant));

    @Override
    public @NotNull LootItemConditionType getType() {
        return TELoots.TELootItemConditions.VARIANT_CONDITION.get();
    }

    @Override
    public boolean test(LootContext context) {
        Integer var3 = context.getParamOrNull(TELootParams.VARIANT);
        return var3!= null && var3 == variant;
    }
    // 这里不知道为什么，rundata时验证不通过，需要注释掉
//   public Set<LootContextParam<?>> getReferencedContextParams() {
//        return ImmutableSet.of(TELootParams.VARIANT);
//    }

    public static Builder of(int variant) {
        return ()-> new VariantCondition(variant);
    }


    public static class VariantConditionSerializer implements Serializer<VariantCondition> {
        public VariantConditionSerializer() {
        }

        public void serialize(JsonObject json, VariantCondition data, @NotNull JsonSerializationContext context) {
            json.addProperty("variant", data.variant);
        }

        public @NotNull VariantCondition deserialize(JsonObject json, @NotNull JsonDeserializationContext context) {
            int v = json.has("variant") ? GsonHelper.getAsInt(json, "variant") : -1;
            return new VariantCondition(v);
        }
    }
}
