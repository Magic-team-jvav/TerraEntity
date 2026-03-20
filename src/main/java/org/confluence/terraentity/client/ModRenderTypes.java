package org.confluence.terraentity.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.terraentity.TerraEntity;

import java.io.IOException;

import static org.confluence.terraentity.TerraEntity.MODID;

public final class ModRenderTypes {
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class Shaders {
        public static ShaderInstance floatBarShader;
        public static ShaderInstance colorBlitShader;
        public static ShaderInstance mixAddShader;
        public static ShaderInstance dissolveBlitShader;
        public static ShaderInstance pixelStyleShader;
        public static ShaderInstance floatFireShader;


        @SubscribeEvent
        public static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
            ResourceProvider resourceProvider = event.getResourceProvider();

            event.registerShader(new ShaderInstance(resourceProvider,
                            TerraEntity.space("float_bar"),
                            DefaultVertexFormat.POSITION_TEX),
                    shader -> {
                        floatBarShader = shader;
                    }
            );
            event.registerShader(new ShaderInstance(resourceProvider,
                            TerraEntity.space("color_blit"),
                            DefaultVertexFormat.BLIT_SCREEN),
                    shader -> {
                        colorBlitShader = shader;
                    }
            );
            event.registerShader(new ShaderInstance(resourceProvider,
                            TerraEntity.space("mix_add"),
                            DefaultVertexFormat.BLIT_SCREEN),
                    shader -> {
                        mixAddShader = shader;
                    }
            );
            event.registerShader(new ShaderInstance(resourceProvider,
                            TerraEntity.space("dissolve_blit"),
                            DefaultVertexFormat.BLIT_SCREEN),
                    shader -> {
                        dissolveBlitShader = shader;
                    }
            );


            event.registerShader(new ShaderInstance(resourceProvider,
                            TerraEntity.space("pixel_style_dissolve"),
                            DefaultVertexFormat.POSITION_TEX),
                    shader -> {
                        pixelStyleShader = shader;
                    }
            );
            event.registerShader(new ShaderInstance(resourceProvider,
                            TerraEntity.space("float_fire"),
                            DefaultVertexFormat.POSITION_TEX),
                    shader -> {
                        floatFireShader = shader;
                    }
            );
        }
    }

    public static ShaderInstance getFloatBarShader() {
        return Shaders.floatBarShader;
    }

    public static ShaderInstance getColorBlitShader() {
        return Shaders.colorBlitShader;
    }

    public static ShaderInstance getMixAddShader() {
        return Shaders.mixAddShader;
    }

    public static ShaderInstance getDissolveBlitShader() {
        return Shaders.dissolveBlitShader;
    }





    public static ShaderInstance getPixelStyleShader() {
        return Shaders.pixelStyleShader;
    }

    public static ShaderInstance getFloatFireShader() {
        return Shaders.floatFireShader;
    }

}
