package org.confluence.terraentity.mixin.level;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ModifiableBiomeInfo.class)
public class ModifyBiomeMixin {

//    @WrapOperation(method = "applyBiomeModifiers", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/world/BiomeModifier;modify(Lnet/minecraft/core/Holder;Lnet/minecraftforge/common/world/BiomeModifier$Phase;Lnet/minecraftforge/common/world/ModifiableBiomeInfo$BiomeInfo$Builder;)V"))
//    public void modify(BiomeModifier instance, Holder<Biome> biomeHolder, BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder, Operation<Void> original) {
//
//    }

}
