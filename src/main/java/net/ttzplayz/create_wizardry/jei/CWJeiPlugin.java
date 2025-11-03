package net.ttzplayz.create_wizardry.jei;//package net.ttzplayz.create_wizardry.jei;

import mezz.jei.api.IModPlugin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.JeiPlugin;

import java.util.List;

import static net.ttzplayz.create_wizardry.item.CWItems.*;

@JeiPlugin
public class CWJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.parse("create_wizardry:information");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addIngredientInfo(List.of(new ItemStack(LIGHTNING_BUCKET)), VanillaTypes.ITEM_STACK, Component.translatable("jei.create_wizardry.liquid_lightning"));
        registration.addIngredientInfo(List.of(new ItemStack(MANA_BUCKET)), VanillaTypes.ITEM_STACK, Component.translatable("jei.create_wizardry.mana"));
        registration.addIngredientInfo(List.of(new ItemStack(BLOOD_BUCKET)), VanillaTypes.ITEM_STACK, Component.translatable("jei.create_wizardry.blood"));
    }
}