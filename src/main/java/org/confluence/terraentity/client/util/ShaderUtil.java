package org.confluence.terraentity.client.util;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.api.entity.trail.ITrail;
import org.joml.Matrix4f;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraft.client.renderer.RenderStateShard.*;

public class ShaderUtil {


    public static void shaderBlit(Matrix4f matrix4f, int x, int y, float uOffset, float vOffset, int width, int height, int textureWidth, int textureHeight) {
        shaderBlit(matrix4f, x , x+width, y, y+height , 0, width, height,uOffset, vOffset, textureWidth, textureHeight);
    }

    static void shaderBlit(Matrix4f matrix4f, int x1, int x2, int y1, int y2, int blitOffset, int uWidth, int vHeight, float uOffset, float vOffset, int textureWidth, int textureHeight) {
        innerBlit(matrix4f, x1, x2, y1, y2, blitOffset, (uOffset + 0.0F) / (float)textureWidth, (uOffset + (float)uWidth) / (float)textureWidth, (vOffset + 0.0F) / (float)textureHeight, (vOffset + (float)vHeight) / (float)textureHeight);
    }

    static void innerBlit(Matrix4f matrix4f,int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f, (float)x1, (float)y1, (float)blitOffset).uv(minU, minV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x1, (float)y2, (float)blitOffset).uv(minU, maxV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x2, (float)y2, (float)blitOffset).uv(maxU, maxV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x2, (float)y1, (float)blitOffset).uv(maxU, minV).endVertex();

//        bufferbuilder.vertex(0.0, (double)f1, 0.0).uv(0.0F, 0.0F).color(255, 255, 255, 255).endVertex();
//        bufferbuilder.vertex((double)f, (double)f1, 0.0).uv(f2, 0.0F).color(255, 255, 255, 255).endVertex();
//        bufferbuilder.vertex((double)f, 0.0, 0.0).uv(f2, f3).color(255, 255, 255, 255).endVertex();
//        bufferbuilder.vertex(0.0, 0.0, 0.0).uv(0.0F, f3).color(255, 255, 255, 255).endVertex();

        BufferUploader.drawWithShader(bufferbuilder.end());
    }


    public static void blitScreen(ShaderInstance shader, Consumer<ShaderInstance> setupShader){


        RenderSystem.assertOnRenderThread();
        GlStateManager._colorMask(true, true, true, false);
        GlStateManager._disableDepthTest();
        GlStateManager._viewport(0, 0, Minecraft.getInstance().getMainRenderTarget().width, Minecraft.getInstance().getMainRenderTarget().height);

//        ShaderInstance shader = ModRenderTypes.Shaders.colorBlitShader;
//        ShaderInstance shaderinstance = Objects.requireNonNull(shader, "Blit shader not loaded");
//        shader.COLOR_MODULATOR.set(1f, 1f, 1f, 0.2f);
//        shaderinstance.setSampler("Sampler0", Minecraft.getInstance().getMainRenderTarget());
//        shaderinstance.setSampler("Sampler1", BrainOfCthulhuRenderer.target);
        float f = Minecraft.getInstance().getMainRenderTarget().width;
        float f1 = Minecraft.getInstance().getMainRenderTarget().height;
        setupShader.accept(shader);
        Matrix4f matrix4f = (new Matrix4f()).setOrtho(0.0F, f, f1, 0.0F, 1000.0F, 3000.0F);
        RenderSystem.setProjectionMatrix(matrix4f, VertexSorting.ORTHOGRAPHIC_Z);
        if (shader.MODEL_VIEW_MATRIX != null) {
            shader.MODEL_VIEW_MATRIX.set((new Matrix4f()).translation(0.0F, 0.0F, -2000.0F));
        }

        if (shader.PROJECTION_MATRIX != null) {
            shader.PROJECTION_MATRIX.set(matrix4f);
        }
        shader.apply();

        float f2 = 1;
        float f3 = 1;
        Tesselator tesselator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferbuilder.vertex(0.0, f1, 0.0).uv(0.0F, 0.0F).color(255, 255, 255, 255).endVertex();
        bufferbuilder.vertex(f, f1, 0.0).uv(f2, 0.0F).color(255, 255, 255, 255).endVertex();
        bufferbuilder.vertex(f, 0.0, 0.0).uv(f2, f3).color(255, 255, 255, 255).endVertex();
        bufferbuilder.vertex(0.0, 0.0, 0.0).uv(0.0F, f3).color(255, 255, 255, 255).endVertex();
        BufferUploader.draw(bufferbuilder.end());
        shader.clear();
        GlStateManager._depthMask(true);
        GlStateManager._colorMask(true, true, true, true);
    }

    public static void renderDebugBlock(VertexConsumer buffer, BlockPos pos, float size, int r, int g, int b, int a){
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        buffer.vertex(x, y + size, z).color(r,g,b,a).endVertex();
        buffer.vertex(x + size, y + size, z).color(r,g,b,a).endVertex();
        buffer.vertex(x + size, y + size, z).color(r,g,b,a).endVertex();
        buffer.vertex(x + size, y + size, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x + size, y + size, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x, y + size, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x, y + size, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x, y + size, z).color(r,g,b,a).endVertex();

        // BOTTvertex()
        buffer.vertex(x + size, y, z).color(r,g,b,a).endVertex();
        buffer.vertex(x + size, y, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x + size, y, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x, y, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x, y, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x, y, z).color(r,g,b,a).endVertex();
        buffer.vertex(x, y, z).color(r,g,b,a).endVertex();
        buffer.vertex(x + size, y, z).color(r,g,b,a).endVertex();

        // Edgevertex()
        buffer.vertex(x + size, y, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x + size, y + size, z + size).color(r,g,b,a).endVertex();

        // Edgevertex()
        buffer.vertex(x + size, y, z).color(r,g,b,a).endVertex();
        buffer.vertex(x + size, y + size, z).color(r,g,b,a).endVertex();

        // Edgevertex()
        buffer.vertex(x, y, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x, y + size, z + size).color(r,g,b,a).endVertex();

        // Edgevertex()
        buffer.vertex(x, y, z).color(r,g,b,a).endVertex();
        buffer.vertex(x, y + size, z).color(r,g,b,a).endVertex();
    }


    public static void renderLightBoundle(MultiBufferSource buffer, Matrix4f matrix4f, float size, float height, int color){
        VertexConsumer consumer = buffer.getBuffer(LIGHT_BOUNDLE_RENDER_TYPE.get());

        ITrail.buildVertex(consumer, matrix4f, new Vec3(0,0,0), color).normal(0,0,-1);
        ITrail.buildVertex(consumer, matrix4f, new Vec3(0,height,0), 0x00000000).normal(0,0,-1);
        ITrail.buildVertex(consumer, matrix4f, new Vec3(size,height,0), 0x00000000).normal(0,0,-1);
        ITrail.buildVertex(consumer, matrix4f, new Vec3(size,0,0), color).normal(0, 0,-1);

        ITrail.buildVertex(consumer, matrix4f, new Vec3(0,0,0), color).normal(1,0,0);
        ITrail.buildVertex(consumer, matrix4f, new Vec3(0,height,0), 0x00000000).normal(1,0,0);
        ITrail.buildVertex(consumer, matrix4f, new Vec3(0,height,size), 0x00000000).normal(1,0,0);
        ITrail.buildVertex(consumer, matrix4f, new Vec3(0,0,size), color).normal(1,0,0);

        ITrail.buildVertex(consumer, matrix4f, new Vec3(0,0,size), color).normal(0, 0,1);
        ITrail.buildVertex(consumer, matrix4f, new Vec3(0,height,size), 0x00000000).normal(0, 0,1);
        ITrail.buildVertex(consumer, matrix4f, new Vec3(size,height,size), 0x00000000).normal(0, 0,1);
        ITrail.buildVertex(consumer, matrix4f, new Vec3(size,0,size), color).normal(0, 0,1);

        ITrail.buildVertex(consumer, matrix4f, new Vec3(size,0,size), color).normal(-1,0,0);
        ITrail.buildVertex(consumer, matrix4f, new Vec3(size,height,size), 0x00000000).normal(-1,0,0);
        ITrail.buildVertex(consumer, matrix4f, new Vec3(size,height,0), 0x00000000).normal(-1,0,0);
        ITrail.buildVertex(consumer, matrix4f, new Vec3(size,0,0), color).normal(-1,0,0);

    }

    public static Supplier<RenderType> TRAIL_RENDER_TYPE = Suppliers.memoize(() -> RenderType.create(
            "trail_render_type",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
                    1536,
                    false,
                    true,
                    RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_LIGHTNING_SHADER)
                                .setWriteMaskState(COLOR_DEPTH_WRITE)
                                .setTransparencyState(LIGHTNING_TRANSPARENCY)
                                .setOutputState(TRANSLUCENT_TARGET)
                                .setCullState(NO_CULL)
                                .createCompositeState(true)
    ));

    public static Supplier<RenderType> LIGHT_BOUNDLE_RENDER_TYPE = Suppliers.memoize(() -> RenderType.create(
            "trail_render_type",
            DefaultVertexFormat.POSITION_COLOR_NORMAL,
            VertexFormat.Mode.QUADS,
            1536,
            false,
            true,
            RenderType.CompositeState.builder()
            .setShaderState(RENDERTYPE_LIGHTNING_SHADER)
                        .setWriteMaskState(COLOR_WRITE)
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setOutputState(TRANSLUCENT_TARGET)
                        .setCullState(NO_CULL)
                        .createCompositeState(true)
        ));
}
