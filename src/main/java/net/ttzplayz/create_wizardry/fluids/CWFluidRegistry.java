package net.ttzplayz.create_wizardry.fluids;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.ttzplayz.create_wizardry.CreateWizardry;
import net.ttzplayz.create_wizardry.item.CWItems;

public class CWFluidRegistry {

    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, CreateWizardry.MOD_ID);
    private static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, CreateWizardry.MOD_ID);

    public static final DeferredHolder<FluidType, FluidType> MAGIC_TYPE =
            FLUID_TYPES.register("mana_type", () ->
                    new FluidType(FluidType.Properties.create()
                            .rarity(Rarity.RARE)
                            .viscosity(200)
                            .canConvertToSource(false)
                            .lightLevel(15)));
//    public static final DeferredHolder<FluidType, FluidType> WATER_TYPE = registerFluidType("fire_ale_fluid",
//            new WaterFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, 0xA1E038D0,
//                    new Vector3f(224f / 255f, 56f / 255f, 208f / 255f),
//                    FluidType.Properties.create().lightLevel(2).viscosity(5).density(15)));

    public static final DeferredHolder<FluidType, FluidType> LIGHTNING_TYPE =
            FLUID_TYPES.register("lightning_type", () ->
                    new FluidType(FluidType.Properties.create()
                            .rarity(Rarity.RARE)
                            .viscosity(200)
                            .temperature(3000)
                            .pathType(PathType.BLOCKED)
                            .canConvertToSource(false)
                            .lightLevel(15)));
    // MANA
    public static final DeferredHolder<Fluid, FlowingFluid> MANA =
            FLUIDS.register("mana", () -> new BaseFlowingFluid.Source(CWFluidRegistry.MANA_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> MANA_FLOWING =
            FLUIDS.register("mana_flowing", () -> new BaseFlowingFluid.Flowing(CWFluidRegistry.MANA_PROPERTIES));

    private static final BaseFlowingFluid.Properties MANA_PROPERTIES =
            new BaseFlowingFluid.Properties(
                    MAGIC_TYPE,
                    MANA,
                    MANA_FLOWING)
                    .explosionResistance(100f)
                    .bucket(CWItems.MANA_BUCKET)
                    .levelDecreasePerBlock(1)
                    .tickRate(20);
    // Optional: register a FluidBlock later

    // LIGHTNING
    public static final DeferredHolder<Fluid, FlowingFluid> LIGHTNING =
            FLUIDS.register("lightning", () -> new BaseFlowingFluid.Source(CWFluidRegistry.LIGHTNING_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> LIGHTNING_FLOWING =
            FLUIDS.register("lightning_flowing", () -> new BaseFlowingFluid.Flowing(CWFluidRegistry.LIGHTNING_PROPERTIES));

    private static final BaseFlowingFluid.Properties LIGHTNING_PROPERTIES =
            new BaseFlowingFluid.Properties(
                    LIGHTNING_TYPE,
                    LIGHTNING,
                    LIGHTNING_FLOWING)
                    .explosionResistance(100f)
                    .bucket(CWItems.LIGHTNING_BUCKET)
                    .levelDecreasePerBlock(1)
                    .tickRate(20);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
        FLUID_TYPES.register(eventBus);
    }


}
