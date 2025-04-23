package net.ttzplayz.create_wizardry.datagen;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.ttzplayz.create_wizardry.CreateWizardry;

@EventBusSubscriber(modid = CreateWizardry.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    public DataGenerators(IEventBus modBus) {
        if (!DatagenModLoader.isRunningDataGen())
            return;
        modBus.register(this);
    }
    @SubscribeEvent
    public static void generate(final GatherDataEvent event) {
        var generator = event.getGenerator();
        event.getExistingFileHelper();
        var lookupProvider = event.getLookupProvider();
        var output = generator.getPackOutput();
        event.includeClient();
        var server = event.includeServer();

        generator.addProvider(server, new CreateWizardryRecipeProvider(output, lookupProvider));

    }
}
