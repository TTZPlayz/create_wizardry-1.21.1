package net.ttzplayz.create_wizardry;

import io.redspace.ironsspellbooks.fluids.SimpleClientFluidType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.ttzplayz.create_wizardry.block.CWBlocks;
import net.ttzplayz.create_wizardry.block.entity.ModBlockEntities;
import net.ttzplayz.create_wizardry.fluids.CWFluidRegistry;
import net.ttzplayz.create_wizardry.item.CWItems;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import static io.redspace.ironsspellbooks.registries.CreativeTabRegistry.MATERIALS_TAB;
import static io.redspace.ironsspellbooks.registries.ItemRegistry.MITHRIL_SCRAP;
import static io.redspace.ironsspellbooks.registries.ItemRegistry.RAW_MITHRIL;
import static net.minecraft.world.item.CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
import static net.ttzplayz.create_wizardry.item.CWItems.*;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CreateWizardry.MOD_ID)
public class CreateWizardry {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "create_wizardry";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace


    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public CreateWizardry(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        CWFluidRegistry.register(modEventBus);

        CWBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        CWItems.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == MATERIALS_TAB.getKey()) {
            event.insertAfter(RAW_MITHRIL.get().getDefaultInstance(), CRUSHED_MITHRIL.get().getDefaultInstance(), PARENT_AND_SEARCH_TABS);
            event.insertAfter(MITHRIL_SCRAP.get().getDefaultInstance(), MITHRIL_NUGGET.get().getDefaultInstance(), PARENT_AND_SEARCH_TABS);
            event.accept(MANA_BUCKET.get());
            event.accept(LIGHTNING_BUCKET.get());
            event.accept(BLOOD_BUCKET.get());
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {


        }

        @SubscribeEvent
        public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
            event.registerFluidType(new SimpleClientFluidType(CreateWizardry.id("block/mana")), CWFluidRegistry.MANA_TYPE);
            event.registerFluidType(new SimpleClientFluidType(CreateWizardry.id("block/lightning")), CWFluidRegistry.LIGHTNING_TYPE);
        }

    }
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
