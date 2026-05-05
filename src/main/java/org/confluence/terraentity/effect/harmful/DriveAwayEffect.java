package org.confluence.terraentity.effect.harmful;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import org.confluence.terraentity.entity.monster.Harpy;
import org.confluence.terraentity.utils.DriveAwaySystem.DriveAwayExecutor;
import java.util.Random;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

/**
 * <h1>驱离效果 - 对飞行单位使用驱离系统</h1>
 * <p>当飞行实体获得此效果时，使用 DriveAwaySystem 将其驱离（让鸟妖自己飞走）</p>
 * <p>参数由基准值 * (amplifier + 1) 决定</p>
 * <p>驱离距离由速度和时间决定：distance = speed * duration（duration由效果持续时间控制）</p>
 * <p>cubeRange > 0 时范围驱离，=0 时单体驱离</p>
 *
 * @param baseSpeed         基准驱离速度
 * @param baseTime          基准驱离时间（tick），实际由效果持续时间控制
 * @param baseRangeRandomMin 基准随机范围最小值
 * @param baseRangeRandomMax 基准随机范围最大值
 * @param baseOffsetMax      基准最大偏移量
 * @param baseCubeRange     基准驱离范围（立方体半边长），>0 时范围驱离，=0 时单体驱离
 */
public class DriveAwayEffect extends MobEffect {
    private final double baseSpeed;
    private final double baseTime;
    private final double baseRangeRandomMin;
    private final double baseRangeRandomMax;
    private final double baseOffsetMax;
    private final double baseCubeRange;

    public DriveAwayEffect(double baseSpeed, double baseTime,
                           double baseRangeRandomMin, double baseRangeRandomMax,
                           double baseOffsetMax, double baseCubeRange) {
        super(MobEffectCategory.HARMFUL, 0x5d478b);
        this.baseSpeed = baseSpeed;
        this.baseTime = baseTime;
        this.baseRangeRandomMin = baseRangeRandomMin;
        this.baseRangeRandomMax = baseRangeRandomMax;
        this.baseOffsetMax = baseOffsetMax;
        this.baseCubeRange = baseCubeRange;
    }

    public static final MapCodec<DriveAwayEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.DOUBLE.fieldOf("base_speed").forGetter(DriveAwayEffect::getBaseSpeed),
            Codec.DOUBLE.fieldOf("base_time").forGetter(DriveAwayEffect::getBaseTime),
            Codec.DOUBLE.fieldOf("base_range_random_min").forGetter(DriveAwayEffect::getBaseRangeRandomMin),
            Codec.DOUBLE.fieldOf("base_range_random_max").forGetter(DriveAwayEffect::getBaseRangeRandomMax),
            Codec.DOUBLE.fieldOf("base_offset_max").forGetter(DriveAwayEffect::getBaseOffsetMax),
            Codec.DOUBLE.fieldOf("base_cube_range").forGetter(DriveAwayEffect::getBaseCubeRange)
    ).apply(instance, DriveAwayEffect::new));

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public double getBaseTime() {
        return baseTime;
    }

    public double getBaseRangeRandomMin() {
        return baseRangeRandomMin;
    }

    public double getBaseRangeRandomMax() {
        return baseRangeRandomMax;
    }

    public double getBaseOffsetMax() {
        return baseOffsetMax;
    }

    public double getBaseCubeRange() {
        return baseCubeRange;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        // 作为MobEffect使用时，从实体获取效果持续时间
        Holder<MobEffect> effectHolder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(this);
        if (entity.hasEffect(effectHolder)) {
            MobEffectInstance effectInstance = entity.getEffect(effectHolder);
            if (effectInstance != null) {
                int duration = effectInstance.getDuration();
                applyDriveAway(entity, amplifier, duration, null);
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration > 0 && duration % 20 == 0;
    }

    private void applyDriveAway(LivingEntity entity, int amplifier, int duration, @Nullable Vec3 pos) {
        boolean isFlying = entity instanceof FlyingAnimal || entity instanceof Harpy;
        if (isFlying && entity instanceof Mob mob) {
            double factor = amplifier + 1;
            double speed = baseSpeed * factor;
            // 使用效果持续时间(duration)作为驱离时间
            double time = duration;
            double rangeRandomMin = baseRangeRandomMin * factor;
            double rangeRandomMax = baseRangeRandomMax * factor;
            double offsetMax = baseOffsetMax * factor;

            // 确定驱离中心
            Vec3 center;
            if (pos != null) {
                // 使用传入的 Vec3 pos 作为驱离中心
                center = pos;
            } else {
                // 在周围随机设置一个距离为1的点作为中心
                double angle = entity.level().random.nextDouble() * Math.PI * 2;
                center = entity.position().add(Math.cos(angle), 0, Math.sin(angle));
            }

            // 计算驱离方向：让实体飞离中心
            Vec3 direction = entity.position().subtract(center).normalize();
            // 随机化y分量
            double randomY = (Math.random() * 4) - 2;
            direction = new Vec3(direction.x, randomY, direction.z).normalize();

            // 直接对当前实体应用驱离效果
            DriveAwayExecutor.applyToSingleEntity(
                    mob,
                    center,
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
     * 应用驱离效果，可指定驱离中心
     * @param entity 目标实体
     * @param amplifier 效果等级
     * @param duration 效果持续时间（tick），如果<=0则使用baseTime * (amplifier + 1)
     * @param pos 驱离中心，如果为 null 则在实体周围随机生成一个距离为1的点作为中心
     */
    public void applyDriveAwayWithPos(LivingEntity entity, int amplifier, int duration, @Nullable Vec3 pos) {
        applyDriveAway(entity, amplifier, duration, pos);
    }
}
