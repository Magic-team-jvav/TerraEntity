package org.confluence.terraentity.init;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.util.Tuple;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.npc.chat.NPCChat;
import org.confluence.terraentity.entity.npc.house.House;
import org.confluence.terraentity.entity.npc.mood.NPCMood;
import org.confluence.terraentity.entity.npc.trade.NPCTradeManager;
import org.confluence.terraentity.entity.npc.trade.TradeParams;
import org.confluence.terraentity.entity.util.KeyframeAnimationCounter;
import org.confluence.terraentity.utils.AdapterUtils;

import java.util.List;
import java.util.function.Supplier;


public final class TEEntityDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, TerraEntity.MODID);

    public static final Supplier<EntityDataSerializer<NPCTradeManager>> NPC_TRADES_SERIALIZER = SERIALIZERS.register(NPCTradeManager.Loader.KEY, () -> EntityDataSerializer.simple(NPCTradeManager.WRITER, NPCTradeManager.READER));
    public static final Supplier<EntityDataSerializer<House>> NPC_HOUSE_SERIALIZER = SERIALIZERS.register(House.KEY, () -> EntityDataSerializer.simple(House.WRITER, House.READER));
    public static final Supplier<EntityDataSerializer<NPCMood>> NPC_MOOD_SERIALIZER = SERIALIZERS.register(NPCMood.KEY, () -> EntityDataSerializer.simple(NPCMood.WRITER, NPCMood.READER));
    public static final Supplier<EntityDataSerializer<TradeParams>> NPC_TRADE_PARAMS_SERIALIZER = SERIALIZERS.register(TradeParams.KEY, () -> EntityDataSerializer.simple(TradeParams.WRITER, TradeParams.READER));

    public static final Supplier<EntityDataSerializer<KeyframeAnimationCounter>> KEYFRAME_ANIMATION_SERIALIZER = SERIALIZERS.register("keyframe_animation", () -> EntityDataSerializer.simple(KeyframeAnimationCounter.WRITER, KeyframeAnimationCounter.READER));
    public static final Supplier<EntityDataSerializer<NPCChat>> NPC_CHAT_SERIALIZER = SERIALIZERS.register("npc_chat", () -> EntityDataSerializer.simple(NPCChat.WRITER, NPCChat.READER));

    static Codec<List<Tuple<Integer, Vec3>>> TUPLE_INT_VEC3_LIST_CODEC = RecordCodecBuilder.<Tuple<Integer, Vec3>>create(instance -> instance.group(
            Codec.INT.fieldOf("first").forGetter(Tuple::getA),
            Vec3.CODEC.fieldOf("second").forGetter(Tuple::getB)
        ).apply(instance, Tuple::new)).listOf();

    static Codec<List<Tuple<Vec3, Integer>>> TUPLET_VEC3_INT_LIST_CODEC = RecordCodecBuilder.<Tuple<Vec3, Integer>>create(instance -> instance.group(
            Vec3.CODEC.fieldOf("first").forGetter(Tuple::getA),
            Codec.INT.fieldOf("second").forGetter(Tuple::getB)
    ).apply(instance, Tuple::new)).listOf();

    public static final Supplier<EntityDataSerializer<List<Tuple<Integer, Vec3>>>> TUPLE_INT_VEC3_LIST_SERIALIZER = SERIALIZERS.register("tuple_int_vec3_list", () -> EntityDataSerializer.simple(AdapterUtils.CodecWriter(TUPLE_INT_VEC3_LIST_CODEC), AdapterUtils.CodecReader(TUPLE_INT_VEC3_LIST_CODEC)));
    public static final Supplier<EntityDataSerializer<List<Tuple<Vec3, Integer>>>> TUPLET_VEC3_INT_LIST_SERIALIZER = SERIALIZERS.register("tuple_vec3_int_list", () -> EntityDataSerializer.simple(AdapterUtils.CodecWriter(TUPLET_VEC3_INT_LIST_CODEC), AdapterUtils.CodecReader(TUPLET_VEC3_INT_LIST_CODEC)));

}
