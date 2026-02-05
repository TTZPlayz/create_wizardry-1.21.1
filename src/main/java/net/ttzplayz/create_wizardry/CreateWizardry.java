package net.ttzplayz.create_wizardry;

import com.simibubi.create.AllContraptionTypes;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import com.simibubi.create.content.equipment.potatoCannon.AllPotatoProjectileBlockHitActions;
import com.simibubi.create.content.equipment.potatoCannon.AllPotatoProjectileEntityHitActions;
import com.simibubi.create.content.equipment.potatoCannon.AllPotatoProjectileRenderModes;
import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
import com.simibubi.create.content.kinetics.mechanicalArm.AllArmInteractionPointTypes;
import com.simibubi.create.content.logistics.item.filter.attribute.AllItemAttributeTypes;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.AllTriggers;
import com.simibubi.create.foundation.render.RenderTypes;
import io.redspace.ironsspellbooks.fluids.SimpleClientFluidType;
import io.redspace.ironsspellbooks.fluids.SimpleTintedClientFluidType;
import io.redspace.ironsspellbooks.registries.FluidRegistry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientExtensionsEvent;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.RegisterEvent;
import net.ttzplayz.create_wizardry.advancement.CWAdvancements;
import net.ttzplayz.create_wizardry.advancement.CWTriggers;
import net.ttzplayz.create_wizardry.block.CWBlocks;
import net.ttzplayz.create_wizardry.block.entity.CWBlockEntities;
import net.ttzplayz.create_wizardry.block.entity.ChannelerBlockEntity;
import net.ttzplayz.create_wizardry.block.entity.renderer.ChannelerRenderer;
import net.ttzplayz.create_wizardry.advancement.CWAdvancements;
import net.ttzplayz.create_wizardry.advancement.CWBuiltInTriggers;
import net.ttzplayz.create_wizardry.event.CWEvents;
import net.ttzplayz.create_wizardry.fluids.CWEffectHandlers;
import net.ttzplayz.create_wizardry.fluids.CWFluidRegistry;
import net.ttzplayz.create_wizardry.item.CWItems;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

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
    public CreateWizardry(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(CWEvents::registerCapabilities);

        MinecraftForge.EVENT_BUS.register(this);

        CWFluidRegistry.register(modEventBus);
        CWBlocks.register(modEventBus);
        CWBlockEntities.register(modEventBus);
        CWItems.register(modEventBus);
        CWBuiltInTriggers.register(modEventBus);
        CWAdvancements.registerTriggers();

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
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
                    BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolderOrThrow(PoiTypes.LIGHTNING_ROD);

            for (BlockState state : CHANNELER.get().getStateDefinition().getPossibleStates()) {
                // Optional: guard against conflicts
                Holder<PoiType> old = GameData.getBlockStatePointOfInterestTypeMap().put(state, lightningRod);
                if (old != null && old != lightningRod) {
                    throw new IllegalStateException("Channeler state already assigned to POI: " + old);
                }
            }
        });
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == MATERIALS_TAB.getKey()) {
            event.insertAfter(RAW_MITHRIL.get().getDefaultInstance(), CRUSHED_MITHRIL.get().getDefaultInstance(), PARENT_AND_SEARCH_TABS);
            event.insertAfter(MITHRIL_SCRAP.get().getDefaultInstance(), MITHRIL_NUGGET.get().getDefaultInstance(), PARENT_AND_SEARCH_TABS);
            event.accept(MANA_BUCKET.get(), PARENT_AND_SEARCH_TABS);
            event.accept(LIGHTNING_BUCKET.get(),  PARENT_AND_SEARCH_TABS);
            event.accept(BLOOD_BUCKET.get(),  PARENT_AND_SEARCH_TABS);
        }
        if (event.getTabKey() == AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey()) {
            event.insertAfter(STEAM_WHISTLE.asStack(), CHANNELER.toStack(), PARENT_AND_SEARCH_TABS);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }
    public static void onRegister(final RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.TRIGGER_TYPE)) {
            CWAdvancements.registerTriggers();
            CWTriggers.register();
        }
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
    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
//            ItemBlockRenderTypes.setRenderLayer(LIGHTNING.get(), RenderType.lightning());
        }

        @SubscribeEvent
        public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
            event.registerFluidType(new SimpleClientFluidType(CreateWizardry.id("block/mana")), CWFluidRegistry.MANA_TYPE.get());
            event.registerFluidType(new SimpleClientFluidType(CreateWizardry.id("block/lightning")), CWFluidRegistry.LIGHTNING_TYPE.get());
            event.registerFluidType(new SimpleTintedClientFluidType(new ResourceLocation("minecraft", "block/water_still"), 0x00831312), FIRE_ALE_TYPE.get());
            event.registerFluidType(new SimpleTintedClientFluidType(new ResourceLocation("forge", "block/milk_still"), 0x00D69D84), NETHERWARD_TINCTURE_TYPE.get());
        }

        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(CWBlockEntities.CHANNELER_BE.get(), ChannelerRenderer::new);
        }

    }
    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
