package org.confluence.terraentity.item;

import PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attribute.PortAttributeExtension;
import PortLib.extensions.net.minecraft.world.item.Item.PortItemExtension;
import PortLib.extensions.net.minecraft.world.item.ItemStack.PortItemStackExtension;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.lib.ConfluenceMagicLib;
import org.confluence.lib.common.LibAttributes;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.api.item.ILeftClickStateItem;
import org.confluence.terraentity.entity.proj.WhipEntity;
import org.confluence.terraentity.init.TEDataComponentTypes;
import org.confluence.terraentity.init.entity.TEProjectileEntities;
import org.confluence.terraentity.registries.hit_effect.IEffectStrategy;
import org.confluence.terraentity.utils.TEUtils;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.registries.PortRegistryEntry;
import org.mesdag.portlib.wrapper.world.entity.PortEquipmentSlotGroup;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;
import org.mesdag.portlib.wrapper.world.item.component.PortItemAttributeModifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class BaseWhipItem extends Item implements ILeftClickStateItem {
    // region client
    public static int clickTime;
    public static int cooldownTime;
    // endregion

    public final int hitCooldown;
    public final Supplier<? extends ParticleOptions> particleOptions;
    public final float chance;
    public boolean canPenetrate;

    public Supplier<BlockState> blockStateSupplier;

    /// # 鞭子
    ///
    /// @param damage      - 召唤伤害
    /// @param markDamage  - 标记伤害
    /// @param attackSpeed - 攻击速度
    /// @param hitCooldown - 击中同一目标的间隔
    public BaseWhipItem(
            Properties properties,
            float damage,
            float markDamage,
            float attackSpeed,
            int hitCooldown,
            float rangeFactor
    ) {
        super(PortItemExtension.Properties.attributes(properties.stacksTo(1), PortItemAttributeModifiers.builder().add(
                LibAttributes.getSummonDamage(),
                new PortAttributeModifier(TerraEntity.space("whip_damage_modifier"), damage, PortAttributeModifier.PortOperation.ADD_VALUE),
                PortEquipmentSlotGroup.MAINHAND
        ).add(
                PortAttributeExtension.wrap(Attributes.ATTACK_SPEED),
                new PortAttributeModifier(TerraEntity.space("whip_attack_speed_modifier"), attackSpeed, PortAttributeModifier.PortOperation.ADD_MULTIPLIED_BASE),
                PortEquipmentSlotGroup.MAINHAND
        ).add(
                ConfluenceMagicLib.MARK_DAMAGE,
                new PortAttributeModifier(TerraEntity.space("whip_mark_damage_modifier"), markDamage, PortAttributeModifier.PortOperation.ADD_VALUE),
                PortEquipmentSlotGroup.MAINHAND
        ).add(
                ConfluenceMagicLib.WHIP_RANGE,
                new PortAttributeModifier(TerraEntity.space("whip_range_modifier"), rangeFactor, PortAttributeModifier.PortOperation.ADD_MULTIPLIED_BASE),
                PortEquipmentSlotGroup.MAINHAND
        ).build()));
        this.hitCooldown = hitCooldown;
        if (properties instanceof WhipProperties whipProperties) {
            this.particleOptions = whipProperties.particleOptions;
            this.chance = whipProperties.chance;
            this.blockStateSupplier = whipProperties.blockStateSupplier;
            this.canPenetrate = whipProperties.canPenetrate;
        } else {
            this.particleOptions = null;
            this.chance = 0f;
        }
    }

    private double getCdReduction(Player player) {
        double speed = player.getAttribute(Attributes.ATTACK_SPEED).getValue();
        return 1 / (speed * 0.25f);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.MOB_LOOTING || super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        var data = PortItemStackExtension.getData(stack, TEDataComponentTypes.EFFECT_STRATEGY);
        if (data != null) {
            IEffectStrategy.appendDescription(tooltipComponents, data.effects(), Component.translatable("tooltip.terra_entity.whip.hit_effect").withStyle(style -> style.withColor(0xB4C363)));
        }
        // 农场主增益
        var data1 = PortItemStackExtension.getData(stack, TEDataComponentTypes.EFFECT_STRATEGY_BENEFICIAL);
        if (data1 != null) {
            tooltipComponents.add(Component.literal(" ? ? ?").withStyle(style -> style.withColor(0x666666).withObfuscated(true)));
//            IEffectStrategy.appendDescription(tooltipComponents, data1.effects(), Component.translatable("tooltip.terra_entity.whip.hit_effect_beneficial").withStyle(style -> style.withColor(0x84C363)), 0x678563);
        }
    }

    @Override
    public void onLeftClick(Player player, ItemStack itemStack) {
        if (player.getCooldowns().cooldowns.keySet().stream().anyMatch(item -> item instanceof BaseWhipItem)) {
            return;
        }
        if (player.isLocalPlayer()) {
            clickTime = player.tickCount;
            cooldownTime = (int) (20 * getCdReduction(player));
        } else if (itemStack.is(this)) {
            int cooldown = (int) (20 * getCdReduction(player));
            player.getCooldowns().addCooldown(this, cooldown);
            if (player.getOffhandItem().getItem() instanceof BaseWhipItem other) {
                player.getCooldowns().addCooldown(other, cooldown);
            }
            WhipEntity whipEntity = TEProjectileEntities.WHIP_PROJECTILE.get().create(player.level());
            if (whipEntity != null) {
                whipEntity.setWeapon(itemStack);
                whipEntity.setExistTick(cooldown);
                whipEntity.setOwner(player);
                whipEntity.setPos(player.position().add(0, player.getBbHeight() * 0.5f, 0).add(TEUtils.getPlayerHandPos(player)));
                whipEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.05f, 0F);
                whipEntity.hitCooldown = hitCooldown;
//                stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                player.level().addFreshEntity(whipEntity);
//                stack.hurtAndBreak(1, player, (Consumer<LivingEntity>) (e -> e.playSound(SoundEvents.)));}
            }
        }
        player.swing(InteractionHand.MAIN_HAND);
    }

    @Override
    public void onLeftRelease(Player player, ItemStack itemStack) {}

    @Override
    public boolean canSwitchWithoutRelease(Player player, ItemStack itemStack) {
        return false;
    }

    public static class WhipProperties extends Properties {
        Supplier<? extends ParticleOptions> particleOptions;
        float chance;
        Supplier<BlockState> blockStateSupplier;

        List<Function<WhipProperties, Properties>> modifiers = new ArrayList<>();
        boolean hasDamage = false;
        boolean canPenetrate = false;

        /// 当没有注册模型时，使用方块状态代替模型渲染
        public WhipProperties setBlock(Supplier<BlockState> blockStateSupplier) {
            this.blockStateSupplier = blockStateSupplier;
            return this;
        }

        /// 设置粒子效果
        ///
        /// @param particleOptions 粒子效果
        /// @param chance          粒子效果出现的几率
        public WhipProperties setParticle(Supplier<? extends ParticleOptions> particleOptions, float chance) {
            this.particleOptions = particleOptions;
            this.chance = chance;
            return this;
        }

        public WhipProperties addModifier(Function<WhipProperties, Properties> modifier) {
            modifiers.add(modifier);
            return this;
        }

        /// 设置耐久度，默认为无限耐久
        ///
        /// @param durability 耐久度
        public WhipProperties setDurability(int durability) {
            modifiers.add(p -> p.durability(durability));
            hasDamage = true;
            return this;
        }

        public WhipProperties setCanPenetrate() {
            this.canPenetrate = true;
            return this;
        }

        public <T> WhipProperties component(PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type, T value) {
            return component(type.get(), value);
        }

        public <T> WhipProperties component(PortDataComponentType<T> type, T value) {
            PortItemExtension.Properties.component(this, type, value);
            return this;
        }

        /// 生成Properties
        public Properties buildProperties() {
            if (!hasDamage) PortItemExtension.Properties.unbreakable(this);
            return modifiers.stream().reduce(this, (p, m) -> (WhipProperties) m.apply(p), (p1, p2) -> p1);
        }
    }
}
