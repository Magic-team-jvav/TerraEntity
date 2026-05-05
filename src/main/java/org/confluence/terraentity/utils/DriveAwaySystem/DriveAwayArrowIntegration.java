package org.confluence.terraentity.utils.DriveAwaySystem;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.monster.Harpy;
import org.confluence.terraentity.init.TEEffects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 弓箭驱离系统集成
 * 每 tick 检测范围内飞行单位，添加 SCARED 效果并驱离
 */
public class DriveAwayArrowIntegration {

    public static final double FLIGHT_CUBE_RANGE = 3.0;
    private static final double FLIGHT_SPEED = 1.3;
    private static final double FLIGHT_TIME = 160.0;
    private static final double FLIGHT_RANGE_RANDOM_MIN = 0.8;
    private static final double FLIGHT_RANGE_RANDOM_MAX = 1.2;
    private static final double FLIGHT_OFFSET_MAX = 1.5;

    public static final double HIT_CUBE_RANGE = 6.0;
    private static final double HIT_SPEED = 1.3;
    private static final double HIT_TIME = 260.0;
    private static final double HIT_RANGE_RANDOM_MIN = 0.5;
    private static final double HIT_RANGE_RANDOM_MAX = 1.5;
    private static final double HIT_OFFSET_MAX = 2.5;
    
    // 存储弓箭上次触发位置（可选，用于距离补偿）
    private static final Map<UUID, Vec3> arrowLastTriggerPos = new HashMap<>();
    
    /**
     * 弓箭每 tick 调用
     * 直接检测范围内飞行单位，给无效果者添加 SCARED 并驱离
     */
    public static void onArrowTick(Projectile arrow) {
        if (arrow.level().isClientSide()) {
            return;
        }

        Level level = arrow.level();
        Vec3 currentPos = arrow.position();
        
        // 每 tick 直接检测并施加效果
        applyScareToFlyingEntities(level, currentPos, FLIGHT_CUBE_RANGE, FLIGHT_SPEED, FLIGHT_TIME,
                FLIGHT_RANGE_RANDOM_MIN, FLIGHT_RANGE_RANDOM_MAX, FLIGHT_OFFSET_MAX);
    }

    
    /**
     * 弓箭命中时调用
     * 大范围检测并施加效果
     */
    public static void onArrowHit(Projectile arrow, Vec3 hitPos) {
        if (arrow.level().isClientSide()) {
            return;
        }

        Level level = arrow.level();

        // 命中时大范围施加效果
        applyScareToFlyingEntities(level, hitPos, HIT_CUBE_RANGE, HIT_SPEED, HIT_TIME,
                HIT_RANGE_RANDOM_MIN, HIT_RANGE_RANDOM_MAX, HIT_OFFSET_MAX);

        // 清理追踪数据
        arrowLastTriggerPos.remove(arrow.getUUID());
    }

    /**
     * 核心方法：检测范围内飞行单位，无效果者添加 SCARED 并驱离
     * @param center 驱离中心（箭矢位置）
     * @param cubeRange 检测范围
     */
    private static void applyScareToFlyingEntities(Level level, Vec3 center, double cubeRange,
                                                    double speed, double time,
                                                    double rangeRandomMin,
                                                    double rangeRandomMax, double offsetMax) {
        AABB searchBox = new AABB(
                center.x - cubeRange, center.y - cubeRange, center.z - cubeRange,
                center.x + cubeRange, center.y + cubeRange, center.z + cubeRange
        );

        // 查询所有 LivingEntity
        var entities = level.getEntitiesOfClass(LivingEntity.class, searchBox);
        
        for (LivingEntity entity : entities) {
            // 只处理飞行单位
            if (!isFlyingEntity(entity)) continue;
            
            // 跳过已有 SCARED 效果的实体
            if (entity.hasEffect(TEEffects.SCARED.getDelegate())) continue;
            
            // 必须是 Mob 才能驱离
            if (!(entity instanceof Mob mob)) continue;
            
            // 1. 添加 SCARED 效果（持续时间与驱离时间一致）
            entity.addEffect(new MobEffectInstance(
                    TEEffects.SCARED.getDelegate(),
                    (int) time,
                    0
            ));
            
            // 2. 立即执行驱离（以箭矢位置为中心）
            Vec3 direction = entity.position().subtract(center).normalize();
            // 飞行单位优先水平远离，略微上升
            direction = new Vec3(direction.x, 0.3, direction.z).normalize();
            
            DriveAwayExecutor.applyToSingleEntity(
                    mob,
                    center,           // 驱离中心 = 箭矢位置
                    direction,
                    speed,
                    time,
                    rangeRandomMin,
                    rangeRandomMax,
                    offsetMax
            );
        }
    }
    
    /**
     * 判断是否为飞行单位
     */
    private static boolean isFlyingEntity(LivingEntity entity) {
        return entity instanceof FlyingAnimal 
            || entity instanceof net.minecraft.world.entity.FlyingMob 
            || entity instanceof Harpy;
    }
    
    /**
     * 计算水平距离
     */
    private static double calculateHorizontalDistance(Vec3 pos1, Vec3 pos2) {
        double dx = pos2.x - pos1.x;
        double dz = pos2.z - pos1.z;
        return Math.sqrt(dx * dx + dz * dz);
    }
    
    public static void clearArrowTrackingData(Projectile arrow) {
        arrowLastTriggerPos.remove(arrow.getUUID());
    }
    
    public static double getDefaultFlightCubeRange() {
        return FLIGHT_CUBE_RANGE;
    }
    
    public static double getDefaultHitCubeRange() {
        return HIT_CUBE_RANGE;
    }
}