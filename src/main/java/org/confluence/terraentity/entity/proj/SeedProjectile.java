package org.confluence.terraentity.entity.proj;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.registries.track.variant.BasisTrack;
import org.confluence.terraentity.utils.TEUtils;

public class SeedProjectile extends BaseProj<SeedProjectile> {
    public Entity target;

    public SeedProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        this(pEntityType, pLevel, null);
    }
    public SeedProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel, Entity target) {
        super(pEntityType, pLevel, (MobEffectInstance) null);
        this.target = target;
        trackType = new BasisTrack(90, 0.05f);
        canPenetrateBlock = true;
        accelerationPower = 0;
    }

    @Override
    public void tick() {
        super.tick();

        if(getOwner() != null && target != null) {
            Vec3 dir = target.position().add(0, target.getEyeHeight() * 0.5f, 0).subtract(position());
            double angle = TEUtils.angleBetween(getDeltaMovement(), dir);
            Vec3 movement = trackType.calDeltaMovement(getDeltaMovement(), dir, angle);
            setDeltaMovement(movement);
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public int getLifetime() {
        return 100;
    }
}
