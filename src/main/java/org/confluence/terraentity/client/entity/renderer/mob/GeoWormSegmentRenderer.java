package org.confluence.terraentity.client.entity.renderer.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.confluence.terraentity.api.entity.IWormSegment;
import org.confluence.terraentity.client.entity.model.GeoNormalModel;
import org.confluence.terraentity.utils.TEUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class GeoWormSegmentRenderer<S extends Entity & IWormSegment & GeoEntity, R extends GeoWormRenderer> extends GeoEntityRenderer<S> {

    float scale;
    float offsetY;

    GeoNormalModel<S> tailModel;
    R parent;

    public GeoWormSegmentRenderer(EntityRendererProvider.Context renderManager, R parent,  ResourceLocation body, ResourceLocation tail) {
        this(renderManager, parent, body, tail,1,0);
    }
    public GeoWormSegmentRenderer(EntityRendererProvider.Context renderManager, R parent,  ResourceLocation body, ResourceLocation tail, float scale, float offsetY) {
        super(renderManager, new GeoNormalModel<>(body, false));
        this.scale=scale;
        this.offsetY=offsetY;
        tailModel = new GeoNormalModel<>(tail,false);
        this.parent = parent;
    }


    @Override
    public void render(S part, float entityYaw, float partialTick, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.scale(scale, scale, scale);
        super.render(part, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public RenderType getRenderType(S animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);

    }

    @Override
    public GeoModel<S> getGeoModel() {
        return this.animatable!=null && this.animatable.isTail()? tailModel : this.model;
    }

    @Override
    public int getPackedOverlay(S animatable, float u, float partialTick) {

        return OverlayTexture.pack(OverlayTexture.u(u),
                OverlayTexture.v(animatable.isHurtOverlay()));
    }

}
