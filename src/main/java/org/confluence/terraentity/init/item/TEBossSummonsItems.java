package org.confluence.terraentity.init.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.item.BossSummonsItem;

public class TEBossSummonsItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TerraEntity.MODID);

    public static RegistryObject<BossSummonsItem<?>> KING_SLIME_SUMMONS = ITEMS.register("slime_crown",
            () -> new BossSummonsItem<>(new Item.Properties(), TEBossEntities.KING_SLIME));

    public static RegistryObject<BossSummonsItem<?>> EYE_OF_CTHULHU_SUMMONS = ITEMS.register("suspicious_looking_eye",
            () -> new BossSummonsItem<>(new Item.Properties(), TEBossEntities.EYE_OF_CTHULHU));

    public static RegistryObject<BossSummonsItem<?>> EATER_OF_WORLDS_SUMMONS = ITEMS.register("worm_food",
            () -> new BossSummonsItem<>(new Item.Properties(), TEBossEntities.EATER_OF_WORLDS));

    public static RegistryObject<BossSummonsItem<?>> BRAIN_OF_CTHULHU_SUMMONS = ITEMS.register("bloody_spine",
            () -> new BossSummonsItem<>(new Item.Properties(), TEBossEntities.BRAIN_OF_CTHULHU));

    public static RegistryObject<BossSummonsItem<?>> QUEEN_BEE_SUMMONS = ITEMS.register("abeemination",
            () -> new BossSummonsItem<>(new Item.Properties(), TEBossEntities.QUEEN_BEE));

    public static RegistryObject<BossSummonsItem<?>> DEER_THING = ITEMS.register("deer_thing",
            () -> new BossSummonsItem<>(new Item.Properties(), TEBossEntities.DEERCLOPS));

    public static RegistryObject<BossSummonsItem<?>> SKELETRON_SUMMONS = ITEMS.register("clothier_voodoo_doll",
            () -> new BossSummonsItem<>(new Item.Properties(), TEBossEntities.SKELETRON));

    public static RegistryObject<BossSummonsItem<?>> WALL_OF_FLESH_SUMMONS = ITEMS.register("guide_voodoo_doll_wall",
            () -> new BossSummonsItem<>(new Item.Properties(), TEBossEntities.WALL_OF_FLESH).setMaxSummonRange(100, 80));

    public static RegistryObject<BossSummonsItem<?>> HILL_OF_FLESH_SUMMONS = ITEMS.register("guide_voodoo_doll_hill",
            () -> new BossSummonsItem<>(new Item.Properties(), TEBossEntities.HILL_OF_FLESH).setMaxSummonRange(40, 15));



}
