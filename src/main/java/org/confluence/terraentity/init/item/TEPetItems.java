package org.confluence.terraentity.init.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.entity.TESummonEntities;
import org.confluence.terraentity.item.ChesterSummonItem;

public class TEPetItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TerraEntity.MODID);

    public static final RegistryObject<Item> CHESTER_STAFF = ITEMS.register("chester_staff", () -> new ChesterSummonItem<>(new Item.Properties(), TESummonEntities.CHESTER));
    public static final RegistryObject<Item> WALLET = ITEMS.register("wallet", () -> new ChesterSummonItem<>(new Item.Properties(), TESummonEntities.PIGGY_BANK));


    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
