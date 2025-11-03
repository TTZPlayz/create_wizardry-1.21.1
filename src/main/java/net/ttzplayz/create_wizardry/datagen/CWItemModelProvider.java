package net.ttzplayz.create_wizardry.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.ttzplayz.create_wizardry.CreateWizardry;
import net.ttzplayz.create_wizardry.item.CWItems;

public class CWItemModelProvider extends ItemModelProvider {

    public CWItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CreateWizardry.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(CWItems.CRUSHED_MITHRIL.get());
        basicItem(CWItems.MITHRIL_NUGGET.get());
        basicItem(CWItems.MANA_BUCKET.get());
        basicItem(CWItems.LIGHTNING_BUCKET.get());
        basicItem(CWItems.BLOOD_BUCKET.get());
    }
}
