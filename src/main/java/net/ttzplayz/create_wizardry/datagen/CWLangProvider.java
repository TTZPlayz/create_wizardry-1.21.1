package net.ttzplayz.create_wizardry.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.ttzplayz.create_wizardry.CreateWizardry;
import net.ttzplayz.create_wizardry.advancement.CWAdvancements;

public class CWLangProvider extends LanguageProvider {
    public CWLangProvider(PackOutput output, String locale) {
        super(output, CreateWizardry.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        CWAdvancements.provideLang(this::add);
        add("fluid_type.create_wizardry.lightning_type", "Liquid Lightning");
        add("fluid_type.create_wizardry.mana_type", "Mana");
        add("fluid_type.create_wizardry.fire_ale_type", "Fire Ale");
        add("fluid_type.create_wizardry.netherward_tincture_type", "Netherward Tincture");
        add("item.create_wizardry.crushed_mithril", "Crushed Raw Mithril");
        add("item.create_wizardry.mithril_nugget", "Mithril Nugget");
        add("item.create_wizardry.mana_bucket", "Bucket of Mana");
        add("item.create_wizardry.mana_bottle", "Bottle of Mana");
        add("item.create_wizardry.lightning_bucket", "Bucket o' Liquid Lightning");
        add("item.create_wizardry.blood_bucket", "Bucket o' Blood");
        add("item.create_wizardry.channeler", "Channeler");
        add("block.create_wizardry.channeler", "Channeler");
        add("jei.create_wizardry.liquid_lightning",
                "An extremely volatile fluid that is charged with electric energy. Can be obtained by channeling lightning bolts using the Channeler block or by extracting from charged creepers in a bottle.");
        add("jei.create_wizardry.mana",
                "The liquid essence of magic. Can be used to form arcane essence when combined with dust.");
        add("jei.create_wizardry.blood",
                "The lifeblood of all organisms. Can be obtained by compressing meat or boiling mobs.");
        //TODO: make easier in a single method type
    }
}
