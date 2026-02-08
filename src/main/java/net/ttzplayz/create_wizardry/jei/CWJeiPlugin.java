package net.ttzplayz.create_wizardry.jei;//package net.ttzplayz.create_wizardry.jei;

import mezz.jei.api.IModPlugin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.JeiPlugin;
import net.ttzplayz.create_wizardry.CreateWizardry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.ttzplayz.create_wizardry.item.CWItems.*;

@JeiPlugin
public class CWJeiPlugin implements IModPlugin {
    private static final ResourceLocation ID = CreateWizardry.id("information");


//    look at create jei implementation /TODO

//    private final List<CreateRecipeCategory<?>> categories = new ArrayList<>();
//    private IIngredientManager ingredientManager;
//
//    public static IJeiRuntime runtime;
//
//    private void loadCategories() {
//        categories.clear();
//
//        CreateRecipeCategory<?>
//
//                milling = builder(AbstractCrushingRecipe.class)
//                .addTypedRecipes(AllRecipeTypes.MILLING)
//                .catalyst(AllBlocks.MILLSTONE::get)
//                .doubleItemIcon(AllBlocks.MILLSTONE.get(), AllItems.WHEAT_FLOUR.get())
//                .emptyBackground(177, 53)
//                .build("milling", MillingCategory::new);
//    }

//    @Override
    public ResourceLocation getPluginUid() {
//        return ResourceLocation.tryParse("create_wizardry:information");
        return ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addIngredientInfo(List.of(new ItemStack(LIGHTNING_BUCKET.get())), VanillaTypes.ITEM_STACK, Component.translatable("jei.create_wizardry.liquid_lightning"));
        registration.addIngredientInfo(List.of(new ItemStack(MANA_BUCKET.get())), VanillaTypes.ITEM_STACK, Component.translatable("jei.create_wizardry.mana"));
        registration.addIngredientInfo(List.of(new ItemStack(BLOOD_BUCKET.get())), VanillaTypes.ITEM_STACK, Component.translatable("jei.create_wizardry.blood"));
    }

}