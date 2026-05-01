package org.confluence.terraentity.entity.summon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;
import org.confluence.terraentity.api.entity.ICollisionAttackEntity;
import org.confluence.terraentity.api.entity.IWorm;
import org.confluence.terraentity.api.entity.IWormSegment;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * 星尘龙体节
 */
public class StardustDragonSegment extends PartEntity<StardustDragon> implements IWormSegment, GeoEntity, ICollisionAttackEntity {

    int index;
    boolean isTail = false;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    public StardustDragonSegment(StardustDragon parent, int index) {
        super(parent);
        this.index = index;
    }


    // StardustDragonSegment.java - 添加updateSegment方法
    public void updateSegment(Vec3 targetPos, float segmentSpacing) {
        // 保存上一帧的位置用于插值平滑
        double oldX = this.getX();
        double oldY = this.getY();
        double oldZ = this.getZ();
        float oldXRot = this.getXRot();
        float oldYRot = this.getYRot();

        // 计算从当前位置到目标位置的方向
        Vec3 toTarget = targetPos.subtract(this.position());
        double distance = toTarget.length();

        // 如果距离大于期望间距，向目标位置移动
        if (distance > 0.01) {
            Vec3 moveDir = toTarget.normalize();

            // 移动距离：不超过期望间距和当前距离的最小值
            double moveDistance = Math.min(segmentSpacing, distance);
            Vec3 newPos = this.position().add(moveDir.scale(moveDistance));

            // 设置位置
            this.setPos(newPos.x, newPos.y, newPos.z);

            // 计算朝向（面向移动方向）
            if (moveDir.length() > 0.001) {
                double horizontalDistance = Math.sqrt(moveDir.x * moveDir.x + moveDir.z * moveDir.z);
                float yaw = (float) (Math.atan2(moveDir.z, moveDir.x) * 180.0 / Math.PI) - 90.0F;
                float pitch = (float) (-Math.atan2(moveDir.y, horizontalDistance) * 180.0 / Math.PI);

                this.setYRot(yaw);
                this.setXRot(pitch);
            }

            // 设置运动速度
            Vec3 delta = this.position().subtract(oldX, oldY, oldZ);
            this.setDeltaMovement(delta.x, delta.y, delta.z);
        }


        float yaw = (float) (Math.atan2(toTarget.z(), toTarget.x()) * 180.0D / Math.PI) + 90.0F;
        float pitch = -(float) (Math.atan2(toTarget.y(), distance) * 180.0D / Math.PI);

        this.setYRot(yaw);
        this.setXRot(pitch);


        this.setDeltaMovement(toTarget);
        this.moveTo(targetPos, yaw, pitch);

        // 同步网络数据（如果需要）
        this.xo = oldX;
        this.yo = oldY;
        this.zo = oldZ;
        this.xRotO = oldXRot;
        this.yRotO = oldYRot;
        this.yRotO = IWorm.wrapYRotation(this.yRotO, yaw);

        this.doCollisionAttack(e->e instanceof LivingEntity living && this.getParent().canAttack(living),
                e->this.getParent().doHurtTarget(e)
        );
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    public boolean isTail() {
        return this.isTail;
    }

    @Override
    public void setTail(boolean tail) {
        this.isTail = tail;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public boolean isHurtOverlay() {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    @Override
    public CollisionProperties getCollisionProperties() {
        return this.getParent().getCollisionProperties();
    }

    @Override
    public boolean shouldDoCollision() {
        return this.getParent().shouldDoCollision();
    }
}
