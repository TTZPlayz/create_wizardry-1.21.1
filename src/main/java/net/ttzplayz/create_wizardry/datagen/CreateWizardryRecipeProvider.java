/*
 * Copyright (C) 2025  DragonsPlus
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.ttzplayz.create_wizardry.datagen;

import com.simibubi.create.compat.jei.ConversionRecipe;
import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import com.simibubi.create.content.fluids.transfer.EmptyingRecipe;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.crusher.CrushingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.ManualApplicationRecipe;
import com.simibubi.create.content.kinetics.fan.processing.HauntingRecipe;
import com.simibubi.create.content.kinetics.fan.processing.SplashingRecipe;
import com.simibubi.create.content.kinetics.millstone.MillingRecipe;
import com.simibubi.create.content.kinetics.mixer.CompactingRecipe;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.MechanicalCraftingRecipeBuilder;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

import static com.simibubi.create.AllItems.EXP_NUGGET;
import static com.simibubi.create.content.processing.recipe.HeatCondition.HEATED;
import static com.simibubi.create.content.processing.recipe.HeatCondition.SUPERHEATED;
import static io.redspace.ironsspellbooks.registries.FluidRegistry.*;
import static io.redspace.ironsspellbooks.registries.ItemRegistry.*;
import static net.minecraft.tags.ItemTags.WOOL;
import static net.minecraft.world.item.Items.*;
import static net.neoforged.neoforge.common.Tags.Items.INGOTS;
import static net.ttzplayz.create_wizardry.fluids.FluidRegistry.*;

public class CreateWizardryRecipeProvider extends RecipeProvider {

    public CreateWizardryRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        buildFillingRecipes(output);
        buildEmptyingRecipes(output);
        buildMixingRecipes(output);
        buildSequencedRecipes(output);
        buildCompactingRecipes(output);
    }
    private void buildMixingRecipes(RecipeOutput output) {

        // INKS
        mixing(COMMON_INK.getId())
                .require(INK_SAC)
                .require(INK_SAC)
                .require(INK_SAC)
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .output(COMMON_INK.get(), 750)
                .build(output);

        mixing(UNCOMMON_INK.getId())
                .require(GLOW_INK_SAC)
                .require(GLOW_INK_SAC)
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(COMMON_INK.get(), 1000)
                .output(UNCOMMON_INK.get(), 750)
                .build(output);

        mixing(RARE_INK.getId())
                .require(LAPIS_LAZULI)
                .require(LAPIS_LAZULI)
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(UNCOMMON_INK.get(), 1000)
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .output(RARE_INK.get(), 750)
                .build(output);

        mixing(EPIC_INK.getId())
                .require(BLOOD.get(), 500)
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(RARE_INK.get(), 1000)
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .output(EPIC_INK.get(), 750)
                .requiresHeat(HEATED)
                .build(output);

        mixing(LEGENDARY_INK.getId())
                .require(BLOOD.get(), 500)
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(EPIC_INK.get(), 1000)
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .output(LEGENDARY_INK.get(), 750)
                .requiresHeat(SUPERHEATED)
                .build(output);

        // MATERIALS
        mixing(ARCANE_ESSENCE.getId())
                .require(LAPIS_LAZULI)
                .require(LAPIS_LAZULI)
                .require(AMETHYST_SHARD)
                .require(AMETHYST_SHARD)
                .require(EXP_NUGGET)
                .output(ARCANE_ESSENCE.get(), 4)
                .requiresHeat(SUPERHEATED)
                .build(output);

        mixing(CINDER_ESSENCE.getId())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(BLAZE_POWDER)
                .require(BLAZE_POWDER)
                .require(NETHERITE_SCRAP)
                .output(CINDER_ESSENCE.get(), 8)
                .requiresHeat(SUPERHEATED)
                .build(output);
        // FLUIDS
        mixing(MANA.getId())
                .require(FluidTags.WATER, 1000)
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .output(MANA.get(), 1000)
                .build(output);
            // add timeless slurry

    }

    private void buildFillingRecipes(RecipeOutput output) {
        // INK
        filling(ItemRegistry.INK_COMMON.getId())
                .require(Items.GLASS_BOTTLE)
                .require(COMMON_INK.get(), 250)
                .output(ItemRegistry.INK_COMMON.get())
                .build(output);
        filling(ItemRegistry.INK_UNCOMMON.getId())
                .require(Items.GLASS_BOTTLE)
                .require(UNCOMMON_INK.get(), 250)
                .output(ItemRegistry.INK_UNCOMMON.get())
                .build(output);
        filling(ItemRegistry.INK_RARE.getId())
                .require(Items.GLASS_BOTTLE)
                .require(RARE_INK.get(), 250)
                .output(ItemRegistry.INK_RARE.get())
                .build(output);
        filling(ItemRegistry.INK_EPIC.getId())
                .require(Items.GLASS_BOTTLE)
                .require(EPIC_INK.get(), 250)
                .output(ItemRegistry.INK_EPIC.get())
                .build(output);
        filling(ItemRegistry.INK_LEGENDARY.getId())
                .require(Items.GLASS_BOTTLE)
                .require(LEGENDARY_INK.get(), 250)
                .output(ItemRegistry.INK_LEGENDARY.get())
                .build(output);
        // POTIONS
        filling(ItemRegistry.OAKSKIN_ELIXIR.getId())
                .require(Items.GLASS_BOTTLE)
                .require(OAKSKIN_ELIXIR_FLUID.get(), 250)
                .output(ItemRegistry.OAKSKIN_ELIXIR.get())
                .build(output);
        filling(ItemRegistry.GREATER_OAKSKIN_ELIXIR.getId())
                .require(Items.GLASS_BOTTLE)
                .require(GREATER_OAKSKIN_ELIXIR_FLUID.get(), 250)
                .output(ItemRegistry.GREATER_OAKSKIN_ELIXIR.get())
                .build(output);
        filling(ItemRegistry.INVISIBILITY_ELIXIR.getId())
                .require(Items.GLASS_BOTTLE)
                .require(INVISIBILITY_ELIXIR_FLUID.get(), 250)
                .output(ItemRegistry.INVISIBILITY_ELIXIR.get())
                .build(output);
        filling(ItemRegistry.GREATER_INVISIBILITY_ELIXIR.getId())
                .require(Items.GLASS_BOTTLE)
                .require(GREATER_INVISIBILITY_ELIXIR_FLUID.get(), 250)
                .output(ItemRegistry.GREATER_INVISIBILITY_ELIXIR.get())
                .build(output);
        filling(ItemRegistry.EVASION_ELIXIR.getId())
                .require(Items.GLASS_BOTTLE)
                .require(EVASION_ELIXIR_FLUID.get(), 250)
                .output(ItemRegistry.EVASION_ELIXIR.get())
                .build(output);
        filling(ItemRegistry.GREATER_EVASION_ELIXIR.getId())
                .require(Items.GLASS_BOTTLE)
                .require(GREATER_EVASION_ELIXIR_FLUID.get(), 250)
                .output(ItemRegistry.GREATER_EVASION_ELIXIR.get())
                .build(output);
        filling(ItemRegistry.GREATER_HEALING_POTION.getId())
                .require(Items.GLASS_BOTTLE)
                .require(GREATER_HEALING_ELIXIR_FLUID.get(), 250)
                .output(ItemRegistry.GREATER_HEALING_POTION.get())
                .build(output);
        // OTHER FLUIDS
        filling(ItemRegistry.BLOOD_VIAL.getId())
                .require(Items.GLASS_BOTTLE)
                .require(BLOOD.get(), 250)
                .output(ItemRegistry.BLOOD_VIAL.get())
                .build(output);
        filling(LIGHTNING_BOTTLE.getId())
                .require(Items.GLASS_BOTTLE)
                .require(LIGHTNING.get(), 250)
                .output(LIGHTNING_BOTTLE.get())
                .build(output);
        filling(ItemRegistry.TIMELESS_SLURRY.getId())
                .require(Items.GLASS_BOTTLE)
                .require(TIMELESS_SLURRY_FLUID.get(), 250)
                .output(ItemRegistry.TIMELESS_SLURRY.get())
                .build(output);

        // MISC
        filling(ARCANE_ESSENCE.getId())
                .require(Tags.Items.DUSTS)
                .require(MANA.get(), 250)
                .output(ARCANE_ESSENCE.get())
                .build(output);
    }
    private void buildEmptyingRecipes(RecipeOutput output) {
        // INK
        emptying(ItemRegistry.INK_COMMON.getId())
                .require(ItemRegistry.INK_COMMON.get())
                .output(COMMON_INK.get(), 250)
                .output(Items.GLASS_BOTTLE)
                .build(output);
        emptying(ItemRegistry.INK_UNCOMMON.getId())
                .require(ItemRegistry.INK_UNCOMMON.get())
                .output(UNCOMMON_INK.get(), 250)
                .output(Items.GLASS_BOTTLE)
                .build(output);
        emptying(ItemRegistry.INK_RARE.getId())
                .require(ItemRegistry.INK_RARE.get())
                .output(RARE_INK.get(), 250)
                .output(Items.GLASS_BOTTLE)
                .build(output);
        emptying(ItemRegistry.INK_EPIC.getId())
                .require(ItemRegistry.INK_EPIC.get())
                .output(EPIC_INK.get(), 250)
                .output(Items.GLASS_BOTTLE)
                .build(output);
        emptying(ItemRegistry.INK_LEGENDARY.getId())
                .require(ItemRegistry.INK_LEGENDARY.get())
                .output(LEGENDARY_INK.get(), 250)
                .output(Items.GLASS_BOTTLE)
                .build(output);
        // POTIONS
        emptying(ItemRegistry.OAKSKIN_ELIXIR.getId())
                .require(ItemRegistry.OAKSKIN_ELIXIR.get())
                .output(OAKSKIN_ELIXIR_FLUID.get(), 250)
                .output(Items.GLASS_BOTTLE)
                .build(output);
        emptying(ItemRegistry.GREATER_OAKSKIN_ELIXIR.getId())
                .require(ItemRegistry.GREATER_OAKSKIN_ELIXIR.get())
                .output(GREATER_OAKSKIN_ELIXIR_FLUID.get(), 250)
                .output(Items.GLASS_BOTTLE)
                .build(output);
        emptying(ItemRegistry.INVISIBILITY_ELIXIR.getId())
                .require(ItemRegistry.INVISIBILITY_ELIXIR.get())
                .output(INVISIBILITY_ELIXIR_FLUID.get(), 250)
                .output(Items.GLASS_BOTTLE)
                .build(output);
        emptying(ItemRegistry.GREATER_INVISIBILITY_ELIXIR.getId())
                .require(ItemRegistry.GREATER_INVISIBILITY_ELIXIR.get())
                .output(GREATER_INVISIBILITY_ELIXIR_FLUID.get(), 250)
                .output(Items.GLASS_BOTTLE)
                .build(output);
        emptying(ItemRegistry.EVASION_ELIXIR.getId())
                .require(ItemRegistry.EVASION_ELIXIR.get())
                .output(EVASION_ELIXIR_FLUID.get(), 250)
                .output(Items.GLASS_BOTTLE)
                .build(output);
        emptying(ItemRegistry.GREATER_EVASION_ELIXIR.getId())
                .require(ItemRegistry.GREATER_EVASION_ELIXIR.get())
                .output(GREATER_EVASION_ELIXIR_FLUID.get(), 250)
                .output(Items.GLASS_BOTTLE)
                .build(output);
        emptying(ItemRegistry.GREATER_HEALING_POTION.getId())
                .require(ItemRegistry.GREATER_HEALING_POTION.get())
                .output(GREATER_HEALING_ELIXIR_FLUID.get(), 250)
                .output(Items.GLASS_BOTTLE)
                .build(output);
        // OTHER FLUIDS
        emptying(BLOOD_VIAL.getId())
                .require(BLOOD_VIAL.get())
                .output(BLOOD.get(), 250)
                .output(GLASS_BOTTLE)
                .build(output);
        emptying(LIGHTNING_BOTTLE.getId())
                .require(LIGHTNING_BOTTLE.get())
                .output(LIGHTNING.get(), 250)
                .output(GLASS_BOTTLE)
                .build(output);
        emptying(TIMELESS_SLURRY.getId())
                .require(TIMELESS_SLURRY.get())
                .output(TIMELESS_SLURRY_FLUID.get(), 250)
                .output(GLASS_BOTTLE)
                .build(output);
    }

    private void buildCompactingRecipes(RecipeOutput output) {
        compacting(BLOOD.getId())
                .require(Tags.Items.FOODS_RAW_MEAT)
                .output(BLOOD.get(), 250)
                .build(output);
    }

    public void buildSequencedRecipes(RecipeOutput output) {
        // RUNES
        sequencedAssembly(MANA_RUNE.getId())
                .require(BLANK_RUNE.get())
                .transitionTo(BLANK_RUNE.get())
                .addOutput(MANA_RUNE.get(), 1)
                .loops(1)
                .addStep(FillingRecipe::new, builder -> builder.require(MANA.get(), 1000))
                .addStep(FillingRecipe::new, builder -> builder.require(MANA.get(), 1000))
                .build(output);
        sequencedAssembly(BLOOD_RUNE.getId())
                .require(BLANK_RUNE.get())
                .transitionTo(BLANK_RUNE.get())
                .addOutput(BLOOD_RUNE.get(), 1)
                .loops(1)
                .addStep(FillingRecipe::new, builder -> builder.require(BLOOD.get(), 1000))
                .addStep(FillingRecipe::new, builder -> builder.require(BLOOD.get(), 1000))
                .build(output);
        sequencedAssembly(LIGHTNING_RUNE.getId())
                .require(BLANK_RUNE.get())
                .transitionTo(BLANK_RUNE.get())
                .addOutput(LIGHTNING_RUNE.get(), 1)
                .loops(1)
                .addStep(FillingRecipe::new, builder -> builder.require(LIGHTNING.get(), 1000))
                .addStep(FillingRecipe::new, builder -> builder.require(LIGHTNING.get(), 1000))
                .build(output);

        // MISC ITEMS
        sequencedAssembly(ARCANE_INGOT.getId())
                .require(INGOTS)
                .transitionTo(COPPER_INGOT)
                .addOutput(ARCANE_INGOT.get(), 1)
                .loops(1)
                .addStep(FillingRecipe::new, builder -> builder.require(MANA.get(), 1000))
                .addStep(FillingRecipe::new, builder -> builder.require(MANA.get(), 1000))
                .build(output);
        sequencedAssembly(MAGIC_CLOTH.getId())
                .require(WOOL)
                .transitionTo(WHITE_WOOL)
                .addOutput(MAGIC_CLOTH.get(), 1)
                .loops(1)
                .addStep(FillingRecipe::new, builder -> builder.require(MANA.get(), 1000))
                .addStep(FillingRecipe::new, builder -> builder.require(MANA.get(), 1000))
                .build(output);
        sequencedAssembly(ENERGIZED_CORE.getId())
                .require(COPPER_BLOCK)
                .transitionTo(COPPER_BLOCK)
                .addOutput(ENERGIZED_CORE.get(), 1)
                .loops(1)
                .addStep(FillingRecipe::new, builder -> builder.require(LIGHTNING.get(), 1000))
                .addStep(FillingRecipe::new, builder -> builder.require(LIGHTNING.get(), 1000))
                .build(output);
        sequencedAssembly(WAYWARD_COMPASS.getId())
                .require(COMPASS)
                .transitionTo(COMPASS)
                .addOutput(WAYWARD_COMPASS.get(), 1)
                .loops(1)
                .addStep(FillingRecipe::new, builder -> builder.require(MANA.get(), 1000))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(SOUL_LANTERN))
                .build(output);
    }






    public static ProcessingRecipeBuilder<ConversionRecipe> conversion(ResourceLocation id) {
        return new ProcessingRecipeBuilder<>(ConversionRecipe::new, id);
    }

    public static ProcessingRecipeBuilder<CrushingRecipe> crushing(ResourceLocation id) {
        return new ProcessingRecipeBuilder<>(CrushingRecipe::new, id);
    }

    public static ProcessingRecipeBuilder<CuttingRecipe> cutting(ResourceLocation id) {
        return new ProcessingRecipeBuilder<>(CuttingRecipe::new, id);
    }

    public static ProcessingRecipeBuilder<MillingRecipe> milling(ResourceLocation id) {
        return new ProcessingRecipeBuilder<>(MillingRecipe::new, id);
    }

    public static ProcessingRecipeBuilder<MixingRecipe> mixing(ResourceLocation id) {
        return new ProcessingRecipeBuilder<>(MixingRecipe::new, id);
    }

    public static ProcessingRecipeBuilder<CompactingRecipe> compacting(ResourceLocation id) {
        return new ProcessingRecipeBuilder<>(CompactingRecipe::new, id);
    }

    public static ProcessingRecipeBuilder<PressingRecipe> pressing(ResourceLocation id) {
        return new ProcessingRecipeBuilder<>(PressingRecipe::new, id);
    }

    public static ProcessingRecipeBuilder<SandPaperPolishingRecipe> polishing(ResourceLocation id) {
        return new ProcessingRecipeBuilder<>(SandPaperPolishingRecipe::new, id);
    }

    public static ProcessingRecipeBuilder<SplashingRecipe> splashing(ResourceLocation id) {
        return new ProcessingRecipeBuilder<>(SplashingRecipe::new, id);
    }

    public static ProcessingRecipeBuilder<HauntingRecipe> haunting(ResourceLocation id) {
        return new ProcessingRecipeBuilder<>(HauntingRecipe::new, id);
    }

    public static ProcessingRecipeBuilder<DeployerApplicationRecipe> deploying(ResourceLocation id) {
        return new ProcessingRecipeBuilder<>(DeployerApplicationRecipe::new, id);
    }

    public static ProcessingRecipeBuilder<FillingRecipe> filling(ResourceLocation id) {
        return new ProcessingRecipeBuilder<>(FillingRecipe::new, id);
    }

    public static ProcessingRecipeBuilder<EmptyingRecipe> emptying(ResourceLocation id) {
        return new ProcessingRecipeBuilder<>(EmptyingRecipe::new, id);
    }

    public static ProcessingRecipeBuilder<ManualApplicationRecipe> manualApplication(ResourceLocation id) {
        return new ProcessingRecipeBuilder<>(ManualApplicationRecipe::new, id);
    }

    public static MechanicalCraftingRecipeBuilder mechanicalCrafting(ItemLike item, int count) {
        return new MechanicalCraftingRecipeBuilder(item, count);
    }

    public static MechanicalCraftingRecipeBuilder mechanicalCrafting(ItemLike item) {
        return new MechanicalCraftingRecipeBuilder(item, 1);
    }

    public static SequencedAssemblyRecipeBuilder sequencedAssembly(ResourceLocation id) {
        return new SequencedAssemblyRecipeBuilder(id);
    }
}