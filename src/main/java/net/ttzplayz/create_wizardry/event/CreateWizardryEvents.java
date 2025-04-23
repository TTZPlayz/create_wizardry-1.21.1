package net.ttzplayz.create_wizardry.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.ttzplayz.create_wizardry.CreateWizardry;

import javax.annotation.Nullable;

import static net.ttzplayz.create_wizardry.fluids.FluidRegistry.LIGHTNING;


@EventBusSubscriber(modid = CreateWizardry.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class CreateWizardryEvents {

    @SubscribeEvent
    public static void onEntitySpawned(EntityJoinLevelEvent event) {
        execute(event, event.getLevel(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
    }

    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        execute(null, world, x, y, z, entity);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null)
            return;
        if (entity instanceof LightningBolt) {
            if ((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == (new Object() {
                public BlockState with(BlockState _bs, Direction newValue) {
                    Property<?> _prop = _bs.getBlock().getStateDefinition().getProperty("facing");
                    if (_prop instanceof DirectionProperty _dp && _dp.getPossibleValues().contains(newValue))
                        return _bs.setValue(_dp, newValue);
                    _prop = _bs.getBlock().getStateDefinition().getProperty("axis");
                    return _prop instanceof EnumProperty _ep && _ep.getPossibleValues().contains(newValue.getAxis()) ? _bs.setValue(_ep, newValue.getAxis()) : _bs;
                }
            }.with(Blocks.LIGHTNING_ROD.defaultBlockState(), Direction.UP)).getBlock()) {
                if (world instanceof ILevelExtension _ext) {
                    IFluidHandler _fluidHandler = _ext.getCapability(Capabilities.FluidHandler.BLOCK, BlockPos.containing(x, y - 2, z), null);
                    if (_fluidHandler != null)
                        _fluidHandler.fill(new FluidStack(LIGHTNING, 500), IFluidHandler.FluidAction.EXECUTE);
                }
            }
            if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == (new Object() {
                public BlockState with(BlockState _bs, Direction newValue) {
                    Property<?> _prop = _bs.getBlock().getStateDefinition().getProperty("facing");
                    if (_prop instanceof DirectionProperty _dp && _dp.getPossibleValues().contains(newValue))
                        return _bs.setValue(_dp, newValue);
                    _prop = _bs.getBlock().getStateDefinition().getProperty("axis");
                    return _prop instanceof EnumProperty _ep && _ep.getPossibleValues().contains(newValue.getAxis()) ? _bs.setValue(_ep, newValue.getAxis()) : _bs;
                }
            }.with(Blocks.LIGHTNING_ROD.defaultBlockState(), Direction.DOWN)).getBlock()) {
                if (world instanceof ILevelExtension _ext) {
                    IFluidHandler _fluidHandler = _ext.getCapability(Capabilities.FluidHandler.BLOCK, BlockPos.containing(x, y + 1, z), null);
                    if (_fluidHandler != null)
                        _fluidHandler.fill(new FluidStack(LIGHTNING, 500), IFluidHandler.FluidAction.EXECUTE);
                }
            }
            if ((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == (new Object() {
                public BlockState with(BlockState _bs, Direction newValue) {
                    Property<?> _prop = _bs.getBlock().getStateDefinition().getProperty("facing");
                    if (_prop instanceof DirectionProperty _dp && _dp.getPossibleValues().contains(newValue))
                        return _bs.setValue(_dp, newValue);
                    _prop = _bs.getBlock().getStateDefinition().getProperty("axis");
                    return _prop instanceof EnumProperty _ep && _ep.getPossibleValues().contains(newValue.getAxis()) ? _bs.setValue(_ep, newValue.getAxis()) : _bs;
                }
            }.with(Blocks.LIGHTNING_ROD.defaultBlockState(), Direction.EAST)).getBlock()) {
                if (world instanceof ILevelExtension _ext) {
                    IFluidHandler _fluidHandler = _ext.getCapability(Capabilities.FluidHandler.BLOCK, BlockPos.containing(x - 1, y - 1, z), null);
                    if (_fluidHandler != null)
                        _fluidHandler.fill(new FluidStack(LIGHTNING, 500), IFluidHandler.FluidAction.EXECUTE);
                }
            }
            if ((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == (new Object() {
                public BlockState with(BlockState _bs, Direction newValue) {
                    Property<?> _prop = _bs.getBlock().getStateDefinition().getProperty("facing");
                    if (_prop instanceof DirectionProperty _dp && _dp.getPossibleValues().contains(newValue))
                        return _bs.setValue(_dp, newValue);
                    _prop = _bs.getBlock().getStateDefinition().getProperty("axis");
                    return _prop instanceof EnumProperty _ep && _ep.getPossibleValues().contains(newValue.getAxis()) ? _bs.setValue(_ep, newValue.getAxis()) : _bs;
                }
            }.with(Blocks.LIGHTNING_ROD.defaultBlockState(), Direction.WEST)).getBlock()) {
                if (world instanceof ILevelExtension _ext) {
                    IFluidHandler _fluidHandler = _ext.getCapability(Capabilities.FluidHandler.BLOCK, BlockPos.containing(x + 1, y - 1, z), null);
                    if (_fluidHandler != null)
                        _fluidHandler.fill(new FluidStack(LIGHTNING, 500), IFluidHandler.FluidAction.EXECUTE);
                }
            }
            if ((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == (new Object() {
                public BlockState with(BlockState _bs, Direction newValue) {
                    Property<?> _prop = _bs.getBlock().getStateDefinition().getProperty("facing");
                    if (_prop instanceof DirectionProperty _dp && _dp.getPossibleValues().contains(newValue))
                        return _bs.setValue(_dp, newValue);
                    _prop = _bs.getBlock().getStateDefinition().getProperty("axis");
                    return _prop instanceof EnumProperty _ep && _ep.getPossibleValues().contains(newValue.getAxis()) ? _bs.setValue(_ep, newValue.getAxis()) : _bs;
                }
            }.with(Blocks.LIGHTNING_ROD.defaultBlockState(), Direction.NORTH)).getBlock()) {
                if (world instanceof ILevelExtension _ext) {
                    IFluidHandler _fluidHandler = _ext.getCapability(Capabilities.FluidHandler.BLOCK, BlockPos.containing(x, y - 1, z - 1), null);
                    if (_fluidHandler != null)
                        _fluidHandler.fill(new FluidStack(LIGHTNING, 500), IFluidHandler.FluidAction.EXECUTE);
                }
            }
            if ((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == (new Object() {
                public BlockState with(BlockState _bs, Direction newValue) {
                    Property<?> _prop = _bs.getBlock().getStateDefinition().getProperty("facing");
                    if (_prop instanceof DirectionProperty _dp && _dp.getPossibleValues().contains(newValue))
                        return _bs.setValue(_dp, newValue);
                    _prop = _bs.getBlock().getStateDefinition().getProperty("axis");
                    return _prop instanceof EnumProperty _ep && _ep.getPossibleValues().contains(newValue.getAxis()) ? _bs.setValue(_ep, newValue.getAxis()) : _bs;
                }
            }.with(Blocks.LIGHTNING_ROD.defaultBlockState(), Direction.SOUTH)).getBlock()) {
                if (world instanceof ILevelExtension _ext) {
                    IFluidHandler _fluidHandler = _ext.getCapability(Capabilities.FluidHandler.BLOCK, BlockPos.containing(x, y - 1, z + 1), null);
                    if (_fluidHandler != null)
                        _fluidHandler.fill(new FluidStack(LIGHTNING, 500), IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }
}
