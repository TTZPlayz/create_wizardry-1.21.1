package net.ttzplayz.create_wizardry;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import io.redspace.ironsspellbooks.fluids.SimpleClientFluidType;
import io.redspace.ironsspellbooks.fluids.SimpleTintedClientFluidType;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.EntityRenderersEvent;
//import net.minecraftforge.client.event.RegisterClientExtensionsEvent; //todo
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.GameData;
import net.ttzplayz.create_wizardry.advancement.CWAdvancements;
//import net.ttzplayz.create_wizardry.advancement.CWTriggers;
import net.ttzplayz.create_wizardry.block.CWBlocks;
import net.ttzplayz.create_wizardry.block.entity.CWBlockEntities;
import net.ttzplayz.create_wizardry.block.entity.renderer.ChannelerRenderer;
import net.ttzplayz.create_wizardry.advancement.CWBuiltInTriggers;
import net.ttzplayz.create_wizardry.fluids.CWEffectHandlers;
import net.ttzplayz.create_wizardry.fluids.CWFluidRegistry;
import net.ttzplayz.create_wizardry.item.CWItems;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import java.util.Map;
import java.util.Set;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.simibubi.create.AllBlocks.STEAM_WHISTLE;
import static io.redspace.ironsspellbooks.registries.CreativeTabRegistry.MATERIALS_TAB;
import static io.redspace.ironsspellbooks.registries.FluidRegistry.ICE_VENOM_FLUID;
import static io.redspace.ironsspellbooks.registries.ItemRegistry.MITHRIL_SCRAP;
import static io.redspace.ironsspellbooks.registries.ItemRegistry.RAW_MITHRIL;
import static net.minecraft.world.item.CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
import static net.ttzplayz.create_wizardry.block.CWBlocks.CHANNELER;
import static net.ttzplayz.create_wizardry.fluids.CWFluidRegistry.*;
import static net.ttzplayz.create_wizardry.item.CWItems.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreateWizardry.MOD_ID)
public class CreateWizardry {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "create_wizardry";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String CW_CREEPER_CHARGE_COUNT = "cw_charged_creepers";
    private static final int MAX_CHARGED_CREEPERS_PER_BOLT = 4;
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace


    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public CreateWizardry() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        CWFluidRegistry.register(modEventBus);
        CWBlocks.register(modEventBus);
        CWBlockEntities.register(modEventBus);
        CWItems.register(modEventBus);
        CWAdvancements.registerTriggers();
        CWBuiltInTriggers.register();

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
//        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        //TODO: Make Config
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> OpenPipeEffectHandler.REGISTRY.register(MANA.get(), new CWEffectHandlers.ManaEffectHandler()));
        event.enqueueWork(() -> OpenPipeEffectHandler.REGISTRY.register(LIGHTNING.get(), new CWEffectHandlers.LightningEffectHandler()));
        event.enqueueWork(() -> OpenPipeEffectHandler.REGISTRY.register(FIRE_ALE_FLUID.get(), new CWEffectHandlers.FireAleEffectHandler()));
        event.enqueueWork(() -> OpenPipeEffectHandler.REGISTRY.register(NETHERWARD_TINCTURE_FLUID.get(), new CWEffectHandlers.NetherwardEffectHandler()));
        event.enqueueWork(() -> OpenPipeEffectHandler.REGISTRY.register(ICE_VENOM_FLUID.get(), new CWEffectHandlers.IceVenomEffectHandler()));

        event.enqueueWork(() -> {
            Holder<PoiType> lightningRod =
                    ForgeRegistries.POI_TYPES.getDelegateOrThrow(PoiTypes.LIGHTNING_ROD);
            Map<BlockState, PoiType> poiMap = GameData.getBlockStatePointOfInterestTypeMap();
            for (BlockState state : CHANNELER.get().getStateDefinition().getPossibleStates()) {
                PoiType existing = poiMap.putIfAbsent(state, lightningRod.value());
                if (existing != null && existing != lightningRod.value()) {
                    throw new IllegalStateException("Channeler state already assigned to POI: " + existing);
                }
            }
        });
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == MATERIALS_TAB.getKey()) {
            event.accept(CRUSHED_MITHRIL.get().getDefaultInstance(), PARENT_AND_SEARCH_TABS);
            event.accept(MITHRIL_NUGGET.get().getDefaultInstance(), PARENT_AND_SEARCH_TABS);
            event.accept(MANA_BUCKET.get(), PARENT_AND_SEARCH_TABS);
            event.accept(LIGHTNING_BUCKET.get(),  PARENT_AND_SEARCH_TABS);
            event.accept(BLOOD_BUCKET.get(),  PARENT_AND_SEARCH_TABS);
        }
        if (event.getTabKey() == AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey()) {
            event.accept(CHANNELER.get(), PARENT_AND_SEARCH_TABS);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @SubscribeEvent
    public void onLightningStrike(EntityStruckByLightningEvent event) {
        if (!(event.getEntity() instanceof Creeper)) {
            return;
        }
        var data = event.getLightning().getPersistentData();
        int charged = data.getInt(CW_CREEPER_CHARGE_COUNT);
        if (charged >= MAX_CHARGED_CREEPERS_PER_BOLT) {
            event.setCanceled(true);
            return;
        }
        data.putInt(CW_CREEPER_CHARGE_COUNT, charged + 1);
    }
    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
//            ItemBlockRenderTypes.setRenderLayer(LIGHTNING.get(), RenderType.lightning());
        }

//        @SubscribeEvent
//        public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
//            event.registerFluidType(new SimpleClientFluidType(CreateWizardry.id("block/mana")), CWFluidRegistry.MANA_TYPE.get());
//            event.registerFluidType(new SimpleClientFluidType(CreateWizardry.id("block/lightning")), CWFluidRegistry.LIGHTNING_TYPE.get());
//            event.registerFluidType(new SimpleTintedClientFluidType(ResourceLocation.withDefaultNamespace( "block/water_still"), 0x00831312), FIRE_ALE_TYPE.get());
//            event.registerFluidType(new SimpleTintedClientFluidType(ResourceLocation.withDefaultNamespace( "block/milk_still"), 0x00D69D84), NETHERWARD_TINCTURE_TYPE.get());
//        } //moved to fluid classes in 1.20.1

        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(CWBlockEntities.CHANNELER_BE.get(), ChannelerRenderer::new);
        }

    }
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}
