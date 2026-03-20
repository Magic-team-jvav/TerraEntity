package org.confluence.terraentity.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.api.event.SummonEvent;
import org.confluence.terraentity.attachment.SummonerAttachment;
import org.confluence.terraentity.api.entity.ISummonMob;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.init.TEAttributes;
import org.confluence.terraentity.init.TESounds;
import org.confluence.terraentity.utils.TEUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class SummonItem<T extends Mob & ISummonMob> extends Item {
    public final RegistryObject<EntityType<T>> entityType;
    public final int consume;

    public final float baseAttackDamage;

    List<Component> tooltips;
    Supplier<SoundEvent> sound;

    public SummonItem(Properties properties, RegistryObject<EntityType<T>>  entityType, int consume, float baseAttackDamage) {
        this(properties, entityType, consume, baseAttackDamage, List.of());
    }

    public SummonItem(Properties properties, RegistryObject<EntityType<T>> entityType, int consume, float baseAttackDamage, List<Component> tooltips) {
        super(properties.stacksTo(1));
        this.entityType = entityType;
        this.consume = consume;
        this.baseAttackDamage = baseAttackDamage;
        this.tooltips = tooltips;
        this.sound = TESounds.ROUTINE_SUMMON;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide) {

            player.getCapability(TEAttachments.SUMMONER_STORAGE).resolve().ifPresent(data->data.refresh((ServerPlayer)player));

            EntityHitResult hit = TEUtils.getEyeTraceHitResult(player, player.getAttributeValue(ForgeMod.ENTITY_REACH.get()));
            if (hit != null) {
                if (hit.getEntity() instanceof ISummonMob) {
                    hit.getEntity().discard();
                    return InteractionResultHolder.success(player.getItemInHand(hand));
                }
            }

            player.startUsingItem(hand);

            return InteractionResultHolder.pass(itemstack);
        }
        return InteractionResultHolder.fail(itemstack);
    }

    protected boolean canDiscard(Entity entity, Player player){
        // 这里设计不合理，不过也没有其他需求
        return entity instanceof ISummonMob summonMob && !summonMob.isPet() &&  summonMob.summon_getOwner() == player;
    }


    public void summon(Player player, ItemStack stack) {
        Level level = player.level();
        SummonEvent.Pre<T> event = new SummonEvent.Pre<>(player, stack, entityType.get());
        ModLoader.get().postEvent(event);
        if (event.isCancel()) {
            return;
        }

        T entity = entityType.get().create(level);
        if (entity!=null) {
            BlockPos pos = TEUtils.getEyeBlockHitResult(player);
            entity.setPos(pos.getX(), pos.getY(), pos.getZ());
            entity.summon(player, stack);
            entity.setCost(consume);
            level.addFreshEntity(entity);
            entity.playSound(this.sound.get(), 1.0F, 1.0F);
            player.getCapability(TEAttachments.SUMMONER_STORAGE).resolve().ifPresent(data->{
                data.summon(consume, entity.getId());
                if (player instanceof ServerPlayer serverPlayer)
                    data.sync(serverPlayer);
            });
        }
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        tooltipComponents.add(Component.translatable("tooltic.terra_entity.summon_item.desc"));

        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null) return;
        float additionAttackDamage = (float) localPlayer.getAttributeValue(TEAttributes.MARK_DAMAGE.get());
            tooltipComponents.add(Component.translatable("attribute.name.player.summon_damage").append(": " +
                            (baseAttackDamage + (additionAttackDamage > 0 ? "  +%.1f".formatted(additionAttackDamage): "")))
                    .withStyle(Style.EMPTY.withColor(0x00AB00)));

        tooltipComponents.add(Component.translatable("tooltip.terra_entity.summon_item_cost", consume).withStyle(Style.EMPTY.withColor(0xABAC00)));
        tooltipComponents.add(Component.translatable("tooltip.terra_entity.summon_item_entity", entityType.get().getDescription()).withStyle(Style.EMPTY.withColor(0x1E90FF)));

        AtomicInteger a = new AtomicInteger();
        Minecraft.getInstance().player.getCapability(TEAttachments.SUMMONER_STORAGE).resolve().ifPresent(data->{
            a.set(data.getCurrentCapacity());
        });
        int b = SummonerAttachment.getMaxCapacity(Minecraft.getInstance().player);
        tooltipComponents.add(Component.translatable("tooltip.terra_entity.summon_info", b - a.get(), b).withStyle(Style.EMPTY.withColor(a.get() <= 0 ? 0xAB0000 : 0x00ABAC)));

        tooltipComponents.addAll(this.tooltips);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 1000;
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity livingEntity, int count) {

        // 召唤
        if (count > getUseDuration(stack) - 20) {
            livingEntity.swing(livingEntity.getUsedItemHand());
            if (livingEntity instanceof ServerPlayer player) {
                player.getCapability(TEAttachments.SUMMONER_STORAGE).resolve().ifPresent(data -> {
                    if (!data.canSummon(consume)){
                        // 如果没有足够的召唤栏位，就移除最后一个仆从，再尝试生成。
                        data.removeLast(player, consume);
                    }
                    if (player.isCreative() || data.canSummon(consume)) {
                        summon(player, stack);
                    }
                });
            }
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        // 收回所有召唤物
        if (getUseDuration(stack) - remainingUseDuration == 20) {
            this.onRetrieve(livingEntity, stack);
        }
    }

    protected void onRetrieve(LivingEntity livingEntity, ItemStack stack) {
        if(livingEntity instanceof ServerPlayer player) {
            player.getCapability(TEAttachments.SUMMONER_STORAGE).resolve().ifPresent(data -> {
                data.clear(player);
                data.sync(player);
            });
        }
    }

    public SummonItem<T> setSound(Supplier<SoundEvent> sound) {
        this.sound = sound;
        return this;
    }
}