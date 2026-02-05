package net.ttzplayz.create_wizardry.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.ttzplayz.create_wizardry.CreateWizardry;

public class CWBlockstateProvider extends BlockStateProvider {
    public CWBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CreateWizardry.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }
}
