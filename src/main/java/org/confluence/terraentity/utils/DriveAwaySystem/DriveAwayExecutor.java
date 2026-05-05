package org.confluence.terraentity.utils.DriveAwaySystem;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.Map;

/**
 * 实体驱离执行器
 * 注意：只适用于 Mob 实体（有导航AI的实体）
 * 使用 DataAttachment 存储驱离数据，绑定到实体生命周期
 * 
 * 修改点：
 * 1. 移除预计算贝塞尔路径，改为每tick动态计算局部路径
 * 2. currentFleeDirection 字段保持方向连续性
 * 3. 飞行实体使用导航，地面实体使用 setDeltaMovement
 * 4. 每tick设置朝向防止原地旋转
 * 5. 前瞻距离限制为 speed * 5，避免直线冲刺
 * 6. 随机因子收紧为 0.9-1.1，保证路径稳定性
 */
public class DriveAwayExecutor {
    
    //卡住检测阈值（距离）
    private static final double STUCK_THRESHOLD = 0.1;
    //最大卡住tick数
    private static final int MAX_STUCK_TICKS = 10;
    
    /**
     * 驱离立方体范围内的所有实体
     * @param level 世界
     * @param center 中心点
     * @param cubeRange 立方体半边长
     * @param speed 驱离速度
     * @param time 驱离时间（tick）
     * @param rangeRandomMin 随机距离最小值因子
     * @param rangeRandomMax 随机距离最大值因子
     * @param offsetMax YZ平面最大偏移
     */
    public static void driveAwayEntitiesInCube(Level level, Vec3 center, double cubeRange,
                                               double speed, double time,
                                               double rangeRandomMin,
                                               double rangeRandomMax, double offsetMax) {
        AABB aabb = new AABB(
            center.x - cubeRange, center.y - cubeRange, center.z - cubeRange,
            center.x + cubeRange, center.y + cubeRange, center.z + cubeRange
        );
        
        // 只驱离 Mob 实体（有导航AI的实体）
        var entities = level.getEntitiesOfClass(Mob.class, aabb);
        
        for (Mob entity : entities) {
            // 计算距离因子（越远距离越小）
            double distanceFactor = DriveAwayMath.calculateDistanceFactor(entity.position(), center, cubeRange);
            double adjustedTime = DriveAwayMath.adjustTimeByFactor(time, distanceFactor);
            
            // 计算驱离方向（从中心指向实体）
            Vec3 direction = entity.position().subtract(center).normalize();
            
            applyToSingleEntity(entity, center, direction, speed, adjustedTime, rangeRandomMin, rangeRandomMax, offsetMax);
        }
    }
    
    /**
     * 对单个实体应用驱离效果 - 使用 DataAttachment
     * 不再预计算贝塞尔路径，改为在 tick 中动态计算
     * 修改：改为 create 强制覆盖，因为驱离是一次性、覆盖式的效果
     */
    public static void applyToSingleEntity(Mob entity, Vec3 center, Vec3 direction,
                                            double speed, double time,
                                            double rangeRandomMin,
                                            double rangeRandomMax, double offsetMax) {
        interruptEntityAI(entity);
        
        double distance = speed * time;
        
        // 改为 create 强制覆盖（驱离是一次性、覆盖式的效果）
        DriveAwayAttachment.DriveAwayData data = DriveAwayDataAttachment.create(
            entity, center, distance, speed, time
        );
        // 不再预计算贝塞尔路径，改为在 tick 中动态计算
        data.lastPosition = entity.position();
        // 设置初始驱离方向
        data.setCurrentFleeDirection(direction);
    }
    
    /**
     * 打断实体AI
     * @param entity 目标实体
     */
    private static void interruptEntityAI(Mob entity) {
        // 停止导航
        entity.getNavigation().stop();
        // 清除目标
        entity.setTarget(null);
    }
    
    /**
     * 每tick更新实体驱离状态 - 从 DataAttachment 读取
     * 使用局部贝塞尔曲线，动态计算路径
     */
    public static boolean tickEntityDriveAway(Mob entity) {
        // 1. 获取 DataAttachment 数据，为空或已完成则返回 false
        DriveAwayAttachment.DriveAwayData data = DriveAwayDataAttachment.get(entity);
        if (data == null || data.completed) {
            return false;
        }
        
        // 2. 检查是否超时完成，完成则清理并返回 false
        if (data.isComplete()) {
            data.complete();
            DriveAwayDataAttachment.remove(entity); // 清理
            return false;
        }
        
        // 3. 卡住检测：调用 data.updatePosition(entity.position(), STUCK_THRESHOLD, MAX_STUCK_TICKS)
        boolean isStuck = data.updatePosition(entity.position(), STUCK_THRESHOLD, MAX_STUCK_TICKS);
        if (isStuck) {
            // 尝试绕行
            attemptDetour(entity, data);
            data.resetStuck();
        }
        
        // 4. 计算进度 progress = data.elapsedTicks / data.time
        double progress = data.elapsedTicks / data.time;
        
        // 5. 计算剩余距离 remainingDist = data.totalDistance * (1 - progress)
        double remainingDist = data.totalDistance * (1 - progress);
        
        // 6. 计算驱离方向 fleeDir = entity.position().subtract(data.center).normalize()
        Vec3 fleeDir = entity.position().subtract(data.center).normalize();
        // 若长度接近0，使用 entity.getLookAngle() 作为备选
        if (fleeDir.length() < 0.001) {
            fleeDir = entity.getLookAngle().normalize();
        }
        
        // 7. 生成局部短距离贝塞尔控制点（前瞻距离：5帧可达）
        Vec3[] localPoints = DriveAwayMath.generateBezierControlPoints(
            entity.position(),           // P0: 当前位置
            fleeDir,                     // 远离中心方向
            Math.min(remainingDist, data.speed * 5), // 前瞻距离：5帧可达
            0.9, 1.1, 1.0                // 随机因子（小范围保证稳定性）
        );
        
        // 8. 采样前瞻目标点（t=0.3 表示曲线前30%位置）
        Vec3 targetPos = DriveAwayMath.sampleCubicBezier(
            localPoints[0], localPoints[1], localPoints[2], localPoints[3], 0.3
        );
        
        // 9. 调用 handleWallCollision 处理墙壁碰撞，得到 finalTarget
        Vec3 finalTarget = handleWallCollision(entity.level(), entity.position(), targetPos, data);
        
        // 10. 区分飞行/地面实体设置移动
        if (entity instanceof FlyingMob || entity instanceof FlyingAnimal) {
            // 飞行实体（FlyingMob/FlyingAnimal）：使用导航系统
            // 注意：不要使用 entity.getNavigation().stop() 后立刻 moveTo，这会打断导航造成卡顿
            entity.getNavigation().moveTo(finalTarget.x, finalTarget.y, finalTarget.z, data.speed);
        } else {
            // 地面实体：使用 setDeltaMovement
            Vec3 velocity = finalTarget.subtract(entity.position()).normalize().scale(data.speed);
            entity.setDeltaMovement(velocity);
            // 强制朝向移动方向（防止原地旋转）
            double yaw = Math.toDegrees(Math.atan2(velocity.z, velocity.x)) - 90;
            entity.setYRot((float) yaw);
            entity.setYHeadRot((float) yaw);
        }
        
        // 11. data.elapsedTicks++
        data.elapsedTicks++;
        
        // 12. 再次检查是否完成，完成则 DriveAwayDataAttachment.remove(entity)
        if (data.isComplete()) {
            DriveAwayDataAttachment.remove(entity);
        }
        
        // 13. 返回 true
        return true;
    }
    
    /**
     * 为飞行实体设置移动目标点
     * 使用导航系统的机制来移动飞行实体
     * @param entity 飞行实体
     * @param targetPos 目标位置
     * @param speed 移动速度
     */
    private static void setFlyingEntityTarget(Mob entity, Vec3 targetPos, double speed) {
        // 对于飞行实体，使用导航系统来设置目标位置
        if (entity.getNavigation() != null) {
            // 使用导航系统移动到目标位置
            entity.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, speed);
        } else {
            // 如果没有导航，回退到直接设置速度
            Vec3 velocity = targetPos.subtract(entity.position()).normalize().scale(speed);
            entity.setDeltaMovement(velocity);
        }
    }
    
    /**
     * 处理墙壁碰撞
     * @param level 世界
     * @param currentPos 当前位置
     * @param targetPos 目标位置
     * @param data 驱离数据
     * @return 实际移动目标位置
     */
    private static Vec3 handleWallCollision(Level level, Vec3 currentPos, Vec3 targetPos,
                                            DriveAwayAttachment.DriveAwayData data) {
        Vec3 desiredDir = targetPos.subtract(currentPos).normalize();
        
        // 检测碰撞
        BlockHitResult hitResult = level.clip(new ClipContext(
            currentPos, targetPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty()
        ));
        
        if (hitResult.getType() == BlockHitResult.Type.BLOCK) {
            // 撞墙了，计算滑动方向
            Vec3 wallNormal = Vec3.atLowerCornerOf(hitResult.getDirection().getNormal()).normalize();
            Vec3 slideDir = DriveAwayMath.computeSlideDirection(desiredDir, wallNormal);
            
            // 重新计算目标位置（沿滑动方向，使用存储的速度）
            return currentPos.add(slideDir.scale(data.speed));
        }
        
        return targetPos;
    }
    
    /**
     * 尝试绕行
     * 修改：不重置 elapsedTicks（保持时间连续性，只改变空间路径）
     * @param entity 目标实体
     * @param data 驱离数据
     */
    private static void attemptDetour(Mob entity, DriveAwayAttachment.DriveAwayData data) {
        // 重新生成路径，增加随机性
        Vec3 direction = entity.position().subtract(data.center).normalize();
        double newDistance = data.totalDistance * 0.5; // 减半防止无限远离
        
        // 注意：不重置 elapsedTicks，保持时间连续性
        // 只改变空间路径方向
        data.setCurrentFleeDirection(direction);
    }
    
    /**
     * 清理实体驱离数据
     */
    public static void clearEntityDriveAway(Mob entity) {
        DriveAwayDataAttachment.remove(entity);
    }
    
    /**
     * 检查实体是否正在被驱离
     */
    public static boolean isEntityDrivingAway(Mob entity) {
        DriveAwayAttachment.DriveAwayData data = DriveAwayDataAttachment.get(entity);
        return data != null && !data.completed && !data.isComplete();
    }
    
    /**
     * 获取实体驱离数据
     */
    public static DriveAwayAttachment.DriveAwayData getEntityDriveAwayData(Mob entity) {
        return DriveAwayDataAttachment.get(entity);
    }
    
    /**
     * 更新所有正在被驱离的实体状态
     */
    public static int tickAllDriveAwayEntities(ServerLevel level) {
        int activeCount = 0;
        
        // 遍历所有已加载实体，检查是否有驱离数据
        for (var entity : level.getAllEntities()) {
            if (!(entity instanceof Mob mob)) continue;
            
            DriveAwayAttachment.DriveAwayData data = DriveAwayDataAttachment.get(mob);
            if (data == null) continue;
            
            if (data.isComplete() || data.completed) {
                DriveAwayDataAttachment.remove(mob);
                continue;
            }
            
            boolean stillActive = tickEntityDriveAway(mob);
            if (stillActive) {
                activeCount++;
            }
        }
        
        return activeCount;
    }
}
