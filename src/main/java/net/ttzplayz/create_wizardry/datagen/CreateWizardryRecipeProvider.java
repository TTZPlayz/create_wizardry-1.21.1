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

import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeBuilder;
import com.simibubi.create.compat.jei.ConversionRecipe;
import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import com.simibubi.create.content.fluids.transfer.EmptyingRecipe;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.crusher.CrushingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.ManualApplicationRecipe;
import com.simibubi.create.content.kinetics.fan.processing.HauntingRecipe;
import com.simibubi.create.content.kinetics.fan.processing.SplashingRecipe;
import com.simibubi.create.content.kinetics.millstone.MillingRecipe;
import com.simibubi.create.content.kinetics.mixer.CompactingRecipe;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
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
import static net.minecraft.tags.FluidTags.LAVA;
import static net.minecraft.tags.ItemTags.WOOL;
import static net.minecraft.world.item.Items.*;
import static net.neoforged.neoforge.common.Tags.Items.DUSTS;
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
        buildMechanicalRecipes(output);
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
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(UNCOMMON_INK.get(), 1000)
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
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(RARE_INK.get(), 1000)
                .output(EPIC_INK.get(), 750)
                .requiresHeat(HEATED)
                .build(output);

        mixing(LEGENDARY_INK.getId())
                .require(LIGHTNING.get(), 500)
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
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(EPIC_INK.get(), 1000)
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
        // OTHER FLUIDS
        mixing(MANA.getId())
                .require(FluidTags.WATER, 1000)
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .output(MANA.get(), 1000)
                .requiresHeat(HEATED)
                .build(output);
        mixing(ICE_VENOM_FLUID.getId())
                .require(FluidTags.WATER, 1000)
                .require(ICY_FANG.get())
                .output(ICE_VENOM_FLUID.get(), 750)
                .requiresHeat(HEATED)
                .build(output);
        mixing(TIMELESS_SLURRY_FLUID.getId())
                .require(FluidTags.WATER, 1000)
                .require(ECHO_SHARD)
                .require(DUSTS)
                .output(TIMELESS_SLURRY_FLUID.get(), 750)
                .requiresHeat(SUPERHEATED)
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
        filling(ICE_VENOM_VIAL.getId())
                .require(Items.GLASS_BOTTLE)
                .require(ICE_VENOM_FLUID.get(), 250)
                .output(ICE_VENOM_VIAL.get())
                .build(output);

        //RUNES
        filling(MANA_RUNE.getId())
                .require(BLANK_RUNE.get())
                .require(MANA.get(), 1000)
                .output(MANA_RUNE.get())
                .build(output);
        filling(LIGHTNING_RUNE.getId())
                .require(BLANK_RUNE.get())
                .require(LIGHTNING.get(), 1000)
                .output(LIGHTNING_RUNE.get())
                .build(output);
        filling(BLOOD_RUNE.getId())
                .require(BLANK_RUNE.get())
                .require(BLOOD.get(), 1000)
                .output(BLOOD_RUNE.get())
                .build(output);
        filling(ICE_RUNE.getId())
                .require(BLANK_RUNE.get())
                .require(ICE_VENOM_FLUID.get(), 250)
                .output(ICE_RUNE.get())
                .build(output);
        //ARMOR
        filling(WANDERING_MAGICIAN_BOOTS.getId())
                .require(LEATHER_BOOTS)
                .require(MANA.get(), 250)
                .output(WANDERING_MAGICIAN_BOOTS.get())
                .build(output);
        filling(WANDERING_MAGICIAN_CHESTPLATE.getId())
                .require(LEATHER_CHESTPLATE)
                .require(MANA.get(), 250)
                .output(WANDERING_MAGICIAN_CHESTPLATE.get())
                .build(output);
        filling(WANDERING_MAGICIAN_HELMET.getId())
                .require(LEATHER_HELMET)
                .require(MANA.get(), 250)
                .output(WANDERING_MAGICIAN_HELMET.get())
                .build(output);
        filling(WANDERING_MAGICIAN_LEGGINGS.getId())
                .require(LEATHER_LEGGINGS)
                .require(MANA.get(), 250)
                .output(WANDERING_MAGICIAN_LEGGINGS.get())
                .build(output);


        // MISC
        filling(ARCANE_ESSENCE.getId())
                .require(Tags.Items.DUSTS)
                .require(MANA.get(), 250)
                .output(ARCANE_ESSENCE.get())
                .build(output);
        filling(ARCANE_INGOT.getId())
                .require(INGOTS)
                .require(MANA.get(), 1000)
                .output(ARCANE_INGOT.get())
                .build(output);
        filling(MAGIC_CLOTH.getId())
                .require(WOOL)
                .require(MANA.get(), 1000)
                .output(MAGIC_CLOTH.get())
                .build(output);
        filling(ENERGIZED_CORE.getId())
                .require(WOOL)
                .require(LIGHTNING.get(), 1000)
                .output(ENERGIZED_CORE.get())
                .build(output);
        filling(ICY_FANG.getId())
                .require(FROZEN_BONE_SHARD.get())
                .require(ICE_VENOM_FLUID.get(), 500)
                .output(ICY_FANG.get())
                .build(output);
        filling(BLOODY_VELLUM.getId())
                .require(HOGSKIN.get())
                .require(BLOOD.get(), 500)
                .output(BLOODY_VELLUM.get())
                .build(output);
        filling(FROZEN_BONE_SHARD.getId())
                .require(BONE)
                .require(ICE_VENOM_FLUID.get(), 250)
                .output(FROZEN_BONE_SHARD.get())
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
                .output(GLASS_BOTTLE)
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
        emptying(ICE_VENOM_FLUID.getId())
                .require(ICE_VENOM_VIAL.get())
                .output(ICE_VENOM_FLUID.get(), 250)
                .output(GLASS_BOTTLE)
                .build(output);
    }

    private void buildCompactingRecipes(RecipeOutput output) {
        compacting(BLOOD.getId())
                .require(Tags.Items.FOODS_RAW_MEAT)
                .output(BLOOD.get(), 250)
                .build(output);
    }

    private void buildSequencedRecipes(RecipeOutput output) {
        sequencedAssembly(WAYWARD_COMPASS.getId())
                .require(COMPASS)
                .transitionTo(COMPASS)
                .addOutput(WAYWARD_COMPASS.get(), 1)
                .loops(1)
                .addStep(FillingRecipe::new, builder -> builder.require(MANA.get(), 1000))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(SOUL_LANTERN))
                .build(output);
        sequencedAssembly(CINDEROUS_SOULCALLER.getId())
                .require(BELL)
                .transitionTo(BELL)
                .addOutput(CINDEROUS_SOULCALLER.get(), 1)
                .loops(1)
                .addStep(FillingRecipe::new, builder -> builder.require(LAVA, 1000))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(NETHERITE_SCRAP))
                .build(output);
        sequencedAssembly(ICE_SPIDER_EGG_BLOCK_ITEM.getId())
                .require(TURTLE_EGG)
                .transitionTo(TURTLE_EGG)
                .addOutput(ICE_SPIDER_EGG_BLOCK_ITEM.get(), 1)
                .loops(1)
                .addStep(FillingRecipe::new, builder -> builder.require(ICE_VENOM_FLUID.get(), 1000))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(SPIDER_EYE))
                .build(output);
        sequencedAssembly(BLANK_RUNE.getId())
                .require(TUFF)
                .transitionTo(TUFF)
                .addOutput(BLANK_RUNE.get(), 1)
                .loops(5)
                .addStep(FillingRecipe::new, builder -> builder.require(MANA.get(), 1000))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
    }

    private void buildMechanicalRecipes (RecipeOutput output) {
        mechanicalCrafting(CINDEROUS_KEYSTONE_BLOCK_ITEM.get(), 1)
                .key('n', NETHERITE_INGOT)
                .key('r', FIRE_RUNE.get())
                .key('s', NETHER_STAR)
                .patternLine("nrn")
                .patternLine("rsr")
                .patternLine("nrn")
                .build(output);
    }






    public static StandardProcessingRecipe.Builder<ConversionRecipe> conversion(ResourceLocation id) {
        return new StandardProcessingRecipe.Builder<>(ConversionRecipe::new, id);
    }

    public static StandardProcessingRecipe.Builder<CrushingRecipe> crushing(ResourceLocation id) {
        return new StandardProcessingRecipe.Builder<>(CrushingRecipe::new, id);
    }

    public static StandardProcessingRecipe.Builder<CuttingRecipe> cutting(ResourceLocation id) {
        return new StandardProcessingRecipe.Builder<>(CuttingRecipe::new, id);
    }

    public static StandardProcessingRecipe.Builder<MillingRecipe> milling(ResourceLocation id) {
        return new StandardProcessingRecipe.Builder<>(MillingRecipe::new, id);
    }

    public static StandardProcessingRecipe.Builder<MixingRecipe> mixing(ResourceLocation id) {
        return new StandardProcessingRecipe.Builder<>(MixingRecipe::new, id);
    }

    public static StandardProcessingRecipe.Builder<CompactingRecipe> compacting(ResourceLocation id) {
        return new StandardProcessingRecipe.Builder<>(CompactingRecipe::new, id);
    }

    public static StandardProcessingRecipe.Builder<PressingRecipe> pressing(ResourceLocation id) {
        return new StandardProcessingRecipe.Builder<>(PressingRecipe::new, id);
    }

    public static StandardProcessingRecipe.Builder<SandPaperPolishingRecipe> polishing(ResourceLocation id) {
        return new StandardProcessingRecipe.Builder<>(SandPaperPolishingRecipe::new, id);
    }

    public static StandardProcessingRecipe.Builder<SplashingRecipe> splashing(ResourceLocation id) {
        return new StandardProcessingRecipe.Builder<>(SplashingRecipe::new, id);
    }

    public static StandardProcessingRecipe.Builder<HauntingRecipe> haunting(ResourceLocation id) {
        return new StandardProcessingRecipe.Builder<>(HauntingRecipe::new, id);
    }

    public static ItemApplicationRecipe.Builder<DeployerApplicationRecipe> deploying(ResourceLocation id) {
        return new ItemApplicationRecipe.Builder<>(DeployerApplicationRecipe::new, id);
    }

    public static StandardProcessingRecipe.Builder<FillingRecipe> filling(ResourceLocation id) {
        return new StandardProcessingRecipe.Builder<>(FillingRecipe::new, id);
    }

    public static StandardProcessingRecipe.Builder<EmptyingRecipe> emptying(ResourceLocation id) {
        return new StandardProcessingRecipe.Builder<>(EmptyingRecipe::new, id);
    }

    public static ItemApplicationRecipe.Builder<ManualApplicationRecipe> manualApplication(ResourceLocation id) {
        return new ItemApplicationRecipe.Builder<>(ManualApplicationRecipe::new, id);
    }

    public static SequencedAssemblyRecipeBuilder sequencedAssembly(ResourceLocation id) {
        return new SequencedAssemblyRecipeBuilder(id);
    }

    public static MechanicalCraftingRecipeBuilder mechanicalCrafting(ItemLike item, int count) {
        return new MechanicalCraftingRecipeBuilder(item, count);
    }
}