package org.confluence.terraentity.client.boss.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.confluence.terraentity.TerraEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

import java.util.function.Supplier;

public class GeoBossModel<T extends Entity & GeoAnimatable> extends GeoModel<T> {
    private final ResourceLocation MODEL;
    protected ResourceLocation TEXTURES;
    private final ResourceLocation ANIMATION;

    public GeoBossModel(Supplier<EntityType<T>> entityType) {
        this(ForgeRegistries.ENTITY_TYPES.getKey(entityType.get()));
    }

    public GeoBossModel(ResourceLocation location) {
        this(location.getPath());
    }

    public GeoBossModel(String entityName) {
        MODEL = TerraEntity.space("geo/entity/boss/" + entityName + ".geo.json");
        TEXTURES = TerraEntity.space("textures/entity/boss/" + entityName + ".png");
        ANIMATION = TerraEntity.space("animations/entity/boss/" + entityName + ".animation.json");
    }

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return TEXTURES;
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return ANIMATION;
    }
}
