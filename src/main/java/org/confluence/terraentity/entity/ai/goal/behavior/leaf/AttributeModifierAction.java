package org.confluence.terraentity.entity.ai.goal.behavior.leaf;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.confluence.terraentity.entity.ai.goal.behavior.BTNode;
import org.confluence.terraentity.utils.AdapterUtils;

import java.util.UUID;

public abstract class AttributeModifierAction extends BTNode {

    final LivingEntity living;
    final Attribute attributeHolder;
    final ResourceLocation id;
    UUID uuid;

    private AttributeModifierAction(LivingEntity living, Attribute attributeHolder, ResourceLocation id) {
        this.living = living;
        this.attributeHolder = attributeHolder;
        this.id = id;
        this.uuid = UUID.fromString(id.toString());
    }

    public static class Add extends AttributeModifierAction {
        final double value;
        final AttributeModifier.Operation operation;
        public Add(LivingEntity living, Attribute attributeHolder, ResourceLocation id, double value, AttributeModifier.Operation operation) {
            super(living, attributeHolder, id);
            this.value = value;
            this.operation = operation;
        }
        @Override
        public BTStatus execute() {
            AdapterUtils.addOrUpdateTransientModifier(living, new AttributeModifier(uuid, id.toString(), value, operation), attributeHolder);
            return null;
        }
    }

    public static class Remove extends AttributeModifierAction {
        public Remove(LivingEntity living, Attribute attributeHolder, ResourceLocation id) {
            super(living, attributeHolder, id);
        }
        @Override
        public BTStatus execute() {
            AdapterUtils.removeModifier(living, uuid, attributeHolder);
            return null;
        }
    }
}
