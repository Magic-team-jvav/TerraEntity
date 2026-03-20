package org.confluence.terraentity.entity.npc;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.serialization.Dynamic;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import org.confluence.lib.util.LibDateUtils;
import org.confluence.terraentity.api.event.NPCEvent;
import org.confluence.terraentity.client.buffer.DebugBlocksHelper;
import org.confluence.terraentity.data.mappeddata.NPCMappedDatas;
import org.confluence.terraentity.entity.ai.goal.NPCTradeGoal;
import org.confluence.terraentity.entity.animation.BoneStateMachine;
import org.confluence.terraentity.entity.animation.BoneStates;
import org.confluence.terraentity.api.entity.animation.IUseItemAnimatable;
import org.confluence.terraentity.entity.config.InitialWeapons;
import org.confluence.terraentity.entity.npc.brain.NPCAi;
import org.confluence.terraentity.entity.npc.chat.ChatArranger;
import org.confluence.terraentity.entity.npc.chat.ChatManager;
import org.confluence.terraentity.entity.npc.chat.NPCChat;
import org.confluence.terraentity.entity.npc.house.House;
import org.confluence.terraentity.entity.npc.house.HouseManager;
import org.confluence.terraentity.entity.npc.misc.NPCNames;
import org.confluence.terraentity.entity.npc.mood.Mood;
import org.confluence.terraentity.entity.npc.mood.NPCMood;
import org.confluence.terraentity.api.npc.trade.ITradeHolder;
import org.confluence.terraentity.entity.npc.trade.NPCTradeManager;
import org.confluence.terraentity.entity.npc.trade.TradeParams;
import org.confluence.terraentity.entity.spawner.NPCSpawner;
import org.confluence.terraentity.init.TEEntityDataSerializers;
import org.confluence.terraentity.init.TEItems;
import org.confluence.terraentity.item.HouseDetectItem;
import org.confluence.terraentity.menu.SimpleTradeMenu;
import org.confluence.terraentity.network.s2c.UpdateNPCTradePacket;
import org.confluence.terraentity.registries.mappeddata.MappedDataTypes;
import org.confluence.terraentity.utils.AdapterUtils;
import org.confluence.terraentity.utils.TEUtils;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * 泰拉风格的npc，集成远程攻击，{@link NPCTradeManager 交易菜单}，{@link HouseManager 房屋系统}，{@link NPCMood 心情系统}，{@link ChatManager 对话系统}
 */
public abstract class AbstractTerraNPC extends PathfinderMob implements GeoEntity, Npc, ITradeHolder, IUseItemAnimatable<BoneStates>, CrossbowAttackMob {


    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<AbstractTerraNPC, Holder<PoiType>>> POI_MEMORIES =
            ImmutableMap.of(
                    MemoryModuleType.HOME, (npc, poi) -> poi.is(PoiTypes.HOME),
                    MemoryModuleType.POTENTIAL_JOB_SITE, (npc, poi) -> VillagerProfession.ALL_ACQUIRABLE_JOBS.test(poi),
                    MemoryModuleType.MEETING_POINT, (npc, poi) -> poi.is(PoiTypes.MEETING)
            );


    private final float moveSpeed = 0.15f;
    private NPCTradeManager trades;
    public Player tradingPlayer;
    private House house = House.EMPTY;
    private NPCMood mood;


    private NPCAi ai;
    private float rangeDistance = 8; // 远程攻击范围
    private Predicate<AbstractTerraNPC> canPerformerAttackTest;

    public int cooldownTick = 0; // 攻击冷却时间
    private int _cooldownTicks;

    public int _chatCount = 100; // 对话显示时间，客户端有效
    public int chatCount = 0;
    public int talkingBrainTick = 0; // 对话行为持续时间
    public AbstractTerraNPC talkingTarget; // 对话目标

    ChatArranger chatArranger;
    ChatManager chatManager;
    public TextureTarget textureTarget;

    public BoneStateMachine<BoneStates> leftArm;
    public BoneStateMachine<BoneStates> rightArm;

    protected @org.jetbrains.annotations.Nullable BlockPos spawnAtPos;


    private static final EntityDataAccessor<Boolean> DATA_RANGE_ATTACK_COOLDOWN = SynchedEntityData.defineId(AbstractTerraNPC.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<NPCTradeManager> DATA_TRADES_DATA = SynchedEntityData.defineId(AbstractTerraNPC.class, TEEntityDataSerializers.NPC_TRADES_SERIALIZER.get());
    private static final EntityDataAccessor<House> DATA_HOUSE_DATA = SynchedEntityData.defineId(AbstractTerraNPC.class, TEEntityDataSerializers.NPC_HOUSE_SERIALIZER.get());
    private static final EntityDataAccessor<NPCMood> DATA_MOOD = SynchedEntityData.defineId(AbstractTerraNPC.class, TEEntityDataSerializers.NPC_MOOD_SERIALIZER.get());
    private static final EntityDataAccessor<TradeParams> DATA_TRADE_PARAMS = SynchedEntityData.defineId(AbstractTerraNPC.class, TEEntityDataSerializers.NPC_TRADE_PARAMS_SERIALIZER.get());
    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(AbstractTerraNPC.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<NPCChat> DATA_CHAT = SynchedEntityData.defineId(AbstractTerraNPC.class, TEEntityDataSerializers.NPC_CHAT_SERIALIZER.get());


    public AbstractTerraNPC(EntityType<? extends AbstractTerraNPC> entityType, Level level) {
        super(entityType, level);

        if (level.isClientSide()) {
            leftArm = new BoneStateMachine<>(BoneStates.IDLE);
            rightArm = new BoneStateMachine<>(BoneStates.IDLE);
        }

        Optional.ofNullable(this.getAttribute(Attributes.MOVEMENT_SPEED)).ifPresent(att -> att.setBaseValue(moveSpeed));

        this.getNavigation().setCanFloat(true);


        ((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
        ((GroundPathNavigation) this.getNavigation()).setCanPassDoors(true);
        if (canPerformerAttackTest == null) {
            canPerformerAttackTest = npc -> npc.getMainHandItem().getItem() instanceof BowItem;
        }
        // confluence mixin here
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (target instanceof Player || target instanceof AbstractTerraNPC) {
            return false;
        }
        return target.canBeSeenAsEnemy();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // confluence mixin here
        return !this.hasCustomName(); // 交互以后不会被刷走
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new GroundPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(BlockPos pos) {
                BlockPos blockpos = pos.below();
                BlockState state = this.level.getBlockState(blockpos);
                return state.isSolidRender(this.level, blockpos) || state.is(Tags.Blocks.GLASS);
            }
        };
    }

    public void initName() {
        if (!level().isClientSide && !this.hasCustomName()) {
            String name = NPCNames.Loader.getInstance().getRandomName(getType());
            if (name != null) {
                this.setCustomName(Component.literal(name));
            }
        }
    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
    }

    /**
     * <p>设置npc的房屋
     * <p>使用前需要使用HouseManager.getInstance().tryAddHouse检查房屋是否可以添加</p>
     */
    public void setHouse(House house) {
        // confluence mixin here
        setHouseNoUpdate(house);
    }

    public House getHouse() {
        return this.house;
    }

    public @Nullable BlockPos getSpawnAtPos() {
        return spawnAtPos;
    }

    public void setHouseNoUpdate(House house) {
        this.house = house;
        this.entityData.set(DATA_HOUSE_DATA, house);
        if (!house.isEmpty()) {
            this.spawnAtPos = house.center();
        }
    }

    public void setSpawnAtPos(@Nullable BlockPos spawnAtPos) {
        this.spawnAtPos = spawnAtPos;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new NPCTradeGoal(this));
    }

    @Override
    protected @NotNull Brain<AbstractTerraNPC> makeBrain(@NotNull Dynamic<?> dynamic) {
        return initAI().makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @Override
    protected Brain.@NotNull Provider<AbstractTerraNPC> brainProvider() {
        return ai.brainProvider();
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Brain<AbstractTerraNPC> getBrain() {
        return (Brain<AbstractTerraNPC>) super.getBrain();
    }

    public void refreshBrain(ServerLevel serverLevel) {
        Brain<AbstractTerraNPC> brain = this.getBrain();
        brain.stopAll(serverLevel, this);
        this.brain = brain.copyWithoutBehaviors();
        initAI().makeBrain(this.getBrain());
    }

    protected NPCAi initAI() {
        NPCEvent.NPCBrainCollector event = new NPCEvent.NPCBrainCollector(this);
        setAttackRange(8); // 初始化晚于父类，手动提前初始化
        setCooldownTicks(20);

        // 初始化心情系统
        this.mood = new NPCMood();
        NPCMood.EntityMood info = NPCMood.Loader.getInstance().getMood(getType());
        if (info != null) {
            EnumMap<Mood, Integer> map = info.getSetting().createEnumMap();
            this.mood.setMoodValueTable(map);
            for (var info1 : info.moodInfos()) {
                this.mood.addMoodInfo(info1.moodInfo());
            }
        }

        // 初始化对话系统
        this.chatManager = ChatManager.get(this.getType());
        if (this.chatManager != null) {
            this.chatManager.setOwner(this);
        }


        // 使用预先注册的事件处理器
        var consumer = NPCEvent.NPCBrainCollectionEvent.getConsumer(getType());
        if (consumer != null) {
            consumer.accept(event);
        }

        if (event.getReplace() != null) {
            ai = event.getReplace();
        } else {
            ai = new NPCAi(this);
        }
        return ai;
    }

    /**
     * 能否攻击敌怪，如果否，则会经常远离敌怪
     */
    public boolean canPerformerAttack() {
        return canPerformerAttackTest != null && canPerformerAttackTest.test(this);
    }

    /**
     * 设置能触发攻击状态的条件
     */
    public void setCanPerformerAttackTest(Predicate<AbstractTerraNPC> canPerformerAttackTest) {
        this.canPerformerAttackTest = canPerformerAttackTest;
    }

    /**
     * 远程攻击的npc ai的走位距离
     */
    public float getAttackRange() {
        return rangeDistance;
    }

    public void setAttackRange(float rangeDistance) {
        this.rangeDistance = rangeDistance;
    }

    public int getCooldownTicks() {
        return _cooldownTicks;
    }

    public void setCooldownTicks(int cooldownTicks) {
        this._cooldownTicks = cooldownTicks;
    }

    public boolean isCooledDown() {
        return this.entityData.get(DATA_RANGE_ATTACK_COOLDOWN);
    }

    public void setCooledDown(boolean cooldown) {
        this.entityData.set(DATA_RANGE_ATTACK_COOLDOWN, cooldown);
    }

    public @NotNull TradeParams getTradeParams() {
        return this.entityData.get(DATA_TRADE_PARAMS);
    }


    public NPCMood getMood() {
        return mood;
    }

    public NPCTradeManager getTradeManager() {
        return trades;
    }


    /**
     * <P>强行同步所有的交易表，当使用初始化的时候需要调用，保证服务器和客户端的交易表一致。
     * <p>当数据量过大时应该采用局部更新</p>
     */
    public void syncTrades() {
        this.entityData.set(DATA_TRADES_DATA, this.trades, true);
    }

    public void syncNpcTrade(int index) {
        UpdateNPCTradePacket.syncNpcTrade(index, this);

    }


    /**
     * 同步心情系统
     */
    public void syncMood() {
        NPCMood mood = new NPCMood();
        mood.copyFrom(this.mood);
        this.entityData.set(DATA_MOOD, mood);
    }

    /**
     * 同步交易表参数，当某些使用参数的交易表任务交易成功后需要调用
     */
    public void syncTradeTasksParams() {
        this.entityData.set(DATA_TRADE_PARAMS, this.getTradeParams(), true);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (level().isClientSide()) {
            if (DATA_TRADES_DATA.equals(key)) {
                this.trades = this.entityData.get(DATA_TRADES_DATA);
                this.trades.initTrades(this, null);
            } else if (DATA_HOUSE_DATA.equals(key)) {
                this.house = this.entityData.get(DATA_HOUSE_DATA);
                if (!house.isEmpty()) {
                    this.spawnAtPos = house.center();
                }
            } else if (DATA_TRADE_PARAMS.equals(key)) {
                this.getTradeManager().refreshAvailableTrades();

            }else if(DATA_CHAT.equals(key) && level().isClientSide()){
                this.chatCount = _chatCount;
                this.chatArranger = new ChatArranger(this.entityData.get(DATA_CHAT).chatElement, Minecraft.getInstance().font);
                this.chatArranger.startTick = this.tickCount;
            }
        }
        if (DATA_MOOD.equals(key)) {
            if (level().isClientSide())
                this.mood.copyFrom(this.entityData.get(DATA_MOOD));
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TRADES_DATA, new NPCTradeManager(List.of()));
        this.entityData.define(DATA_HOUSE_DATA, House.EMPTY);
        this.entityData.define(DATA_RANGE_ATTACK_COOLDOWN, false);
        this.entityData.define(DATA_MOOD, new NPCMood());
        this.entityData.define(DATA_TRADE_PARAMS, TradeParams.create());
        this.entityData.define(DATA_IS_CHARGING_CROSSBOW, false);
        this.entityData.define(DATA_CHAT, new NPCChat(List.of()));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("te_npc_data", 10)) {
            NPCTradeManager.CODEC.parse(NbtOps.INSTANCE, tag.get("te_npc_data")).result().ifPresent(npcTrades -> {
                this.trades = npcTrades;
                this.trades.initTrades(this, null);
                syncTrades();

                TradeParams.CODEC.parse(NbtOps.INSTANCE, tag.get("te_npc_trade_params")).result().ifPresent(params -> {
                    this.entityData.set(DATA_TRADE_PARAMS, params);
                });
            });
        }
        if (tag.contains("House", Tag.TAG_COMPOUND)) {
            setHouseNoUpdate(House.CODEC.parse(NbtOps.INSTANCE, tag.get("House")).result().orElse(House.EMPTY));
        }
        if (tag.contains("SpawnAtPos", Tag.TAG_COMPOUND)) {
            this.spawnAtPos = readBlockPos(tag, "SpawnAtPos").orElse(null);
        }
        // confluence mixin here
    }
    public static Optional<BlockPos> readBlockPos(CompoundTag tag, String key) {
        int[] aint = tag.getIntArray(key);
        return aint.length == 3 ? Optional.of(new BlockPos(aint[0], aint[1], aint[2])) : Optional.empty();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (trades != null) {
            NPCTradeManager.CODEC.encodeStart(NbtOps.INSTANCE, trades).result().ifPresent(trade -> {
                tag.put("te_npc_data", trade);
            });
            if (!this.trades.trades().isEmpty() && !this.getTradeParams().isEmpty()) {
                TradeParams.CODEC.encodeStart(NbtOps.INSTANCE, this.getTradeParams()).result().ifPresent(params -> {
                    tag.put("te_npc_trade_params", params);
                });
            }
        }
        if (house != null) {
            House.CODEC.encodeStart(NbtOps.INSTANCE, house).result().ifPresent(tag1 -> tag.put("House", tag1));
        }
        if (spawnAtPos != null) {
            tag.put("SpawnAtPos", NbtUtils.writeBlockPos(spawnAtPos));
        }
        // confluence mixin here
    }


    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();

        // 如果是第一次生成
        if (trades == null && !level().isClientSide) {
            NPCEvent.InitNPCTradeEvent event = AdapterUtils.postEvent(new NPCEvent.InitNPCTradeEvent(this, BuiltInRegistries.ENTITY_TYPE.getKey(this.getType())));
            trades = NPCTradeManager.getCopy(event.getOrigin(), NbtOps.INSTANCE);
            if (trades != null) {
                trades.initTrades(this, event.getOrigin());
                onInitTrades();
                syncTrades();
            }
            // forge 奇妙的bug，也是奇妙的修复方式
            this.entityData.set(DATA_TRADE_PARAMS, TradeParams.create());
        }
    }

    protected void onInitTrades() {
    }


    @Override
    public void tick() {

        super.tick();
        this.updateSwingTime();


//        if(level().isClientSide){
//            if(mood.getValue() != 100){
//                System.out.println(mood..getValue()); // debug
//            }
//        }

        if (isCooledDown()) {
            this.cooldownTick++;
        } else {
            this.cooldownTick = 0;
        }
        --this.chatCount;
        if (!level().isClientSide) {
            if (this.chatManager != null) {
                this.chatManager.update(1);
//                if(this.tickCount % 150 == 0){ // todo debug
//                    this.setChat(new NPCChat(List.of(new SpriteChatElement(List.of(TerraEntity.space("textures/gui/sprites/random_gift.png")), 2f))));
//                }
            }

            // 由于NPCHouseBehaviors#walkToHouse疑似不能触发，于是在tick里判断
            // 过远时传送回自己的出生点
            if (spawnAtPos != null &&
                    (house == null || !house.contains(blockPosition())) &&
                    level().getGameTime() % 100 == 2 && level().players().stream().noneMatch(player -> player.distanceToSqr(this) < 32 * 32)
            ) {
                double sqr = blockPosition().distSqr(spawnAtPos);
                if (sqr > 64 * 64 || (sqr > 500 && LibDateUtils.isNight(level()))) {
                    teleportTo(spawnAtPos.getX(), spawnAtPos.getY(), spawnAtPos.getZ());
                }
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (isEffectiveAi()) {
            this.getBrain().tick((ServerLevel) this.level(), this);
        }

        // 用于显示房间
        if (level().isClientSide && (tickCount & 127) == 0 && !house.isEmpty()) {
            if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getMainHandItem().getItem() instanceof HouseDetectItem) {
                DebugBlocksHelper.Singleton().addDebugBlock(List.of(house.min(), house.max()));
            }
        }
    }

    @SuppressWarnings("all")
    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!(player instanceof ServerPlayer serverPlayer)) {
            if (stack.is(TEItems.HOUSE_DETECTOR.get())) {
                return InteractionResult.PASS;
            }

            return InteractionResult.SUCCESS;
        }

        initName();

        if (hand == InteractionHand.OFF_HAND) {
            return InteractionResult.SUCCESS;
        }

        this.setCustomNameVisible(true);

        if (stack.is(TEItems.HOUSE_DETECTOR.get())) {
            return InteractionResult.PASS;
        }

        if (stack.getItem() instanceof ArmorItem armorItem) {
            // 如果是装备，则穿上
            swapItem(player, armorItem.getEquipmentSlot(), hand, stack);
            return InteractionResult.SUCCESS;
        } else if (player.isShiftKeyDown()) {
            // 如果按下shift
            if (!stack.isEmpty()) {
                // 如果玩家手中有物品，则交换物品
                swapItem(player, EquipmentSlot.MAINHAND, hand, stack);
                return InteractionResult.SUCCESS;
            }
            // 如果是空手，则取下装备
            Vec3 hit = TEUtils.calRayToAABB(player.getEyePosition(), player.getViewVector(0.5f), this.getBoundingBox());
            if (hit != null) {
                double dx = hit.y - position().y;
                if (dx > 1.2) {
                    dropEquipmentToHand(EquipmentSlot.HEAD, player, hand);
                } else if (dx > 0.7f) {
                    double dminx = Math.max(hit.x - getBoundingBox().minX, getBoundingBox().maxX - hit.x);
                    double dminz = Math.max(hit.z - getBoundingBox().minZ, getBoundingBox().maxZ - hit.z);
                    double dmin = Math.min(dminx, dminz);
                    if (dmin > 0.5f) {
                        // 命中包围盒侧边，去下手中物品
                        dropEquipmentToHand(EquipmentSlot.MAINHAND, player, hand);
                    } else {
                        // 命中包围盒正面，去下胸甲
                        dropEquipmentToHand(EquipmentSlot.CHEST, player, hand);
                    }
                } else if (dx > 0.3f) {
                    dropEquipmentToHand(EquipmentSlot.LEGS, player, hand);
                } else {
                    dropEquipmentToHand(EquipmentSlot.FEET, player, hand);
                }
            }
            return InteractionResult.PASS;
        }

        NPCEvent.InteractNPCEvent event = AdapterUtils.postEvent(new NPCEvent.InteractNPCEvent(this, serverPlayer));
        event.execute((npc, player1) -> {
            if (getTradeManager() != null) {
                this.getTradeManager().reCheckAvailableTrades(player1);
            }
            player.openMenu(new SimpleMenuProvider((id, playerInventory, player2) ->
                    new SimpleTradeMenu(id, playerInventory, this), Component.translatable("title.terra_entity.npc_trade")));
        });
        tradingPlayer = player;
        return event.getInteractionResult();
    }

    private void swapItem(Player player, EquipmentSlot slot, InteractionHand hand, ItemStack stack) {
        ItemStack npcItem = getItemBySlot(slot);
        this.setItemSlot(slot, stack);
        player.setItemInHand(hand, npcItem);
    }

    private void dropEquipmentToHand(EquipmentSlot slot, Player player, InteractionHand hand) {
        ItemStack drop = this.getItemBySlot(slot);
        if (!drop.isEmpty()) {
            player.setItemInHand(hand, drop.copy());
            this.setItemSlot(slot, ItemStack.EMPTY);
        }
    }

    public boolean isAllianceTo(LivingEntity entity) {
        return entity instanceof AbstractTerraNPC || entity instanceof Player;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Walk/Idle", 5, state ->
                state.setAndContinue(state.isMoving() ? DefaultAnimations.WALK : DefaultAnimations.IDLE)
        ));
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE)
                .add(Attributes.MAX_HEALTH)
                .add(Attributes.ARMOR)
                .add(Attributes.MOVEMENT_SPEED)
                .add(Attributes.FOLLOW_RANGE)
                .add(Attributes.KNOCKBACK_RESISTANCE)
                ;
    }


    @Override
    public void startSleeping(@NotNull BlockPos pos) {
        super.startSleeping(pos);
        this.brain.setMemory(MemoryModuleType.LAST_SLEPT, this.level().getGameTime());
        this.brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        this.brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }

    @Override
    public void stopSleeping() {
        super.stopSleeping();
        this.brain.setMemory(MemoryModuleType.LAST_WOKEN, this.level().getGameTime());
    }

    @Override
    public void die(@NotNull DamageSource cause) {

        this.releaseAllPois();
        super.die(cause);
        HouseManager.getInstance().removeHouse(uuid);

    }

    @Override
    protected void dropEquipment() {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = this.getItemBySlot(slot);
            if (!stack.isEmpty()) {
                this.spawnAtLocation(stack);
                this.setItemSlot(slot, ItemStack.EMPTY);
            }
        }
    }

    private void releaseAllPois() {
        this.releasePoi(MemoryModuleType.HOME);
//        this.releasePoi(MemoryModuleType.JOB_SITE);
//        this.releasePoi(MemoryModuleType.POTENTIAL_JOB_SITE);
//        this.releasePoi(MemoryModuleType.MEETING_POINT);
    }

    @Override
    protected void hurtArmor(@NotNull DamageSource damageSource, float damage) {
        if (!(damage <= 0.0F)) {
            damage /= 4.0F;
            if (damage < 1.0F) {
                damage = 1.0F;
            }
            for (var stack : this.getArmorSlots()) {
                boolean fireResistant = damageSource.is(DamageTypeTags.IS_FIRE) && stack.getItem().isFireResistant();
                if (!fireResistant && stack.getItem() instanceof ArmorItem) {
                    stack.hurtAndBreak((int) damage, this, (maid) -> {

                    });
                }
            }

        }
    }

    // poi暂时没用上
    public void releasePoi(MemoryModuleType<GlobalPos> moduleType) {
        if (this.level() instanceof ServerLevel) {
            MinecraftServer minecraftserver = ((ServerLevel) this.level()).getServer();
            this.brain.getMemory(moduleType).ifPresent(pos -> {
                ServerLevel serverlevel = minecraftserver.getLevel(pos.dimension());
                if (serverlevel != null) {
                    PoiManager poimanager = serverlevel.getPoiManager();
                    Optional<Holder<PoiType>> optional = poimanager.getType(pos.pos());
                    BiPredicate<AbstractTerraNPC, Holder<PoiType>> bipredicate = POI_MEMORIES.get(moduleType);
                    if (optional.isPresent() && bipredicate.test(this, optional.get())) {
                        poimanager.release(pos.pos());
                        DebugPackets.sendPoiTicketCountPacket(serverlevel, pos.pos());
                    }
                }
            });
        }
    }

    public static boolean checkRoutineNPCSpawn(EntityType<? extends Mob> type, ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {

        if (pLevel instanceof ServerLevel serverLevel) {
            if (!Animal.checkAnimalSpawnRules(null, pLevel, pSpawnType, pPos, pRandom)) {
                return false;
            }

            int y = pPos.getY();
            if (y >= 260) {
                return false; // 不能生成在 y = 260 或更高的位置
            }

            return NPCSpawner.getInstance().trySpawn(type, serverLevel, pPos, pRandom);
        }
        return false;
    }

    @Override
    protected @NotNull Vec3 getLeashOffset() {
        return new Vec3(-0.3, this.getEyeHeight() * 0.5f, this.getBbWidth() * 0.1F);
    }

    /**
     * 用于渲染躺下的姿势，如渔夫
     */
    public boolean isLieDown() {
        return false;
    }

    public boolean isChargingCrossbow() {
        return this.entityData.get(DATA_IS_CHARGING_CROSSBOW);
    }

    public void setChargingCrossbow(boolean isCharging) {
        this.entityData.set(DATA_IS_CHARGING_CROSSBOW, isCharging);
    }

    @Override
    public void shootCrossbowProjectile(@NotNull LivingEntity target, @NotNull ItemStack itemStack, @NotNull Projectile projectile, float angle) {

        if (projectile instanceof AbstractArrow arrow) {
            AttributeInstance attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE);
            double attackValue = 2.0;
            if (attackDamage != null) {
                attackValue = attackDamage.getBaseValue();
            }

            float multiplier = (float) (attackValue / 2.0);
            arrow.setBaseDamage(arrow.getBaseDamage() * (double) multiplier);
        }

        this.shootCrossbowProjectile(this, target, projectile, angle, 1.6F);
    }

    public void shootCrossbowProjectile(@NotNull LivingEntity shooter, @NotNull LivingEntity target, @NotNull Projectile projectile, float angle, float velocityIn) {
        CrossbowAttackMob.super.shootCrossbowProjectile(shooter, target, projectile, angle, velocityIn);
    }

    @Override
    public void onCrossbowAttackPerformed() {

    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity livingEntity, float v) {

    }

    public int getChargingTicks() {
        return this.cooldownTick;
    }

    public BoneStateMachine<BoneStates> getLeftArmBoneStateMachine() {
        return leftArm;
    }

    public BoneStateMachine<BoneStates> getRightArmBoneStateMachine() {
        return rightArm;

    }

//    @Override
//    public Vec3 getVehicleAttachmentPoint(Entity entity) {
//        return super.getVehicleAttachmentPoint(entity).add(0,0.65,0);
//    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag tag) {
        spawnGroupData = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData, tag);
        this.setLeftHanded(false);
        InitialWeapons data = MappedDataTypes.getData(MappedDataTypes.NPC_MAP_DATAS, NPCMappedDatas.NPC_WEAPON);
        ItemStack stack = data.getRandom(this.getType());
        this.setItemSlot(EquipmentSlot.MAINHAND, stack);
        return spawnGroupData;
    }

    public void setChat(NPCChat chat) {
        if (chat.getChatElement() != null && !chat.getChatElement().isEmpty()) {
            this.entityData.set(DATA_CHAT, chat.generateChat(this.random), true);
        }
    }

    public ChatArranger getChat() {
        if (this.chatCount > 0) {
            return this.chatArranger;
        }
        return null;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }


}
