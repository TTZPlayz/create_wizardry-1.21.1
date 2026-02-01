package net.ttzplayz.create_wizardry.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.ttzplayz.create_wizardry.CreateWizardry;
import net.ttzplayz.create_wizardry.advancement.CWAdvancements;
import net.ttzplayz.create_wizardry.datagen.recipe.CWRecipeProvider;
import net.ttzplayz.create_wizardry.datagen.CWLangProvider;

import java.util.Collections;
import java.util.List;

@EventBusSubscriber(modid = CreateWizardry.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CWDataGenerators {
    public CWDataGenerators(IEventBus modBus) {
        if (!DatagenModLoader.isRunningDataGen())
            return;
        modBus.register(this);
    }
    @SubscribeEvent
    public static void generate(final GatherDataEvent event) {
        var generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        var lookupProvider = event.getLookupProvider();
        var output = generator.getPackOutput();
        event.includeClient();
        var server = event.includeServer();
        PackOutput packOutput = generator.getPackOutput();

        generator.addProvider(server, new CWRecipeProvider(output, lookupProvider));
        generator.addProvider(event.includeClient(), new CWItemModelProvider(packOutput, existingFileHelper));
        BlockTagsProvider blockTagsProvider = new CWBlockTagProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeClient(), new CWBlockstateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new CWItemTagProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));

        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(CWBlockDropsProvider::new, LootContextParamSets.BLOCK)), lookupProvider));

        generator.addProvider(server, new CWAdvancements(output, lookupProvider));
        generator.addProvider(event.includeClient(), new CWLangProvider(packOutput, "en_us"));

    }
}
