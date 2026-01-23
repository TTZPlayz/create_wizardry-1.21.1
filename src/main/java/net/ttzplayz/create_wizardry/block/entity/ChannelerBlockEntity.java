package net.ttzplayz.create_wizardry.block.entity;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.FluidStack;
import net.ttzplayz.create_wizardry.fluids.CWFluidRegistry;

import java.util.List;

import static net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.BLOCK;
import static net.ttzplayz.create_wizardry.block.channeler.ChannelerBlock.POWERED;

public class ChannelerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    // tank behaviour (primary Create integration point)
    private SmartFluidTankBehaviour internalTank;

    // cooldowns
    private int creeperDrainCooldown = 0;
    private int lightningDrainCooldown = 0;

    // amounts (mB)
    private static final int MB_PER_LIGHTNING_STRIKE = 1000; // 1 bucket
    private static final int MB_PER_CHARGED_CREEPER = 250;   // tweak balance

    // tick interval for scanning creepers (we'll scan every 10 ticks to reduce cost)
    private int scanCooldown = 0;
    private static final int SCAN_INTERVAL = 10;

    public ChannelerBlockEntity(BlockPos pos, BlockState state) {
        super(CWBlockEntities.CHANNELER_BE.get(), pos, state);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                BLOCK, // The capability type
                CWBlockEntities.CHANNELER_BE.get(), // BE type
                (be, context) -> {
                    if (be.internalTank == null) return null;
                    return be.internalTank.getPrimaryHandler();
                }
        );
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (internalTank == null) return false;
        return containedFluidTooltip(tooltip, isPlayerSneaking, internalTank.getPrimaryHandler());
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        // Create-style: create a single SmartFluidTankBehaviour for lightning fluid.
        // Capacity: 16_000 mB (16 buckets) — adjust as needed
        internalTank = SmartFluidTankBehaviour.single(this, 16000)
                .allowInsertion()
                .allowExtraction();
        behaviours.add(internalTank);
//        registerAwardables(behaviours, AllAdvancements.DRAIN);

        // If you want the Channeler to allow insertion from above (lightning rod style), remove forbidInsertion or add more fine-grained rules.
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null) return;

        if (!level.isClientSide) {
            tickServer();
        }
    }

    public void tickServer() {
        if (level == null) return;

        // Creeper scan logic (run every SCAN_INTERVAL ticks)
        if (scanCooldown > 0) scanCooldown--;
        if (scanCooldown <= 0) {
            scanCooldown = SCAN_INTERVAL;
            absorbNearbyChargedCreepers();
            absorbNearbyLightning();
        }

        if (creeperDrainCooldown > 0) creeperDrainCooldown--;
        if (lightningDrainCooldown > 0) lightningDrainCooldown--;
    }

    private int tryFillLightning(int mb) {
        if (internalTank == null) return 0;
        IFluidHandler tank = internalTank.getPrimaryHandler();
        if (tank == null || mb <= 0) return 0;


        FluidStack stack = new FluidStack(CWFluidRegistry.LIGHTNING.get(), mb);
        int accepted = internalTank.getPrimaryHandler().fill(stack, IFluidHandler.FluidAction.EXECUTE);
        if (accepted > 0) {
//            getBlockState().setValue(POWERED, true);
            setChanged();
            sendData();
        }
        return accepted;
    }

//    private boolean isLightningFluid(FluidStack stack) {
//        return !stack.isEmpty() && stack.getFluid() == CWFluidRegistry.LIGHTNING.get();
//    }

    private void absorbNearbyChargedCreepers() {
        if (level == null) return;

        // Small area around the block; tweak if you want a bigger capture zone
        AABB box = new AABB(worldPosition).inflate(1.5, 1.5, 1.5);
        List<Creeper> creepers = level.getEntitiesOfClass(Creeper.class, box, c -> c != null && c.isAlive() && c.isPowered());

        for (Creeper creeper : creepers) {
            if (drainFromChargedCreeper(creeper)) {
                level.playLocalSound(worldPosition, SoundRegistry.LIGHTNING_WOOSH_01.get(), SoundSource.BLOCKS, 0.75f, 0.9f + 0.2f * (float) Math.random(), false);
                // only drain one per scan to limit farming throughput
                break;
            }
        }
    }
    private void absorbNearbyLightning() {
        if (level == null) return;

        // Small area around the block; tweak if you want a bigger capture zone
        AABB box = new AABB(worldPosition).inflate(1.5, 1.5, 1.5);
        List<LightningBolt> bolts = level.getEntitiesOfClass(LightningBolt.class, box, l -> l != null && l.isAlive());

        for (LightningBolt bolt : bolts) {
            if (onLightningStrike(bolt)) {
                level.playLocalSound(worldPosition, SoundRegistry.LIGHTNING_WOOSH_01.get(), SoundSource.BLOCKS, 0.75f, 0.9f + 0.2f * (float) Math.random(), false);
                // only drain one per scan to limit farming throughput
                break;
            }
        }
    }

    // Called when lightning hits the block
    public boolean onLightningStrike(LightningBolt bolt) {
        if (level == null || level.isClientSide) return false;
        if (lightningDrainCooldown > 0) return false;

        int filled = tryFillLightning(MB_PER_LIGHTNING_STRIKE);
        if (filled > 0) {
            level.playSound(null, worldPosition, SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.BLOCKS, 1.0f, 1.0f);
            level.addParticle(ParticleTypes.ELECTRIC_SPARK,
                    worldPosition.getX() + 0.5, worldPosition.getY() + 1.1, worldPosition.getZ() + 0.5,
                    0.0, 0.08, 0.0);
            lightningDrainCooldown = 100; // 5 second cooldown
            bolt.setPos(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
//            setChanged();
//            sendData();
            return true;
        }
        return false;
    }

    // Drain fluid when charged creeper passes through; return true if drained
    public boolean drainFromChargedCreeper(Creeper creeper) {
        if (level == null || level.isClientSide) return false;
        if (creeperDrainCooldown > 0) return false;
        if (creeper == null || !creeper.isAlive()) return false;
        if (!creeper.isPowered()) return false;

        int filled = tryFillLightning(MB_PER_CHARGED_CREEPER);
        if (filled <= 0) return false;

        // No public API to “un-charge” a creeper; remove it to prevent infinite farming.
        try {
            creeper.kill();
        } catch (Throwable ignored) {
        }

        creeperDrainCooldown = 20; // 1 second cooldown
//        setChanged();
//        sendData();
        return true;
    }


    // Removed obsolete fluid and bucket methods.
    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.putInt("creeperCooldown", creeperDrainCooldown);
        compound.putInt("lightningCooldown", lightningDrainCooldown);
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        creeperDrainCooldown = compound.getInt("creeperCooldown");
        lightningDrainCooldown = compound.getInt("lightningCooldown");
        super.read(compound, registries, clientPacket);
    }
}