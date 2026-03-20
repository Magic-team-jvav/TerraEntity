package org.confluence.terraentity.level;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.confluence.terraentity.TerraEntity;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

public class LivingSpawnForbidden extends SimpleJsonResourceReloadListener {
    public static final String KEY = "spawn_forbidden";

    private record TuplePattern(Pattern entityTypePattern, Pattern biomePattern){}
    private final List<TuplePattern> forbidden = new ArrayList<>();

    private HashMap<Tuple, Boolean> cache = new HashMap<>();

    private record Tuple(String entityType, String biome){
        private static final Codec<Tuple> CODEC = RecordCodecBuilder.create(instance->instance.group(
                Codec.STRING.fieldOf("entityType").forGetter(Tuple::entityType),
                Codec.STRING.fieldOf("biome").forGetter(Tuple::biome)
        ).apply(instance, Tuple::new));

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Tuple tuple = (Tuple) o;
            return Objects.equals(biome, tuple.biome) && Objects.equals(entityType, tuple.entityType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(entityType, biome);
        }
    }
    private record ModifierList(List<Tuple> list) {
        private static final Codec<ModifierList> CODEC = Tuple.CODEC.listOf().xmap(ModifierList::new, ModifierList::list).fieldOf("values").codec();
    }


    private static LivingSpawnForbidden instance;
    public static LivingSpawnForbidden getInstance() {
        return instance == null ? instance = new LivingSpawnForbidden() : instance;
    }

    public LivingSpawnForbidden() {
        super(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(), KEY);

    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        List<Tuple> forbiddenList = new ArrayList<>();
        cache.clear();
        forbidden.clear();
        resourceLocationJsonElementMap.forEach((location, jsonElement) -> {
            if(location.toString().equals("terra_entity:test")) {
                return;
            }
            DataResult<ModifierList> data = ModifierList.CODEC.parse(JsonOps.INSTANCE, jsonElement);
            if(data.error().isPresent()){
                TerraEntity.LOGGER.error("Error parsing {}: {}", location, data.error().get().message());
            }else{
                forbiddenList.addAll(data.getOrThrow(true, TerraEntity.LOGGER::error).list());
            }

        });

        for (Tuple tuple : forbiddenList) {
            forbidden.add(new TuplePattern(Pattern.compile(tuple.entityType), Pattern.compile(tuple.biome)));
        }

    }

    public boolean checkForbidden(ResourceLocation entityType, ResourceLocation biome) {
        Tuple cacheKey = new Tuple(entityType.toString(), biome.toString());
        if(cache.containsKey(cacheKey)){
            return cache.get(cacheKey);
        }
        for (TuplePattern tuple : forbidden) {
            if(tuple.entityTypePattern.matcher(entityType.toString()).matches() && tuple.biomePattern.matcher(biome.toString()).matches()){
                cache.put(cacheKey, true);
                return true;
            }
        }
        cache.put(cacheKey, false);
        return false;
    }

}
