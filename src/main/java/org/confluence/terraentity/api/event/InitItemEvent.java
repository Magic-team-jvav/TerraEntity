package org.confluence.terraentity.api.event;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import org.confluence.terraentity.item.Boomerang;
import net.minecraft.world.item.Item.Properties;


public class InitItemEvent extends Event implements IModBusEvent {

    Properties properties;

    public InitItemEvent(Item.Properties properties) {
        this.properties = properties;
    }
    public Properties getProperties() {
        return properties;
    }

    public static class InitBoomerang extends InitItemEvent {

        Boomerang.BoomerangModifier modifier;
        public InitBoomerang(Properties properties, Boomerang.BoomerangModifier modifier) {
            super(properties);
            this.modifier = modifier;
        }

        public Boomerang.BoomerangModifier getModifier() {
            return modifier;
        }
    }

    public static class InitWhip extends InitItemEvent {
        public float damage;
        public float markDamage;
        public float attackSpeed;
        public int hitCooldown;
        public float rangeFactor;
        public InitWhip(Properties properties, float damage,
                        float markDamage,
                        float attackSpeed,
                        int hitCooldown,
                        float rangeFactor) {
            super(properties);
            this.damage = damage;
            this.markDamage = markDamage;
            this.attackSpeed = attackSpeed;
            this.hitCooldown = hitCooldown;
            this.rangeFactor = rangeFactor;
        }

    }

    public static class InitYoyos extends InitItemEvent {
        public float attackDamage;
        public int maxRange;
        public int stringColor;
        public float existTime;
        public InitYoyos(Properties properties,
                         float attackDamage,
                         int maxRange,
                         int stringColor,
                         float existTime) {
            super(properties);
            this.attackDamage = attackDamage;
            this.maxRange = maxRange;
            this.stringColor = stringColor;
            this.existTime = existTime;
        }
    }

    public static class InitSummonItem extends InitItemEvent {
        public int consume;
        public float baseAttackDamage;
        public InitSummonItem(Properties properties, int consume, float baseAttackDamage) {
            super(properties);
            this.consume = consume;
            this.baseAttackDamage = baseAttackDamage;
        }
    }



}
