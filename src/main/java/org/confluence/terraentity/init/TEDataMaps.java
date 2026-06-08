package org.confluence.terraentity.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import org.confluence.terraentity.TerraEntity;
import org.mesdag.portlib.datamap.PortDataMapType;
import org.mesdag.portlib.event.registries.PortRegisterDataMapTypesEvent;

public class TEDataMaps {
//    public static final DataMapType<Item, WhipData> WHIP_DATA_MAP =
//            DataMapType.builder(TerraEntity.space("boomerang_datas"), Registries.ITEM, WhipData.CODEC)
//                    .synced(WhipData.CODEC, false)
//                    .build();

    public static final PortDataMapType<EntityType<?>, Integer> ENTITY_XP_DATA_MAP = PortDataMapType.builder(TerraEntity.space("entity_xp_datas"), Registries.ENTITY_TYPE, Codec.INT)
//                    .synced(Codec.INT, false)
            .build();

    public static void registerDataMapTypes(PortRegisterDataMapTypesEvent event) {
        event.register(ENTITY_XP_DATA_MAP);
    }
}
