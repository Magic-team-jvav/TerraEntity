package org.confluence.terraentity.mixed;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.confluence.terraentity.mixin.accessor.GameRendererAccessor;

public class HotSwap {
    public static int consume = 2;
    public static void doSomething(float partialTicks, TextureTarget target, PoseStack poseStack){

        // 这里导致tacz渲染问题
//        target.setClearColor(0, 0, 0, 0);
//        target.clear(true);
//        target.bindWrite(true);
//        GameRenderer gr = Minecraft.getInstance().gameRenderer;
//        Camera camera = gr.getMainCamera();
//        consume = 2;
//        ((GameRendererAccessor)gr).callRenderItemInHand(poseStack, camera, partialTicks);
//
//        Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        target.blitToScreen(Minecraft.getInstance().getMainRenderTarget().width,Minecraft.getInstance().getMainRenderTarget().height,false);

    }
}
