package org.confluence.terraentity.api.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;


public abstract class TEBossEvent extends Event implements IModBusEvent {

    EntityType<?> entityType;

    public TEBossEvent(EntityType<?> entityType) {
        this.entityType = entityType;
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }


    public static class Summon extends TEBossEvent {
        Player player;
        boolean shouldChangeCamera = true;
        float distance;

        public Summon(EntityType<?> entityType, Player player, float distance) {
            super(entityType);
            this.player = player;
            this.distance = distance;
        }

        public Player getPlayer() {
            return player;
        }

        public boolean shouldChangeCamera() {
            return shouldChangeCamera;
        }

        public void setNotShouldChangeCamera() {
            this.shouldChangeCamera = false;
        }

        public float getDistance() {
            return distance;
        }

        public void setDistance(float distance) {
            this.distance = distance;
        }

    }
}
