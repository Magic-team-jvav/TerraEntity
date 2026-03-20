package org.confluence.terraentity.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.terraentity.network.s2c.SyncWallOfFleshTargetPacket;
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
        }

        public Player player() {
            return this.context.getSender();
        }
    }

    record Type<T extends CustomPacketPayload>(ResourceLocation location) {
    }
}
