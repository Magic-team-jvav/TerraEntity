package org.confluence.terraentity.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;


import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.util.ClientAdapterUtil;
import org.confluence.terraentity.data.util.AmountIngredient;
import org.confluence.terraentity.entity.npc.trade.NPCTradeManager;
import org.confluence.terraentity.api.npc.trade.ITrade;
import org.confluence.terraentity.api.npc.trade.ITradeHealth;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static org.confluence.terraentity.client.gui.container.TETradeScreen.MENU_LOCATION;

@JeiPlugin
public final class TEJeiPlugin implements IModPlugin {
    public static final ResourceLocation UID = TerraEntity.space("jei_plugin");
    public static final ResourceLocation ARROW_RIGHT = TerraEntity.space("textures/gui/sprites/random_gift.png");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        registration.addRecipeCategories(new NPCTradeRecipeCategory(jeiHelpers));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {

        NPCTradeManager.Loader.getInstance().getTradeMap().forEach((npc, manager)->{
            List<ITrade> trades = manager.getRawTrades().getAllSupportedTrades();
//            if(ConfluenceMagicLib.isConfluenceLoaded()){
//                // 汇流加载时，替换掉配方
//                if(npc.getNamespace().equals(TerraEntity.MODID)){
//                    return;
//                }
//            }
            registration.addRecipes(NPCTradeRecipeCategory.TYPE, trades.stream().map(t->{
                NPCRecipe recipe = new NPCRecipe(t, TerraEntity.space(npc.getPath()));
                if(recipe.trade instanceof ITradeHealth re){
                    recipe.setDrawResultCallback((guiGraphics,x,y)->{
                        Font font = Minecraft.getInstance().font;
                        ResourceLocation iconSprite = TerraEntity.space("hud/heart/full");
                        ClientAdapterUtil.blitSprite(guiGraphics, iconSprite, x, y, 16, 16);
                        String s = "↑" + re.getHealth(Minecraft.getInstance().player);
                        guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
                        guiGraphics.drawString(font, s, x + 19 - 2 - font.width(s), y + 6 + 3, 0x12bc63, true);
                    });
                }
                return recipe;
            }).toList());
        });
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
//        registration.addRecipeCatalyst(TCItems.WORKSHOP.get().getDefaultInstance(), WorkshopCategory.TYPE);

    }

    public static void drawArrowRight(GuiGraphics guiGraphics, int x, int y, boolean usable) {
        guiGraphics.blit(MENU_LOCATION,x,y,276,0,35,17,512,256);
    }

//    public static void addInput(IRecipeLayoutBuilder builder, int x, int y, Ingredient ingredient) {
//        if (!ingredient.isEmpty()) {
//            if (ingredient.getCustomIngredient() instanceof AmountIngredient amountIngredient) {
//                builder.addInputSlot(x, y).addIngredients(VanillaTypes.ITEM_STACK, amountIngredient.getItems().toList());
//            } else {
//                builder.addSlot().addInputSlot(x, y).addIngredients(ingredient);
//            }
//        }
//    }

    public static void addInput(IRecipeLayoutBuilder builder, int x, int y, Ingredient ingredient) {
        if (!ingredient.isEmpty()) {
            if (ingredient instanceof AmountIngredient amountIngredient) {
                List<ItemStack> stacks = Arrays.stream(amountIngredient.getItems()).toList();
                for (ItemStack stack : stacks) {
                    stack.setCount(amountIngredient.amount());
                }
                builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(VanillaTypes.ITEM_STACK, stacks);
            } else {
                builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(ingredient);
            }
        }
    }
}
