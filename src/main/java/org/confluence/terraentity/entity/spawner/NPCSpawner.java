package org.confluence.terraentity.entity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.HashMap;
import java.util.Map;

public class NPCSpawner {
    public static NPCSpawner instance;
    public static NPCSpawner getInstance() {
        if (instance == null) {
            instance = new NPCSpawner();
        }
        return instance;
    }
    public Map<EntityType<?>,Integer> delayMap = new HashMap<>();

    private int getRange(EntityType<?> entityType){
        return 100;
    }

    private int defaultDelay(){
        return 20;
    }

    private int getDelay(EntityType<?> entityType){
        return this.delayMap.getOrDefault(entityType, defaultDelay());
    }

    public boolean trySpawn(EntityType<?> entityType, ServerLevel level, BlockPos pos, RandomSource random){
        NPCSpawnData data = npcSpawnData.computeIfAbsent(entityType, k -> new NPCSpawnData(getDelay(entityType)));
        boolean isTimeToSpawn = data.trySpawn(level, random);
        if(isTimeToSpawn){
            int range = getRange(entityType);
            if(level.getEntities((Entity) null, new AABB(pos.offset(-range, -range, -range), pos.offset(range, range, range)), e->e.getType() == entityType).isEmpty()){
//                level.players().get(0).sendSystemMessage(Component.literal("spawn a npc"));

                return true;
            }
        }
//        level.players().get(0).sendSystemMessage(Component.literal(String.valueOf(level.getGameTime() - data.lastSpawnTime)));
        return false;
    }

    private final Map<EntityType<?>, NPCSpawnData> npcSpawnData = new HashMap<>();


    private static class NPCSpawnData{
        private long lastSpawnTime = -1;
        private final int _internal;

        private NPCSpawnData(int internal){
            this._internal = internal;

        }

        private boolean trySpawn(Level level, RandomSource random){
            long gameTime = (int)level.getGameTime();

            if(lastSpawnTime == -1){
                lastSpawnTime = gameTime + (long) (_internal * (random.nextFloat() + 1));
                return false;
            }
            if(gameTime >= lastSpawnTime){
                lastSpawnTime = gameTime + (long) (_internal * (random.nextFloat() + 1));
                return true;
            }
            return false;
        }
    }

}
