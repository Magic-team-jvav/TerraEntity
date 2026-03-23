package org.confluence.terraentity.item;

import com.github.edg_thexu.cafelib.api.datacomponent.IDataComponentType;
import com.github.edg_thexu.cafelib.api.item.CafeItemProperties;
import com.github.edg_thexu.cafelib.data.component.Unbreakable;
import com.github.edg_thexu.cafelib.init.CafeDataComponentTypes;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.state.BlockState;
import org.confluence.terraentity.api.event.InitItemEvent;
import org.confluence.terraentity.init.TEDataComponentTypes;
import org.confluence.terraentity.entity.proj.WhipEntity;
import org.confluence.terraentity.init.TEAttributes;
import org.confluence.terraentity.init.entity.TEProjectileEntities;
import org.confluence.terraentity.registries.hit_effect.IEffectStrategy;
import org.confluence.terraentity.utils.TEUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class BaseWhipItem extends Item {

    public final int hitCooldown;

    public final Supplier<? extends ParticleOptions> particleOptions;
    public final float chance;
    public boolean canPenetrate;
    public static int clickTime;
    public static int cooldownTime;

    public final float markDamage;
    public final float attackSpeed;
    public final float damage;
    public final float rangeFactor;

    public Supplier<BlockState> blockStateSupplier;

    CafeItemProperties properties;
    /**
     * <h1>鞭子
     * @param damage - 召唤伤害
     * @param markDamage - 标记伤害
     * @param attackSpeed - 攻击速度
     * @param hitCooldown - 击中同一目标的间隔
     * @param rangeFactor - 攻击范围
     */
    public BaseWhipItem(CafeItemProperties properties,
                        float damage,
                        float markDamage,
                        float attackSpeed,
                        int hitCooldown,
                        float rangeFactor) {
        super(properties);
        InitItemEvent.InitWhip event = new InitItemEvent.InitWhip(properties, damage, markDamage, attackSpeed, hitCooldown, rangeFactor);
        this.hitCooldown = event.hitCooldown;
        this.markDamage = event.markDamage;
        this.attackSpeed = event.attackSpeed;
        this.rangeFactor = event.rangeFactor;
        this.damage = event.damage * 0.5f; //对于原版的适配
        this.properties = properties;
        if(properties instanceof WhipProperties whipProperties) {
            this.particleOptions = whipProperties.particleOptions;
            this.chance = whipProperties.chance;
            this.blockStateSupplier = whipProperties.blockStateSupplier;
            this.canPenetrate = whipProperties.canPenetrate;
        }
        else {
            this.particleOptions = null;
            this.chance = 0f;
        }
    }

    private double getCdReduction(Player player) {
        double speed = player.getAttribute(Attributes.ATTACK_SPEED).getValue();
        return 1 / (speed * 0.25f);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if(usedHand == InteractionHand.OFF_HAND) return InteractionResultHolder.success(stack);
        if(!level.isClientSide){

            if(stack.getItem() instanceof BaseWhipItem self) {
                int cooldown = (int) (20 * getCdReduction(player));
                player.getCooldowns().addCooldown(this, cooldown);
                if(player.getOffhandItem().getItem() instanceof BaseWhipItem other){
                    player.getCooldowns().addCooldown(other, cooldown);
                }
                WhipEntity whipEntity = TEProjectileEntities.WHIP_PROJECTILE.get().create(level);
                whipEntity.setWeapon(stack);
                whipEntity.setExistTick(cooldown);
                whipEntity.setOwner(player);
                whipEntity.setPos(player.position().add(0, player.getBbHeight() * 0.5f, 0).add(TEUtils.getPlayerHandPos(player)));
                whipEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.5f, 1.0F);
                whipEntity.hitCooldown = hitCooldown;
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                level.addFreshEntity(whipEntity);
//                stack.hurtAndBreak(1, player, (Consumer<LivingEntity>) (e -> e.playSound(SoundEvents.)));
            }
        }else{
            clickTime = player.tickCount;
            cooldownTime = (int) (20 * getCdReduction(player));
        }
        player.swing(usedHand);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.MOB_LOOTING || super.canApplyAtEnchantingTable(stack,enchantment);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        var data = IDataComponentType.getData(stack, TEDataComponentTypes.EFFECT_STRATEGY.get());
        if (data != null) {
            IEffectStrategy.appendDescription(tooltipComponents, data.effects(), Component.translatable("tooltip.terra_entity.whip.hit_effect").withStyle(style -> style.withColor(0xB4C363)));
        }
        // 农场主增益
        var data1 = IDataComponentType.getData(stack, TEDataComponentTypes.EFFECT_STRATEGY_BENEFICIAL);
        if (data1 != null) {
            tooltipComponents.add(Component.literal(" ? ? ?").withStyle(style -> style.withColor(0x666666).withObfuscated(true)));
//            IEffectStrategy.appendDescription(tooltipComponents, data1.effects(), Component.translatable("tooltip.terra_entity.whip.hit_effect_beneficial").withStyle(style -> style.withColor(0x84C363)), 0x678563);
        }
    }

    public static class WhipProperties extends CafeItemProperties {
        Supplier<? extends ParticleOptions> particleOptions;
        float chance;
        Supplier<BlockState> blockStateSupplier;

        List<Function<CafeItemProperties, CafeItemProperties>> modifiers = new ArrayList<>();
        boolean hasDamage = false;
        boolean canPenetrate = false;

        /**
         * 当没有注册模型时，使用方块状态代替模型渲染
         */
        public WhipProperties setBlock(Supplier<BlockState> blockStateSupplier) {
            this.blockStateSupplier = blockStateSupplier;
            return this;
        }

        /**
         * 设置粒子效果
         * @param particleOptions 粒子效果
         * @param chance 粒子效果出现的几率
         */
        public WhipProperties setParticle(Supplier<? extends ParticleOptions> particleOptions, float chance) {
            this.particleOptions = particleOptions;
            this.chance = chance;
            return this;
        }

        public WhipProperties addModifier(Function<CafeItemProperties, CafeItemProperties> modifier) {
            modifiers.add(modifier);
            return this;
        }

        /**
         * 设置耐久度，默认为无限耐久
         * @param durability 耐久度
         */
        public WhipProperties setDurability(int durability) {
            modifiers.add(p-> (CafeItemProperties)p.durability(durability));
            hasDamage = true;
            return this;
        }

        public WhipProperties setCanPenetrate() {
            this.canPenetrate = true;
            return this;
        }

        /**
         * 生成Properties
         */
        public CafeItemProperties buildProperties() {

            if(!hasDamage) this.component(CafeDataComponentTypes.UNBREAKABLE_COMPONENT, new Unbreakable(true)).stacksTo(1);
            return modifiers.stream().reduce(this, (p, m)-> (WhipProperties) m.apply(p), (p1, p2)->p1);
        }
    }


    static UUID uuid1 = UUID.fromString("bb3e0d35-6fff-4448-a899-2c82d4558b44");
    static UUID uuid2 = UUID.fromString("ed5ea748-2b5c-4763-9d7b-4ed7071fa31c");
    static UUID uuid3 = UUID.fromString("8d0c9872-0e74-4ff1-a03b-ad8f998527e5");
    static UUID uuid4 = UUID.fromString("9eee1c9e-ca46-443b-8e22-70d0410eeec3");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if(slot == EquipmentSlot.MAINHAND)
            return ImmutableMultimap.of(
                    TEAttributes.SUMMON_DAMAGE.get(), new AttributeModifier(uuid1,"whip_damage_modifier", damage, AttributeModifier.Operation.ADDITION),
                    TEAttributes.MARK_DAMAGE.get(), new AttributeModifier(uuid2,"whip_mark_damage_modifier", markDamage, AttributeModifier.Operation.ADDITION),
                    Attributes.ATTACK_SPEED, new AttributeModifier(uuid3,"whip_attack_speed_modifier", attackSpeed, AttributeModifier.Operation.MULTIPLY_BASE),
                    TEAttributes.WHIP_RANGE.get(), new AttributeModifier(uuid4,"whip_range_modifier", rangeFactor, AttributeModifier.Operation.MULTIPLY_BASE)

            );
        return super.getAttributeModifiers(slot, stack);
    }
}