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

package net.ttzplayz.create_wizardry.datagen.recipe;

import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.registries.PotionRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.crafting.SizedFluidIngredient;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.simibubi.create.AllBlocks.COGWHEEL;
import static com.simibubi.create.AllFluids.*;
import static com.simibubi.create.AllItems.*;
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
import static net.minecraftforge.common.Tags.Fluids.EXPERIENCE;
import static net.minecraftforge.common.Tags.Items.*;
import static net.minecraftforge.common.Tags.Items.FENCES;
import static net.ttzplayz.create_wizardry.block.CWBlocks.CHANNELER;
import static net.ttzplayz.create_wizardry.fluids.CWFluidRegistry.*;
import static net.ttzplayz.create_wizardry.item.CWItems.*;
import static net.ttzplayz.create_wizardry.datagen.recipe.CreateRecipeHelpers.*;

public class CWRecipeProvider extends RecipeProvider {

    public CWRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    private static ResourceLocation itemId(ItemLike item) {
        return net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item.asItem());
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        buildFillingRecipes(output);
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
        buildVanillaRecipes(output);
        buildMechanicalRecipes(output);
        buildSummoningRecipes(output);
    }
    private void buildVanillaRecipes(RecipeOutput output) {
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
    }
    private void buildMechanicalRecipes(RecipeOutput output) {
        mechanicalCrafting(CHANNELER.get(), 1)
                .patternLine(" E ")
                .patternLine("IHI")
                .patternLine("CBC")
                .key('C', COPPER_SHEET)
                .key('B', LIGHTNING_BOTTLE.get())
                .key('E', ENERGIZED_CORE.get())
                .key('H', HOPPER)
                .key('I', IRON_SHEET)
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
        runeSequence(output, POISONOUS_POTATO, NATURE_RUNE.get());
        runeSequence(output, BLAZE_ROD, FIRE_RUNE.get());
        runeSequence(output, EMERALD, EVOCATION_RUNE.get());
        runeSequence(output, FROZEN_BONE_SHARD.get(), ICE_RUNE.get());
        runeSequence(output, DIVINE_PEARL.get(), HOLY_RUNE.get());
        runeSequence(output, ENDER_PEARL, ENDER_RUNE.get());
        runeSequence(output, PHANTOM_MEMBRANE, COOLDOWN_RUNE.get());
        runeSequence(output, PUFFERFISH, PROTECTION_RUNE.get());

        runeFilling(output, MANA_RUNE.get(), MANA.get());
        runeFilling(output, BLOOD_RUNE.get(), BLOOD.get());
        runeFilling(output, LIGHTNING_RUNE.get(), LIGHTNING.get());
        itemFilling(output, ICE_RUNE.get(), BLANK_RUNE.get(), ICE_VENOM_FLUID.get(), 250);

        orbSequence(output, NATURE_RUNE.get(), NATURE_UPGRADE_ORB.get());
        orbSequence(output, FIRE_RUNE.get(), FIRE_UPGRADE_ORB.get());
        orbSequence(output, EVOCATION_RUNE.get(), EVOCATION_UPGRADE_ORB.get());
        orbSequence(output, ICE_RUNE.get(), ICE_UPGRADE_ORB.get());
        orbSequence(output, HOLY_RUNE.get(), HOLY_UPGRADE_ORB.get());
        orbSequence(output, ENDER_RUNE.get(), ENDER_UPGRADE_ORB.get());
        orbSequence(output, MANA_RUNE.get(), MANA_UPGRADE_ORB.get());
        orbSequence(output, BLOOD_RUNE.get(), BLOOD_UPGRADE_ORB.get());
        orbSequence(output, LIGHTNING_RUNE.get(), LIGHTNING_UPGRADE_ORB.get());
        orbSequence(output, COOLDOWN_RUNE.get(), COOLDOWN_UPGRADE_ORB.get());
        orbSequence(output, PROTECTION_RUNE.get(), PROTECTION_UPGRADE_ORB.get());
    }

    private void buildArmorRecipes(RecipeOutput output) {
        armorDeploying(output, FIRE_RUNE.get(), "pyromancer");
        armorDeploying(output, ICE_RUNE.get(), "cryomancer");
        armorDeploying(output, HOLY_RUNE.get(), "priest");
        armorDeploying(output, LIGHTNING_RUNE.get(), "electromancer");
        armorDeploying(output, BLOOD_RUNE.get(), "cultist");
        armorDeploying(output, EVOCATION_RUNE.get(), "archevoker");
        armorDeploying(output, ENDER_RUNE.get(), "shadowwalker");
        armorDeploying(output, NATURE_RUNE.get(), "plagued");
        armorDeploying(output, NETHERITE_INGOT, "netherite_mage");
        armorFilling(output, "wandering_magician");

        manaFilling(output, PUMPKIN_BOOTS.get(), MAGIC_CLOTH.get(), 500);
        manaFilling(output, PUMPKIN_CHESTPLATE.get(), LEATHER, 500);
        manaFilling(output, PUMPKIN_HELMET.get(), CARVED_PUMPKIN, 500);
        manaFilling(output, PUMPKIN_LEGGINGS.get(), HAY_BLOCK, 500);
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
        manaFilling(output, ARCANE_ESSENCE.get(), DUSTS, 250);
        manaFilling(output, ARCANE_INGOT.get(), INGOTS, 1000);
        manaFilling(output, MAGIC_CLOTH.get(), WOOL, 1000);
        itemFilling(output, ENERGIZED_CORE.get(), COPPER_BLOCK, LIGHTNING.get(), 1000);
        itemFilling(output, ICY_FANG.get(), FROZEN_BONE_SHARD.get(), ICE_VENOM_FLUID.get(), 500);
        itemFilling(output, FROZEN_BONE_SHARD.get(), BONES, ICE_VENOM_FLUID.get(), 500);
        itemFilling(output, CRYING_OBSIDIAN, OBSIDIAN, EVASION_ELIXIR_FLUID.get(), 500);
        itemFilling(output, BLOODY_VELLUM.get(), HOGSKIN.get(), BLOOD.get(), 500);
        haunting(HOGSKIN.getId())
                .require(LEATHER)
                .output(HOGSKIN.get())
                .build(output);
        // SEQUENCED
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
        sequencedAssembly(ANCIENT_FURLED_MAP.getId())
                .require(PAPER)
                .transitionTo(PAPER)
                .addOutput(ANCIENT_FURLED_MAP.get(), 1)
                .loops(1)
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(LOST_KNOWLEDGE_FRAGMENT.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(CINDER_ESSENCE.get()))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);
        // BOSS/MOB SUMMON ITEMS
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
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MITHRIL_SCRAP.get()))
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MAGIC_CLOTH.get()))
                .addStep(FillingRecipe::new, builder -> builder.require(ICE_VENOM_FLUID.get(), 250))
                .addStep(PressingRecipe::new, builder -> (builder))
                .build(output);

        //TODO: make a method to simplify
    }

    private void buildSummoningRecipes(RecipeOutput output) {
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

    private void buildBlockRecipes(RecipeOutput output) {
        baseDeployingRecipe(output, BOOK_STACK_BLOCK_ITEM.get(), BOOK, BOOK);
        baseDeployingRecipe(output, FIREFLY_JAR_ITEM.get(), GLASS_BOTTLE, LANTERN);
        baseDeployingRecipe(output, BRAZIER_ITEM.get(), LOGS, IRON_NUGGET);
        baseDeployingRecipe(output, SOUL_BRAZIER_ITEM.get(), SOUL_FIRE_BASE_BLOCKS, IRON_NUGGET);
        manaFilling(output, WISEWOOD_BOOKSHELF_BLOCK_ITEM.get(), BOOKSHELVES, 10);
        manaFilling(output, WISEWOOD_CHISELED_BOOKSHELF_BLOCK_ITEM.get(), CHISELED_BOOKSHELF, 10);
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
    }

    private void buildStaffRecipes(RecipeOutput output) {
        manualApplication(LIGHTNING_ROD_STAFF.getId())
                .require(LIGHTNING_ROD)
                .require(ENERGIZED_CORE.get())
                .output(LIGHTNING_ROD_STAFF.get(), 1)
                .build(output);
        baseDeployingRecipe(output, LIGHTNING_ROD_STAFF.get(), LIGHTNING_ROD, ENERGIZED_CORE.get());
        baseDeployingRecipe(output, ICE_STAFF.get(), FROSTED_HELVE.get(), ICE_CRYSTAL.get());
        // OTHER STAFFS
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
        // TODO: Simplify into potionRecipe() method
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
        // TODO: 0.5.0/1.0.0: Mana recipes will only produce 100 mana (2.5 arcane essence per)
        // basically, every recipe will be divided by 10
        // this is for the **Blaze Caster**, which will accept up to 500 mana (equivalent to maxed-out player)
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
                .require(PotionFluidHandler.potionIngredient(Potions.MUNDANE,  1000))
                .require(ECHO_SHARD)
                .output(TIMELESS_SLURRY_FLUID.get(), 1000)
                .requiresHeat(SUPERHEATED)
                .build(output);
        mixing(FIRE_ALE_FLUID.getId())
                .require(WATER, 1000)
                .require(WHEAT)
                .require(APPLE)
                .require(MAGMA_CREAM)
                .require(MUSHROOMS)
                .output(FIRE_ALE_FLUID.get(), 500)
                .requiresHeat(HEATED)
                .build(output);
        mixing(NETHERWARD_TINCTURE_FLUID.getId())
                .require(PotionFluidHandler.potionIngredient(Potions.MUNDANE,  1000))
                .require(NETHER_WART)
                .require(NETHER_WART)
                .require(BLAZE_POWDER)
                .require(GOLD_NUGGET)
                .output(NETHERWARD_TINCTURE_FLUID.get(), 250)
                .requiresHeat(SUPERHEATED)
                .build(output);
    }

    private void buildFillingRecipes(RecipeOutput output) {
        // INK
        bottleEmptyingAndFilling(output, COMMON_INK.get(), INK_COMMON.get());
        bottleEmptyingAndFilling(output, UNCOMMON_INK.get(), INK_UNCOMMON.get());
        bottleEmptyingAndFilling(output, RARE_INK.get(), INK_RARE.get());
        bottleEmptyingAndFilling(output, EPIC_INK.get(), INK_EPIC.get());
        bottleEmptyingAndFilling(output, LEGENDARY_INK.get(), INK_LEGENDARY.get());
        // POTIONS
        bottleEmptyingAndFilling(output, OAKSKIN_ELIXIR_FLUID.get(), OAKSKIN_ELIXIR.get());
        bottleEmptyingAndFilling(output, GREATER_OAKSKIN_ELIXIR_FLUID.get(), GREATER_OAKSKIN_ELIXIR.get());
        bottleEmptyingAndFilling(output, INVISIBILITY_ELIXIR_FLUID.get(), INVISIBILITY_ELIXIR.get());
        bottleEmptyingAndFilling(output, GREATER_INVISIBILITY_ELIXIR_FLUID.get(), GREATER_INVISIBILITY_ELIXIR.get());
        bottleEmptyingAndFilling(output, EVASION_ELIXIR_FLUID.get(), EVASION_ELIXIR.get());
        bottleEmptyingAndFilling(output, GREATER_EVASION_ELIXIR_FLUID.get(), GREATER_EVASION_ELIXIR.get());
        bottleEmptyingAndFilling(output, GREATER_HEALING_ELIXIR_FLUID.get(), GREATER_HEALING_POTION.get());
        // OTHER FLUIDS
        bottleEmptyingAndFilling(output, BLOOD.get(), BLOOD_VIAL.get());
        bottleEmptyingAndFilling(output, LIGHTNING.get(), LIGHTNING_BOTTLE.get());
        bottleEmptyingAndFilling(output, ICE_VENOM_FLUID.get(), ICE_VENOM_VIAL.get());
        bottleEmptyingAndFilling(output, TIMELESS_SLURRY_FLUID.get(), TIMELESS_SLURRY.get());
        bucketEmptyingAndFilling(output, BLOOD.get(), BLOOD_BUCKET.get());
        bottleEmptyingAndFilling(output, FIRE_ALE_FLUID.get(), FIRE_ALE.get());
        bottleEmptyingAndFilling(output, NETHERWARD_TINCTURE_FLUID.get(), NETHERWARD_TINCTURE.get());
    }
    private void buildCompactingRecipes(RecipeOutput output) {
        compacting(BLOOD.getId())
                .require(Tags.Items.FOODS_RAW_MEAT)
                .require(Tags.Items.FOODS_RAW_MEAT)
                .require(Tags.Items.FOODS_RAW_MEAT)
                .output(BLOOD.get(), 250)
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
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MITHRIL_INGOT.get())) //TODO
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
                .addStep(DeployerApplicationRecipe::new, builder -> builder.require(MITHRIL_INGOT.get())) //TODO
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
        manaFillingWithItem(output, ARCANE_ESSENCE.get(), FLOURS.tag, "flour");
        manaFillingWithItem(output, ARCANE_ESSENCE.get(), SUGAR, "sugar");
        manaFillingWithItem(output, ARCANE_ESSENCE.get(), CINDER_FLOUR, "cinder_flour");
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

    // HELPERS
    private void runeSequence(RecipeOutput output,
                              ItemLike focus, ItemLike targetRune) {
        mixing(ResourceLocation.parse(String.valueOf(targetRune)))
                .require(BLANK_RUNE.get())
                .require(focus)
                .require(focus)
                .require(focus)
                .require(focus)
                .output(targetRune, 1)
                .build(output);
    }
    private void runeFilling(RecipeOutput output, ItemLike rune, FlowingFluid fluid) {
        filling(itemId(rune))
                .require(BLANK_RUNE.get())
                .require(SizedFluidIngredient.of(fluid, 1000))
                .output(rune)
                .build(output);
    }
    private void orbSequence(RecipeOutput output,
                             ItemLike rune, ItemLike targetOrb) {
        mixing(ResourceLocation.parse(String.valueOf(targetOrb)))
                .require(UPGRADE_ORB.get())
                .require(rune)
                .require(rune)
                .require(rune)
                .require(rune)
                .output(targetOrb, 1)
                .build(output);
    }
    private void bottleEmptyingAndFilling(RecipeOutput output, FlowingFluid fluid, ItemLike vesselItem) {
        filling(ResourceLocation.parse(String.valueOf(vesselItem)))
                .require(GLASS_BOTTLE)
                .require(SizedFluidIngredient.of(fluid, 250))
                .output(vesselItem, 1)
                .build(output);
        emptying(ResourceLocation.parse(String.valueOf(vesselItem)))
                .require(vesselItem)
                .output(GLASS_BOTTLE, 1)
                .output(fluid, 250)
                .build(output);
    }
    private void bucketEmptyingAndFilling(RecipeOutput output, FlowingFluid fluid, ItemLike vesselItem) {
        filling(ResourceLocation.parse(String.valueOf(vesselItem)))
                .require(BUCKETS)
                .require(SizedFluidIngredient.of(fluid, 1000))
                .output(vesselItem, 1)
                .build(output);
        emptying(ResourceLocation.parse(String.valueOf(vesselItem)))
                .require(vesselItem)
                .output(BUCKET, 1)
                .output(fluid, 1000)
                .build(output);
    }
    private void itemFilling(RecipeOutput output, ItemLike result, ItemLike input, FlowingFluid fluid, int mb) {
        filling(itemId(result))
                .require(input)
                .require(SizedFluidIngredient.of(fluid, mb))
                .output(result)
                .build(output);
    }

    private void baseDeployingRecipe(RecipeOutput output, ItemLike result, ItemLike input, ItemLike deployedItem) {
        deploying(itemId(result))
                .require(input)
                .require(deployedItem)
                .output(result)
                .build(output);
    }
    private void baseDeployingRecipe(RecipeOutput output, ItemLike result, TagKey<Item> input, ItemLike deployedItem) {
        deploying(itemId(result))
                .require(input)
                .require(deployedItem)
                .output(result)
                .build(output);
    }
    private void armorDeploying(RecipeOutput output, ItemLike deployedItem, String prefix) {
        var armors = new Item[]{WIZARD_BOOTS.get(),
                WIZARD_LEGGINGS.get(),
                WIZARD_CHESTPLATE.get(), WIZARD_HELMET.get()};
        for (Item baseArmor : armors) {
            ArmorItem.Type armorType = ((ArmorItem) baseArmor).getType();
            ResourceLocation itemId = new ResourceLocation(IronsSpellbooks.MODID,
                    String.format(prefix + "_" + armorType.getName()));
            ItemStack result = BuiltInRegistries.ITEM.get(itemId).getDefaultInstance();
            baseDeployingRecipe(output, result.getItem(), baseArmor, deployedItem);
        }
    }
    private void manaFilling(RecipeOutput output, ItemLike result, ItemLike input, int mb) {
        itemFilling(output, result, input, MANA.get(), mb);
    }
    private void manaFilling(RecipeOutput output, ItemLike result, TagKey<Item> input, int mb) {
        itemFilling(output, result, input, MANA.get(), mb);
    }
    private void manaFillingWithItem(RecipeOutput output, ItemLike result, ItemLike input, String prefix) {
        ResourceLocation itemId1 = ResourceLocation.parse(result + "_" + prefix + "_" + "filling");
        filling(itemId1)
                .require(input)
                .require(MANA.get(), 250)
                .output(result)
                .build(output);
    }
    private void manaFillingWithItem(RecipeOutput output, ItemLike result, TagKey<Item> input, String prefix) {
        ResourceLocation itemId1 = ResourceLocation.parse(result + "_" + prefix + "_" + "filling");
        filling(itemId1)
                .require(input)
                .require(MANA.get(), 250)
                .output(result)
                .build(output);
    }
    private void itemFilling(RecipeOutput output, ItemLike result, TagKey<Item> input, FlowingFluid fluid, int mb) {
        filling(itemId(result))
                .require(input)
                .require(SizedFluidIngredient.of(fluid, mb))
                .output(result)
                .build(output);
    }
    private void armorFilling(RecipeOutput output, String armorName) {
        var leather_armors = new Item[]{LEATHER_BOOTS, LEATHER_LEGGINGS, LEATHER_CHESTPLATE, LEATHER_HELMET};
        for (Item baseArmor : leather_armors) {
            ResourceLocation itemId = new ResourceLocation(IronsSpellbooks.MODID, String.format(armorName + "_" + ((ArmorItem) baseArmor).getType().getName()));
            ItemStack result = BuiltInRegistries.ITEM.get(itemId).getDefaultInstance();
            manaFilling(output, result.getItem(), baseArmor, 250);
        }
    }
}
