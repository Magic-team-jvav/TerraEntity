package org.confluence.terraentity.entity.summon;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;
import org.confluence.terraentity.api.entity.IWorm;
import org.confluence.terraentity.attachment.SummonerAttachment;
import org.confluence.terraentity.entity.ai.goal.behavior.BTFactory;
import org.confluence.terraentity.entity.ai.goal.behavior.BTNode;
import org.confluence.terraentity.entity.ai.goal.behavior.BTRoot;
import org.confluence.terraentity.entity.ai.goal.behavior.condition.Condition;
import org.confluence.terraentity.entity.ai.goal.behavior.condition.TargetExistCondition;
import org.confluence.terraentity.entity.ai.goal.behavior.leaf.SetAttributeAction;
import org.confluence.terraentity.entity.ai.motion.DragonMovement;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.utils.CircularArrayBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.List;

/**
 * 星尘龙
 */
public class StardustDragon extends AbstractSummonMob implements FlyingAnimal, IWorm<StardustDragonSegment> {

    List<StardustDragonSegment> segments;
//    int segmentCount;
    CircularArrayBuffer<Vec3> buffer;
    protected StardustDragonMovement movement;
    private static final EntityDataAccessor<Integer> DATA_SEGMENT_COUNT = SynchedEntityData.defineId(StardustDragon.class, EntityDataSerializers.INT);

    public StardustDragon(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
//        this.segmentCount = 1;
        this.segments = this.initParts();
        this.noCulling = true;
        this.noPhysics = true;
        this.setNoGravity( true);
        this.buffer = new CircularArrayBuffer<>(new Vec3[this.getEntityData().get(DATA_SEGMENT_COUNT) * 10]);
        this.movement = new StardustDragonMovement(this, 0.7f, true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SEGMENT_COUNT, 1);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if(key == DATA_SEGMENT_COUNT && level().isClientSide) {
            this.addSegment();
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.tickWormMove();
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, this.createBT());
        summon_registerMoveGoal();
        summon_registerTargetGoals();
    }
    protected BTRoot<StardustDragon> createBT(){
        return new StardustDragonBT(this);
    }

    private static class StardustDragonBT extends BTRoot<StardustDragon> {

        public StardustDragonBT(StardustDragon mob) {
            super(mob);
        }

        @Override
        protected @NotNull BTNode createBehaviorTree() {
            return BTFactory.infinite(BTFactory.selector()
                    // 游走
                    .addWithCondition(Condition.not(new TargetExistCondition(this.mob)), BTFactory.sequence()
                            .addChild(new SetTurnSpeedAction(0.3f))
                            .addChild(new SetAttributeAction(this.mob, Attributes.MOVEMENT_SPEED, 0.3))
                            .addChild(new RandomStrollAction())
                            .addChild(BTFactory.wait(10))

                    )
                    // 攻击
                    .addWithCondition(new TargetExistCondition(this.mob), BTFactory.sequence()
                            .addChild(new SetTurnSpeedAction(0.7f))
                            .addChild(new SetAttributeAction(this.mob, Attributes.MOVEMENT_SPEED, 0.7))
                            .addChild(new SetTargetPosAction())
                            .addChild(BTFactory.wait(5))
                    ));
        }

        private class SetTargetPosAction extends BTNode {
            @Override
            public BTStatus execute() {
                if(mob.getTarget() != null) {
                    mob.movement.targetPos = mob.getTarget().getBoundingBox().getCenter();
                }
                return BTStatus.FAILURE;
            }
        }

        private class RandomStrollAction extends BTNode {
            @Override
            public BTStatus execute() {
                if(mob.summon_getOwner() != null) {
                    Vec3 ownerPos = mob.summon_getOwner().position().offsetRandom(mob.random, 5).add(0, 2, 0);
                    mob.movement.targetPos = ownerPos;
                    return BTStatus.SUCCESS;
                }
                return BTStatus.FAILURE;
            }
        }

        private class SetTurnSpeedAction extends BTNode {
            private final float turnSpeed;
            public SetTurnSpeedAction(float turnSpeed) {
                this.turnSpeed = turnSpeed;
            }
            @Override
            public BTStatus execute() {
                mob.movement.turnSpeed = turnSpeed;
                return BTStatus.SUCCESS;
            }
        }

    }

    @Override
    public void tickWormMove() {
        this.buffer.add(this.position());

        if(!this.level().isClientSide) {
//            if(this.getTarget() != null) {
//                this.movement.targetPos = this.getTarget().position().add(0,3,0);
//            }
            this.movement.tickAI();
        }

        List<StardustDragonSegment> segments = this.getBodySegments();
        int segmentCount = segments.size();

        // 身体节之间的期望距离（单位：方块）
        float segmentSpacing = this.getSegmentSpacing();
        Vec3 prevPos = buffer.get(0); // 最新位置（头部当前位置）
        float accumulatedDist = 0;
        int index = 1;

        for (int i = 0; i < segmentCount; i++) {
            StardustDragonSegment segment = segments.get(i);

            // 计算这一节应该距离头部的路径长度
            float targetDistance = segmentSpacing * (i + 1);

            // 从历史轨迹中获取目标位置
//            Vec3 targetPos = getPositionAtDistance(targetDistance);
            Vec3 targetPos = null;
            for (; index < buffer.size() && targetPos == null; index++) {
                Vec3 currentPos = buffer.get(index);
                if (currentPos == null) {
                    continue;
                }

                float stepDist = (float) prevPos.distanceTo(currentPos);

                // 检查距离是否有效
                if (stepDist <= 0.0001f) {
                    // 两点重合，跳过这个点继续下一个
                    continue;
                }

                if (accumulatedDist + stepDist >= targetDistance) {
                    // 目标点落在这个线段上，进行插值
                    float t = (targetDistance - accumulatedDist) / stepDist;
                    // 确保 t 在 [0, 1] 范围内
                    t = Math.min(1.0f, Math.max(0.0f, t));
                    targetPos = prevPos.lerp(currentPos, t);
                    break;
                }

                accumulatedDist += stepDist;
                prevPos = currentPos;
            }

            if (targetPos != null) {
                // 再次检查 targetPos 是否有效
                if (!Double.isNaN(targetPos.x) && !Double.isNaN(targetPos.y) && !Double.isNaN(targetPos.z)) {
                    segment.updateSegment(targetPos, segmentSpacing);
                }
            }else{
                // 缓冲区不够长

            }
        }
    }

    private void addSegment() {
        segments.get(segments.size() - 1).setTail(false);
        var tail = createPart(this.getSegmentCount());
        tail.setTail(true);
        segments.add(tail);
        this.setCost(this.getCost() + 1);
        if(!level().isClientSide) {
            this.entityData.set(DATA_SEGMENT_COUNT, this.getSegmentCount() + 1);
        }
        this.buffer.expand(this.getSegmentCount() * 10);

    }

    @Override
    public SummonResult summon(@UnknownNullability ServerPlayer player, ItemStack stack) {
        SummonerAttachment data = player.getData(TEAttachments.SUMMONER_STORAGE);
        List<Integer> list = data.getIds();
        for (Integer integer : list) {
            Entity e = level().getEntity(integer);
            if (e instanceof StardustDragon dragon) {
                dragon.addSegment();
                data.summon(this.getCost(), integer);
                data.sync(player);
                return SummonResult.MERGE;
            }
        }
        return super.summon(player, stack);
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public PartEntity<?> @NotNull [] getParts() {
        return IWorm.super.getWormParts();
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.recreateWormFromPacket();
    }

    @Override
    public void onRemovedFromLevel() {
        super.onRemovedFromLevel();
        this.onWormRemovedFromLevel();
    }

    @Override
    public int getSegmentCount() {
        return this.getEntityData().get(DATA_SEGMENT_COUNT);
    }

    @Override
    public StardustDragonSegment createPart(int index) {
        return new StardustDragonSegment(this, index);
    }

    @Override
    public List<StardustDragonSegment> getBodySegments() {
        return this.segments;
    }
    @Override
    public int getMaxHeadXRot() {
        return 85;
    }

    protected float getSegmentSpacing() {
        return 0.55f;
    }

    protected static class StardustDragonMovement extends DragonMovement {

        public StardustDragonMovement(Mob mob, float turnSpeed, boolean shouldMove) {
            super(mob, turnSpeed, shouldMove);
        }

        @Override
        protected void applyYawMovement(float yawRotA) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, yawRotA * 0.0002F, 0));
        }

    }
}
