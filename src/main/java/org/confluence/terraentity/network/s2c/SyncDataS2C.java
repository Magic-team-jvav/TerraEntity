package org.confluence.terraentity.network.s2c;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.terraentity.entity.animation.HillOfFleshModelAnimationTable;
import org.confluence.terraentity.entity.animation.ModelPositionTable;
import org.confluence.terraentity.entity.npc.misc.NPCDialogs;
import org.confluence.terraentity.entity.npc.mood.NPCMood;
import org.confluence.terraentity.network.NetworkHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SyncDataS2C {

    /**
     * @param <T> 用于类型推断
     */
    public interface DataType<T> {
        int getId();
        @Contract(pure = true)
        static <T> @NotNull DataType<T> create(int id) {
            return () -> id;
        }
    }

    private static final Map<Integer, Handler<Object>> handlers = new HashMap<>();

    public static final DataType<Map<EntityType<?>, NPCDialogs>> NPC_DIALOGS = register(NPCDialogs.Loader.CODEC, NPCDialogs.Loader::handle);
    public static final DataType<Map<EntityType<?>, NPCMood.EntityMood>> NPC_MOODS = register(NPCMood.Loader.CODEC, NPCMood.Loader::handle);
    public static final DataType<ModelPositionTable> HILL_ANIMATION = register(ModelPositionTable.CODEC, HillOfFleshModelAnimationTable::handle);


    private final int dataId;
    private final Object data;

    private SyncDataS2C(int dataId, Object data) {
        this.dataId = dataId;
        this.data = data;
    }

    private SyncDataS2C(FriendlyByteBuf buffer) {
        dataId = buffer.readVarInt();
        this.data = buffer.readJsonWithCodec(handlers.get(dataId).codec);
    }


    public static SyncDataS2C decode(FriendlyByteBuf buffer) {
        return new SyncDataS2C(buffer);
    }

    public static void encode(SyncDataS2C value, FriendlyByteBuf buffer) {
        buffer.writeVarInt(value.dataId);
        buffer.writeJsonWithCodec(handlers.get(value.dataId).codec, value.data);
    }

    @SuppressWarnings("unchecked")
    private static <T> DataType<T> register(Codec<T> codec, Consumer<T> consumer) {
        DataType<T> id = DataType.create(handlers.size());
        handlers.put(id.getId(), (Handler<Object>) new Handler<>(codec, consumer));
        return id;
    }

    public static void handle(SyncDataS2C packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> handlers.get(packet.dataId).consumer.accept(packet.data)).exceptionally(e -> null);
    }

    public static <T> void sync(ServerPlayer player, DataType<T> dataId, T value) {
        NetworkHandler.sendToPlayer(player, new SyncDataS2C(dataId.getId(), value));
    }

    public static void syncAll(ServerPlayer player){
        syncNpcDialogs(player);
        syncNpcMoods(player);
        syncHillAnimation(player);
    }

    public static void syncNpcDialogs(ServerPlayer player) {
        sync(player, NPC_DIALOGS, NPCDialogs.Loader.getInstance().getDialogs());
    }

    public static void syncNpcMoods(ServerPlayer player) {
        sync(player, NPC_MOODS, NPCMood.Loader.getInstance().getByType());
    }

    public static void syncHillAnimation(ServerPlayer player) {
        sync(player, HILL_ANIMATION, HillOfFleshModelAnimationTable.getTable());
    }

    public record Handler<T>(Codec<T> codec, Consumer<T> consumer) {}
}
