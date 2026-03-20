package org.confluence.terraentity.data.gen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TETags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TEBiomeTagsProvider extends TagsProvider<Biome> {

    public TEBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.BIOME, lookupProvider, TerraEntity.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(TETags.Biomes.IS_EVER_WHERE).addTags(
                // 出现较大群系内容扩展时更改此标签(当对应群系同时具有专属群系地下宝箱，渔获，敌怪时）
                BiomeTags.IS_FOREST,
                Tags.Biomes.IS_PLAINS,
                BiomeTags.IS_TAIGA,
                BiomeTags.IS_SAVANNA,
                Tags.Biomes.IS_SWAMP
        ).add(
                Biomes.DRIPSTONE_CAVES,
                Biomes.DEEP_DARK,
                Biomes.WINDSWEPT_FOREST,
                Biomes.WINDSWEPT_HILLS,
                Biomes.WINDSWEPT_GRAVELLY_HILLS,
                Biomes.WINDSWEPT_SAVANNA,
                Biomes.OLD_GROWTH_BIRCH_FOREST,
                Biomes.OLD_GROWTH_PINE_TAIGA,
                Biomes.OLD_GROWTH_SPRUCE_TAIGA,
                Biomes.STONY_PEAKS,
                Biomes.STONY_SHORE
        );
    }
}
