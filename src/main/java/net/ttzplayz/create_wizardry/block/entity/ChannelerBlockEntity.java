package net.ttzplayz.create_wizardry.block.entity;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LightningBolt;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.ttzplayz.create_wizardry.fluids.CWFluidRegistry;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class ChannelerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    // capacity in millibuckets (mB)
    private static final int CAPACITY_MB = 16_000;
    // stored amount
    private int storedMb = 0;
    SmartFluidTankBehaviour internalTank;
    TransportedItemStack heldItem;
    protected int processingTicks;

    // cooldown to avoid repeated drains
    private int creeperDrainCooldown = 0;

    public ChannelerBlockEntity(BlockPos pos, BlockState state) {
        super(CWBlockEntities.CHANNELER_BE.get(), pos, state);
    }

    // Called when lightning hits the block
    public void onLightningStrike(LightningBolt bolt) {
        if (level == null || level.isClientSide) return;
        // Amount per strike (tweak as you like)
        int amount = 2000; // 2 buckets worth (example)
        addFluid(amount);
        level.playSound(null, worldPosition, SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.BLOCKS, 1.0f, 1.0f);
        level.addParticle(ParticleTypes.ELECTRIC_SPARK, worldPosition.getX() + 0.5, worldPosition.getY() + 1.1, worldPosition.getZ() + 0.5, 18, 0.35, 0.35);
        markDirtyAndNotify();
    }

    // Drain fluid when charged creeper passes through; return true if drained
    public boolean drainFromChargedCreeper(Creeper creeper) {
        if (level == null || level.isClientSide) return false;
        if (creeperDrainCooldown > 0) return false;
        if (!creeper.isPowered()) return false;

        int amount = 1000;
        addFluid(amount);

        // change to clear powered state
        try {
            creeper.kill();
        } catch (Throwable ignored) {
//            creeper.kill();
        }

        creeperDrainCooldown = 20; // 1 second cooldown
        markDirtyAndNotify();
        return true;
    }


    private void addFluid(int mb) {
        storedMb = Math.min(CAPACITY_MB, storedMb + mb);
    }

    // Expose basic neoforge-style fill/drain methods using FluidStack (not full capability wiring)
    public int fill(FluidStack stack, boolean simulate) {
        if (stack == null || stack.isEmpty()) return 0;
        // Only accept Lightning fluid
        if (!stack.getFluid().toString().equals(CWFluidRegistry.LIGHTNING.getId().toString())) {
            return 0;
        }
        int toFill = Math.min(stack.getAmount(), CAPACITY_MB - storedMb);
        if (!simulate) storedMb += toFill;
        return toFill;
    }

    public FluidStack drain(int maxAmount, boolean simulate) {
        int toDrain = Math.min(maxAmount, storedMb);
        if (toDrain <= 0) return FluidStack.EMPTY;
        if (!simulate) storedMb -= toDrain;
        return new FluidStack(CWFluidRegistry.LIGHTNING.get(), toDrain);
    }

    // Convenience for bucket interaction: drain exactly 1000mB and return drained amount
    public int drainIntoBucket() {
        int needed = 1000;
        if (storedMb >= needed) {
            storedMb -= needed;
            return needed;
        }
        return 0;
    }

    // Fill from lightning bucket: add 1000mB if space
    public int fillFromBucket() {
        int amount = 1000;
        if (storedMb + amount <= CAPACITY_MB) {
            storedMb += amount;
            return amount;
        }
        return 0;
    }

    public void tickServer() {
        if (creeperDrainCooldown > 0) creeperDrainCooldown--;
        // optional: slowly dissipate or push to adjacent pipes (implement capability wiring)
    }

    public int getStoredMb() {
        return storedMb;
    }

    public int getCapacity() {
        return CAPACITY_MB;
    }

    public void markDirtyAndNotify() {
        setChanged();
        if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this).allowingBeltFunnels()
                .setInsertionHandler(this::tryInsertingFromSide));
        behaviours.add(internalTank = SmartFluidTankBehaviour.single(this, 1500)
                .allowExtraction()
                .forbidInsertion());
        registerAwardables(behaviours, AllAdvancements.DRAIN, AllAdvancements.CHAINED_DRAIN);
    }

    private ItemStack tryInsertingFromSide(TransportedItemStack transportedStack, Direction side, boolean simulate) {
        ItemStack inserted = transportedStack.stack;
        ItemStack returned = ItemStack.EMPTY;

        if (!getHeldItemStack().isEmpty())
            return inserted;

        if (inserted.getCount() > 1 && GenericItemEmptying.canItemBeEmptied(level, inserted)) {
            returned = inserted.copyWithCount(inserted.getCount() - 1);
            inserted = inserted.copyWithCount(1);
        }

        if (simulate)
            return returned;

        transportedStack = transportedStack.copy();
        transportedStack.stack = inserted.copy();
        transportedStack.beltPosition = side.getAxis()
                .isVertical() ? .5f : 0;
        transportedStack.prevSideOffset = transportedStack.sideOffset;
        transportedStack.prevBeltPosition = transportedStack.beltPosition;
        setHeldItem(transportedStack, side);
        setChanged();
        sendData();

        return returned;
    }

    public ItemStack getHeldItemStack() {
        return heldItem == null ? ItemStack.EMPTY : heldItem.stack;
    }

    public void setHeldItem(TransportedItemStack heldItem, Direction insertedFrom) {
        this.heldItem = heldItem;
        this.heldItem.insertedFrom = insertedFrom;
    }

    // NBT persistence
    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.putInt("ProcessingTicks", processingTicks);
        if (heldItem != null)
            compound.put("HeldItem", heldItem.serializeNBT(registries));
        compound.putInt("storedMb", storedMb);
        compound.putInt("creeperCooldown", creeperDrainCooldown);
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        heldItem = null;
        processingTicks = compound.getInt("ProcessingTicks");
        if (compound.contains("HeldItem"))
            heldItem = TransportedItemStack.read(compound.getCompound("HeldItem"), registries);
        storedMb = compound.getInt("storedMb");
        creeperDrainCooldown = compound.getInt("creeperCooldown");
        super.read(compound, registries, clientPacket);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking, level.getCapability(Capabilities.FluidHandler.BLOCK, worldPosition, null));
    }
}