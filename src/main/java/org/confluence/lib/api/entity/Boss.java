package org.confluence.lib.api.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.entity.util.DeathAnimOptions;
import org.confluence.terraentity.utils.FloatRGB;

/**
 * All bosses should implement this interface
 * <p>
 * 所有boss都应该实现这个接口
 */
public interface Boss extends Enemy, IDiscardWhenRespawnEntity {

    default boolean shouldShowMessage(){
        return isMainBody();
    }

    default boolean isMainBody(){
        return true;
    }

    default boolean shouldEnhanceMultiplayer(){
        return true;
    }

    interface BossPart extends Boss {
        @Override
        default boolean isMainBody(){
            return false;
        }
    }

    static void sendBossSpawnMessage(Entity entity){
        Level level = entity.level();
        if (entity instanceof Boss boss && !level.isClientSide){
            if (boss.shouldShowMessage()){
                Component mes;
                FloatRGB color;
                if (entity instanceof DeathAnimOptions dao){
                    float[] _color = dao.getBloodColor();
                    color = new FloatRGB(_color[0], _color[1], _color[2]);
                } else {
                    color = new FloatRGB(0.7F, 0, 0);
                }

                mes = Component.translatable("message.terraentity.boss_spawn",
                        entity.getDisplayName()).withStyle(Style.EMPTY.withColor(color.get()).withBold(true));

                for (Player player : level.players()){
                    player.sendSystemMessage(mes);
                }
            }
        }
    }

    static void sendBossDeathMessage(Entity entity){
        Level level = entity.level();
        if (entity instanceof Boss boss && !level.isClientSide){
            if (boss.shouldShowMessage()){
                Component mes;
                FloatRGB color;
                if (entity instanceof DeathAnimOptions dao){
                    float[] _color = dao.getBloodColor();
                    color = new FloatRGB(_color[0], _color[1], _color[2]);
                } else {
                    color = new FloatRGB(0.7F, 0, 0);
                }
                mes = Component.translatable("message.terraentity.boss_leave",
                        entity.getDisplayName()).withStyle(Style.EMPTY.withColor(color.get()).withBold(true));

                for (Player player : level.players()){
                    player.sendSystemMessage(mes);
                }
            }
        }
    }
}
