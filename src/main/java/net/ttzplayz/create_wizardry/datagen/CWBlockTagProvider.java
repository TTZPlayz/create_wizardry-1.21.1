package net.ttzplayz.create_wizardry.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.ttzplayz.create_wizardry.CreateWizardry;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CWBlockTagProvider extends BlockTagsProvider {
    public CWBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CreateWizardry.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

    }
}
