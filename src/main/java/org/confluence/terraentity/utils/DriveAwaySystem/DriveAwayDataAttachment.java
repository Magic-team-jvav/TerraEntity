package org.confluence.terraentity.utils.DriveAwaySystem;

import net.minecraft.world.entity.Mob;
import org.confluence.terraentity.init.TEAttachments;
import org.jetbrains.annotations.Nullable;

/**
 * 实体驱离数据 Attachment - 绑定到实体生命周期
 * 使用 TEAttachments.DRIVE_AWAY_DATA 作为 AttachmentType
 * 
 * 修改：驱离是一次性、覆盖式的效果，箭矢命中时就应该重置所有参数
 */
public class DriveAwayDataAttachment {

    /**
     * 创建实体的驱离数据（强制覆盖）
     * @param entity 目标实体
     * @param center 驱离中心点
     * @param distance 驱离总距离
     * @param speed 驱离速度
     * @param time 驱离时间
     * @return 新创建的驱离数据
     */
    public static DriveAwayAttachment.DriveAwayData create(Mob entity, net.minecraft.world.phys.Vec3 center, double distance, double speed, double time) {
        DriveAwayAttachment.DriveAwayData data = new DriveAwayAttachment.DriveAwayData(center, distance, speed, time);
        entity.setData(TEAttachments.DRIVE_AWAY_DATA.get(), data);
        return data;
    }

    /**
     * 获取实体驱离数据（可能为null）
     * 判断是否是有效数据（非空且非默认初始化状态）
     */
    @Nullable
    public static DriveAwayAttachment.DriveAwayData get(Mob entity) {
        DriveAwayAttachment.DriveAwayData data = entity.getData(TEAttachments.DRIVE_AWAY_DATA.get());
        // 判断是否是有效数据（非空初始化状态）
        // 如果 speed == 0 && time == 0，说明是默认工厂创建的空对象
        return (data != null && (data.speed != 0 || data.time != 0)) ? data : null;
    }

    /**
     * 移除实体驱离数据
     * 重置为空状态而非 removeData（因为 Attachment 绑定生命周期）
     */
    public static void remove(Mob entity) {
        // 重置为空状态而非 removeData（因为 Attachment 绑定生命周期）
        entity.setData(TEAttachments.DRIVE_AWAY_DATA.get(), new DriveAwayAttachment.DriveAwayData());
    }

    /**
     * 检查是否有驱离数据
     */
    public static boolean has(Mob entity) {
        DriveAwayAttachment.DriveAwayData data = get(entity);
        return data != null;
    }
}
