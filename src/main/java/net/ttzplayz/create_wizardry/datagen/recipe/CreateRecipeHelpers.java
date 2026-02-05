package net.ttzplayz.create_wizardry.datagen.recipe;

import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeBuilder;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;


public class CreateRecipeHelpers {

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

    public static SequencedAssemblyRecipeBuilder sequencedAssembly(ResourceLocation id) {
        return new SequencedAssemblyRecipeBuilder(id);
    }

    public static MechanicalCraftingRecipeBuilder mechanicalCrafting(ItemLike item, int count) {
        return new MechanicalCraftingRecipeBuilder(item, count);
    }
}
