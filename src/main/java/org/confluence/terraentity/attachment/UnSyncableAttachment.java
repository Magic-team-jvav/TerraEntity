package org.confluence.terraentity.attachment;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;
import org.confluence.terraentity.entity.boss.hillofflesh.HillOfFlesh;
import org.confluence.terraentity.item.DebugItem;
import org.jetbrains.annotations.UnknownNullability;

public class UnSyncableAttachment implements INBTSerializable<CompoundTag> {

    HillOfFlesh fightingHillOfFlesh;
    /**
     * 防止在风暴中走不出来
     */
    int invulnerableStormTime = 20;
    DebugItem.DebugMode debugMode = DebugItem.DebugMode.BT_WEB_VIEWER;


    public void setFightingHillOfFlesh(HillOfFlesh fightingHillOfFlesh) {
        this.fightingHillOfFlesh = fightingHillOfFlesh;
    }

    public HillOfFlesh getFightingHillOfFlesh() {
        return fightingHillOfFlesh;
    }

    public void triggerInvulnerableStorm(LivingEntity entity) {
        invulnerableStormTime = 40;
        if(entity instanceof ServerPlayer player){
//            ClientBoundEventPacket.resetCrimsonStorm(player);
        }
    }

    public void tickInvulnerableStorm() {
        if (invulnerableStormTime > 0) {
            invulnerableStormTime--;
        }
    }

    public boolean isInvulnerableStormActive() {
        return invulnerableStormTime > 0;
    }

    public DebugItem.DebugMode getDebugMode() {
        return this.debugMode;
    }

    public void setDebugMode(DebugItem.DebugMode debugMode) {
        this.debugMode = debugMode;
    }


    @Override
    public @UnknownNullability CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
    }
}
