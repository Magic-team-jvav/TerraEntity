package org.confluence.terraentity.utils.DriveAwaySystem;

import net.minecraft.world.phys.Vec3;

import java.util.Random;

//实体驱离系统数学工具类
public class DriveAwayMath {
    
    private static final Random RANDOM = new Random();
    
    /**
     * 构建局部坐标系
     * @param xAxis X轴方向（射线方向）
     * @return [yAxis, zAxis] 正交基
     */
    public static Vec3[] buildLocalCoordinateSystem(Vec3 xAxis) {
        Vec3 normalizedX = xAxis.normalize();
        
        // 选择一个不与X轴平行的参考向量
        Vec3 ref;
        if (Math.abs(normalizedX.y) < 0.9) {
            ref = new Vec3(0, 1, 0);
        } else {
            ref = new Vec3(1, 0, 0);
        }
        
        // 计算Z轴 = X轴 × 参考向量
        Vec3 zAxis = normalizedX.cross(ref).normalize();
        
        // 计算Y轴 = Z轴 × X轴
        Vec3 yAxis = zAxis.cross(normalizedX).normalize();
        
        return new Vec3[]{yAxis, zAxis};
    }
    
    /**
     * 生成三次贝塞尔曲线控制点
     * @param start 起始点
     * @param direction 驱离方向（从中心指向实体）
     * @param distance 驱离距离
     * @param rangeRandomMin 随机距离最小值
     * @param rangeRandomMax 随机距离最大值
     * @param offsetMax YZ平面最大偏移
     * @return 4个控制点 [P0, P1, P2, P3]
     */
    public static Vec3[] generateBezierControlPoints(Vec3 start, Vec3 direction, double distance, 
                                                      double rangeRandomMin, double rangeRandomMax, double offsetMax) {
        Vec3 normalizedDir = direction.normalize();
        
        // 随机化距离
        double actualDistance = distance * (rangeRandomMin + RANDOM.nextDouble() * (rangeRandomMax - rangeRandomMin));
        
        // 构建局部坐标系
        Vec3[] basis = buildLocalCoordinateSystem(normalizedDir);
        Vec3 yAxis = basis[0];
        Vec3 zAxis = basis[1];
        
        // P0: 起始点
        Vec3 p0 = start;
        
        // P1: 第一个控制点，沿方向一定距离，带随机偏移
        double offset1 = RANDOM.nextDouble() * offsetMax;
        double angle1 = RANDOM.nextDouble() * Math.PI * 2;
        Vec3 offset1Vec = yAxis.scale(Math.cos(angle1) * offset1).add(zAxis.scale(Math.sin(angle1) * offset1));
        Vec3 p1 = p0.add(normalizedDir.scale(actualDistance * 0.33)).add(offset1Vec);
        
        // P2: 第二个控制点，继续沿方向，带随机偏移
        double offset2 = RANDOM.nextDouble() * offsetMax;
        double angle2 = RANDOM.nextDouble() * Math.PI * 2;
        Vec3 offset2Vec = yAxis.scale(Math.cos(angle2) * offset2).add(zAxis.scale(Math.sin(angle2) * offset2));
        Vec3 p2 = p0.add(normalizedDir.scale(actualDistance * 0.66)).add(offset2Vec);
        
        // P3: 终点
        double offset3 = RANDOM.nextDouble() * offsetMax * 0.5;
        double angle3 = RANDOM.nextDouble() * Math.PI * 2;
        Vec3 offset3Vec = yAxis.scale(Math.cos(angle3) * offset3).add(zAxis.scale(Math.sin(angle3) * offset3));
        Vec3 p3 = p0.add(normalizedDir.scale(actualDistance)).add(offset3Vec);
        
        return new Vec3[]{p0, p1, p2, p3};
    }
    
    /**
     * 采样三次贝塞尔曲线位置
     * @param p0 控制点0
     * @param p1 控制点1
     * @param p2 控制点2
     * @param p3 控制点3
     * @param t 参数 [0, 1]
     * @return 曲线上的位置
     */
    public static Vec3 sampleCubicBezier(Vec3 p0, Vec3 p1, Vec3 p2, Vec3 p3, double t) {
        double u = 1 - t;
        double tt = t * t;
        double uu = u * u;
        double uuu = uu * u;
        double ttt = tt * t;
        
        Vec3 result = p0.scale(uuu);
        result = result.add(p1.scale(3 * uu * t));
        result = result.add(p2.scale(3 * u * tt));
        result = result.add(p3.scale(ttt));
        
        return result;
    }
    
    /**
     * 采样三次贝塞尔曲线切线方向
     * @param p0 控制点0
     * @param p1 控制点1
     * @param p2 控制点2
     * @param p3 控制点3
     * @param t 参数 [0, 1]
     * @return 切线方向（已归一化）
     */
    public static Vec3 sampleCubicBezierTangent(Vec3 p0, Vec3 p1, Vec3 p2, Vec3 p3, double t) {
        double u = 1 - t;
        
        // 导数: B'(t) = 3(1-t)²(P1-P0) + 6(1-t)t(P2-P1) + 3t²(P3-P2)
        Vec3 tangent = p1.subtract(p0).scale(3 * u * u)
                .add(p2.subtract(p1).scale(6 * u * t))
                .add(p3.subtract(p2).scale(3 * t * t));
        
        return tangent.normalize();
    }
    
    /**
     * 计算墙面滑动方向
     * @param desiredDir 期望移动方向
     * @param wallNormal 墙面法向量
     * @return 投影到墙面切平面的方向
     */
    public static Vec3 computeSlideDirection(Vec3 desiredDir, Vec3 wallNormal) {
        // 将速度投影到墙面切平面: v' = v - (v·n)n
        double dot = desiredDir.dot(wallNormal);
        Vec3 projected = desiredDir.subtract(wallNormal.scale(dot));
        
        // 如果投影后长度太小，使用墙面的一个随机切向
        if (projected.length() < 0.01) {
            Vec3[] basis = buildLocalCoordinateSystem(wallNormal);
            double angle = RANDOM.nextDouble() * Math.PI * 2;
            projected = basis[0].scale(Math.cos(angle)).add(basis[1].scale(Math.sin(angle)));
        }
        
        return projected.normalize();
    }
    
    /**
     * 计算实体到中心的距离因子（越远因子越小）
     * @param entityPos 实体位置
     * @param center 中心点
     * @param maxRange 最大范围
     * @return 距离因子 [0, 1]，0表示在中心，1表示在最大范围
     */
    public static double calculateDistanceFactor(Vec3 entityPos, Vec3 center, double maxRange) {
        double distance = entityPos.distanceTo(center);
        return Math.min(1.0, distance / maxRange);
    }
    
    /**
     * 根据距离因子调整驱离距离（越远距离越小）
     * @param baseDistance 基础距离
     * @param distanceFactor 距离因子 [0, 1]
     * @return 调整后的距离
     */
    public static double adjustDistanceByFactor(double baseDistance, double distanceFactor) {
        // 使用二次衰减：越远衰减越多
        return baseDistance * (1.0 - distanceFactor * distanceFactor * 0.5);
    }

    /**
     * 根据距离因子调整驱离时间（越远距离越小）
     * @param baseTime 基础时间（tick）
     * @param distanceFactor 距离因子 [0, 1]
     * @return 调整后的时间
     */
    public static double adjustTimeByFactor(double baseTime, double distanceFactor) {
        // 使用二次衰减：越远衰减越多
        return baseTime * (1.0 - distanceFactor * distanceFactor * 0.5);
    }
}
