package org.confluence.terraentity.network.s2c;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.post.BossSpawnCameraManager;
import org.confluence.terraentity.network.CustomPacketPayload;
import org.confluence.terraentity.network.NetworkHandler;
import org.jetbrains.annotations.NotNull;

/**
 * 使用这个包来设定boss召唤的过场动画
 */
public class SummonBossPacket implements CustomPacketPayload {

    int id;
    float distance;


    public static final Type<SummonBossPacket> TYPE = new Type<>(TerraEntity.fromSpaceAndPath(TerraEntity.MODID, "summon_boss"));
//    public static final StreamCodec<RegistryFriendlyByteBuf, SummonBossPacket> STREAM_CODEC = CustomPacketPayload.codec(SummonBossPacket::write, SummonBossPacket::new);

    public SummonBossPacket(int id, float distance) {
        this.id = id;
        this.distance = distance;
    }

    public SummonBossPacket(FriendlyByteBuf buf) {
        this.id = buf.readInt();
        this.distance = buf.readFloat();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(id);
        buf.writeFloat(distance);
    }

    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {

            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                Entity entity = mc.level.getEntity(id);
                if(entity instanceof LivingEntity living){
                    BossSpawnCameraManager.INSTANCE.bakeBossSpawn(living, distance);
                }
            }

        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }



    public static void sendTo(ServerPlayer player, LivingEntity entity, float distance) {
        NetworkHandler.sendToPlayer(player, new SummonBossPacket(entity.getId(), distance));
    }
}