package org.confluence.terraentity.utils.DriveAwaySystem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.Vec3;

/**
 * 实体驱离系统数据存储 - 使用 Codec 序列化
 */
public class DriveAwayAttachment {

    //驱离数据 - 使用 Codec 序列化
    public static class DriveAwayData {
        //已经过的tick数
        public int elapsedTicks;
        //上一位置（用于检测卡住）
        public Vec3 lastPosition;
        //卡住计数
        public int stuckTicks;
        //驱离中心点
        public Vec3 center;
        //驱离总距离
        public double totalDistance;
        //驱离速度
        public double speed;
        //驱离时间（tick）
        public double time;
        //是否已完成驱离
        public boolean completed;
        //当前帧的驱离方向（用于连续性）
        public Vec3 currentFleeDirection;

        // DriveAwayData 的 Codec（移除 bezierPath）
        public static final Codec<DriveAwayData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("elapsedTicks").forGetter(d -> d.elapsedTicks),
                Vec3.CODEC.optionalFieldOf("lastPosition", Vec3.ZERO).forGetter(d -> d.lastPosition != null ? d.lastPosition : Vec3.ZERO),
                Codec.INT.fieldOf("stuckTicks").forGetter(d -> d.stuckTicks),
                Vec3.CODEC.fieldOf("center").forGetter(d -> d.center),
                Codec.DOUBLE.fieldOf("totalDistance").forGetter(d -> d.totalDistance),
                Codec.DOUBLE.fieldOf("speed").forGetter(d -> d.speed),
                Codec.DOUBLE.fieldOf("time").forGetter(d -> d.time),
                Codec.BOOL.fieldOf("completed").forGetter(d -> d.completed),
                Vec3.CODEC.optionalFieldOf("currentFleeDirection", Vec3.ZERO).forGetter(d -> d.currentFleeDirection)
        ).apply(instance, DriveAwayData::new));

        public DriveAwayData() {
            this.elapsedTicks = 0;
            this.lastPosition = Vec3.ZERO;
            this.stuckTicks = 0;
            this.center = Vec3.ZERO;
            this.totalDistance = 0.0F;
            this.speed = 0.0F;
            this.time = 0.0F;
            this.completed = false;
            this.currentFleeDirection = Vec3.ZERO;
        }

        public DriveAwayData(int elapsedTicks, Vec3 lastPosition, int stuckTicks,
                            Vec3 center, double totalDistance, double speed, double time, boolean completed,
                            Vec3 currentFleeDirection) {
            this.elapsedTicks = elapsedTicks;
            this.lastPosition = lastPosition;
            this.stuckTicks = stuckTicks;
            this.center = center;
            this.totalDistance = totalDistance;
            this.speed = speed;
            this.time = time;
            this.completed = completed;
            this.currentFleeDirection = currentFleeDirection;
        }

        public DriveAwayData(Vec3 center, double totalDistance, double speed, double time) {
            this.elapsedTicks = 0;
            this.lastPosition = Vec3.ZERO;
            this.stuckTicks = 0;
            this.center = center;
            this.totalDistance = totalDistance;
            this.speed = speed;
            this.time = time;
            this.completed = false;
            this.currentFleeDirection = Vec3.ZERO;
        }

        /**
         * 设置当前驱离方向
         * @param dir 驱离方向向量
         */
        public void setCurrentFleeDirection(Vec3 dir) {
            this.currentFleeDirection = dir.normalize();
        }

        public boolean isComplete() {
            return completed || elapsedTicks >= time;
        }

        public boolean updatePosition(Vec3 currentPos, double stuckThreshold, int maxStuckTicks) {
            if (lastPosition != null) {
                double moved = currentPos.distanceTo(lastPosition);
                if (moved < stuckThreshold) {
                    stuckTicks++;
                } else {
                    stuckTicks = 0;
                }
            }
            lastPosition = currentPos;
            return stuckTicks >= maxStuckTicks;
        }

        public void resetStuck() {
            stuckTicks = 0;
        }

        public void complete() {
            this.completed = true;
        }
    }
}
