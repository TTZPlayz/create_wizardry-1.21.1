package net.ttzplayz.create_wizardry.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LightningBolt;
import net.ttzplayz.create_wizardry.fluids.CWFluidRegistry;
import net.neoforged.neoforge.fluids.FluidStack;

public class ChannelerBlockEntity extends BlockEntity {

    // capacity in millibuckets (mB)
    private static final int CAPACITY_MB = 16_000;
    // stored amount
    private int storedMb = 0;

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

        int amount = 1000; // 1 bucket from a charged creeper
        addFluid(amount);

        // attempt to clear powered state; mapping may differ across versions.
        try {
            creeper.kill();
        } catch (Throwable ignored) {
//            creeper.kill();
        }

        creeperDrainCooldown = 20; // 1 second cooldown
        markDirtyAndNotify();
        return true;
    }

    // Add fluid (mB)
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

    // NBT persistence
    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        storedMb = tag.getInt("storedMb");
        creeperDrainCooldown = tag.getInt("creeperCooldown");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("storedMb", storedMb);
        tag.putInt("creeperCooldown", creeperDrainCooldown);
    }
}