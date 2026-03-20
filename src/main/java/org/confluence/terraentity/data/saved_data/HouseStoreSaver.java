package org.confluence.terraentity.data.saved_data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.confluence.terraentity.entity.npc.house.HouseManager;

// 暂时还只存主世界，后面要存储维度信息
public class HouseStoreSaver extends SavedData {

    public static final String NAME = "house_storage";

    public static HouseStoreSaver create() {
        return new HouseStoreSaver();
    }


    @Override
    public CompoundTag save(CompoundTag tag) {
        JsonElement jsonElement = HouseManager.CODEC.encodeStart(JsonOps.INSTANCE, HouseManager.getInstance()).result().get();
        tag.putString(NAME, jsonElement.toString());
        return tag;
    }

    public HouseStoreSaver load(CompoundTag nbt) {
        HouseStoreSaver data = this.create();
        HouseManager.getInstance().load(HouseManager.CODEC.decode(JsonOps.INSTANCE, GsonHelper.parse(nbt.getString(NAME))).result().get().getFirst());
        return data;
    }


    public static HouseStoreSaver decode(CompoundTag tag){
        HouseStoreSaver modLevelSaveData = HouseStoreSaver.create();
        modLevelSaveData.load(tag);
        return modLevelSaveData;
    }


    public static HouseStoreSaver get(Level worldIn) {
        if (!(worldIn instanceof ServerLevel)) {
            return null;
        }
        ServerLevel world = worldIn.getServer().getLevel(ServerLevel.OVERWORLD);
        DimensionDataStorage dataStorage = world.getDataStorage();

        HouseStoreSaver t = dataStorage.computeIfAbsent(HouseStoreSaver::decode, HouseStoreSaver::create , HouseStoreSaver.NAME);
        t.setDirty();
        return t;
    }
}
