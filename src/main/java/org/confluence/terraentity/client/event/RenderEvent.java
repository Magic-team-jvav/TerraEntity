package org.confluence.terraentity.client.event;



import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.attachment.WeaponStorage;
import org.confluence.terraentity.client.buffer.DebugBlocksHelper;
import org.confluence.terraentity.client.buffer.DebugEntityHelper;
import org.confluence.terraentity.client.buffer.NPCChatBubbleBuffer;
import org.confluence.terraentity.client.gui.CustomizeBossHealthBar;
import org.confluence.terraentity.client.init.model.EntityBlockModelRegister;
import org.confluence.terraentity.client.post.BossSpawnCameraManager;
import org.confluence.terraentity.client.post.BrainTranslucent;
import org.confluence.terraentity.client.post.TongueRenderer;
import org.confluence.terraentity.config.ClientConfig;
import org.confluence.terraentity.effect.harmful.TheTongueEffect;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFleshMouth;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.init.TEEffects;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.integration.ModChecker;
import org.confluence.terraentity.item.BaseWhipItem;
import org.confluence.terraentity.item.YoyosItem;
import org.confluence.terraentity.utils.TEUtils;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static org.confluence.terraentity.TerraEntity.MODID;

@Mod.EventBusSubscriber(modid = MODID,bus = Mod.EventBusSubscriber.Bus.FORGE,value = Dist.CLIENT)
public class RenderEvent {






    @SubscribeEvent
    public static void drawBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
//        String name = ((TranslatableContents)event.getBossEvent().getName().getContents()).getKey().split("\\.",2)[1];
        if(ClientConfig.BossBarStyle.get() != 0){
            try{
                CustomizeBossHealthBar bar = CustomizeBossHealthBar.getBossHealthBars((event.getBossEvent().getName().getString()));
                if(bar!= null)
                    bar.render(event);
            }catch (Exception e){
                TerraEntity.LOGGER.warn(e.getLocalizedMessage());
            }

        }
    }

    public static boolean isIrisShader = false;
    public static boolean isAfterSky = false;


    @SubscribeEvent
    public static void renderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL){
            isIrisShader = ModChecker.iris.isLoaded() && !(RenderSystem.getShader() instanceof ShaderInstance);

            BrainTranslucent.render(event);
            DebugBlocksHelper.Singleton().render(event);
            NPCChatBubbleBuffer.getInstance().render(event);
            isAfterSky = false;
        } else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            TongueRenderer.renderFirstPerson(event);

        } else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            isAfterSky = true;
        } else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
//            isIrisShader = ModChecker.iris.isLoaded() && RenderSystem.getShader() instanceof ExtendedShader;
            DebugEntityHelper.INSTANCE.render(event);
        }
    }

    @SubscribeEvent
    public static void MobRenderTongue(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        LivingEntity livingEntity = event.getEntity();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        float partialTick = event.getPartialTick();
        int packedLight = event.getPackedLight();
        if (livingEntity.hasEffect(TEEffects.THE_TONGUE.get())) {
            MobEffect rawEffect = livingEntity.getEffect(TEEffects.THE_TONGUE.get()).getEffect();
            if (rawEffect instanceof TheTongueEffect effect) {
                WallOfFleshMouth mouth = effect.getWallOfFleshMouth();
                if (mouth != null && mouth.isAlive() && livingEntity.isAlive()) {
                    Vec3 init = mouth.position();

                    poseStack.pushPose();
                    if (Minecraft.getInstance().player != null) {
                        // 错位渲染，防止正对的时候看到的是片面
                        Vec3 p = Minecraft.getInstance().player.position().subtract(init);
                        double a = p.cross(new Vec3(0, 1, 0)).dot(Minecraft.getInstance().player.position().subtract(livingEntity.position()));
                        poseStack.mulPose(Axis.YN.rotation(a > 0 ? 0.5f : -0.5f));
                    }

                    poseStack.popPose();

                    Vec3 lerpPos = new Vec3(
                            Mth.lerp(partialTick, livingEntity.xOld, livingEntity.getX()),
                            Mth.lerp(partialTick, livingEntity.yOld+livingEntity.getEyeHeight()*0.5f, livingEntity.getY()+livingEntity.getEyeHeight()*0.5f),
                            Mth.lerp(partialTick, livingEntity.zOld, livingEntity.getZ())
                    );

                    Vec3 _diff = lerpPos.subtract(init);
                    Vec3 diffNorm = _diff.normalize();

                    // 让叶子紧贴实体
                    Vec3 offset = diffNorm.scale(-1);
                    Vec3 diff = _diff.subtract(offset);
                    double distance = _diff.length(); // 获取实体与初始点的直线距离[6](@ref)
                    int baseCount = 5; // 基础数量
                    float densityFactor = 0.8f; // 每米增加的数量密度

                    int count = Mth.clamp(
                            (int) (distance * densityFactor) + baseCount,
                            baseCount,
                            50
                    );
                    double dx = diff.x / count;
                    double dy = diff.y / count;
                    double dz = diff.z / count;
                    Quaternionf rotate = TEUtils.rotateFromV1ToV2(new Vector3f(0, 1, 0), new Vector3f((float) dx, (float) dy, (float) dz));
                    BakedModel model = Minecraft.getInstance().getModelManager().getModel(EntityBlockModelRegister.getInstance().getModelResourceLocation(TEMonsterEntities.THE_HUNGRY.get()));

                    for (int i = 0; i < count; i++) {
                        Vec3 pos = new Vec3(-i * dx + offset.x, -i * dy + offset.y, -i * dz + offset.z);
                        poseStack.pushPose();
                        poseStack.translate(-0.5f, 0.5f, -0.5f);
                        poseStack.translate(pos.x, pos.y, pos.z);
//            poseStack.translate(offset.x, offset.y, offset.z);
                        poseStack.translate(0.5, 0, 0.5);

                        poseStack.mulPose(rotate);
                        poseStack.mulPose(Axis.YN.rotation(i * 0.5f));
                        poseStack.translate(-0.5, 0, -0.5);

//            poseStack.translate(-0.5,0,0);

                        ItemStack stack = Items.AIR.getDefaultInstance();
                        for (RenderType rendertype : model.getRenderTypes(stack, false)) {
                            VertexConsumer vertexconsumer = ItemRenderer.getFoilBuffer(bufferSource, rendertype, false, stack.isEnchanted());
                            Minecraft.getInstance().getItemRenderer().renderModelLists(
                                    model, stack, packedLight, OverlayTexture.NO_OVERLAY,
                                    poseStack, vertexconsumer);
                        }

                        poseStack.popPose();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderHand(RenderHandEvent event) {
        if(BossSpawnCameraManager.INSTANCE.isAnimating()){
            event.setCanceled(true);
        }

        ItemStack stack = event.getItemStack();
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null){
            return;
        }
        if (event.getHand() == InteractionHand.MAIN_HAND && stack.getItem() instanceof BaseWhipItem item) {
            // 右手使用鞭子时取消渲染
            if (player.getCooldowns().isOnCooldown(item)) {
//                ci.cancel();
                float progress = (player.tickCount - BaseWhipItem.clickTime + event.getPartialTick());
                int cooldown = BaseWhipItem.cooldownTime;
                progress = Math.min(progress, cooldown) / cooldown;

                progress = progress > 0.5? 2 - progress * 2 : progress * 2;
                event.getPoseStack().translate(0, -progress  ,0);
            }
        }
//        Minecraft.getInstance().getBlockRenderer().renderBatched();
        if (event.getHand() == InteractionHand.MAIN_HAND) {

            Item item = player.getMainHandItem().getItem();
            // 使用有悠悠球时渲染手臂
            LazyOptional<WeaponStorage> cap = player.getCapability(TEAttachments.WEAPON_STORAGE);
            if(item instanceof YoyosItem && cap.isPresent() && cap.orElseThrow(RuntimeException::new).yoyosEntity != null) {

                PlayerRenderer playerrenderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
                PoseStack poseStack = event.getPoseStack();
                poseStack.pushPose();
                poseStack.translate(0.3, 0.1, -0.5);


//            poseStack.translate(0.5,-0.2,-1.2);
                var buffer = event.getMultiBufferSource();
                int packedLight = event.getPackedLight();
                float f = 1.0F;
                poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(20.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(f * -60.0F));
                poseStack.translate(f * 0.3F, -1.1F, 0.45F);
                poseStack.translate(0.8, 1.0, 0.3);

                playerrenderer.renderRightHand(poseStack, buffer, packedLight, player);

                poseStack.popPose();
            }
        }


    }



}
