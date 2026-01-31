package net.ttzplayz.create_wizardry.datagen.recipe;

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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;


public class CreateRecipeHelpers {

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
