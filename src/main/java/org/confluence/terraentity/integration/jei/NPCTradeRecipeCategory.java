package org.confluence.terraentity.integration.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.item.TESpawnEggItems;


import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NPCTradeRecipeCategory implements IRecipeCategory<NPCRecipe> {
    public static final RecipeType<NPCRecipe> TYPE = RecipeType.create(TerraEntity.MODID, "npc_trade", NPCRecipe.class);
    private static final Component TITLE = Component.translatable("title.terra_entity.npc_trade");
    private final IDrawable icon;

    public NPCTradeRecipeCategory(IJeiHelpers jeiHelpers) {
        this.icon = jeiHelpers.getGuiHelper().createDrawableItemStack(new ItemStack(TESpawnEggItems.GUIDE_SPAWN_EGG.get()));
    }

    @Override
    public RecipeType<NPCRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }

    @Override
    public int getWidth() {
        return 128;
    }

    @Override
    public int getHeight() {
        return 54;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, NPCRecipe recipe, IFocusGroup focusGroup) {

//        List<ITrade> trades = recipe.getRawTrades().getAllSupportedTrades();

        List<Ingredient> ingredients = recipe.trade.normalizeCost();
        if(ingredients.isEmpty()){
            return;
        }
        List<ItemStack> outputs = recipe.trade.normalizeResult();
        if(outputs.isEmpty()){
            return;
        }
        // input
        int size = ingredients.size();
        int line = size / 3;
        boolean remain = size % 3 != 0;
        int x = 0;
        int y = 0;
        if (line == 0) {
            y = 24;
        } else if (line == 1) {
            y = remain ? 16 : 24;
        } else if (line == 2) {
            y = remain ? 8 : 16;
        } else if (line == 3) {
            y = remain ? 0 : 8;
        }
        for (Ingredient ingredient : ingredients) {

            TEJeiPlugin.addInput(builder, x, y, ingredient);
            x += 16;
            if (x == 48) {
                x = 0;
                y += 16;
            }
        }
        // output

        builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 24).addItemStacks(outputs);
    }

    @Override
    public void draw(NPCRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        TEJeiPlugin.drawArrowRight(guiGraphics, 50, 22, true);
        recipe.drawResultCallback.draw(guiGraphics, 50, 22);
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(recipe.id);
        var item = ForgeSpawnEggItem.fromEntityType(type);
        if (item != null) {
            guiGraphics.renderItem(item.getDefaultInstance(), 50, 10);
        }
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, type.getDescription(), 58, 0, 0xFFFFFF);
    }
}
