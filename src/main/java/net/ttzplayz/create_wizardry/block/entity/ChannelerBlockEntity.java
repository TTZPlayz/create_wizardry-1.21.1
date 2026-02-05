package net.ttzplayz.create_wizardry.block.entity;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.particle.ZapParticleOption;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;

import static net.ttzplayz.create_wizardry.block.channeler.ChannelerBlock.POWERED;
import static net.ttzplayz.create_wizardry.fluids.CWFluidRegistry.LIGHTNING;

public class ChannelerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public SmartFluidTankBehaviour internalTank;

    // cooldowns
    private int creeperDrainCooldown = 0;
    private int lightningDrainCooldown = 0;
    private int poweredCooldown = 2;

    // amounts (mB)
    private static final int MB_PER_LIGHTNING_STRIKE = 1000;
    private static final int MB_PER_CHARGED_CREEPER = 250;

    // tick interval for scanning creepers
    private int scanCooldown = 0;
    private static final int SCAN_INTERVAL = 10;

    public ChannelerBlockEntity(BlockPos pos, BlockState state) {
        super(CWBlockEntities.CHANNELER_BE.get(), pos, state);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                ForgeCapabilities.FLUID_HANDLER,
                CWBlockEntities.CHANNELER_BE.get(), // BE type
                (be, context) -> {
                    if (be.internalTank == null) return null;
                    return be.internalTank.getCapability();
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
        internalTank = SmartFluidTankBehaviour.single(this, 4000)
                .allowInsertion()
                .allowExtraction();
        behaviours.add(internalTank);
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

        if (scanCooldown > 0) scanCooldown--;
        if (scanCooldown <= 0) {
            scanCooldown = SCAN_INTERVAL;
            absorbNearbyChargedCreepers();
            absorbNearbyLightning();
        }

        if (getBlockState().getValue(POWERED)) {
            if (poweredCooldown > 0)
                poweredCooldown--;
        if (poweredCooldown <= 0) {
            poweredCooldown = 0;
            level.setBlock(worldPosition, getBlockState().setValue(POWERED, false), 3);
        }}


        if (creeperDrainCooldown > 0) creeperDrainCooldown--;
        if (lightningDrainCooldown > 0) lightningDrainCooldown--;
    }

    private int tryFillLightning(int mb) {
        if (internalTank == null) return 0;
        IFluidHandler tank = internalTank.getPrimaryHandler();
        if (tank == null || mb <= 0) return 0;


        FluidStack stack = new FluidStack(LIGHTNING.get(), mb);
        int accepted = internalTank.getPrimaryHandler().fill(stack, IFluidHandler.FluidAction.EXECUTE);
        if (accepted > 0) {
            setChanged();
            internalTank.sendDataLazily();
        }
        return accepted;
    }

    private void absorbNearbyChargedCreepers() {
        if (level == null) return;

        AABB box = new AABB(worldPosition).inflate(2, 2, 2);
        List<Creeper> creepers = level.getEntitiesOfClass(Creeper.class, box, c -> c != null && c.isAlive() && c.isPowered());

        for (Creeper creeper : creepers) {
            if (drainFromChargedCreeper(creeper)) {
                level.setBlock(worldPosition, getBlockState().setValue(POWERED, true), 3);
                poweredCooldown = 2;
                level.playSound(null, worldPosition, SoundRegistry.LIGHTNING_CAST.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                MagicManager.spawnParticles(level, ParticleHelper.ELECTRICITY, creeper.getX(), creeper.getY() + creeper.getBbHeight() / 2, creeper.getZ(), 10,
                        creeper.getBbWidth() / 3, creeper.getBbHeight() / 3, creeper.getBbWidth() / 3, 0.1, false);
                Vec3 start = creeper.getBoundingBox().getCenter();
                Vec3 dest = worldPosition.getCenter();
                ((ServerLevel) level).sendParticles(new ZapParticleOption(dest), start.x, start.y, start.z, 1, 0, 0, 0, 0);
                // only drain one per scan to limit farming throughput
                break;
            }
        }
    }
    private void absorbNearbyLightning() {
        if (level == null) return;

        AABB box = new AABB(worldPosition).inflate(8, 8, 8);
        List<LightningBolt> bolts = level.getEntitiesOfClass(LightningBolt.class, box, l -> l != null && l.isAlive());

        for (LightningBolt bolt : bolts) {
            if (onLightningStrike(bolt)) {
                level.setBlock(worldPosition, getBlockState().setValue(POWERED, true), 3);
                poweredCooldown = 2;
                level.playSound(null, worldPosition, SoundRegistry.LIGHTNING_CAST.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                for (int i = 0; i < 5; i++) {
                    Vec3 start = worldPosition.getCenter();
                    Vec3 dest = start.add(Utils.getRandomVec3(1).multiply(1.5, 0.5, 1.5).add(2, 0, 2));
                    ((ServerLevel) level).sendParticles(new ZapParticleOption(dest), start.x, start.y, start.z, 1, 0, 0, 0, 0);
                }
                MagicManager.spawnParticles(level, ParticleHelper.ELECTRICITY, worldPosition.getCenter().x, worldPosition.getCenter().y, worldPosition.getCenter().z, 10,
                        0.1, 0.1, 0.1, 0.1, false);
                BlockPos.betweenClosedStream(box).forEach(pos -> dowseFire(level, pos));
                bolt.setDamage(0);
                bolt.setVisualOnly(true);
                bolt.kill();
                // only drain one per scan to limit farming throughput
                break;
            }
        }
    }

    private void dowseFire(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.is(BlockTags.FIRE)) {
            level.removeBlock(pos, false);
        }
    }

    public boolean onLightningStrike(LightningBolt bolt) {
        if (level == null || level.isClientSide) return false;
        if (lightningDrainCooldown > 0) return false;
        if (bolt.getCause() != null) return false;
        int filled = tryFillLightning(MB_PER_LIGHTNING_STRIKE);
        if (filled > 0) {
            lightningDrainCooldown = 100; // 5 second cooldown
            setChanged();
            internalTank.sendDataLazily();
            return true;
        }
        return false;
    }

    // Drain fluid when charged creeper passes through
    public boolean drainFromChargedCreeper(Creeper creeper) {
        if (level == null || level.isClientSide) return false;
        if (creeperDrainCooldown > 0) return false;
        if (creeper == null || !creeper.isAlive()) return false;
        if (!creeper.isPowered()) return false;

        int filled = tryFillLightning(MB_PER_CHARGED_CREEPER);
        if (filled <= 0) return false;

        try {
            creeper.kill();
        } catch (Throwable ignored) {
        }

        creeperDrainCooldown = 20; // 1 second cooldown
        setChanged();
        internalTank.sendDataLazily();
        return true;
    }

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
