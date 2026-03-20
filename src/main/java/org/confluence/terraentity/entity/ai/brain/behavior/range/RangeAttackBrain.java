package org.confluence.terraentity.entity.ai.brain.behavior.range;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.utils.TEUtils;

import java.util.List;

/**
 * 执行远程攻击的行为
 */
public class RangeAttackBrain<T extends Mob> extends Behavior<T> {

    protected int prepareTime; // 看向敌人后，瞄准需要时间
    protected int _prepareTime;
    protected boolean isPreparing; // 是否准备攻击
    float attackRange;

    public RangeAttackBrain(int prepareTime, float attackRange) {
        super(ImmutableMap.of(
                        MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                        MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                        MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_PRESENT
                ),
                200);
        this.prepareTime = prepareTime;
        this._prepareTime = prepareTime;
        this.attackRange = attackRange;

    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, T owner) {
        LivingEntity target = owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        if(target.distanceTo(owner) > attackRange){
            return false;
        }
        return !owner.getBrain().getMemory(MemoryModuleType.ATTACK_COOLING_DOWN).get();
    }

    protected void tickLook(ServerLevel level, T owner, LivingEntity target){
        BehaviorUtils.lookAtEntity(owner, target);
        owner.lookAt(target, 10, owner.getMaxHeadYRot());
    }

    @Override
    protected void tick(ServerLevel level, T owner, long gameTime) {
        owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((target) -> {

            this.tickLook(level, owner, target);
            // 如果能触发攻击
            if(this.canTrigger(owner, target)) {
                if(owner.getSensing().hasLineOfSight(target)){
                    // 开始准备
                    isPreparing = true;
                }else{
                    this.doStop(level, owner, gameTime);
                    owner.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
                    owner.getBrain().updateActivityFromSchedule(level.getDayTime(), gameTime);
                }
            }

            if(isPreparing){
                // 准备阶段
                onPrepare(level, owner, target, prepareTime);
                if(--prepareTime <= 0) {
                    // 开始攻击
                    doAttack(level, owner, target);
                }
            }

        });

    }

    /**
     * 是否可以触发远程攻击，默认条件是视线朝向敌人
     */
    protected boolean canTrigger(T owner, LivingEntity target){
        double angle = TEUtils.angleBetween(owner.getLookAngle(), target.getEyePosition().subtract(owner.getEyePosition()).normalize());
        angle = Mth.wrapDegrees(angle);
        return angle < 0.1f;
    }

    /**
     * 准备开始执行远程攻击
     */
    protected void onPrepare(ServerLevel level, T owner, LivingEntity target, int prepareTime){
        // 可能要停下来瞄准
//        if(owner.getRandom().nextFloat() < 0.1f){
//            owner.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
//        }
        owner.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        owner.getNavigation().stop();
        owner.lookAt(target, 30,30);
        double angel = TEUtils.angleBetween(owner.getLookAngle(), target.getEyePosition().subtract(owner.getEyePosition()).normalize());
        if(angel <0.1f){
            this.prepareTime-= 2;
        }
    }

    @Override
    protected void start(ServerLevel level, T entity, long gameTimeIn) {
        entity.startUsingItem(InteractionHand.MAIN_HAND);
    }

    @Override
    protected void stop(ServerLevel level, T entity, long gameTimeIn) {
        entity.getBrain().setMemory(MemoryModuleType.ATTACK_COOLING_DOWN, true);
        isPreparing = false;
        prepareTime = _prepareTime;
        entity.stopUsingItem();
//        entity.swing(InteractionHand.MAIN_HAND, true);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, T entity, long gameTimeIn) {
        return prepareTime > 0 && entity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET);
    }

    protected void doAttack(ServerLevel level, T owner, LivingEntity target){
        if(customDoAttack(level, owner, target)){
            return;
        }
        defaultDoAttack(level, owner, target);
    }

    /**
     * 自定义远程攻击逻辑, 返回true表示已自定义攻击逻辑，否则使用默认逻辑
     */
    protected boolean customDoAttack(ServerLevel level, T owner, LivingEntity target){
        ItemStack stack = owner.getMainHandItem();
        if(stack.getItem() instanceof BowItem bow){

            ItemStack itemstack = Items.ARROW.getDefaultInstance();
            ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
            AbstractArrow abstractarrow = arrowitem.createArrow(level, itemstack, owner);
            abstractarrow = bow.customArrow(abstractarrow);
            abstractarrow.shootFromRotation(owner, owner.getXRot(), owner.yHeadRot, 0.0F, 1.5f, 1.0F);
            if (owner.getRandom().nextFloat() < 0.33f) {
                abstractarrow.setCritArrow(true);
            }

            int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
            if (j > 0) {
                abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double)j * 0.5 + 0.5);
            }

            int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
            if (k > 0) {
                abstractarrow.setKnockback(k);
            }

            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                abstractarrow.setSecondsOnFire(100);
            }

            stack.hurtAndBreak(1, owner, (self) -> {
                self.broadcastBreakEvent(owner.getUsedItemHand());
            });


            level.addFreshEntity(abstractarrow);
//            weaponItem.shoot( level, owner, InteractionHand.MAIN_HAND, stack, List.of(Items.ARROW.getDefaultInstance()), 1.5f, 1f, owner.getRandom().nextFloat() < 0.3f, target);
            return true;
        }
        return false;
    }

    /**
     * 默认远程攻击逻辑
     */
    protected void defaultDoAttack(ServerLevel level, T owner, LivingEntity target){
        // 调用默认的方法
        Arrow arrow = new Arrow(owner.level(), owner);
        arrow.setPos(owner.getX(), owner.getY() + owner.getEyeHeight(), owner.getZ());
        arrow.shootFromRotation(owner, owner.getXRot(), owner.yHeadRot, 0.0F, 1.5F, 1.0F);
        level.addFreshEntity(arrow);
    }

    protected AbstractArrow getArrow(Level pLevel, T owner, ItemStack pCrossbowStack, ItemStack pAmmoStack) {
        ArrowItem arrowitem = (ArrowItem)(pAmmoStack.getItem() instanceof ArrowItem ? pAmmoStack.getItem() : Items.ARROW);
        AbstractArrow abstractarrow = arrowitem.createArrow(pLevel, pAmmoStack, owner);

        abstractarrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        abstractarrow.setShotFromCrossbow(true);
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, pCrossbowStack);
        if (i > 0) {
            abstractarrow.setPierceLevel((byte)i);
        }

        return abstractarrow;
    }
}