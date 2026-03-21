package org.confluence.terraentity.network.s2c;

import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.attachment.UnSyncableAttachment;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.network.CustomPacketPayload;
import org.confluence.terraentity.network.NetworkHandler;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.function.Consumer;

public record EventPacketS2C(TypeEnum typeEnum) implements CustomPacketPayload {
    public static final Type<EventPacketS2C> TYPE = new Type<>(TerraEntity.space("event_s2c"));
//    public static final StreamCodec<FriendlyByteBuf, EventPacketS2C> STREAM_CODEC = LibStreamCodecUtils.fromEnum(TypeEnum.values())
//            .map(EventPacketS2C::new, EventPacketS2C::typeEnum);
    private static final EnumMap<TypeEnum, Consumer<Player>> handlers = Util.make(new EnumMap<>(TypeEnum.class), map -> {
        map.put(TypeEnum.RESET_CRIMSON_STORM, (player) -> {
            if (player.isLocalPlayer()) {
                player.getCapability(TEAttachments.UNSYNC).orElse(new UnSyncableAttachment()).triggerInvulnerableStorm(player);
            }
        });
    });

    public static EventPacketS2C decode(FriendlyByteBuf buf) {
        return new EventPacketS2C(buf.readEnum(TypeEnum.class));
    }

    @Override
    public @NotNull Type<EventPacketS2C> type() {
        return TYPE;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(typeEnum());
    }

    @Override
    public void handle(IPayloadContext context) {
        work(this.typeEnum, context.player());

    }

    private static void work(TypeEnum typeEnum, Player player) {
        Consumer<Player> consumer = handlers.get(typeEnum);
        if (consumer == null) {
            TerraEntity.LOGGER.warn("Unknown client-bound event packet type: {}", typeEnum);
        } else {
            consumer.accept(player);
        }
    }

    public static void resetCrimsonStorm(ServerPlayer player) {
        NetworkHandler.sendToPlayer(player, new EventPacketS2C(TypeEnum.RESET_CRIMSON_STORM));
        work(TypeEnum.RESET_CRIMSON_STORM, player);
    }

    public enum TypeEnum {
        RESET_CRIMSON_STORM
    }
}
