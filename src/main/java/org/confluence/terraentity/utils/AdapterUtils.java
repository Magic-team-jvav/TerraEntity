package org.confluence.terraentity.utils;

import com.github.edg_thexu.cafelib.api.datacomponent.IDataComponentType;
import com.github.edg_thexu.cafelib.data.codec.DataComponentProvider;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Supplier;


public class AdapterUtils {

    //    public static <T extends Event> T postModEvent(T event){
//        return (event);
//    }

    public static <T extends Event> T postEvent(T event){
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static Codec<MobEffect> getEffectCodec(){
        return ForgeRegistries.MOB_EFFECTS.getCodec();
    }

    public static <T> FriendlyByteBuf.Writer<T> CodecWriter(Codec<T> CODEC){
        return (buffer, trade) -> buffer.writeJsonWithCodec(CODEC, trade);
    }

    public static <T> FriendlyByteBuf.Reader<T> CodecReader(Codec<T> CODEC){
        return buffer -> buffer.readJsonWithCodec(CODEC);
    }

    public static void enchant(ItemStack stack, Enchantment enchantment, int level){
        stack.enchant(enchantment,level);
    }

    public static void enchant(ItemStack stack, Enchantment enchantment, int level, @Nullable HolderLookup.RegistryLookup<Enchantment> enchantLookup){
        stack.enchant(enchantment,level);
    }

    public static void setPotion(ItemStack stack, Holder<Potion> potion){
        PotionUtils.setPotion(stack, Potions.STRONG_HEALING);
    }

    public static void setFirework(ItemStack stack, int duration){
        FireworkRocketItem.setDuration(stack, (byte)duration);
    }

    public static <T extends IDataComponentType<T>> @Nullable T getDataComponent(ItemStack itemStack, DataComponentProvider<T> dataComponentType){
        return IDataComponentType.getData(itemStack, dataComponentType);
    }

    public static <T extends IDataComponentType<T>> @Nullable T getDataComponent(ItemStack itemStack, Supplier<DataComponentProvider<T>> dataComponentType){
        return IDataComponentType.getData(itemStack, dataComponentType);
    }

    public static void addOrUpdateTransientModifier(LivingEntity living, AttributeModifier modifier, Attribute attribute) {
        AttributeInstance instance = living.getAttributes().getInstance(attribute);
        if (instance != null) {
            if(instance.hasModifier(modifier)) {
                instance.removeModifier(modifier);
            }
            instance.addTransientModifier(modifier);
        }
    }

    public static void addOrUpdatePermanentModifier(LivingEntity living, AttributeModifier modifier, Attribute attribute) {
        AttributeInstance instance = living.getAttributes().getInstance(attribute);
        if (instance != null) {
            if(instance.hasModifier(modifier)) {
                instance.removeModifier(modifier);
            }
            instance.addPermanentModifier(modifier);
        }
    }

    public static void removeModifier(LivingEntity living, AttributeModifier modifier, Attribute attribute) {
        AttributeInstance instance = living.getAttributes().getInstance(attribute);
        if (instance != null) {
            if(instance.hasModifier(modifier)) {
                instance.removeModifier(modifier);
            }
        }
    }

    public static void removeModifier(LivingEntity living, UUID modifierId, Attribute attribute) {
        AttributeInstance instance = living.getAttributes().getInstance(attribute);
        if (instance != null) {
            if(instance.getModifier(modifierId) != null) {
                instance.removeModifier(modifierId);
            }
        }
    }
}
