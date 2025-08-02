//package net.ttzplayz.create_wizardry.jei;
//
//import io.redspace.ironsspellbooks.registries.ItemRegistry;
//import mezz.jei.api.IModPlugin;
//import mezz.jei.api.helpers.IGuiHelper;
//import mezz.jei.api.helpers.IJeiHelpers;
//import mezz.jei.api.helpers.IPlatformFluidHelper;
//import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
//import mezz.jei.api.registration.*;
//import mezz.jei.api.runtime.IIngredientManager;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.ItemLike;
//import net.ttzplayz.create_wizardry.CreateWizardry;
//import net.ttzplayz.create_wizardry.fluids.FluidRegistry;
//
//public class CreateWizardryJeiPlugin implements IModPlugin {
//    @Override
//    public ResourceLocation getPluginUid() {
//        return ResourceLocation.fromNamespaceAndPath(CreateWizardry.MOD_ID, "jei_plugin");
//    }
//
//    @Override
//    public void registerCategories(IRecipeCategoryRegistration registration) {
//        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
//        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
//    }
//
//    public void registerRecipes(IRecipeRegistration registration) {
//        IIngredientManager ingredientManager = registration.getIngredientManager();
//        IVanillaRecipeFactory vanillaRecipeFactory = registration.getVanillaRecipeFactory();
//
//        registration.addIngredientInfo(((ItemLike) FluidRegistry.LIGHTNING.get()), Component.translatable("item.create_wizardry.lightning.guide"));
//
//
//    }
//
//
//}
