package org.confluence.terraentity.mixin.client;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.confluence.terraentity.client.post.BossSpawnCameraManager;
import org.confluence.terraentity.mixed.HotSwap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {


    @Unique
    TextureTarget confluence$handTarget;

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;renderItemInHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/Camera;F)V"))
    public void renderLevel(float pPartialTicks, long pFinishTimeNano, PoseStack pPoseStack, CallbackInfo ci) {
        if(confluence$handTarget == null)
            confluence$handTarget = new TextureTarget(Minecraft.getInstance().getMainRenderTarget().width, Minecraft.getInstance().getMainRenderTarget().height,false,false);

        HotSwap.doSomething(pPartialTicks , confluence$handTarget, pPoseStack);

    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V", shift = At.Shift.AFTER))
    public void setupCamera(float pPartialTicks, long pFinishTimeNano, PoseStack pPoseStack, CallbackInfo ci) {
        BossSpawnCameraManager.INSTANCE.update(pPartialTicks);
    }
}
