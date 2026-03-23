package org.confluence.terraentity.integration.sodium_dynamic_light;

import com.google.common.base.Suppliers;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandler;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.confluence.terraentity.init.entity.TEProjectileEntities;
import org.confluence.terraentity.init.entity.TESummonEntities;
import org.confluence.terraentity.init.item.TEBoomerangItems;

import java.util.function.Supplier;

public class SDHelper {

    public static Supplier<Boolean> isLoaded = Suppliers.memoize(()-> ModList.get().isLoaded("sodiumdynamiclights"));



    public static void registerDynamicLight(){
        if(!isLoaded.get()){
            return;
        }

        DynamicLightHandlers.registerDynamicLightHandler(TESummonEntities.TERRAPRISMA.get(), o -> 15);
        DynamicLightHandlers.registerDynamicLightHandler(TEProjectileEntities.BOOMERANG_PROJECTILE.get(), entity -> {
            ItemStack stack = entity.weapon;
            if(stack != null){
                return stack.getItem() == TEBoomerangItems.FLAMARANG.get()? 8 : 0;
            }
            return 0;
        });
        DynamicLightHandlers.registerDynamicLightHandler(TEProjectileEntities.FIRE_IMP_PROJ.get(), o -> 10);
        DynamicLightHandlers.registerDynamicLightHandler(TEProjectileEntities.DARK_CASTER_PROJ.get(), o -> 6);
        DynamicLightHandlers.registerDynamicLightHandler(TEProjectileEntities.VILE_SPIT_PROJ.get(), o -> 6);
        DynamicLightHandlers.registerDynamicLightHandler(TEProjectileEntities.BOOMERANG_PROJECTILE.get(), entity -> entity.getModifier().luminance);

    }
}
