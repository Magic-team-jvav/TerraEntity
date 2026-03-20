package org.confluence.terraentity.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.mixed.IMobEffectExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "onEffectAdded", at = @At("HEAD"), cancellable = true)
    private void addEffect(MobEffectInstance pEffectInstance, Entity pEntity, CallbackInfo ci) {
        if(pEffectInstance.getEffect() instanceof IMobEffectExtension extension){
            extension.onEffectStarted((LivingEntity) (Object) this, pEffectInstance.getAmplifier());
        }
    }

    @Inject(method = "onEffectUpdated", at = @At("HEAD"), cancellable = true)
    private void updateEffect(MobEffectInstance pEffectInstance, boolean pForced, Entity pEntity, CallbackInfo ci) {
        if(pEffectInstance.getEffect() instanceof IMobEffectExtension extension){
            extension.onEffectStarted((LivingEntity) (Object) this, pEffectInstance.getAmplifier());
        }
    }

    @WrapOperation(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"))
    private void cancelKnockback(LivingEntity instance, double vec31, double v, double pStrength, Operation<Void> original, @Local(argsOnly = true, index = 1) DamageSource source) {
        if(source.is(TETags.DamageTypes.NO_KNOCKBACK_DAMAGE_TYPE)) {

        }else{
            original.call(instance, vec31, v, pStrength);
        }
    }
}
