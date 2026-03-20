package org.confluence.terraentity.data.gen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;

import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.confluence.terraentity.init.TETags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static org.confluence.terraentity.TerraEntity.MODID;


public class TEDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public TEDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, MODID, existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.Provider holderLookup) {
        tag(DamageTypeTags.BYPASSES_COOLDOWN)
                .add(TETags.DamageTypes.SUMMONER)
        ;
        TagKey<DamageType> NO_KNOCKBACK = TagKey.create(Registries.DAMAGE_TYPE,TETags.DamageTypes.NO_KNOCKBACK.location());
        tag(NO_KNOCKBACK).add(
                TETags.DamageTypes.SUMMON,
                TETags.DamageTypes.SUMMONER,
                TETags.DamageTypes.FROST_BURN
        );

    }

}
