package org.confluence.terraentity.init.item;

import com.mojang.datafixers.util.Function3;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.lib.common.particle.CrossDustParticleOptions;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.component.SingleBooleanComponent;
import org.confluence.terraentity.entity.util.trail.BoomerangTrail;
import org.confluence.terraentity.init.TEDataComponentTypes;
import org.confluence.terraentity.init.TEEffectStrategies;
import org.confluence.terraentity.item.Boomerang;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class TEBoomerangItems {
    private static final Function3<Integer, Float, Float, Boomerang.BoomerangModifier> NORMAL_BOOMERANG_MODIFIER =
            (forwardTick, flySpeedFactor, backSpeedFactor) -> new Boomerang.BoomerangModifier().setForwardTick(forwardTick).setFlySpeedFactor(flySpeedFactor).setBackSpeedFactor(backSpeedFactor);
    private static final Function3<Integer, Integer, Boomerang.BoomerangModifier, Boomerang.BoomerangModifier> MULTI_BOOMERANG_MODIFIER =
            (cd, count, modifier) -> modifier.setNotWaitForBack().setCd(cd).setMaxCount(count);


    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TerraEntity.MODID);


    public static final RegistryObject<Boomerang> WOOD_BOOMERANG = register("wood_boomerang", 3.8F,
            new Boomerang.BoomerangModifier().setDurability(100)
    );
    public static final RegistryObject<Boomerang> ENCHANTED_BOOMERANG = register("enchanted_boomerang", 6.6F,
            NORMAL_BOOMERANG_MODIFIER.apply(15, 1.55f, 1.55f).setDurability(300).setLuminance(5)
    );
    public static final RegistryObject<Boomerang> SHROOMERANG = register("shroomerang", 8.6F,
            NORMAL_BOOMERANG_MODIFIER.apply(15, 1.55f, 1.55f).setDurability(500).setLuminance(5)
    );
    public static final RegistryObject<Boomerang> ICE_BOOMERANG = register("ice_boomerang", 8.3F,
            NORMAL_BOOMERANG_MODIFIER.apply(16, 1.6f, 1.6f)
                    .setOnHitEffect(TEEffectStrategies.Components.FROST_BURN_BOOMERANG_EFFECT.get())
                    .setDurability(500)
                    .setTrail(() -> new BoomerangTrail(20, 0x2553be, 0x255385))
                    .setLuminance(5)
                    .setParticle(e -> ParticleTypes.SNOWFLAKE)
    );   //50% 的几率造成 3 秒的霜冻。

    public static final RegistryObject<Boomerang> TRIMARANG = register("trimarang", 8.3F,
            MULTI_BOOMERANG_MODIFIER.apply(10, 3,
                    NORMAL_BOOMERANG_MODIFIER.apply(17, 1.85f, 1.85f)).setDurability(1000).setLuminance(6)
    );   // 使用三次

    public static final RegistryObject<Boomerang> COMBAT_WRENCH = register("combat_wrench", 9.0F,
            NORMAL_BOOMERANG_MODIFIER.apply(10, 3f, 1.85f).setDurability(1500)
    );

    public static final RegistryObject<Boomerang> FLAMARANG = register("flamarang", 20.3F,
            NORMAL_BOOMERANG_MODIFIER.apply(18, 1.85f, 1.85f)
                    .setOnHitEffect(TEEffectStrategies.Components.HELL_FIRE_EFFECT.get())
                    .setDurability(1500)
                    .setLuminance(7)
                    .setTrail(() -> new BoomerangTrail(200, 0xFB6464, 0xbc2525))
                    .setParticle(e -> {
                        int r = e.getRandom().nextInt(3);
                        int centerColor;
                        int edgeColor;
                        if (r == 0) {
                            centerColor = 0xffFFFF53;
                            edgeColor = 0xefFF7A53;
                        } else if (r == 1) {
                            centerColor = 0xffFFFF53;
                            edgeColor = 0xffFFB552;
                        } else {
                            centerColor = 0xffFFB552;
                            edgeColor = 0xefFF7A53;
                        }
                        Vector3f velocity = e.getDeltaMovement().normalize().scale(0.04).toVector3f();
                        Vector4f curve = new Vector4f(0, 1, 1, 1);
                        return new CrossDustParticleOptions(false, centerColor, edgeColor, velocity,
                                curve, e.getRandom().nextFloat() * 0.3f + 1.2f, 11, velocity.x > 0 ? 10 : -10,
                                curve, true, true, true, false);
                    }, 2)
    );   //狱炎效果。


    public static final RegistryObject<Boomerang> DEVELOPER_BOOMERANG = register("developer_boomerang", 9999,
            new Boomerang.BoomerangModifier().setNotWaitForBack().setCd(0) // 不需要等待返回，设置cd
                    .setForwardTick(50)
                    .setFlySpeedFactor(2) // 设置向前飞行速度
                    .setBackSpeedFactor(2f) // 设置后退速度
                    .setCanPenetrate()
                    .setLuminance(6)
                    .setMaxCount(10)
    );


    public static final RegistryObject<Boomerang> BeiDou_BOOMERANG = register("bei_dou_boomerang", 5,
            MULTI_BOOMERANG_MODIFIER.apply(5, 4,
                    NORMAL_BOOMERANG_MODIFIER.apply(40, 3.0f, 3.0f)
                            .setMaxPenetration(7)
                            .setOnHitEffect(TEEffectStrategies.Components.BEI_DOU_EFFECT.get())
            )
    );

    private static RegistryObject<Boomerang> register(String name, float damage, Boomerang.BoomerangModifier boomerangModifier) {
        return ITEMS.register(name, () -> new Boomerang(damage, boomerangModifier, new Item.Properties().stacksTo(1)
                .component(TEDataComponentTypes.BOOMERANG_READY, SingleBooleanComponent.TRUE)
//                .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
                .component(DataComponents.ATTRIBUTE_MODIFIERS, boomerangModifier.attributeModifiersBuilder.build())));
    }
}
