package org.confluence.terraentity.network.c2s;

import net.minecraft.network.FriendlyByteBuf;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.terraentity.api.entity.IFlyRideableMob;
import org.confluence.terraentity.network.NetworkHandler;

import java.util.function.Supplier;

public class ServerBoundVehicleExtensionPacket{

    public enum Action {
        START_INPUT_JUMP,
        STOP_INPUT_JUMP

    }
    Action action;

    public ServerBoundVehicleExtensionPacket(Action action) {
        this.action = action;

    }

    public ServerBoundVehicleExtensionPacket(FriendlyByteBuf buf) {

        this.action = Action.values()[buf.readByte()];

    }

    public static ServerBoundVehicleExtensionPacket decode(FriendlyByteBuf buffer) {
        return new ServerBoundVehicleExtensionPacket(buffer);
    }

    public static void encode(ServerBoundVehicleExtensionPacket packet, FriendlyByteBuf buf) {
        buf.writeByte(packet.action.ordinal());
    }

    public static void handle(ServerBoundVehicleExtensionPacket packet, Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> {
            Player player = context.getSender();
            if (player != null && player.getVehicle() instanceof IFlyRideableMob mob) {
                switch (packet.action) {
                    case START_INPUT_JUMP:
                        mob.onLocalStartInputJump();
                        break;
                    case STOP_INPUT_JUMP:
                        mob.onLocalStopInputJump();
                        break;

                }
            }
        });
        context.setPacketHandled(true);
    }

    public static void sendAction(Action action){
        NetworkHandler.sendToServer(new ServerBoundVehicleExtensionPacket(action));
    }
}
