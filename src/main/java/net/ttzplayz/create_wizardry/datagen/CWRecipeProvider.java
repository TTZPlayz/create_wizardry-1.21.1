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
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import io.redspace.ironsspellbooks.fluids.NoopFluid;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.ttzplayz.create_wizardry.item.CWItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.simibubi.create.AllBlocks.COGWHEEL;
import static com.simibubi.create.AllFluids.*;
import static com.simibubi.create.AllItems.*;
import static com.simibubi.create.AllTags.AllItemTags.AMETHYST;
import static com.simibubi.create.AllTags.AllItemTags.FLOURS;
import static com.simibubi.create.content.processing.recipe.HeatCondition.HEATED;
import static com.simibubi.create.content.processing.recipe.HeatCondition.SUPERHEATED;
import static com.simibubi.create.foundation.data.recipe.CommonMetal.SILVER;
import static io.redspace.ironsspellbooks.registries.FluidRegistry.*;
import static io.redspace.ironsspellbooks.registries.ItemRegistry.*;
import static io.redspace.ironsspellbooks.registries.ItemRegistry.FIRE_UPGRADE_ORB;
import static net.minecraft.tags.FluidTags.*;
import static net.minecraft.tags.ItemTags.*;
import static net.minecraft.world.item.Items.*;
import static net.minecraft.world.item.Items.ANVIL;
import static net.minecraft.world.item.Items.DIRT;
import static net.neoforged.neoforge.common.Tags.Fluids.EXPERIENCE;
import static net.neoforged.neoforge.common.Tags.Items.*;
import static net.neoforged.neoforge.common.Tags.Items.FENCES;
import static net.ttzplayz.create_wizardry.block.CWBlocks.CHANNELER;
import static net.ttzplayz.create_wizardry.fluids.CWFluidRegistry.*;
import static net.ttzplayz.create_wizardry.item.CWItems.CRUSHED_MITHRIL;
import static net.ttzplayz.create_wizardry.item.CWItems.MITHRIL_NUGGET;

public class CWRecipeProvider extends RecipeProvider {

    public CWRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        buildFillingRecipes(output);
        buildEmptyingRecipes(output);
        buildMixingRecipes(output);
        buildCompactingRecipes(output);
        buildCrushingRecipes(output);
        buildWashingRecipes(output);
        buildArmorRecipes(output);
        buildMiscItemRecipes(output);
        buildStaffRecipes(output);
        buildBlockRecipes(output);
        buildRuneAndOrbRecipes(output);
        buildSpellbookRecipes(output);
        buildWeaponRecipes(output);
        buildCharmRecipes(output);
        buildCompatRecipes(output);
        buildManaRecipes(output);


        List<ItemLike> MITHRIL_SMELTABLES = List.of(CRUSHED_MITHRIL.get(), MITHRIL_ORE_BLOCK_ITEM.get(), MITHRIL_ORE_DEEPSLATE_BLOCK_ITEM.get());

        oreSmelting(output, MITHRIL_SMELTABLES, RecipeCategory.MISC, MITHRIL_SCRAP.get(), 0.25f, 200, "mithril_scrap");
        oreBlasting(output, MITHRIL_SMELTABLES, RecipeCategory.MISC, MITHRIL_SCRAP.get(), 0.25f, 100, "mithril_scrap");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MITHRIL_SCRAP.get(), 1)
                .pattern("MMM")
                .pattern("MMM")
                .pattern("MMM")
                .define('M', MITHRIL_NUGGET.get())
                .unlockedBy("has_mithril_nugget", has(MITHRIL_NUGGET.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CHANNELER.get(), 1)
                .pattern(" L ")
                .pattern("IHI")
                .pattern("CCC")
                .define('C', COPPER_SHEET)
                .define('L', LIGHTNING_ROD)
                .define('H', HOPPER)
                .define('I', IRON_SHEET)
                .unlockedBy("has_copper_sheet", has(COPPER_SHEET))
                .save(output);

    }

    private void buildCompatRecipes(RecipeOutput output) {
        mixing(ResourceLocation.parse("cei_rare_ink_recipe"))
                .whenModLoaded("create_enchantment_industry")
                .require(GOLD_INGOT)
                .require(GOLD_INGOT)
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(UNCOMMON_INK.get(), 1000)
                .require(EXPERIENCE, 3)
                .output(RARE_INK.get(), 500)
                .build(output);
        mixing(ResourceLocation.parse("cei_epic_ink_recipe"))
                .whenModLoaded("create_enchantment_industry")
                .require(DIAMOND)
                .require(DIAMOND)
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(RARE_INK.get(), 1000)
                .require(EXPERIENCE, 6)
                .output(EPIC_INK.get(), 500)
                .requiresHeat(HEATED)
                .build(output);
        mixing(ResourceLocation.parse("cei_legendary_ink_recipe"))
                .whenModLoaded("create_enchantment_industry")
                .require(AMETHYST_SHARD)
                .require(AMETHYST_SHARD)
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(EPIC_INK.get(), 1000)
                .require(EXPERIENCE, 9)
                .output(LEGENDARY_INK.get(), 500)
                .requiresHeat(SUPERHEATED)
                .build(output);
        compacting(ResourceLocation.parse("cei_exp_to_exp_nugget"))
                .require(EXPERIENCE, 3)
                .require(SLIME_BALLS)
                .output(EXP_NUGGET)
                .build(output);
        compacting(ResourceLocation.parse("cei_exp_to_exp_nugget_with_honeycomb"))
                .require(EXPERIENCE, 3)
                .require(HONEYCOMB)
                .output(EXP_NUGGET)
                .build(output);
    }

    private void buildRuneAndOrbRecipes(RecipeOutput output) {
        runeSequence(output, NATURE_RUNE.getId(), POISONOUS_POTATO, NATURE_RUNE.get());
        runeSequence(output, FIRE_RUNE.getId(), BLAZE_ROD, FIRE_RUNE.get());
        runeSequence(output, EVOCATION_RUNE.getId(), EMERALD, EVOCATION_RUNE.get());
        runeSequence(output, ICE_RUNE.getId(), FROZEN_BONE_SHARD.get(), ICE_RUNE.get());
        runeSequence(output, HOLY_RUNE.getId(), DIVINE_PEARL.get(), HOLY_RUNE.get());
        runeSequence(output, ENDER_RUNE.getId(), ENDER_PEARL, ENDER_RUNE.get());
        runeSequence(output, COOLDOWN_RUNE.getId(), PHANTOM_MEMBRANE, COOLDOWN_RUNE.get());
        runeSequence(output, PROTECTION_RUNE.getId(), PUFFERFISH, PROTECTION_RUNE.get());

        orbSequence(output, NATURE_UPGRADE_ORB.getId(), NATURE_RUNE.get(), NATURE_UPGRADE_ORB.get());
        orbSequence(output, FIRE_UPGRADE_ORB.getId(), FIRE_RUNE.get(), FIRE_UPGRADE_ORB.get());
        orbSequence(output, EVOCATION_UPGRADE_ORB.getId(), EVOCATION_RUNE.get(), EVOCATION_UPGRADE_ORB.get());
        orbSequence(output, ICE_UPGRADE_ORB.getId(), ICE_RUNE.get(), ICE_UPGRADE_ORB.get());
        orbSequence(output, HOLY_UPGRADE_ORB.getId(), HOLY_RUNE.get(), HOLY_UPGRADE_ORB.get());
        orbSequence(output, ENDER_UPGRADE_ORB.getId(), ENDER_RUNE.get(), ENDER_UPGRADE_ORB.get());
        orbSequence(output, MANA_UPGRADE_ORB.getId(), MANA_RUNE.get(), MANA_UPGRADE_ORB.get());
        orbSequence(output, BLOOD_UPGRADE_ORB.getId(), BLOOD_RUNE.get(), BLOOD_UPGRADE_ORB.get());
        orbSequence(output, LIGHTNING_UPGRADE_ORB.getId(), LIGHTNING_RUNE.get(), LIGHTNING_UPGRADE_ORB.get());
        orbSequence(output, COOLDOWN_UPGRADE_ORB.getId(), COOLDOWN_RUNE.get(), COOLDOWN_UPGRADE_ORB.get());
        orbSequence(output, PROTECTION_UPGRADE_ORB.getId(), PROTECTION_RUNE.get(), PROTECTION_UPGRADE_ORB.get());


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
    }


    private void buildArmorRecipes(RecipeOutput output) {
// CRYOMANCER ARMOR
        deploying(CRYOMANCER_BOOTS.getId())
                .require(WIZARD_BOOTS.get())
                .require(ICE_RUNE.get())
                .output(CRYOMANCER_BOOTS.get(), 1)
                .build(output);
        deploying(CRYOMANCER_CHESTPLATE.getId())
                .require(WIZARD_CHESTPLATE.get())
                .require(ICE_RUNE.get())
                .output(CRYOMANCER_CHESTPLATE.get(), 1)
                .build(output);
        deploying(CRYOMANCER_HELMET.getId())
                .require(WIZARD_HELMET.get())
                .require(ICE_RUNE.get())
                .output(CRYOMANCER_HELMET.get(), 1)
                .build(output);
        deploying(CRYOMANCER_LEGGINGS.getId())
                .require(WIZARD_LEGGINGS.get())
                .require(ICE_RUNE.get())
                .output(CRYOMANCER_LEGGINGS.get(), 1)
                .build(output);
// ELECTROMANCER ARMOR
        deploying(ELECTROMANCER_BOOTS.getId())
                .require(WIZARD_BOOTS.get())
                .require(LIGHTNING_RUNE.get())
                .output(ELECTROMANCER_BOOTS.get(), 1)
                .build(output);
        deploying(ELECTROMANCER_CHESTPLATE.getId())
                .require(WIZARD_CHESTPLATE.get())
                .require(LIGHTNING_RUNE.get())
                .output(ELECTROMANCER_CHESTPLATE.get(), 1)
                .build(output);
        deploying(ELECTROMANCER_HELMET.getId())
                .require(WIZARD_HELMET.get())
                .require(LIGHTNING_RUNE.get())
                .output(ELECTROMANCER_HELMET.get(), 1)
                .build(output);
        deploying(ELECTROMANCER_LEGGINGS.getId())
                .require(WIZARD_LEGGINGS.get())
                .require(LIGHTNING_RUNE.get())
                .output(ELECTROMANCER_LEGGINGS.get(), 1)
                .build(output);
// ARCHEVOKER ARMOR
        deploying(ARCHEVOKER_BOOTS.getId())
                .require(WIZARD_BOOTS.get())
                .require(EVOCATION_RUNE.get())
                .output(ARCHEVOKER_BOOTS.get(), 1)
                .build(output);
        deploying(ARCHEVOKER_CHESTPLATE.getId())
                .require(WIZARD_CHESTPLATE.get())
                .require(EVOCATION_RUNE.get())
                .output(ARCHEVOKER_CHESTPLATE.get(), 1)
                .build(output);
        deploying(ARCHEVOKER_HELMET.getId())
                .require(WIZARD_HELMET.get())
                .require(EVOCATION_RUNE.get())
                .output(ARCHEVOKER_HELMET.get(), 1)
                .build(output);
        deploying(ARCHEVOKER_LEGGINGS.getId())
                .require(WIZARD_LEGGINGS.get())
                .require(EVOCATION_RUNE.get())
                .output(ARCHEVOKER_LEGGINGS.get(), 1)
                .build(output);
// CULTIST ARMOR
        deploying(CULTIST_BOOTS.getId())
                .require(WIZARD_BOOTS.get())
                .require(BLOOD_RUNE.get())
                .output(CULTIST_BOOTS.get(), 1)
                .build(output);
        deploying(CULTIST_CHESTPLATE.getId())
                .require(WIZARD_CHESTPLATE.get())
                .require(BLOOD_RUNE.get())
                .output(CULTIST_CHESTPLATE.get(), 1)
                .build(output);
        deploying(CULTIST_HELMET.getId())
                .require(WIZARD_HELMET.get())
                .require(BLOOD_RUNE.get())
                .output(CULTIST_HELMET.get(), 1)
                .build(output);
        deploying(CULTIST_LEGGINGS.getId())
                .require(WIZARD_LEGGINGS.get())
                .require(BLOOD_RUNE.get())
                .output(CULTIST_LEGGINGS.get(), 1)
                .build(output);
// SHADOWWALKER ARMOR
        deploying(SHADOWWALKER_BOOTS.getId())
                .require(WIZARD_BOOTS.get())
                .require(ENDER_RUNE.get())
                .output(SHADOWWALKER_BOOTS.get(), 1)
                .build(output);
        deploying(SHADOWWALKER_CHESTPLATE.getId())
                .require(WIZARD_CHESTPLATE.get())
                .require(ENDER_RUNE.get())
                .output(SHADOWWALKER_CHESTPLATE.get(), 1)
                .build(output);
        deploying(SHADOWWALKER_HELMET.getId())
                .require(WIZARD_HELMET.get())
                .require(ENDER_RUNE.get())
                .output(SHADOWWALKER_HELMET.get(), 1)
                .build(output);
        deploying(SHADOWWALKER_LEGGINGS.getId())
                .require(WIZARD_LEGGINGS.get())
                .require(ENDER_RUNE.get())
                .output(SHADOWWALKER_LEGGINGS.get(), 1)
                .build(output);
// PRIEST ARMOR
        deploying(PRIEST_BOOTS.getId())
                .require(WIZARD_BOOTS.get())
                .require(HOLY_RUNE.get())
                .output(PRIEST_BOOTS.get(), 1)
                .build(output);
        deploying(PRIEST_CHESTPLATE.getId())
                .require(WIZARD_CHESTPLATE.get())
                .require(HOLY_RUNE.get())
                .output(PRIEST_CHESTPLATE.get(), 1)
                .build(output);
        deploying(PRIEST_HELMET.getId())
                .require(WIZARD_HELMET.get())
                .require(HOLY_RUNE.get())
                .output(PRIEST_HELMET.get(), 1)
                .build(output);
        deploying(PRIEST_LEGGINGS.getId())
                .require(WIZARD_LEGGINGS.get())
                .require(HOLY_RUNE.get())
                .output(PRIEST_LEGGINGS.get(), 1)
                .build(output);
// PLAGUED ARMOR
        deploying(PLAGUED_BOOTS.getId())
                .require(WIZARD_BOOTS.get())
                .require(NATURE_RUNE.get())
                .output(PLAGUED_BOOTS.get(), 1)
                .build(output);
        deploying(PLAGUED_CHESTPLATE.getId())
                .require(WIZARD_CHESTPLATE.get())
                .require(NATURE_RUNE.get())
                .output(PLAGUED_CHESTPLATE.get(), 1)
                .build(output);
        deploying(PLAGUED_HELMET.getId())
                .require(WIZARD_HELMET.get())
                .require(NATURE_RUNE.get())
                .output(PLAGUED_HELMET.get(), 1)
                .build(output);
        deploying(PLAGUED_LEGGINGS.getId())
                .require(WIZARD_LEGGINGS.get())
                .require(NATURE_RUNE.get())
                .output(PLAGUED_LEGGINGS.get(), 1)
                .build(output);
// PYROMANCER ARMOR
        deploying(PYROMANCER_BOOTS.getId())
                .require(WIZARD_BOOTS.get())
                .require(FIRE_RUNE.get())
                .output(PYROMANCER_BOOTS.get(), 1)
                .build(output);
        deploying(PYROMANCER_CHESTPLATE.getId())
                .require(WIZARD_CHESTPLATE.get())
                .require(FIRE_RUNE.get())
                .output(PYROMANCER_CHESTPLATE.get(), 1)
                .build(output);
        deploying(PYROMANCER_HELMET.getId())
                .require(WIZARD_HELMET.get())
                .require(FIRE_RUNE.get())
                .output(PYROMANCER_HELMET.get(), 1)
                .build(output);
        deploying(PYROMANCER_LEGGINGS.getId())
                .require(WIZARD_LEGGINGS.get())
                .require(FIRE_RUNE.get())
                .output(PYROMANCER_LEGGINGS.get(), 1)
                .build(output);
        deploying(CHAINED_BOOK.getId())
                .require(BOOK)
                .require(CHAIN)
                .output(CHAINED_BOOK.get())
                .build(output);
// WANDERING MAGICIAN ARMOR
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
// SCARECROW ARMOR
        filling(PUMPKIN_BOOTS.getId())
                .require(MAGIC_CLOTH.get())
                .require(MANA.get(), 250)
                .output(PUMPKIN_BOOTS.get())
                .build(output);
        filling(PUMPKIN_CHESTPLATE.getId())
                .require(LEATHER)
                .require(MANA.get(), 250)
                .output(PUMPKIN_CHESTPLATE.get())
                .build(output);
        filling(PUMPKIN_HELMET.getId())
                .require(PUMPKINS_CARVED)
                .require(MANA.get(), 250)
                .output(PUMPKIN_HELMET.get())
                .build(output);
        filling(PUMPKIN_LEGGINGS.getId())
                .require(HAY_BLOCK)
                .require(MANA.get(), 250)
                .output(PUMPKIN_LEGGINGS.get())
                .build(output);
// NETHERITE MAGE ARMOR

        deploying(NETHERITE_MAGE_BOOTS.getId())
                .require(WIZARD_BOOTS.get())
                .require(NETHERITE_INGOT)
                .output(NETHERITE_MAGE_BOOTS.get())
                .build(output);
        deploying(NETHERITE_MAGE_CHESTPLATE.getId())
                .require(WIZARD_CHESTPLATE.get())
                .require(NETHERITE_INGOT)
                .output(NETHERITE_MAGE_CHESTPLATE.get())
                .build(output);
        deploying(NETHERITE_MAGE_HELMET.getId())
                .require(WIZARD_HELMET.get())
                .require(NETHERITE_INGOT)
                .output(NETHERITE_MAGE_HELMET.get())
                .build(output);
        deploying(NETHERITE_MAGE_LEGGINGS.getId())
                .require(WIZARD_LEGGINGS.get())
                .require(NETHERITE_INGOT)
                .output(NETHERITE_MAGE_LEGGINGS.get())
                .build(output);
// UNIQUE ARMOR
        sequencedAssembly(TARNISHED_CROWN.getId())
                .require(IRON_HELMET)
                .transitionTo(IRON_HELMET)
                .addOutput(TARNISHED_CROWN.get(), 1)
                .loops(3)
                .addStep(FillingRecipe::new, builder -> builder.require(MANA.get(), 500))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(INFERNAL_SORCERER_CHESTPLATE.getId())
                .require(PYROMANCER_CHESTPLATE.get())
                .transitionTo(PYROMANCER_CHESTPLATE.get())
                .addOutput(INFERNAL_SORCERER_CHESTPLATE.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(DIVINE_SOULSHARD.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MITHRIL_WEAVE.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(PYRIUM_INGOT.get()))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(PALADIN_CHESTPLATE.getId())
                .require(PRIEST_CHESTPLATE.get())
                .transitionTo(PRIEST_CHESTPLATE.get())
                .addOutput(PALADIN_CHESTPLATE.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(GOLD_INGOT))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MITHRIL_WEAVE.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(GOLD_INGOT))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(DIVINE_SOULSHARD.get()))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(BOOTS_OF_SPEED.getId())
                .require(ARCHEVOKER_BOOTS.get())
                .transitionTo(ARCHEVOKER_BOOTS.get())
                .addOutput(BOOTS_OF_SPEED.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(DIVINE_SOULSHARD.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(FEATHER))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
    }

    private void buildMiscItemRecipes(RecipeOutput output) {
// MISC ITEMS
        sequencedAssembly(BLANK_RUNE.getId())
                .require(TUFF)
                .transitionTo(TUFF_SLAB)
                .addOutput(BLANK_RUNE.get(), 30)
                .addOutput(TUFF, 55)
                .addOutput(COBBLESTONE, 45)
                .addOutput(COBBLESTONE_SLAB, 10)
                .addOutput(COBBLED_DEEPSLATE_SLAB, 5)
                .addOutput(ARCANE_ESSENCE.get(), 5)
                .loops(3)
                .addStep(FillingRecipe::new, builder -> builder.require(MANA.get(), 1000))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(WEAPON_PARTS.getId())
                .require(ARCANE_INGOT.get())
                .transitionTo(MITHRIL_INGOT.get())
                .addOutput(WEAPON_PARTS.get(), 1)
                .loops(2)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MITHRIL_INGOT.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(ARCANE_INGOT.get()))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(ICE_CRYSTAL.getId())
                .require(AMETHYST_SHARD)
                .transitionTo(AMETHYST_SHARD)
                .addOutput(ICE_CRYSTAL.get(), 1)
                .loops(3)
                .addStep(FillingRecipe::new, builder -> builder.require(ICE_VENOM_FLUID.get(), 1000))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(BLUE_ICE))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(ELDRITCH_PAGE.getId())
                .require(PAPER)
                .transitionTo(PAPER)
                .addOutput(ELDRITCH_PAGE.get(), 75)
                .addOutput(LOST_KNOWLEDGE_FRAGMENT.get(), 50)
                .addOutput(ECHO_SHARD, 10)
                .addOutput(SCULK_SHRIEKER, 10)
                .addOutput(POTATO, 5)
                .loops(4)
                .addStep(FillingRecipe::new, builder -> builder.require(TIMELESS_SLURRY_FLUID.get(), 125))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(LOST_KNOWLEDGE_FRAGMENT.get()))
                .build(output);
        sequencedAssembly(DRAGONSKIN.getId())
                .require(PHANTOM_MEMBRANE)
                .transitionTo(PHANTOM_MEMBRANE)
                .addOutput(DRAGONSKIN.get(), 15)
                .addOutput(OBSIDIAN, 75)
                .addOutput(PHANTOM_MEMBRANE, 35)
                .addOutput(CRYING_OBSIDIAN, 10)
                .addOutput(ENDER_EYE, 10)
                .addOutput(STURDY_SHEET, 5)
                .loops(5)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(END_CRYSTAL))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(OBSIDIAN))
                .addStep(FillingRecipe::new, builder -> builder.require(TIMELESS_SLURRY_FLUID.get(), 1000))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(FROSTED_HELVE.getId())
                .require(FROZEN_BONE_SHARD.get())
                .transitionTo(FROZEN_BONE_SHARD.get())
                .addOutput(FROSTED_HELVE.get(), 1)
                .loops(2)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(ARCANE_INGOT.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(FROZEN_BONE_SHARD.get()))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(MITHRIL_WEAVE.getId())
                .require(MITHRIL_INGOT.get())
                .transitionTo(MITHRIL_INGOT.get())
                .addOutput(MITHRIL_WEAVE.get(), 1)
                .loops(2)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(CHAIN))
                .addStep(PressingRecipe::new, builder -> (builder))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MITHRIL_INGOT.get()))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(RUINED_BOOK.getId())
                .require(ENCHANTED_BOOK)
                .transitionTo(ENCHANTED_BOOK)
                .addOutput(RUINED_BOOK.get(), 45)
                .addOutput(LEATHER, 60)
                .addOutput(PAPER, 30)
                .addOutput(SCULK_VEIN, 5)
                .addOutput(EXP_NUGGET, 5)
                .addOutput(ECHO_SHARD, 3)
                .addOutput(SCULK_SHRIEKER, 2)
                .loops(3)
                .addStep(FillingRecipe::new, builder -> builder.require(TIMELESS_SLURRY_FLUID.get(), 500))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(SCULK_CATALYST))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(HITHER_THITHER_WAND.getId())
                .require(RODS_WOODEN)
                .transitionTo(STICK)
                .addOutput(HITHER_THITHER_WAND.get(), 1)
                .addOutput(AMETHYST_SHARD, 1)
                .addOutput(ENDER_PEARL, 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(AMETHYST_SHARD))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(ENDER_PEARL))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(GOLD_NUGGET))
                .addStep(FillingRecipe::new, builder -> builder.require(MANA.get(), 1000))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(UPGRADE_ORB.getId())
                .require(ARCANE_INGOT.get())
                .transitionTo(ARCANE_INGOT.get())
                .addOutput(UPGRADE_ORB.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(CINDER_ESSENCE.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MITHRIL_INGOT.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(CINDER_ESSENCE.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(ARCANE_INGOT.get()))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);

        //CHECK IF THIS WORKS
        sequencedAssembly(ANCIENT_FURLED_MAP.getId())
                .require(PAPER)
                .transitionTo(PAPER)
                .addOutput(ANCIENT_FURLED_MAP.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(LOST_KNOWLEDGE_FRAGMENT.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(CINDER_ESSENCE.get()))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);

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
                .require(COPPER_BLOCK)
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
        haunting(HOGSKIN.getId())
                .require(LEATHER)
                .output(HOGSKIN.get())
                .build(output);

        // BOSS/MOB SUMMON ITEMS
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
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(NETHERITE_SCRAP))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(CINDER_ESSENCE.get()))
                .addStep(FillingRecipe::new, builder -> builder.require(LAVA, 1000))
                .build(output);
    }
    private void buildSpellbookRecipes(RecipeOutput output) {
        sequencedAssembly(COPPER_SPELL_BOOK.getId())
                .require(COPPER_INGOT)
                .transitionTo(COPPER_INGOT)
                .addOutput(COPPER_SPELL_BOOK.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(PAPER))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(PAPER))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(COPPER_INGOT))
                .build(output);
        sequencedAssembly(IRON_SPELL_BOOK.getId())
                .require(LEATHER)
                .transitionTo(LEATHER)
                .addOutput(IRON_SPELL_BOOK.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(PAPER))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(CHAIN))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(PAPER))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(LEATHER))
                .build(output);
        sequencedAssembly(GOLD_SPELL_BOOK.getId())
                .require(GOLDEN_SHEET)
                .transitionTo(GOLDEN_SHEET)
                .addOutput(GOLD_SPELL_BOOK.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(ARCANE_ESSENCE.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(HOGSKIN.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(ARCANE_ESSENCE.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(GOLDEN_SHEET))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(DIAMOND_SPELL_BOOK.getId())
                .require(ENCHANTED_BOOK)
                .transitionTo(ENCHANTED_BOOK)
                .addOutput(DIAMOND_SPELL_BOOK.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MAGIC_CLOTH.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(HOGSKIN.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(DIAMOND))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MAGIC_CLOTH.get()))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(NETHERITE_SPELL_BOOK.getId())
                .require(RUINED_BOOK.get())
                .transitionTo(RUINED_BOOK.get())
                .addOutput(NETHERITE_SPELL_BOOK.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MAGIC_CLOTH.get()))
                .addStep(FillingRecipe::new, builder -> builder.require(BLOOD.get(), 500))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(NETHERITE_INGOT))
                .addStep(FillingRecipe::new, builder -> builder.require(LIGHTNING.get(), 250))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(ROTTEN_SPELL_BOOK.getId())
                .require(ENCHANTED_BOOK)
                .transitionTo(ENCHANTED_BOOK)
                .addOutput(ROTTEN_SPELL_BOOK.get(), 3)
                .addOutput(DIRT, 1)
                .addOutput(BONE_MEAL, 2)
                .addOutput(ROTTEN_FLESH, 4)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(ROTTEN_FLESH))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(BONE_MEAL))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(DIRT))
                .addStep(FillingRecipe::new, builder -> builder.require(MANA.get(), 500))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(BLAZE_SPELL_BOOK.getId())
                .require(ENCHANTED_BOOK)
                .transitionTo(ENCHANTED_BOOK)
                .addOutput(BLAZE_SPELL_BOOK.get(), 4)
                .addOutput(NETHERITE_SCRAP, 16)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(NETHERITE_SCRAP))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(BLAZE_POWDER))
                .addStep(FillingRecipe::new, builder -> builder.require(LAVA, 1000))
                .build(output);
        sequencedAssembly(DRAGONSKIN_SPELL_BOOK.getId())
                .require(RUINED_BOOK.get())
                .transitionTo(RUINED_BOOK.get())
                .addOutput(DRAGONSKIN_SPELL_BOOK.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(DRAGONSKIN.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(OBSIDIANS))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(DRAGONSKIN.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(OBSIDIANS))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(VILLAGER_SPELL_BOOK.getId())
                .require(ENCHANTED_BOOK)
                .transitionTo(ENCHANTED_BOOK)
                .addOutput(VILLAGER_SPELL_BOOK.get(), 1)
                .loops(3)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(DIVINE_PEARL.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(EMERALD_BLOCK))
                .addStep(FillingRecipe::new, builder -> builder.require(MANA.get(), 1000))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(DRUIDIC_SPELL_BOOK.getId())
                .require(ROTTEN_SPELL_BOOK.get())
                .transitionTo(ROTTEN_SPELL_BOOK.get())
                .addOutput(DRUIDIC_SPELL_BOOK.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(AMETHYST_SHARD))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(SPORE_BLOSSOM))
                .addStep(FillingRecipe::new, builder -> builder.require(HONEY.get(), 250))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(CURSED_DOLL_SPELLBOOK.getId())
                .require(RUINED_BOOK.get())
                .transitionTo(RUINED_BOOK.get())
                .addOutput(CURSED_DOLL_SPELLBOOK.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(BLOODY_VELLUM.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(ARCANE_INGOT.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(BLOODY_VELLUM.get()))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(ICE_SPELL_BOOK.getId())
                .require(RUINED_BOOK.get())
                .transitionTo(RUINED_BOOK.get())
                .addOutput(ICE_SPELL_BOOK.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MAGIC_CLOTH.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MITHRIL_SCRAP.get()))
                .addStep(FillingRecipe::new, builder -> builder.require(ICE_VENOM_FLUID.get(), 250))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
    }

    private void buildBlockRecipes(RecipeOutput output) {
        deploying(BOOK_STACK_BLOCK_ITEM.getId())
                .require(BOOK)              // base item on the belt
                .require(BOOK)              // item in the deployer
                .output(BOOK_STACK_BLOCK_ITEM.get())
                .build(output);
        deploying(FIREFLY_JAR_ITEM.getId())
                .require(GLASS_BOTTLE)              // base item on the belt
                .require(LANTERN)              // item in the deployer
                .output(FIREFLY_JAR_ITEM.get())
                .build(output);
        deploying(BRAZIER_ITEM.getId())
                .require(LOGS)
                .require(IRON_NUGGET)
                .output(BRAZIER_ITEM.get())
                .build(output);
        deploying(SOUL_BRAZIER_ITEM.getId())
                .require(SOUL_FIRE_BASE_BLOCKS)
                .require(IRON_NUGGET)
                .output(SOUL_BRAZIER_ITEM.get())
                .build(output);
        filling(WISEWOOD_BOOKSHELF_BLOCK_ITEM.getId())
                .require(BOOKSHELF)
                .require(MANA.get(), 10)
                .output(WISEWOOD_BOOKSHELF_BLOCK_ITEM.get())
                .build(output);
        filling(WISEWOOD_CHISELED_BOOKSHELF_BLOCK_ITEM.getId())
                .require(CHISELED_BOOKSHELF)
                .require(MANA.get(), 10)
                .output(WISEWOOD_CHISELED_BOOKSHELF_BLOCK_ITEM.get())
                .build(output);
// USEFUL BLOCKS
        sequencedAssembly(ACANE_ANVIL_BLOCK_ITEM.getId())
                .require(POLISHED_DEEPSLATE)
                .transitionTo(ANVIL)
                .addOutput(ACANE_ANVIL_BLOCK_ITEM.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(ANVIL))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(AMETHYST_BLOCK))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(DIAMOND))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(PORTAL_FRAME_ITEM.getId())
                .require(IRON_BARS)
                .transitionTo(IRON_BARS)
                .addOutput(PORTAL_FRAME_ITEM.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MITHRIL_SCRAP.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(ENDER_PEARL))
                .addStep(FillingRecipe::new, builder -> builder.require(MANA.get(), 250))
                .build(output);
        sequencedAssembly(INSCRIPTION_TABLE_BLOCK_ITEM.getId())
                .require(FENCES)
                .transitionTo(OAK_FENCE)
                .addOutput(INSCRIPTION_TABLE_BLOCK_ITEM.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(WOODEN_SLABS))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(BOOK))
                .build(output);
        sequencedAssembly(ALCHEMIST_CAULDRON_BLOCK_ITEM.getId())
                .require(CAULDRON)
                .transitionTo(CAULDRON)
                .addOutput(ALCHEMIST_CAULDRON_BLOCK_ITEM.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(IRON_INGOT))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(CINDER_ESSENCE.get()))
                .addStep(FillingRecipe::new, builder -> builder.require(MANA.get(), 250))
                .build(output);
        sequencedAssembly(SCROLL_FORGE_BLOCK.getId())
                .require(CRYING_OBSIDIAN)
                .transitionTo(CRYING_OBSIDIAN)
                .addOutput(SCROLL_FORGE_BLOCK.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(POLISHED_DEEPSLATE))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
// BOSS/MOB SUMMON BLOCKS
        sequencedAssembly(ARMOR_PILE_BLOCK_ITEM.getId())
                .require(NETHERITE_SCRAP)
                .transitionTo(NETHERITE_SCRAP)
                .addOutput(ARMOR_PILE_BLOCK_ITEM.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(CINDER_ESSENCE.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(NETHERITE_SCRAP))
                .build(output);
        sequencedAssembly(ICE_SPIDER_EGG_BLOCK_ITEM.getId())
                .require(TURTLE_EGG)
                .transitionTo(TURTLE_EGG)
                .addOutput(ICE_SPIDER_EGG_BLOCK_ITEM.get(), 15)
                .addOutput(EGG, 90)
                .addOutput(TURTLE_EGG, 30)
                .addOutput(SPIDER_EYE, 15)
                .loops(1)
                .addStep(FillingRecipe::new, builder -> builder.require(ICE_VENOM_FLUID.get(), 1000))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(SPIDER_EYE))
                .build(output);
        mechanicalCrafting(CINDEROUS_KEYSTONE_BLOCK_ITEM.get(), 1)
                .key('n', NETHERITE_INGOT)
                .key('r', FIRE_RUNE.get())
                .key('s', NETHER_STAR)
                .patternLine("nrn")
                .patternLine("rsr")
                .patternLine("nrn")
                .build(output);
    }

    private void buildStaffRecipes(RecipeOutput output) {
        manualApplication(LIGHTNING_ROD_STAFF.getId())
                .require(LIGHTNING_ROD)
                .require(ENERGIZED_CORE.get())
                .output(LIGHTNING_ROD_STAFF.get(), 1)
                .build(output);
        deploying(ICE_STAFF.getId())
                .require(FROSTED_HELVE.get())
                .require(ICE_CRYSTAL.get())
                .output(ICE_STAFF.get(), 1)
                .build(output);
        deploying(LIGHTNING_ROD_STAFF.getId())
                .require(LIGHTNING_ROD)
                .require(ENERGIZED_CORE.get())
                .output(LIGHTNING_ROD_STAFF.get(), 1)
                .build(output);

        // STAFFS
        sequencedAssembly(GRAYBEARD_STAFF.getId())
                .require(STICK)
                .transitionTo(STICK)
                .addOutput(GRAYBEARD_STAFF.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(IRON_NUGGET))
                .addStep(FillingRecipe::new, builder -> builder.require(MANA.get(), 250))
                .build(output);
        sequencedAssembly(ARTIFICER_STAFF.getId())
                .require(CRIMSON_PLANKS)
                .transitionTo(STICK)
                .addOutput(ARTIFICER_STAFF.get(), 1)
                .loops(1)
                .addStep(PressingRecipe::new, builder -> (builder))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(GOLD_INGOT))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(AMETHYST_SHARD))
                .build(output);
        sequencedAssembly(PYRIUM_STAFF.getId())
                .require(NETHERITE_SCRAP)
                .transitionTo(NETHERITE_SCRAP)
                .addOutput(PYRIUM_STAFF.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MAGIC_CLOTH.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(PYRIUM_INGOT.get()))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
    }


    private void buildWashingRecipes(RecipeOutput output) {
        splashing(CRUSHED_MITHRIL.getId())
                .require(CRUSHED_MITHRIL.get())
                .output(MITHRIL_NUGGET.get(), 9)
                .output(0.75F, ARCANE_ESSENCE.getId(), 3)
                .build(output);
    }
    private void buildCrushingRecipes(RecipeOutput output) {
        crushing(LOST_KNOWLEDGE_FRAGMENT.getId())
                .require(ELDRITCH_PAGE.get())
                .output(LOST_KNOWLEDGE_FRAGMENT.get(), 3)
                .output(0.5F, LOST_KNOWLEDGE_FRAGMENT.getId(), 1)
                .output(0.25F, LOST_KNOWLEDGE_FRAGMENT.getId(), 1)
                .build(output);
        crushing(MITHRIL_ORE_BLOCK_ITEM.getId())
                .require(MITHRIL_ORE_BLOCK_ITEM.get())
                .output(CRUSHED_MITHRIL.get(), 1)
                .output(0.75F, CRUSHED_MITHRIL.get(), 1)
                .output(0.75F, EXP_NUGGET.get(), 3)
                .output(0.125F, COBBLESTONE, 1)
                .build(output);
        crushing(MITHRIL_ORE_DEEPSLATE_BLOCK_ITEM.getId())
                .require(MITHRIL_ORE_DEEPSLATE_BLOCK_ITEM.get())
                .output(CRUSHED_MITHRIL.get(), 2)
                .output(0.25F, CRUSHED_MITHRIL.get(), 1)
                .output(0.75F, EXP_NUGGET.get(), 3)
                .output(0.125F, COBBLED_DEEPSLATE, 1)
                .build(output);
        crushing(RAW_MITHRIL.getId())
                .require(RAW_MITHRIL.get())
                .output(CRUSHED_MITHRIL.get(), 1)
                .output(0.75F, EXP_NUGGET.get(), 1)
                .build(output);
    }

    private void buildMixingRecipes(RecipeOutput output) {
        // INKS (6.0.6 restrictions - cannot be more than nine items)
        mixing(COMMON_INK.getId())
                .require(COPPER_INGOT)
                .require(COPPER_INGOT)
                .require(INK_SAC)
                .require(ARCANE_ESSENCE.get())
                .output(COMMON_INK.get(), 500)
                .build(output);

        mixing(UNCOMMON_INK.getId())
                .require(IRON_INGOT)
                .require(IRON_INGOT)
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(COMMON_INK.get(), 1000)
                .output(UNCOMMON_INK.get(), 500)
                .build(output);

        mixing(RARE_INK.getId())
                .require(GOLD_INGOT)
                .require(GOLD_INGOT)
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(EXP_NUGGET)
                .require(UNCOMMON_INK.get(), 1000)
                .output(RARE_INK.get(), 500)
                .build(output);

        mixing(EPIC_INK.getId())
                .require(DIAMOND)
                .require(DIAMOND)
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(RARE_INK.get(), 1000)
                .output(EPIC_INK.get(), 500)
                .requiresHeat(HEATED)
                .build(output);

        mixing(LEGENDARY_INK.getId())
                .require(AMETHYST_SHARD)
                .require(AMETHYST_SHARD)
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(ARCANE_ESSENCE.get())
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(EPIC_INK.get(), 1000)
                .output(LEGENDARY_INK.get(), 500)
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
        // POTIONS/ELIXIRS (FINALLY)
        mixing(OAKSKIN_ELIXIR_FLUID.getId())
                .require(PotionFluidHandler.potionIngredient(Potions.STRONG_HEALING, 1000))
                .require(OAK_LOGS)
                .output(OAKSKIN_ELIXIR_FLUID.get(), 1000)
                .requiresHeat(HEATED)
                .build(output);
        mixing(GREATER_OAKSKIN_ELIXIR_FLUID.getId())
                .require(OAKSKIN_ELIXIR_FLUID.get(), 1000)
                .require(AMETHYST_SHARD)
                .output(GREATER_OAKSKIN_ELIXIR_FLUID.get(), 1000)
                .requiresHeat(HEATED)
                .build(output);
        mixing(INVISIBILITY_ELIXIR_FLUID.getId())
                .require(PotionFluidHandler.potionIngredient(Potions.LONG_INVISIBILITY, 1000))
                .require(SHRIVING_STONE.get())
                .output(INVISIBILITY_ELIXIR_FLUID.get(), 1000)
                .requiresHeat(HEATED)
                .build(output);
        mixing(GREATER_INVISIBILITY_ELIXIR_FLUID.getId())
                .require(INVISIBILITY_ELIXIR_FLUID.get(), 1000)
                .require(AMETHYST_SHARD)
                .output(GREATER_INVISIBILITY_ELIXIR_FLUID.get(), 1000)
                .requiresHeat(HEATED)
                .build(output);
        mixing(EVASION_ELIXIR_FLUID.getId())
                .require(PotionFluidHandler.potionIngredient(PotionRegistry.INSTANT_MANA_THREE, 1000))
                .require(ENDER_PEARL)
                .output(EVASION_ELIXIR_FLUID.get(), 1000)
                .requiresHeat(HEATED)
                .build(output);
        mixing(GREATER_EVASION_ELIXIR_FLUID.getId())
                .require(EVASION_ELIXIR_FLUID.get(), 1000)
                .require(DRAGON_BREATH)
                // could be dragon's breath or phantom membrane, deciding
                .output(GREATER_EVASION_ELIXIR_FLUID.get(), 1000)
                .requiresHeat(HEATED)
                .build(output);
        mixing(GREATER_HEALING_ELIXIR_FLUID.getId())
                .require(PotionFluidHandler.potionIngredient(Potions.STRONG_HEALING, 1000))
                .require(AMETHYST_SHARD)
                .output(GREATER_HEALING_ELIXIR_FLUID.get(), 1000)
                .requiresHeat(HEATED)
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

        filling(ItemRegistry.LIGHTNING_BOTTLE.getId())
                .require(Items.GLASS_BOTTLE)
                .require(LIGHTNING.get(), 250)
                .output(ItemRegistry.LIGHTNING_BOTTLE.get())
                .build(output);
        filling(CWItems.BLOOD_BUCKET.getId())
                .require(BUCKET)
                .require(BLOOD.get(), 1000)
                .output(CWItems.BLOOD_BUCKET.get())
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
                .require(Tags.Items.FOODS_RAW_MEAT)
                .require(Tags.Items.FOODS_RAW_MEAT)
                .output(BLOOD.get(), 250)
                .build(output);
    }

    // Helper to build a "Blank Rune + 3x ingredient + Press -> Specific Rune" sequence
    private void runeSequence(RecipeOutput output,
                              ResourceLocation outId,
                              ItemLike ingredient,
                              ItemLike targetRune) {
        mixing(outId)
                .require(BLANK_RUNE.get())
                .require(ingredient)
                .require(ingredient)
                .require(ingredient)
                .require(ingredient)
                .output(targetRune, 1)
                .build(output);
    }
    private void orbSequence(RecipeOutput output,
                              ResourceLocation outId,
                              ItemLike ingredient,
                              ItemLike targetOrb) {
        mixing(outId)
                .require(UPGRADE_ORB.get())
                .require(ingredient)
                .require(ingredient)
                .require(ingredient)
                .require(ingredient)
                .output(targetOrb, 1)
                .build(output);
    }
    private void buildWeaponRecipes(RecipeOutput output) {
        // WEAPONS + WEAPON PARTS
        sequencedAssembly(ICE_GREATSWORD.getId())
                .require(WEAPON_PARTS.get())
                .transitionTo(WEAPON_PARTS.get())
                .addOutput(ICE_GREATSWORD.get(), 1)
                .loops(1)
                .addStep(FillingRecipe::new, builder -> builder.require(ICE_VENOM_FLUID.get(), 250))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(ICE))
//                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MITHRIL_INGOT.get()))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(SPELLBREAKER.getId())
                .require(WEAPON_PARTS.get())
                .transitionTo(WEAPON_PARTS.get())
                .addOutput(SPELLBREAKER.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(DIAMOND))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MITHRIL_INGOT.get()))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(TWILIGHT_GALE.getId())
                .require(WEAPON_PARTS.get())
                .transitionTo(WEAPON_PARTS.get())
                .addOutput(TWILIGHT_GALE.get(), 1)
                .loops(1)
                .addStep(FillingRecipe::new, builder -> builder.require(LIGHTNING.get(), 250))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(QUARTZ))
//                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MITHRIL_INGOT.get()))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(AMETHYST_RAPIER.getId())
                .require(WEAPON_PARTS.get())
                .transitionTo(WEAPON_PARTS.get())
                .addOutput(AMETHYST_RAPIER.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(CHAIN))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(AMETHYST_SHARD))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(HELLRAZOR.getId())
                .require(DECREPIT_SCYTHE.get())
                .transitionTo(DECREPIT_SCYTHE.get())
                .addOutput(HELLRAZOR.get(), 1)
                .loops(1)
                .addStep(FillingRecipe::new, builder -> builder.require(TIMELESS_SLURRY_FLUID.get(), 250))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(PYRIUM_INGOT.get()))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(LEGIONNAIRE_FLAMBERGE.getId())
                .require(KEEPER_FLAMBERGE.get())
                .transitionTo(KEEPER_FLAMBERGE.get())
                .addOutput(LEGIONNAIRE_FLAMBERGE.get(), 1)
                .loops(1)
                .addStep(FillingRecipe::new, builder -> builder.require(TIMELESS_SLURRY_FLUID.get(), 250))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(PYRIUM_INGOT.get()))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(KEEPER_FLAMBERGE.getId())
                .require(NETHERITE_SWORD)
                .transitionTo(NETHERITE_SWORD)
                .addOutput(KEEPER_FLAMBERGE.get(), 15)
                .addOutput(NETHERITE_INGOT, 15)
                .addOutput(NETHERITE_SWORD, 120)
                .loops(5)
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(MAGEHUNTER.getId())
                .require(IRON_SWORD)
                .transitionTo(NETHERITE_SWORD)
                .addOutput(MAGEHUNTER.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(DIAMOND))
                .addStep(FillingRecipe::new, builder -> builder.require(MANA.get(), 1000))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        mechanicalCrafting(AUTOLOADER_CROSSBOW.get(), 1)
                .key('s', STICK)
                .key('b', BRASS_INGOT)
                .key('c', CROSSBOW)
                .key('g', COGWHEEL)
                .key('p', PRECISION_MECHANISM)             // e.g., Create: andesite casing/gearbox; swap to what you prefer
                .key('r', STRING)
                .patternLine("bssp")
                .patternLine("rgcs")
                .patternLine("rsgs")
                .patternLine("srrb")
                .build(output);
    }

    private void buildCharmRecipes(RecipeOutput output) {
        sequencedAssembly(ResourceLocation.parse(String.valueOf(MANA_RING.get())))
                .require(ARCANE_INGOT.get())
                .transitionTo(ARCANE_INGOT.get())
                .addOutput(MANA_RING.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, b -> b.require(GEMS_DIAMOND))
                .addStep(PressingRecipe::new, b -> b)
                .build(output);
        sequencedAssembly(ResourceLocation.parse(String.valueOf(SILVER_RING.get())))
                .require(SILVER.ingots)
                .transitionTo(IRON_INGOT)
                .addOutput(SILVER_RING.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, b -> b.require(MITHRIL_NUGGET.get()))
                .addStep(PressingRecipe::new, b -> b)
                .build(output);
        sequencedAssembly(ResourceLocation.parse(String.valueOf(COOLDOWN_RING.get())))
                .require(INGOTS_COPPER)
                .transitionTo(COPPER_INGOT)
                .addOutput(COOLDOWN_RING.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, b -> b.require(MITHRIL_NUGGET.get()))
                .addStep(PressingRecipe::new, b -> b)
                .build(output);
        sequencedAssembly(ResourceLocation.parse(String.valueOf(CAST_TIME_RING.get())))
                .require(INGOTS_COPPER)
                .transitionTo(COPPER_INGOT)
                .addOutput(CAST_TIME_RING.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, b -> b.require(MITHRIL_NUGGET.get()))
                .addStep(DeployerApplicationRecipe::new, b -> b.require(AMETHYST_CLUSTER))
                .addStep(PressingRecipe::new, b -> b)
                .build(output);
        sequencedAssembly(ResourceLocation.parse(String.valueOf(EMERALD_STONEPLATE_RING.get())))
                .require(GOLD_INGOT)
                .transitionTo(GOLD_INGOT)
                .addOutput(EMERALD_STONEPLATE_RING.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, b -> b.require(MITHRIL_NUGGET.get()))
                .addStep(DeployerApplicationRecipe::new, b -> b.require(EXP_NUGGET))
                .addStep(PressingRecipe::new, b -> b)
                .build(output);
        sequencedAssembly(ResourceLocation.parse(String.valueOf(FIREWARD_RING.get())))
                .require(ARCANE_INGOT.get())
                .transitionTo(ARCANE_INGOT.get())
                .addOutput(FIREWARD_RING.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, b -> b.require(MITHRIL_NUGGET.get()))
                .addStep(DeployerApplicationRecipe::new, b -> b.require(CINDER_ESSENCE.get()))
                .addStep(PressingRecipe::new, b -> b)
                .build(output);
        sequencedAssembly(ResourceLocation.parse(String.valueOf(FROSTWARD_RING.get())))
                .require(ARCANE_INGOT.get())
                .transitionTo(ARCANE_INGOT.get())
                .addOutput(FROSTWARD_RING.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, b -> b.require(MITHRIL_NUGGET.get()))
                .addStep(DeployerApplicationRecipe::new, b -> b.require(ICE_CRYSTAL.get()))
                .addStep(PressingRecipe::new, b -> b)
                .build(output);
        sequencedAssembly(ResourceLocation.parse(String.valueOf(POISONWARD_RING.get())))
                .require(ARCANE_INGOT.get())
                .transitionTo(ARCANE_INGOT.get())
                .addOutput(POISONWARD_RING.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, b -> b.require(MITHRIL_NUGGET.get()))
                .addStep(DeployerApplicationRecipe::new, b -> b.require(NATURE_RUNE.get()))
                .addStep(PressingRecipe::new, b -> b)
                .build(output);
        sequencedAssembly(ResourceLocation.parse(String.valueOf(AFFINITY_RING.get())))
                .require(ARCANE_INGOT.get())
                .transitionTo(ARCANE_INGOT.get())
                .addOutput(AFFINITY_RING.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, b -> b.require(MITHRIL_NUGGET.get()))
                .addStep(DeployerApplicationRecipe::new, b -> b.require(BUCKET))
                .addStep(PressingRecipe::new, b -> b)
                .build(output);
        sequencedAssembly(ResourceLocation.parse(String.valueOf(EXPULSION_RING.get())))
                .require(ARCANE_INGOT.get())
                .transitionTo(ARCANE_INGOT.get())
                .addOutput(EXPULSION_RING.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, b -> b.require(MITHRIL_NUGGET.get()))
                .addStep(DeployerApplicationRecipe::new, b -> b.require(WIND_CHARGE))
                .addStep(PressingRecipe::new, b -> b)
                .build(output);
        sequencedAssembly(ResourceLocation.parse(String.valueOf(VISIBILITY_RING.get())))
                .require(GOLD_INGOT)
                .transitionTo(GOLD_INGOT)
                .addOutput(VISIBILITY_RING.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, b -> b.require(MITHRIL_NUGGET.get()))
                .addStep(DeployerApplicationRecipe::new, b -> b.require(SPYGLASS))
                .addStep(PressingRecipe::new, b -> b)
                .build(output);
        sequencedAssembly(ResourceLocation.parse(String.valueOf(SIGNET_OF_THE_BETRAYER.get())))
                .require(NETHERITE_INGOT)
                .transitionTo(GOLD_INGOT)
                .addOutput(SIGNET_OF_THE_BETRAYER.get(), 1)
                .loops(1)
                .addStep(FillingRecipe::new, b -> b.require(TIMELESS_SLURRY_FLUID.get(), 1000))
                .addStep(DeployerApplicationRecipe::new, b -> b.require(DIVINE_SOULSHARD.get()))
                .addStep(PressingRecipe::new, b -> b)
                .build(output);
        sequencedAssembly(ResourceLocation.parse(String.valueOf(INVISIBILITY_RING.get())))
                .require(VISIBILITY_RING.get())
                .transitionTo(VISIBILITY_RING.get())
                .addOutput(INVISIBILITY_RING.get(), 1)
                .loops(1)
                .addStep(FillingRecipe::new, b -> b.require(TIMELESS_SLURRY_FLUID.get(), 1000))
                .addStep(PressingRecipe::new, b -> b)
                .build(output);

        sequencedAssembly(ResourceLocation.parse(String.valueOf(AMETHYST_RESONANCE_NECKLACE.get())))
                .require(LEATHER)
                .transitionTo(LEATHER)
                .addOutput(AMETHYST_RESONANCE_NECKLACE.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(AMETHYST_SHARD))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(LEATHER))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        sequencedAssembly(ResourceLocation.parse(String.valueOf(HEAVY_CHAIN.get())))
                .require(CHAIN)
                .transitionTo(CHAIN)
                .addOutput(HEAVY_CHAIN.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, b -> b.require(MITHRIL_NUGGET.get()))
                .addStep(DeployerApplicationRecipe::new, b -> b.require(CHAIN))
                .addStep(PressingRecipe::new, b -> b)
                .build(output);
        sequencedAssembly(ResourceLocation.parse(String.valueOf(CONJURERS_TALISMAN.get())))
                .require(SKELETON_SKULL)
                .transitionTo(SKELETON_SKULL)
                .addOutput(CONJURERS_TALISMAN.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, b -> b.require(MITHRIL_NUGGET.get()))
                .addStep(DeployerApplicationRecipe::new, b -> b.require(STRING))
                .addStep(PressingRecipe::new, b -> b)
                .build(output);
        sequencedAssembly(ResourceLocation.parse(String.valueOf(GREATER_CONJURERS_TALISMAN.get())))
                .require(CONJURERS_TALISMAN.get())
                .transitionTo(CONJURERS_TALISMAN.get())
                .addOutput(GREATER_CONJURERS_TALISMAN.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, b -> b.require(MANA_RUNE.get()))
                .addStep(DeployerApplicationRecipe::new, b -> b.require(BLOODY_VELLUM.get()))
                .addStep(PressingRecipe::new, b -> b)
                .build(output);
        sequencedAssembly(ResourceLocation.parse(String.valueOf(CONCENTRATION_AMULET.get())))
                .require(CHAIN)
                .transitionTo(CHAIN)
                .addOutput(CONCENTRATION_AMULET.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, b -> b.require(MITHRIL_SCRAP.get()))
                .addStep(DeployerApplicationRecipe::new, b -> b.require(CHAIN))
                .addStep(PressingRecipe::new, b -> b)
                .build(output);
        sequencedAssembly(ResourceLocation.parse(String.valueOf(TELEPORTATION_AMULET.get())))
                .require(CHAIN)
                .transitionTo(CHAIN)
                .addOutput(TELEPORTATION_AMULET.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, b -> b.require(MITHRIL_NUGGET.get()))
                .addStep(DeployerApplicationRecipe::new, b -> b.require(ENDER_PEARL))
                .addStep(PressingRecipe::new, b -> b)
                .build(output);
    }

    private void buildManaRecipes(RecipeOutput output) {
        // ARCANE ESSENCE FILLING
        filling(ResourceLocation.parse(ARCANE_ESSENCE.getRegisteredName() + "_flour_filling"))
                .require(FLOURS.tag)
                .require(MANA.get(), 250)
                .output(ARCANE_ESSENCE.get())
                .build(output);
        filling(ResourceLocation.parse(ARCANE_ESSENCE.getRegisteredName() + "_sugar_filling"))
                .require(SUGAR)
                .require(MANA.get(), 250)
                .output(ARCANE_ESSENCE.get())
                .build(output);
        filling(ResourceLocation.parse(ARCANE_ESSENCE.getRegisteredName() + "_cinder_flour_filling"))
                .require(CINDER_FLOUR)
                .require(MANA.get(), 250)
                .output(ARCANE_ESSENCE.get())
                .build(output);
        // MIXING
        mixing(ResourceLocation.parse(CINDER_ESSENCE.getRegisteredName() + "_mana_recipe"))
                .require(MANA.get(), 1000)
                .require(BLAZE_POWDER)
                .require(BLAZE_POWDER)
                .require(NETHERITE_SCRAP)
                .output(CINDER_ESSENCE.get(), 8)
                .requiresHeat(SUPERHEATED)
                .build(output);
        mixing(ResourceLocation.parse(COMMON_INK.getRegisteredName() + "_mana_recipe"))
                .require(COPPER_INGOT)
                .require(COPPER_INGOT)
                .require(INK_SAC)
                .require(MANA.get(), 250)
                .output(COMMON_INK.get(), 500)
                .build(output);
        mixing(ResourceLocation.parse(UNCOMMON_INK.getRegisteredName() + "_mana_recipe"))
                .require(IRON_INGOT)
                .require(IRON_INGOT)
                .require(MANA.get(), 500)
                .require(COMMON_INK.get(), 1000)
                .output(UNCOMMON_INK.get(), 500)
                .build(output);
        mixing(ResourceLocation.parse(RARE_INK.getRegisteredName() + "_mana_recipe"))
                .require(GOLD_INGOT)
                .require(GOLD_INGOT)
                .require(MANA.get(), 750)
                .require(EXP_NUGGET)
                .require(UNCOMMON_INK.get(), 1000)
                .output(RARE_INK.get(), 500)
                .build(output);
        mixing(ResourceLocation.parse(EPIC_INK.getRegisteredName() + "_mana_recipe"))
                .require(DIAMOND)
                .require(DIAMOND)
                .require(MANA.get(), 1000)
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(RARE_INK.get(), 1000)
                .output(EPIC_INK.get(), 500)
                .requiresHeat(HEATED)
                .build(output);
        mixing(ResourceLocation.parse(LEGENDARY_INK.getRegisteredName() + "_mana_recipe"))
                .require(AMETHYST_SHARD)
                .require(AMETHYST_SHARD)
                .require(MANA.get(), 1000)
                .require(ARCANE_ESSENCE.get())
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(EPIC_INK.get(), 1000)
                .output(LEGENDARY_INK.get(), 500)
                .requiresHeat(SUPERHEATED)
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

    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }
}