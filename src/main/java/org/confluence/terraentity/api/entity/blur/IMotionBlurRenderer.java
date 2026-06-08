package org.confluence.terraentity.api.entity.blur;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/// 渲染动作模糊
///
/// @param <T> 实体类型
/// @param <C> 模糊信息上下文类型
@OnlyIn(Dist.CLIENT)
public interface IMotionBlurRenderer<T extends Entity & IMotionBlurHolder<C>, C extends IMotionBlurContext> {
    void renderBlur(PoseStack poseStack, T animatable, float partialTick, RenderCallback renderCallback);

    @FunctionalInterface
    interface RenderCallback {
        void accept(float red, float green, float blue, float alpha);
    }
}
