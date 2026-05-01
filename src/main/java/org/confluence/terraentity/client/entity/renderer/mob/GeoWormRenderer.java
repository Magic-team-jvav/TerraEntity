package org.confluence.terraentity.client.entity.renderer.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.api.entity.IWorm;
import org.confluence.terraentity.api.entity.IWormSegment;
import org.confluence.terraentity.client.entity.renderer.GeoNormalRenderer;
import org.confluence.terraentity.utils.TEUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;

import java.util.ArrayList;
import java.util.List;


public class GeoWormRenderer<T extends Mob & IWorm<S> & GeoEntity, S extends IWormSegment> extends GeoNormalRenderer<T> {
    GeoWormSegmentRenderer partRenderer;
    double lerpx;
    double lerpy;
    double lerpz;

    /**
     * 文件命名：
     * <p>{path}.geo.json</p>
     * <p>{path}_segment.geo.json</p>
     * <p>{path}_tail.geo.json</p>
     *
     * @param path entity
     */
    public GeoWormRenderer(EntityRendererProvider.Context renderManager, ResourceLocation path) {
        this(renderManager, path, 1.0f, 0.0f);
    }

    /**
     * 文件命名：
     * <p>{path}.geo.json</p>
     * <p>{path}_segment.geo.json</p>
     * <p>{path}_tail.geo.json</p>
     *
     * @param path entity
     */
    public GeoWormRenderer(EntityRendererProvider.Context renderManager, ResourceLocation path, float scale, float offsetY) {
        super(renderManager, path, true, scale, offsetY);
        partRenderer = createPartRenderer(renderManager, path);
    }

    protected GeoWormSegmentRenderer createPartRenderer(EntityRendererProvider.Context renderManager, ResourceLocation path) {
        String name = path.getPath();
        String segment = name + "_segment";
        String tail = name + "_tail";
        return new GeoWormSegmentRenderer<>(renderManager, this,
                TerraEntity.space(segment),
                TerraEntity.space(tail), scale, offsetY);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        Entity part1 = (Entity) entity.getBodySegments().getFirst();

        lerpx = Mth.lerp(partialTick, entity.xo, entity.getX());
        lerpy = Mth.lerp(partialTick, entity.yo, entity.getY());
        lerpz = Mth.lerp(partialTick, entity.zo, entity.getZ());
        double lerpDx = lerpx - Mth.lerp(partialTick, part1.xo, part1.getX());
        double lerpDy = lerpy - Mth.lerp(partialTick, part1.yo, part1.getY());
        double lerpDz = lerpz - Mth.lerp(partialTick, part1.zo, part1.getZ());

        float yRot = Mth.lerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
        float rad = yRot * Mth.DEG_TO_RAD;
        float pitch = (float) (Mth.atan2(lerpDy, Math.sqrt(lerpDx * lerpDx + lerpDz * lerpDz)));
        poseStack.mulPose(Axis.of(new Vector3f(Mth.cos(rad), 0, Mth.sin(rad))).rotation(-pitch));

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();

        renderPart(entity, partialTick, poseStack, bufferSource);
    }

    protected void renderPart(T entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource) {
        Vec3 prePos = new Vec3(lerpx, lerpy, lerpz);
        List<Vec3> lerpPoss = new ArrayList<>();
        for (S part : entity.getBodySegments()) {
            Entity part1 = (Entity) part;
            double lerpx = Mth.lerp(partialTick, part1.xo, part1.getX());
            double lerpy = Mth.lerp(partialTick, part1.yo, part1.getY());
            double lerpz = Mth.lerp(partialTick, part1.zo, part1.getZ());
            lerpPoss.add(new Vec3(lerpx, lerpy, lerpz));
        }
        int i = 0;
        for (S part : entity.getBodySegments()) {
            Entity part1 = (Entity) part;
            poseStack.pushPose();

            // 下一段体节位置
            Vec3 nextPos = i < lerpPoss.size() - 1 ? lerpPoss.get(i + 1) : lerpPoss.get(i);

            // 当前体节位置
            Vec3 curPos = lerpPoss.get(i);
            poseStack.translate(curPos.x - lerpx, curPos.y - lerpy, curPos.z - lerpz);

            // 前后两体节的方向
            Vec3 midDir = prePos.subtract(nextPos);
            var rots = TEUtils.dirToRot(midDir);
            float pitch = -rots[1];
            float lerpYRot = rots[0];

            poseStack.mulPose(Axis.YN.rotationDegrees(lerpYRot));
            poseStack.mulPose(Axis.XN.rotationDegrees(pitch));
            partRenderer.render(part1, lerpYRot, partialTick, poseStack, bufferSource, entityRenderDispatcher.getPackedLightCoords(part1, partialTick));
            prePos = lerpPoss.get(i);

            poseStack.popPose();
            i++;
        }
    }

    protected void rotateX(PoseStack poseStack, T animatable, float partialTick) {}

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }
}
