package org.confluence.terraentity.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface CustomPacketPayload {

    @NotNull Type<? extends CustomPacketPayload> type();

    void encode(FriendlyByteBuf buf);

    default void handle(Supplier<NetworkEvent.Context> ctx) {
        handle(new IPayloadContext(ctx));
    }

    void handle(IPayloadContext context);

    class IPayloadContext {
        NetworkEvent.Context context;
        public IPayloadContext(Supplier<NetworkEvent.Context> ctx) {
            this.context = ctx.get();
        }
        public void enqueueWork(Runnable runnable) {
            this.context.enqueueWork(runnable);
            this.context.setPacketHandled(true);
        }

        public Player player() {
            Player player = this.context.getSender();
            if(player == null) {
                player =  Minecraft.getInstance().player;
            }
            return player;
        }
    }

    record Type<T extends CustomPacketPayload>(ResourceLocation location) {
    }
}
