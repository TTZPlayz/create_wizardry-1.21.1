package net.ttzplayz.create_wizardry.fluids;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.ttzplayz.create_wizardry.CreateWizardry;
import net.ttzplayz.create_wizardry.block.CWBlocks;

public class CWFluidRegistry {

    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, CreateWizardry.MOD_ID);
    private static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, CreateWizardry.MOD_ID);

    public static final DeferredHolder<FluidType, FluidType> MANA_TYPE =
            FLUID_TYPES.register("mana_type", () ->
                    new FluidType(FluidType.Properties.create()));
    public static final DeferredHolder<FluidType, FluidType> LIGHTNING_TYPE =
            FLUID_TYPES.register("lightning_type", () ->
                    new FluidType(FluidType.Properties.create()));
    // MANA
    public static final DeferredHolder<Fluid, FlowingFluid> MANA =
            FLUIDS.register("mana", () -> new BaseFlowingFluid.Source(CWFluidRegistry.MANA_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> MANA_FLOWING =
            FLUIDS.register("mana_flowing", () -> new BaseFlowingFluid.Flowing(CWFluidRegistry.LIGHTNING_PROPERTIES));

    private static final BaseFlowingFluid.Properties MANA_PROPERTIES =
            new BaseFlowingFluid.Properties(
                    MANA_TYPE,
                    MANA,
                    MANA_FLOWING
            ).bucket(() -> Items.BUCKET)
                    .block(CWBlocks.MANA_BLOCK);
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
                    LIGHTNING_FLOWING
            ).bucket(() -> Items.BUCKET)
                    .block(CWBlocks.LIGHTNING_BLOCK);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
        FLUID_TYPES.register(eventBus);
    }


}
